package flow.definition.api;

import DTO.ExecuteDataDTO;
import Exceptions.*;
import mySchema.*;
import step.StepDefinitionRegistry;
import step.api.DataDefinitionDeclaration;
import step.api.StepDefinition;

import java.util.*;

public class FlowManagerImpl implements FlowManager {
    private final stFlows stFlows;
    private final List<FlowDefinition> flowsList = new ArrayList<>();
    private final Map<String, FlowDefinition> flowsMap = new HashMap<>();
    private final int theadPoolSize;

    public FlowManagerImpl(stStepper stepper) throws FlowNameExist, StepNotExist, StepNameNotUnique, OutputNameNotUnique,
            UserInputNotFriendly, DataNotExistCustomMapping, CustomDataNotmatch, ReferenceToForwardStep,
            DataNotExistFlowLevelAliasing, FlowOutputNotExist, UserInputTypeCollision, InitialInputValueNotExist,
            FlowNotExist, DataNotExistContinuation, InitialInputValueTypeNotMatch {
        this.stFlows = stepper.getFlows();
        this.theadPoolSize = stepper.getThreadPoolSize();
        this.validateFlows();
        this.initFlows();
        this.validateContinuation(); //must come after init
    }

    private void initFlows() throws OutputNameNotUnique, UserInputNotFriendly, DataNotExistCustomMapping, CustomDataNotmatch,
            ReferenceToForwardStep, DataNotExistFlowLevelAliasing, FlowOutputNotExist, UserInputTypeCollision,
            InitialInputValueNotExist, InitialInputValueTypeNotMatch {
        FlowDefinition flowDefinition;
        for (stFlow flow : this.stFlows.getFlowsList()) {
            flowDefinition = createFlowDefinition(flow);
            this.flowsMap.put(flow.getName(),flowDefinition);
            this.flowsList.add(flowDefinition);
        }
    }

    @Override
    public void updateDataUsingXml(stStepper stepper) throws OutputNameNotUnique, UserInputNotFriendly, DataNotExistCustomMapping, CustomDataNotmatch,
            ReferenceToForwardStep, DataNotExistFlowLevelAliasing, FlowOutputNotExist, UserInputTypeCollision,
            InitialInputValueNotExist, InitialInputValueTypeNotMatch, StepNotExist, StepNameNotUnique, FlowNameExist, FlowNotExist, DataNotExistContinuation {
        FlowDefinition flowDefinition;
        for (stFlow flow : stepper.getFlows().getFlowsList()) {
            if (!flowsMap.containsKey(flow.getName())) {
                flowDefinition = createFlowDefinition(flow);
                this.flowsMap.put(flow.getName(), flowDefinition);
                this.flowsList.add(flowDefinition);
            }
        }
        this.validateFlows();
        this.initFlows();
        this.validateContinuation();
    }

    private FlowDefinition createFlowDefinition(stFlow flow) throws OutputNameNotUnique, UserInputNotFriendly, DataNotExistCustomMapping, CustomDataNotmatch,
            ReferenceToForwardStep, DataNotExistFlowLevelAliasing, FlowOutputNotExist, UserInputTypeCollision,
            InitialInputValueNotExist, InitialInputValueTypeNotMatch {
        return new FlowDefinitionImpl(flow.getName(),
                flow.getStFlowDescription(),
                flow.getStFlowOutput(),
                getStepsDeclarationsList(flow),
                getStepsDeclarationsMap(flow),
                getStepAliasList(flow),
                getDataAlias(flow),
                flow.getStCustomMappings().getCustomMappings(),
                getInitialInputValues(flow.getStInitialInputValues()),
                getContinuation(flow.getStContinuation()));
    }

    @Override
    public Map<String, FlowDefinition> getFlows() {
        return this.flowsMap;
    }

    @Override
    public FlowDefinition getFlow(String name) {
        return this.flowsMap.get(name);
    }

    @Override
    public int getTheadPoolSize() {
        return this.theadPoolSize;
    }

    @Override
    public List<String> getFlowsName() {
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, FlowDefinition> entry : this.flowsMap.entrySet()) {
            names.add(entry.getKey());
        }

        return names;
    }

    private List<Map<String, String>> getStepAliasList(stFlow flow) {
        List<Map<String, String>> aliases = new ArrayList<>();
        for (stStepInFlow step : flow.getStStepsInFlow().getStepsInFlowList()) {
            aliases.add(new HashMap<String, String>() {{
                put(step.getName(), step.getFinalName());
            }});
        }

        return aliases;
    }

    private List<StepUsageDeclaration> getStepsDeclarationsList(stFlow flow) {
        List<StepUsageDeclaration> stepsDeclarations = new ArrayList<>();
        for (stStepInFlow step : flow.getStStepsInFlow().getStepsInFlowList()) {
            stepsDeclarations.add(new StepUsageDeclarationImpl(
                    StepDefinitionRegistry.getStepDefinitionByName(step.getName()).getStepDefinition(),
                    step.getContinueIfFailing(), step.getName(), step.getAlias()));
        }

        return stepsDeclarations;
    }

    private Map<String, StepUsageDeclaration> getStepsDeclarationsMap(stFlow flow) {
        Map<String, StepUsageDeclaration> stepsDeclarations = new HashMap<>();
        for (stStepInFlow step : flow.getStStepsInFlow().getStepsInFlowList()) {
            stepsDeclarations.put(step.getFinalName(), new StepUsageDeclarationImpl(
                    StepDefinitionRegistry.getStepDefinitionByName(step.getName()).getStepDefinition(),
                    step.getContinueIfFailing(), step.getName(), step.getAlias()));
        }

        return stepsDeclarations;
    }

    private Map<String, Map<String, String>> getDataAlias(stFlow flow) {
        Map<String, Map<String, String>> dataAlias = new HashMap<>();
        for (stFlowLevelAlias flowLevelAlias : flow.getStFlowLevelAliasing().getFlowAlias()) {
            if (!dataAlias.containsKey(flowLevelAlias.getStepName())) {
                dataAlias.put(flowLevelAlias.getStepName(), new HashMap<String, String>() {{
                    put(flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
                }});
            }
            else {
                dataAlias.get(flowLevelAlias.getStepName()).
                        put(flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
            }
        }

        return dataAlias;
    }

    private List<Continuation> getContinuation(stContinuations stContinuations) {
        List<Continuation> continuations = new ArrayList<>();
        if (stContinuations != null) {
            for (stContinuation continuation : stContinuations.getContinuations()) {
                continuations.add(new Continuation(
                        continuation.getFlowName(),
                        continuation.getMappings().getMappings()
                ));
            }
        }
        return continuations;
    }

    private Map<String, Object> getInitialInputValues(stInitialInputValues stInitialInputValues) {
        Map<String, Object> initialInputValues = new HashMap<>();
        for (stInitialInputValue initialInputValue : stInitialInputValues.getInputValues()) {
            initialInputValues.put(initialInputValue.getInputName() ,initialInputValue.getInitialValue());
        }
        return initialInputValues;
    }

    private void validateFlows() throws StepNotExist, StepNameNotUnique, FlowNameExist, DataNotExistFlowLevelAliasing {
        validateFlowNameIsUnique();
        validateStepNameIsUnique();
        validateEachStepExist();
        validateSourceDataExist();
    }

    private void validateFlowNameIsUnique() throws FlowNameExist {
        Set<String> foundFlows = new HashSet<>();
        for (stFlow flow : this.stFlows.getFlowsList()) {
            if (foundFlows.contains(flow.getName()))
                throw new FlowNameExist(flow.getName());
            foundFlows.add(flow.getName());
        }
    }

    private void validateStepNameIsUnique() throws StepNameNotUnique {
        Set<String> foundSteps;
        for (Map.Entry<String, stFlow> flow : this.stFlows.getFlowsMap().entrySet()) {
            foundSteps = new HashSet<>();
            for (stStepInFlow step : flow.getValue().getStStepsInFlow().getStepsInFlowList()) { // StepsInFlow
                if (foundSteps.contains(step.getFinalName())) {
                    throw new StepNameNotUnique(flow.getKey(), step.getFinalName());
                }
                foundSteps.add(step.getFinalName());
            }
        }
    }

    private void validateEachStepExist() throws StepNotExist {
        Set<String> foundSteps;
        for (Map.Entry<String, stFlow> flow : this.stFlows.getFlowsMap().entrySet()) {
            foundSteps = new HashSet<>();
            for (stStepInFlow step : flow.getValue().getStStepsInFlow().getStepsInFlowList()) { // StepsInFlow
                StepDefinitionRegistry stepDefinition = StepDefinitionRegistry.getStepDefinitionByName(step.getName());
                if (stepDefinition == null) {
                    throw new StepNotExist(flow.getKey(), "ST-StepsInFlow", step.getName());
                }
                foundSteps.add(step.getFinalName());
            }
            for (stFlowLevelAlias flowLevelAlias : flow.getValue().getStFlowLevelAliasing().getFlowAlias()) { // FlowLevelAliasing
                if (!foundSteps.contains(flowLevelAlias.getStepName())) {
                    throw new StepNotExist(flow.getKey(), "ST-FlowLevelAliasing", flowLevelAlias.getStepName());
                }
            }
            for (stCustomMapping customMapping : flow.getValue().getStCustomMappings().getCustomMappings()) { // CustomMappings
                if (!foundSteps.contains(customMapping.getSourceStep())) {
                    throw new StepNotExist(flow.getKey(), "ST-CustomMappings", customMapping.getSourceStep());
                }
                if (!foundSteps.contains(customMapping.getTargetStep())) {
                    throw new StepNotExist(flow.getKey(), "ST-CustomMappings", customMapping.getTargetStep());
                }
            }
        }
    }

    private void validateSourceDataExist() throws DataNotExistFlowLevelAliasing {
        List<Map<String, String>> stepsAliases;
        String originStepName;

        for (Map.Entry<String, stFlow> flow : this.stFlows.getFlowsMap().entrySet()) {
            stepsAliases = getStepAliasList(flow.getValue());
            for (stFlowLevelAlias flowLevelAlias : flow.getValue().getStFlowLevelAliasing().getFlowAlias()) {
                for (Map<String, String> alias : stepsAliases) {
                    originStepName = alias.keySet().iterator().next();
                    if (alias.values().iterator().next().equals(flowLevelAlias.getStepName())) {
                        StepDefinition step = StepDefinitionRegistry.getStepDefinitionByName(originStepName).getStepDefinition();
                        if (!step.getInputs().containsKey(flowLevelAlias.getSourceDataName()) &&
                                !step.getOutputs().containsKey(flowLevelAlias.getSourceDataName())) {
                            throw new DataNotExistFlowLevelAliasing(flow.getKey(), flowLevelAlias.getStepName(),
                                    flowLevelAlias.getSourceDataName());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void validateContinuation() throws FlowNotExist, DataNotExistContinuation, CustomDataNotmatch {
        validateContinuationFlowName();
        validateContinuationData();
    }

    private void validateContinuationFlowName() throws FlowNotExist {
        String targetFlow;
        for (FlowDefinition flowDefinition : this.flowsList) {
            if (flowDefinition.getContinuation() != null) {
                for (Continuation continuation : flowDefinition.getContinuation()) {
                    targetFlow = continuation.getFlowName();
                    if (!this.flowsMap.containsKey(targetFlow))
                        throw new FlowNotExist(flowDefinition.getName(), "ST-Continuations", targetFlow);
                }
            }
        }
    }

    private void validateContinuationData() throws DataNotExistContinuation, CustomDataNotmatch {
        Map<String, Map<String, String>> foundOutputs = new HashMap<>();
        Map<String, Map<String, String>> foundInputs = new HashMap<>();
        Map<String, String> flowInputs;
        Map<String, String> flowOutputs;
        String inputAlias, inputType, outputAlias, outputType, targetFlow;
        DataDefinitionDeclaration inputDefinition, outputDefinition;

        // Get the Inputs and outputs from each flow
        for (FlowDefinition flowDefinition : this.flowsList) {
            flowOutputs = new HashMap<>();
            flowInputs = new HashMap<>();
            for (StepUsageDeclaration step : flowDefinition.getFlowStepsList()) {
                for (Map.Entry<String, String> entry : flowDefinition.getStep2inputMapping().get(step.getFinalStepName()).entrySet()) {
                    inputAlias = entry.getValue();
                    inputDefinition = step.getStepDefinition().getInputs().get(entry.getKey());
                    inputType = inputDefinition.getDataDefinition().getName();

                    flowInputs.put(inputAlias, inputType);
                }
                for (Map.Entry<String, String> entry : flowDefinition.getStep2outputMapping().get(step.getFinalStepName()).entrySet()) {
                    outputAlias = entry.getValue();
                    outputDefinition = step.getStepDefinition().getOutputs().get(entry.getKey());
                    outputType = outputDefinition.getDataDefinition().getName();

                    flowOutputs.put(outputAlias, outputType);
                }
            }
            foundInputs.put(flowDefinition.getName(), flowInputs);
            foundOutputs.put(flowDefinition.getName(), flowOutputs);
        }

        for (FlowDefinition flowDefinition : this.flowsList) {
            if (flowDefinition.getContinuation() != null) {
                for (Continuation continuation : flowDefinition.getContinuation()) {
                    targetFlow = continuation.getFlowName();
                    String typeOfSource, typeOfTarget;

                    for (stContinuationMapping mapping : continuation.getMappings()) {
                        if (!foundOutputs.get(flowDefinition.getName()).containsKey(mapping.getSourceData())) {
                            throw new DataNotExistContinuation(flowDefinition.getName(), mapping.getSourceData());
                        }
                        if (!foundInputs.get(targetFlow).containsKey(mapping.getTargetData())) {
                            throw new DataNotExistContinuation(flowDefinition.getName(), mapping.getTargetData());
                        }
                        typeOfSource = foundOutputs.get(flowDefinition.getName()).values().iterator().next();
                        typeOfTarget = foundInputs.get(targetFlow).values().iterator().next();
                        if (!typeOfSource.equals(typeOfTarget)) {
                            throw new CustomDataNotmatch(flowDefinition.getName(), "continuation", mapping.getSourceData(),
                                    typeOfSource, mapping.getTargetData(), typeOfTarget);
                        }
                    }
                }
            }
        }
    }
}

