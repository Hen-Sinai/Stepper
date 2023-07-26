package DTO;

import flow.execution.FlowExecutionResult;

import java.util.UUID;

public class FlowExecutedInfoDTO {
    private final String name;
    private final UUID id;
    private final String timeStamp;
    private final FlowExecutionResult result;
    private final String ranByUser;
    private final boolean isRanByManager;

    public FlowExecutedInfoDTO(String name, UUID id, String timeStamp, FlowExecutionResult result,
                               String ranByUser, boolean isRanByManager) {
        this.name = name;
        this.id = id;
        this.timeStamp = timeStamp;
        this.result = result;
        this.ranByUser = ranByUser;
        this.isRanByManager = isRanByManager;
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

    public String getRanByUser() {
        return this.ranByUser;
    }

    public boolean isRanByManager() {
        return this.isRanByManager;
    }
}
