package DTO;

import flow.definition.api.Continuation;

import java.util.List;

public class FlowDTO {
    private final String name;
    private final String description;
    private final List<String> formalOutputs;
    private final boolean isReadonly;
    private final List<StepDTO> stepDTO;
    private final List<FreeInputDTO> inputDTO;
    private final List<OutputDTO> outputDTO;
    private final List<String> continuations;

    public FlowDTO(String name, String description, List<String> formalOutputs, boolean isReadonly,
                   List<StepDTO> stepDTO, List<FreeInputDTO> inputDTO, List<OutputDTO> outputDTO,
                   List<String> continuations) {
        this.name = name;
        this.description = description;
        this.formalOutputs = formalOutputs;
        this.isReadonly = isReadonly;
        this.stepDTO = stepDTO;
        this.inputDTO = inputDTO;
        this.outputDTO = outputDTO;
        this.continuations = continuations;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getFormalOutputs() {
        return this.formalOutputs;
    }

    public boolean getIsReadonly() {
        return this.isReadonly;
    }

    public List<StepDTO> getStepsDTO() {
        return this.stepDTO;
    }

    public StepDTO getStepByName(String name) {
        for (StepDTO step : stepDTO) {
            if (step.getFinalName().equals(name))
                return step;
        }
        return null;
    }

    public List<FreeInputDTO> getInputDTO() {
        return this.inputDTO;
    }

    public List<OutputDTO> getOutputDTO() {
        return this.outputDTO;
    }

    public List<String> getContinuations() {
        return this.continuations;
    }
}
