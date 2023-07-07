package DTO;

import flow.execution.FlowExecutionResult;

import java.util.List;
import java.util.UUID;

public class ExecutionResultDTO {
    private final UUID id;
    private final String name;
    private final FlowExecutionResult result;
    private final List<OutputDTO> userString2data;
//    private final Map<String, OutputDTO> userString2data;

    public ExecutionResultDTO(UUID id, String name, FlowExecutionResult result, List<OutputDTO> userString2data) {
        this.id = id;
        this.name = name;
        this.result = result;
        this.userString2data = userString2data;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public FlowExecutionResult getResult() {
        return this.result;
    }

//    public Map<String, OutputDTO> getUserString2data() {
//        return this.userString2data;
//    }
    public List<OutputDTO> getUserString2data() {
        return this.userString2data;
    }
}
