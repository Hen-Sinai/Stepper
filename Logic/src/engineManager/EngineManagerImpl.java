package engineManager;

import DTO.*;
import flow.definition.api.*;
import flow.execution.StatsData;
import Exceptions.*;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.stepInfo.StepInfoManager;
import flow.stepInfo.log.Log;
import mySchema.stContinuationMapping;
import roles.*;
import step.api.DataDefinitionDeclaration;
import step.api.DataNecessity;
import user.User;
import user.UserManager;
import utils.Xml;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class EngineManagerImpl implements EngineManager {
    private static EngineManagerImpl instance;
    private FlowManager flowsManager;
    private List<FlowExecution> executedFlowsList;
    private Map<UUID, FlowExecution> executedFlowsMap;
    private final Map<String, Map<String, StatsData>> stats;
    private ExecutorService threadExecutor;
    private final RolesManager roles;
    private final UserManager userManager = new UserManager();

    private EngineManagerImpl() {
        executedFlowsList = new ArrayList<>();
        executedFlowsMap = new HashMap<>();
        stats = new HashMap<>();
        roles = new RolesManager();
    }

    // Static method to retrieve the singleton instance
    public static EngineManager getInstance() {
        if (instance == null) {
            instance = new EngineManagerImpl();
        }
        return instance;
    }

    @Override
    public void loadXmlFile(InputStream inputStream) throws JAXBException, IOException, NoXmlFormat, ReferenceToForwardStep,
            FlowOutputNotExist, StepNotExist, StepNameNotUnique, OutputNameNotUnique, DataNotExistFlowLevelAliasing,
            UserInputNotFriendly, FlowNameExist, DataNotExistCustomMapping, CustomDataNotmatch, UserInputTypeCollision,
            InitialInputValueNotExist, FlowNotExist, DataNotExistContinuation, InitialInputValueTypeNotMatch {

        this.flowsManager = new FlowManagerImpl(Xml.readFromXml(inputStream));
        initMembers();
        updateRolesAvailableFlows();
    }

    private void initMembers() {
        executedFlowsList = new ArrayList<>();
        executedFlowsMap = new HashMap<>();
        initFlowStats();
        initStepsStats();
        threadExecutor = Executors.newFixedThreadPool(flowsManager.getTheadPoolSize());
    }

    @Override
    public void updateXml(InputStream inputStream) throws JAXBException, IOException, NoXmlFormat, ReferenceToForwardStep,
            FlowOutputNotExist, StepNotExist, StepNameNotUnique, OutputNameNotUnique, DataNotExistFlowLevelAliasing,
            UserInputNotFriendly, FlowNameExist, DataNotExistCustomMapping, CustomDataNotmatch, UserInputTypeCollision,
            InitialInputValueNotExist, FlowNotExist, DataNotExistContinuation, InitialInputValueTypeNotMatch {
        this.flowsManager = new FlowManagerImpl(Xml.readFromXml(inputStream));
        initMembers();
        updateRolesAvailableFlows();
    }

    @Override
    public FlowsNameDTO getFlowsNames(String username) {
        List<String> newFlowsName = new ArrayList<>();
        User user = this.userManager.getUser(username);
        Map<String, Role> roles = user.getRoles();

        if (user.getIsManager()) {
            for (String name : this.flowsManager.getFlowsName()) {
                newFlowsName.add(name);
            }
        }
        else {
            for (String name : this.flowsManager.getFlowsName()) {
                for (Map.Entry<String, Role> role : roles.entrySet()) {
                    if (role.getValue().getAllowedFlows().contains(name)) {
                        newFlowsName.add(name);
                    }
                }
            }
        }
        return new FlowsNameDTO(newFlowsName);
    }

    @Override
    public List<FlowDTO> getAllFlowsData() {
        List<FlowDTO> flows = new ArrayList<>();
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            flows.add(new FlowDTO(flow.getValue().getName(), flow.getValue().getDescription(),
                    new ArrayList<>(flow.getValue().getFlowFormalOutputs().keySet()),
                    flow.getValue().getIsReadonly(), getSteps(flow.getKey()), getFreeInputs(flow.getKey()),
                    getOutputs(flow.getKey()), getContinuationsName(flow.getValue().getContinuation())));
        }
        return flows;
    }

    @Override
    public FlowDTO getFlowData(String name) {
        FlowDefinition flow = this.flowsManager.getFlows().get(name);
        return new FlowDTO(flow.getName(), flow.getDescription(),  new ArrayList<>(flow.getFlowFormalOutputs().keySet()),
                flow.getIsReadonly(), getSteps(name), getFreeInputs(name), getOutputs(name),
                getContinuationsName(flow.getContinuation()));
    }

    private List<String> getContinuationsName(List<Continuation> continuations) {
        List<String> continuationsName = new ArrayList<>();
        for (Continuation continuation : continuations) {
            continuationsName.add(continuation.getFlowName());
        }

        return continuationsName;
    }

    private List<StepDTO> getSteps(String name) {
        FlowDefinition flow = this.flowsManager.getFlows().get(name);
        List<StepDTO> steps = new ArrayList<>();
        this.flowsManager.getFlows().get(name).getFlowFreeInputs();
        for (StepUsageDeclaration step : flow.getFlowStepsList()) {
            StepDTO newStep = step.getAlias() != null ?
                    new StepDTO(step.getName(), step.getAlias(), step.getStepDefinition().isReadonly(), getStepsInputs(flow.getName(), step), getStepsOutputs(flow.getName(), step)) :
                    new StepDTO(step.getName(), step.getStepDefinition().isReadonly(), getStepsInputs(flow.getName(), step), getStepsOutputs(flow.getName(), step));
            steps.add(newStep);
        }

        return steps;
    }

    private List<StepInputDTO> getStepsInputs(String flowName, StepUsageDeclaration step) {
        List<StepInputDTO> inputs = new ArrayList<>();
        FlowDefinition flow = this.flowsManager.getFlows().get(flowName);
        Map<String, Map<String, String>> step2inputAlias = flow.getStep2inputMapping();
        Map<String, String> stepInputData = flow.getConnectedInputs().get(step.getFinalStepName());
        DataNecessity necessity;
        String sourceStep, sourceOutput, inputAliasName;

        for (Map.Entry<String, DataDefinitionDeclaration> inputEntry : step.getStepDefinition().getInputs().entrySet()) {
            necessity = inputEntry.getValue().getNecessity();
            inputAliasName = step2inputAlias.get(step.getFinalStepName()).get(inputEntry.getKey());
            if (stepInputData != null && stepInputData.containsKey(inputAliasName)) {
                sourceStep = stepInputData.get(inputAliasName);
                sourceOutput = inputAliasName;
                inputs.add(new StepInputDTO(inputAliasName, necessity, sourceStep, sourceOutput, inputEntry.getValue().getDataDefinition().getName()));
            }
            else {
                inputs.add(new StepInputDTO(inputAliasName, necessity));
            }
        }

        return inputs;
    }

    private List<StepOutputDTO> getStepsOutputs(String flowName, StepUsageDeclaration step) {
        List<StepOutputDTO> outputs = new ArrayList<>();
        FlowDefinition flow = this.flowsManager.getFlows().get(flowName);
        Map<String, Map<String, String>> step2outputAlias = flow.getStep2outputAlias();
        Map<String, Map<String, String>> step2outputData = flow.getConnectedOutputs().get(step.getFinalStepName());
        String outputAliasName;

        for (Map.Entry<String, DataDefinitionDeclaration> outputEntry : step.getStepDefinition().getOutputs().entrySet()) {
            outputAliasName = step2outputAlias.get(step.getFinalStepName()).get(outputEntry.getKey());
            if (step2outputData != null && step2outputData.containsKey(outputAliasName)) {
                Map<String, String> output = step2outputData.get(outputAliasName);
                outputs.add(new StepOutputDTO(outputAliasName, output));
            }
            else {
                outputs.add(new StepOutputDTO(outputAliasName));
            }
        }

        return outputs;
    }

    @Override
    public List<FreeInputDTO> getFreeInputs(String flowName) {
        List<FreeInputDTO> inputs = new ArrayList<>();
        FlowDefinition flow = this.flowsManager.getFlows().get(flowName);
        Map<String, Object> initialValues = flow.getInitialInputValues();
        for (Map.Entry<String, Map<DataDefinitionDeclaration, List<String>>> entry : flow.getFlowFreeInputs().entrySet()) {
            for (Map.Entry<DataDefinitionDeclaration, List<String>> input : entry.getValue().entrySet()) {
                if (initialValues.containsKey(entry.getKey())) {
                    inputs.add(new FreeInputDTO(entry.getKey(), input.getKey().getUserString(), input.getKey().getDataDefinition().getName(),
                            input.getValue(), input.getKey().getNecessity(), true, initialValues.get(entry.getKey())));
                }
                else {
                    inputs.add(new FreeInputDTO(entry.getKey(), input.getKey().getUserString(), input.getKey().getDataDefinition().getName(),
                            input.getValue(), input.getKey().getNecessity(), false));
                }
            }
        }
        return inputs.stream()
                .sorted(Comparator.comparing(FreeInputDTO::getNecessity))
                .collect(Collectors.toList());
    }

    private List<OutputDTO> getOutputs(String name) {
        List<OutputDTO> outputs = new ArrayList<>();
        String originalOutputName;
        int dotIndex;
        FlowDefinition flow = this.flowsManager.getFlows().get(name);

        for (Map.Entry<String, StepUsageDeclaration> entry : flow.getFlowOutputs().entrySet()) {
            dotIndex = entry.getKey().indexOf(".");
            originalOutputName = entry.getKey().substring(0, dotIndex);
            outputs.add(new OutputDTO(entry.getKey().substring(dotIndex + 1),
                    entry.getValue().getStepDefinition().getOutputs().get(originalOutputName).getDataDefinition().getName(),
                    entry.getValue().getFinalStepName()));
        }

        return outputs;
    }

    @Override
    synchronized public ExecutionResultDTO executeFlow(ExecuteDataDTO executeDataDTO, String username) throws MyInputMismatchException {
        castInput(executeDataDTO.getDataValues(), executeDataDTO.getFlowName());

        Map<String, Object> initialInputValues = this.flowsManager.getFlow(executeDataDTO.getFlowName()).getInitialInputValues();
        castInput(initialInputValues, executeDataDTO.getFlowName());
        FlowExecution flowExecution = new FlowExecution(UUID.randomUUID(), username,
                this.flowsManager.getFlow(executeDataDTO.getFlowName()), initialInputValues);

        executedFlowsList.add(flowExecution);
        executedFlowsMap.put(flowExecution.getUniqueId(), flowExecution);

        threadExecutor.execute(new FlowExecutor(flowExecution, executeDataDTO.getDataValues(), initialInputValues));

        return new ExecutionResultDTO(flowExecution.getUniqueId(), executeDataDTO.getFlowName(),
                flowExecution.getFlowExecutionResult(), getFormalOutputs(flowExecution));
    }

    private void castInput(Map<String, Object> values, String flowName) throws MyInputMismatchException {
        String dataName, dataType;
        FlowDefinition flow = this.flowsManager.getFlows().get(flowName);
        for (Map.Entry<String, Map<DataDefinitionDeclaration, List<String>>> entry : flow.getFlowFreeInputs().entrySet()) {
            dataName = entry.getKey();
            for (Map.Entry<DataDefinitionDeclaration, List<String>> input : entry.getValue().entrySet()) {
                if (values.containsKey(dataName)) {
                    dataType = input.getKey().getDataDefinition().getName();
                    values.put(dataName, convertType(dataName, values.get(dataName), dataType));
                }
            }
        }
    }

    private Object convertType(String name, Object value, String type) throws MyInputMismatchException {
        try {
            switch (type) {
                case "Number":
                    value = Integer.parseInt((String) value);
                    break;
                case "Double":
                    value = Double.parseDouble((String) value);
                    break;
            }
        } catch (Exception e) {
            throw new MyInputMismatchException(name);
        }
        return value;
    }



    private List<OutputDTO> getFormalOutputs(FlowExecution flowExecution) {
        Map<String, Map<DataDefinitionDeclaration, Object>> allOutputs = flowExecution.getOutputs2data();
        Map<String, DataDefinitionDeclaration> formalOutputs = flowExecution.getFlowDefinition().getFlowFormalOutputs();
        Map<DataDefinitionDeclaration, Object> foundOutput;
        List<OutputDTO> resultOutputs = new ArrayList<>();
        Object notCreated = "Not created due to failure in flow";

        for (Map.Entry<String, DataDefinitionDeclaration> entry : formalOutputs.entrySet()) {
            if (allOutputs.containsKey(entry.getKey())) {
                foundOutput = allOutputs.get(entry.getKey());
                for (Map.Entry<DataDefinitionDeclaration, Object> dataEntry : foundOutput.entrySet()) {
                    resultOutputs.add(new OutputDTO(dataEntry.getKey().getUserString(), //maybe dataEntry.getKey().getUserString() ????
                            dataEntry.getKey().getDataDefinition().getName(),
                            dataEntry.getValue() != null ? dataEntry.getValue() : notCreated));
                }
            }
            else {
                resultOutputs.add(new OutputDTO(entry.getValue().getUserString(), //maybe dataEntry.getKey().getUserString() ????
                        entry.getValue().getDataDefinition().getName(),
                        notCreated));
            }
        }



        return resultOutputs;
    }

    @Override
    public List<FlowExecutedInfoDTO> getFlowsExecutedInfoDTO(String filter, String username) {
        List<FlowExecutedInfoDTO> ExecutionsInfo = new ArrayList<>();
        Map<String, Role> roles = this.userManager.getUserRoles(username);

        for (FlowExecution execution : this.executedFlowsList) {
            for (Map.Entry<String, Role> entry : roles.entrySet()) {
                if (entry.getValue().getAllowedFlows().contains(execution.getFlowDefinition().getName())) {
                    if (filter.equals("All") || execution.getFlowExecutionResult().toString().equals(filter))
                        ExecutionsInfo.add(new FlowExecutedInfoDTO(execution.getFlowDefinition().getName(), execution.getUniqueId(),
                                execution.getStartTimeStamp(), execution.getFlowExecutionResult()));
                }
            }

        }
        Collections.reverse(ExecutionsInfo);
        return ExecutionsInfo;
    }

    synchronized private List<OutputDTO> convertToOutputsList(Map<String, Map<DataDefinitionDeclaration, Object>> output2data) {
        List<OutputDTO> outputs = new ArrayList<>();

        for (Map.Entry<String, Map<DataDefinitionDeclaration, Object>> entry : output2data.entrySet()) {
            for (Map.Entry<DataDefinitionDeclaration, Object> dataEntry : entry.getValue().entrySet()) {
                outputs.add(new OutputDTO(entry.getKey(),
                        dataEntry.getKey().getDataDefinition().getName(),
                        dataEntry.getValue()));
            }
        }

        return outputs;
    }

    synchronized private List<StepResultDTO> getStepsResultData(UUID flowId) {
        List<StepResultDTO> stepsResult = new ArrayList<>();
        FlowExecution executedFlow = executedFlowsMap.get(flowId);
        for (StepInfoManager stepInfo : executedFlow.getStepsData()) {
            stepsResult.add(
                    new StepResultDTO(stepInfo.getName(),
                            stepInfo.getDuration(),
                            stepInfo.getStepResult(),
                            stepInfo.getSummaryLine(),
                            getLogsDTO(stepInfo.getLogs()),
                            stepInfo.getStartTimeStamp(),
                            stepInfo.getFinishTimeStamp(),
                            getStepInputs(executedFlow, stepInfo.getName()),
                            getStepOutputs(executedFlow, stepInfo.getName())));
        }

        return stepsResult;
    }

    private List<InputDTO> getStepInputs(FlowExecution executedFlow, String stepName) {
        List<InputDTO> stepInputs = new ArrayList<>();
        StepUsageDeclaration stepUsageDeclaration = executedFlow.getFlowDefinition().getFlowStepsMap().get(stepName);
        Object data;

        for (Map.Entry<String, String> entry : executedFlow.getFlowDefinition().getStep2inputMapping().get(stepName).entrySet()) {
            if (executedFlow.getInputs2data().containsKey(entry.getValue())) {
                data = executedFlow.getInputs2data().get(entry.getValue()).values().iterator().next();
                if (data != null) {
                    stepInputs.add(new InputDTO(
                            entry.getValue(),
                            stepUsageDeclaration.getStepDefinition().getInputs().get(entry.getKey()).getDataDefinition().getName(),
                            data
                    ));
                }
            }
            else {
                data = executedFlow.getOutputs2data().get(entry.getValue()).values().iterator().next();
                if (data != null) {
                    stepInputs.add(new InputDTO(
                            entry.getValue(),
                            stepUsageDeclaration.getStepDefinition().getInputs().get(entry.getKey()).getDataDefinition().getName(),
                            data
                    ));
                }
            }
        }

        return stepInputs;
    }

    private List<OutputDTO> getStepOutputs(FlowExecution executedFlow, String stepName) {
        List<OutputDTO> stepOutputs = new ArrayList<>();
        StepUsageDeclaration stepUsageDeclaration = executedFlow.getFlowDefinition().getFlowStepsMap().get(stepName);
        Object data;

        for (Map.Entry<String, String> entry : executedFlow.getFlowDefinition().getStep2outputMapping().get(stepName).entrySet()) {
            if (executedFlow.getOutputs2data().containsKey(entry.getValue())) {
                data = executedFlow.getOutputs2data().get(entry.getValue()).values().iterator().next();
                if (data != null) {
                    stepOutputs.add(new OutputDTO(
                            entry.getValue(),
                            stepUsageDeclaration.getStepDefinition().getOutputs().get(entry.getKey()).getDataDefinition().getName(),
                            data
                    ));
                }
            }
        }

        return stepOutputs;
    }

    private List<FreeInputDTO> getInsertedInputs(String flowName, UUID flowId) {
        List<FreeInputDTO> inputs = new ArrayList<>();
        FlowDefinition flow = this.flowsManager.getFlows().get(flowName);
        FlowExecution executedFlow = executedFlowsMap.get(flowId);
        for (Map.Entry<String, Map<DataDefinitionDeclaration, List<String>>> entry : flow.getFlowFreeInputs().entrySet()) {
            for (Map.Entry<DataDefinitionDeclaration, List<String>> input : entry.getValue().entrySet()) {
                if (executedFlow.getInputs2data().get(entry.getKey()) != null)
                    inputs.add(new FreeInputDTO(entry.getKey(), input.getKey().getDataDefinition().getName(),
                            executedFlow.getInputs2data().get(entry.getKey()).values().iterator().next(),
                            input.getKey().getNecessity(), false));
            }
        }
        return inputs.stream()
                .sorted(Comparator.comparing(FreeInputDTO::getNecessity))
                .collect(Collectors.toList());
    }

    @Override
    public FlowExecutedDataDTO getFlowExecutedDataDTO(UUID flowId) {
        FlowExecution executedFlow = executedFlowsMap.get(flowId);
        String flowName = executedFlow.getFlowDefinition().getName();
        return new FlowExecutedDataDTO(flowName, executedFlow.getUniqueId(), executedFlow.getFlowExecutionResult(),
                executedFlow.getDuration(), executedFlow.getStartTimeStamp(), executedFlow.getFinishTimeStamp(),
                getInsertedInputs(flowName, flowId),
                convertToOutputsList(executedFlow.getOutputs2data()), getStepsResultData(flowId));
    }

    @Override
    public void setStats(UUID id) {
        FlowExecution flowExecution = executedFlowsMap.get(id);
        setFlowStats(flowExecution);
        setStepsStats(flowExecution);
    }

    @Override
    public void setFinishTimeStamp(UUID id) {
        FlowExecution flowExecution = executedFlowsMap.get(id);
        flowExecution.setFinishTimeStamp();
    }

    private void initFlowStats() {
        stats.put("Flows", new HashMap<>());
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            this.stats.get("Flows").put(flow.getKey(), new StatsData());
        }
    }

    private void initStepsStats() {
        stats.put("Steps", new HashMap<>());
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            for (Map.Entry<String, StepUsageDeclaration> step : flow.getValue().getFlowStepsMap().entrySet()) {
                this.stats.get("Steps").put(step.getValue().getName(), new StatsData());
            }
        }
    }

    private void updateFlowStats() {
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            if (!this.stats.get("Flows").containsKey(flow.getKey()))
                this.stats.get("Flows").put(flow.getKey(), new StatsData());
        }
    }

    private void updateStepsStats() {
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            for (Map.Entry<String, StepUsageDeclaration> step : flow.getValue().getFlowStepsMap().entrySet()) {
                if (!this.stats.get("Steps").containsKey(step.getValue().getName()))
                    this.stats.get("Steps").put(step.getValue().getName(), new StatsData());
            }
        }
    }

    private void setFlowStats(FlowExecution flowExecution) {
        String flowName = flowExecution.getFlowDefinition().getName();
        this.stats.get("Flows").get(flowName).addAmountOfExecutions();
        this.stats.get("Flows").get(flowName).addDuration(flowExecution.getDuration());
    }

    private void setStepsStats(FlowExecution flowExecution) {
        String originName;
        List<StepUsageDeclaration> executedSteps = flowExecution.getExecutedSteps();
        Map<String, StepUsageDeclaration> allSteps = flowExecution.getFlowDefinition().getFlowStepsMap();
        for (StepUsageDeclaration executedStep : executedSteps) {
            originName = allSteps.get(executedStep.getFinalStepName()).getName();
            this.stats.get("Steps").get(originName).addAmountOfExecutions();
            this.stats.get("Steps").get(originName).addDuration(
                    flowExecution.getStep2Data().get(executedStep.getFinalStepName()).getDuration());
        }
    }

    @Override
    public Map<String,StatsDTO> getsStats() {
        Map<String,StatsDTO> stats = new HashMap<>();
        stats.put("Steps", new StatsDTO(this.stats.get("Steps")));
        stats.put("Flows", new StatsDTO(this.stats.get("Flows")));
        return stats;
    }

    @Override
    public StatsDTO getStepsStats() {
        return new StatsDTO(this.stats.get("Steps"));
    }

    @Override
    public StatsDTO getFlowsStats() {
        return new StatsDTO(this.stats.get("Flows"));
    }

    @Override
    public List<FreeInputDTO> continueToNextFlow(UUID currentFlowID, String chosenFlow) {
        FlowExecution executedFlow = executedFlowsMap.get(currentFlowID);
        List<Continuation> continuations = executedFlow.getFlowDefinition().getContinuation();
        List<stContinuationMapping> continuationMapping = null;
        List<FreeInputDTO> inputs = this.getFreeInputs(chosenFlow);
        List<OutputDTO> outputs = this.getFlowExecutedDataDTO(currentFlowID).getOutputs();

        for (FreeInputDTO input : inputs) {
            for (OutputDTO output : outputs) {
                if (input.getName().equals(output.getName())) {
                    input.setData(output.getData());
                }
            }
        }

        for (Continuation continuation : continuations) {
            if (continuation.getFlowName().equals(chosenFlow))
                continuationMapping = continuation.getMappings();
        }

        for (stContinuationMapping stContinuationMapping : continuationMapping) {
            for (FreeInputDTO input : inputs) {
                if (input.getName().equals(stContinuationMapping.getTargetData())) {
                    for (OutputDTO output : outputs) {
                        if (output.getName().equals(stContinuationMapping.getSourceData())) {
                            input.setData(output.getData());
                        }
                    }
                }
            }
        }


        return inputs;
    }

    @Override
    public List<FreeInputDTO> reRunFlow(UUID currentFlowID, String chosenFlow) {
        FlowExecution executedFlow = executedFlowsMap.get(currentFlowID);
        List<FreeInputDTO> inputs = this.getFreeInputs(chosenFlow);
        String dataName;

        for (Map.Entry<String, Map<DataDefinitionDeclaration, Object>> stepEntry : executedFlow.getInputs2data().entrySet()) {
            for (Map.Entry<DataDefinitionDeclaration, Object> dataEntry : stepEntry.getValue().entrySet()) {
                dataName = dataEntry.getKey().getName();
                for (FreeInputDTO input : inputs) {
                    if (input.getName().equals(dataName))
                        input.setData(dataEntry.getValue());
                }
            }
        }

        return inputs;
    }

    private List<LogDTO> getLogsDTO(List<Log> logs) {
        List<LogDTO> logsList = new ArrayList<>();
        for (Log log: logs) {
            logsList.add(new LogDTO(log.getData(), log.getTimeStamp()));
        }
        return logsList;
    }

    @Override
    public Map<String, RoleDTO> getRoles() {
        Map<String, RoleDTO> roles = new HashMap<>();
        for (Map.Entry<String, Role> roleEntry : this.roles.getName2Role().entrySet()) {
            roles.put(roleEntry.getKey(), new RoleDTO(roleEntry.getValue().getName(), roleEntry.getValue().getDescription(),
                    roleEntry.getValue().getAllowedFlows()));
        }
        return roles;
    }

    @Override
    public RoleDTO getRole(String roleName) {
        Role role = this.roles.getName2Role().get(roleName);
        return new RoleDTO(role.getName(), role.getDescription(), role.getAllowedFlows());
    }

    @Override
    public Map<String, RoleDTO> getUserRoles(String username) {
        Map<String, RoleDTO> roles = new HashMap<>();
        for (Map.Entry<String, Role> userEntry : this.userManager.getUserRoles(username).entrySet()) {
            roles.put(userEntry.getKey(), new RoleDTO(userEntry.getValue().getName(), userEntry.getValue().getDescription(),
                    userEntry.getValue().getAllowedFlows()));
        }
        return roles;
    }

    @Override
    public void addRole(RoleDTO role) {
        this.roles.addRole(new RoleImpl(role.getName(), role.getDescription(), role.getAllowedFlows()));
    }

    @Override
    public void updateRole(RoleDTO role) {
        this.roles.updateRole(new RoleImpl(role.getName(), role.getDescription(), role.getAllowedFlows()));
    }

    @Override
    public String getAdminName() {
        return this.userManager.getAdminName();
    }

    @Override
    public void setAdminName(String username) {
        this.userManager.setAdminName(username);
    }

    @Override
    public boolean isUserExist(String username) {
        return this.userManager.isUserExists(username);
    }

    @Override
    public void addUser(String username, boolean isManager) {
        this.userManager.addUser(username, isManager);
    }

    @Override
    public Set<UserDTO> getUsers() {
        Set<UserDTO> users = new HashSet<>();
        for (Map.Entry<String, User> userEntry : this.userManager.getUsers().entrySet()) {
            if (!userEntry.getKey().equals("Admin"))
                users.add(new UserDTO(userEntry.getKey(), userEntry.getValue().getIsManager()));
        }
        return users;
    }

    @Override
    public UserDTO getUser(String username) {
        User user = this.userManager.getUser(username);
        return new UserDTO(user.getName(), user.getIsManager());
    }

    @Override
    public void updateUser(UserDTO userData) {
        this.userManager.updateUser(userData);
    }

    private void updateRolesAvailableFlows() {
        Role allFlows = this.roles.getRole("All Flows");
        Role readOnlyFlows = this.roles.getRole("Read Only Flows");
        for (Map.Entry<String, FlowDefinition> flow : this.flowsManager.getFlows().entrySet()) {
            if (!allFlows.getAllowedFlows().contains(flow.getKey()))
                allFlows.addAllowedFlow(flow.getKey());
            if (!readOnlyFlows.getAllowedFlows().contains(flow.getKey()) && flow.getValue().getIsReadonly())
                readOnlyFlows.addAllowedFlow(flow.getKey());
        }
    }
}