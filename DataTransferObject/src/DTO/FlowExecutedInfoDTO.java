package DTO;

import flow.execution.FlowExecutionResult;

import java.util.UUID;

public class FlowExecutedInfoDTO {
    private final String name;
    private final UUID id;
    private final String timeStamp;
    private final FlowExecutionResult result;

    public FlowExecutedInfoDTO(String name, UUID id, String timeStamp, FlowExecutionResult result) {
        this.name = name;
        this.id = id;
        this.timeStamp = timeStamp;
        this.result = result;
    }

    public String getName() {
        return this.name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public FlowExecutionResult getResult() {
        return this.result;
    }
}
