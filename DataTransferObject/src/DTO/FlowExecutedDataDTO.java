package DTO;

import flow.execution.FlowExecutionResult;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlowExecutedDataDTO {
    private final String flowName;
    private final UUID id;
    private final FlowExecutionResult result;
    private final Duration duration;
    private final String startTimeStamp;
    private final String finishTimeStamp;
    private final List<FreeInputDTO> inputs;
    private final List<OutputDTO> outputs;
    private final List<StepResultDTO> stepsList;
    private final Map<String, StepResultDTO> stepsMap;

    public FlowExecutedDataDTO(String flowName, UUID id, FlowExecutionResult result, Duration duration,
                               String startTimeStamp, String finishTimeStamp,
                               List<FreeInputDTO> inputs, List<OutputDTO> outputs, List<StepResultDTO> steps) {
        this.flowName = flowName;
        this.id = id;
        this.result = result;
        this.duration = duration;
        this.startTimeStamp = startTimeStamp;
        this.finishTimeStamp = finishTimeStamp;
        this.inputs = inputs;
        this.outputs = outputs;
        this.stepsList = steps;
        this.stepsMap = convertListToMap();
    }

    public String getFlowName() {
        return this.flowName;
    }

    public UUID getId() {
        return this.id;
    }

    public FlowExecutionResult getResult() {
        return this.result;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public List<FreeInputDTO> getInputs() {
        return this.inputs;
    }

    public List<OutputDTO> getOutputs() {
        return this.outputs;
    }

    public List<StepResultDTO> getStepsList() {
        return this.stepsList;
    }

    public Map<String, StepResultDTO> getStepsMap() {
        return this.stepsMap;
    }

    public String getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public String getFinishTimeStamp() {
        return this.finishTimeStamp;
    }

    private Map<String, StepResultDTO> convertListToMap() {
        Map<String, StepResultDTO> map = new HashMap<>();
        for (StepResultDTO stepResult: this.stepsList) {
            map.put(stepResult.getName(), stepResult);
        }
        return map;
    }
}
