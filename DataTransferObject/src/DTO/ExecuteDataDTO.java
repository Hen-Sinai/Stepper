package DTO;

import java.util.Map;

public class ExecuteDataDTO {
    private final String flowName;
    private final Map<String, Object> dataValues;

    public ExecuteDataDTO(String flowName, Map<String, Object> dataValues) {
        this.flowName = flowName;
        this.dataValues = dataValues;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public Map<String, Object> getDataValues() {
        return this.dataValues;
    }
}
