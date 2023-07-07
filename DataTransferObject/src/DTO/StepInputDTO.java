package DTO;

import step.api.DataNecessity;

import java.util.Map;

public class StepInputDTO {
    private final String name;
    private final DataNecessity necessity;
    private String sourceStep;
    private String sourceOutput;
    private String type;

    public StepInputDTO(String name, DataNecessity necessity) {
        this.name = name;
        this.necessity = necessity;
    }

    public StepInputDTO(String name, DataNecessity necessity, String sourceStep, String sourceOutput, String type) {
        this.name = name;
        this.necessity = necessity;
        this.sourceStep = sourceStep;
        this.sourceOutput = sourceOutput;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public DataNecessity getNecessity() {
        return this.necessity;
    }

    public String getSourceStep() {
        return this.sourceStep;
    }

    public String getSourceOutput() {
        return this.sourceOutput;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append('\n');
        sb.append("Necessity: ").append(necessity).append('\n');
        if (sourceStep != null && sourceOutput != null) {
            sb.append("Source Step: ").append(sourceStep).append('\n');
            sb.append("Source Output: ").append(sourceOutput).append('\n');
        }
        return sb.toString();
    }
}
