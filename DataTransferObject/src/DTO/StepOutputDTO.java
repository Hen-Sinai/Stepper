package DTO;

import flow.definition.api.FlowDefinition;

import java.util.List;
import java.util.Map;

public class StepOutputDTO {
    private final String name;
    private Map<String, String> step2input;

    public StepOutputDTO(String name) {
        this.name = name;
    }

    public StepOutputDTO(String name, Map<String, String> step2input) {
        this.name = name;
        this.step2input = step2input;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getStep2input() {
        return this.step2input;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append('\n');
        if (step2input != null) {
            for (Map.Entry<String, String> entry : this.step2input.entrySet()) {
                sb.append("Connect to step: ").append(entry.getKey()).append(", to input: ").append(entry.getValue()).append('\n');
            }
        }
        return sb.toString();
    }
}
