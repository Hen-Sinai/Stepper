package DTO;

import flow.stepInfo.log.Log;
import step.api.StepResult;

import java.time.Duration;
import java.util.List;

public class StepResultDTO {
    private final String name;
    private final Duration duration;
    private final String startTimeStamp;
    private final String finishTimeStamp;
    private final StepResult result;
    private final String summary;
    private final List<LogDTO> logs;
    private final List<InputDTO> inputs;
    private final List<OutputDTO> outputs;

    public StepResultDTO(String name, Duration duration, StepResult result, String summary, List<LogDTO> logs,
                         String startTimeStamp, String finishTimeStamp, List<InputDTO> inputs, List<OutputDTO> outputs) {
        this.name = name;
        this.duration = duration;
        this.result = result;
        this.summary = summary;
        this.logs = logs;
        this.startTimeStamp = startTimeStamp;
        this.finishTimeStamp = finishTimeStamp;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getName() {
        return this.name;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public StepResult getStepResult() {
        return this.result;
    }

    public String getSummary() {
        return this.summary;
    }

    public List<LogDTO> getLogs() {
        return this.logs;
    }

    public List<InputDTO> getInputs() {
        return this.inputs;
    }

    public List<OutputDTO> getOutputs() {
        return this.outputs;
    }

    public String getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public String getFinishTimeStamp() {
        return this.finishTimeStamp;
    }
}
