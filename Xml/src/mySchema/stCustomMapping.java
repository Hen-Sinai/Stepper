package mySchema;

import jaxb.schema.generated.STCustomMapping;

import java.io.Serializable;

public class stCustomMapping implements Serializable {
    private final String targetStep;
    private final String targetData;
    private final String sourceStep;
    private final String sourceData;

    public stCustomMapping(STCustomMapping customMapping) {
        this.sourceStep = customMapping.getSourceStep();
        this.sourceData = customMapping.getSourceData();
        this.targetStep = customMapping.getTargetStep();
        this.targetData = customMapping.getTargetData();
    }

    public String getTargetStep() {
        return this.targetStep;
    }

    public String getTargetData() {
        return this.targetData;
    }

    public String getSourceStep() {
        return this.sourceStep;
    }

    public String getSourceData() {
        return this.sourceData;
    }
}
