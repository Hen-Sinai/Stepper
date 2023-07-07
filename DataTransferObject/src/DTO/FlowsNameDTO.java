package DTO;

import java.util.List;

public class FlowsNameDTO {
    private final List<String> flowsNames;

    public FlowsNameDTO(List<String> flowsNames) {
        this.flowsNames = flowsNames;
    }

    public List<String> getFlowsNames() {
        return this.flowsNames;
    }
}
