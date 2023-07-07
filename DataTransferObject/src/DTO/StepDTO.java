package DTO;

import java.util.List;

public class StepDTO {
    private final String name;
    private String alias;
    private final List<StepInputDTO> inputs;
    private final List<StepOutputDTO> outputs;
    private final boolean isReadonly;

    public StepDTO(String name, boolean isReadonly, List<StepInputDTO> inputs, List<StepOutputDTO> outputs) {
        this.name = name;
        this.isReadonly = isReadonly;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public StepDTO(String name, String alias, boolean isReadonly, List<StepInputDTO> inputs, List<StepOutputDTO> outputs) {
        this.name = name;
        this.alias = alias;
        this.isReadonly = isReadonly;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }
    public String getFinalName() {
        return this.alias != null ? this.alias : this.name;
    }

    public boolean getIsReadonly() {
        return this.isReadonly;
    }

    public List<StepInputDTO> getInputs() {
        return this.inputs;
    }

    public List<StepOutputDTO> getOutputs() {
        return this.outputs;
    }
}
