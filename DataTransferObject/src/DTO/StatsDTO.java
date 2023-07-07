package DTO;

import flow.definition.api.FlowDefinition;
import flow.execution.StatsData;

import java.util.Map;

public class StatsDTO {
    private final Map<String, StatsData> stats;

    public StatsDTO(Map<String, StatsData> stats) {
        this.stats = stats;
    }

    public Map<String, StatsData> getStats() {
        return this.stats;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, StatsData> flow : this.stats.entrySet()) {
            sb.append("Step name: ").append(flow.getKey()).append("\n");
            sb.append(flow.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
}
