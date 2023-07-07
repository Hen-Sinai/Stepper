package flow.execution;

import flow.definition.api.FlowDefinition;
import flow.definition.api.StepUsageDeclaration;
import flow.stepInfo.StepInfoManager;
import step.api.DataDefinitionDeclaration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class FlowExecution implements Serializable {
    private final static SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
    private final UUID uniqueId;
    private final String ranByUser;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    private final String startTimeStamp;
    private String finishTimeStamp;
    private final List<StepInfoManager> stepsData;
    private final Map<String, StepInfoManager> step2data;
    private final Map<String, Object> initialInputs;
    private final Map<String, Map<DataDefinitionDeclaration, Object>> input2data;
    private final Map<String, Map<DataDefinitionDeclaration, Object>> output2data;
    private final List<StepUsageDeclaration> executedSteps = new ArrayList<>();

    public FlowExecution(UUID uniqueId, String ranByUser, FlowDefinition flowDefinition, Map<String, Object> initialInputs) {
        this.uniqueId = uniqueId;
        this.ranByUser = ranByUser;
        this.flowDefinition = flowDefinition;
        this.initialInputs = initialInputs;
        this.startTimeStamp = sdfDate.format(new Date());
        this.input2data = new HashMap<>();
        this.output2data = new HashMap<>();
        this.stepsData = new ArrayList<>();
        this.step2data = new HashMap<>();
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return this.flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return this.flowExecutionResult;
    }

    public void setFlowExecutionResult(FlowExecutionResult result) {
        this.flowExecutionResult = result;
    }

    public Duration getDuration() {
        return this.totalTime;
    }

    public String getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public String getFinishTimeStamp() {
        return this.finishTimeStamp;
    }

    public void setFinishTimeStamp() {
        this.finishTimeStamp = sdfDate.format(new Date());
    }

    public List<StepUsageDeclaration> getExecutedSteps() {
        return this.executedSteps;
    }

    public void setTotalTime(Duration duration) {
        this.totalTime = duration;
    }

    public List<StepInfoManager> getStepsData() {
        return this.stepsData;
    }

    public void addStepData(StepInfoManager stepInfo) {
        this.stepsData.add(stepInfo);
        this.step2data.put(stepInfo.getName(), stepInfo);
    }

    public Map<String, StepInfoManager> getStep2Data() {
        return this.step2data;
    }

    public StepInfoManager getData(String stepName) {
        return this.step2data.get(stepName);
    }

    public Map<String, Map<DataDefinitionDeclaration, Object>> getInputs2data() {
        return this.input2data;
    }

    public Map<String, Map<DataDefinitionDeclaration, Object>> getOutputs2data() {
        return this.output2data;
    }

//    public Map<String, Object> getInitialInputs() {
//        return this.initialInputs;
//    }

    public void setInput2data(Map<String, Object> dataValues) {
        for (Map.Entry<String, Map<DataDefinitionDeclaration, List<String>>> entry : flowDefinition.getFlowFreeInputs().entrySet()) {
            if (dataValues.containsKey(entry.getKey())) {
                input2data.put(entry.getKey(), new HashMap<>());
                input2data.get(entry.getKey()).put(entry.getValue().entrySet().iterator().next().getKey(), dataValues.get(entry.getKey()));
            }
        }
    }

    public void setOutput2data(Map<String, Object> dataValues) {
        for (Map.Entry<String, StepUsageDeclaration> entry : flowDefinition.getFlowOutputs().entrySet()) {
            int dotIndex = entry.getKey().indexOf(".");
            String originOutputName = entry.getKey().substring(0, dotIndex);
            String aliasOutputName = entry.getKey().substring(dotIndex + 1);
            String stepName = entry.getValue().getFinalStepName();

            if (dataValues.containsKey(aliasOutputName)) {
                this.output2data.put(aliasOutputName, new HashMap<>());
                this.output2data.get(aliasOutputName).
                        put(flowDefinition.getFlowStepsMap().get(stepName).getStepDefinition().getOutputs().get(originOutputName),
                                dataValues.get(aliasOutputName));
            }
        }
    }

    public void addExecutedStep(StepUsageDeclaration step) {
        executedSteps.add(step);
    }
}
