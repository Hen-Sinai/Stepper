package mySchema;

import jaxb.schema.generated.STFlowLevelAlias;

import java.io.Serializable;

public class stFlowLevelAlias implements Serializable {
    private final String stepName;
    private final String sourceDataName;
    private final String alias;

    public stFlowLevelAlias(STFlowLevelAlias flowLevelAlias) {
        this.stepName = flowLevelAlias.getStep();
        this.sourceDataName = flowLevelAlias.getSourceDataName();
        this.alias = flowLevelAlias.getAlias();
    }

    public String getStepName() {
        return this.stepName;
    }

    public String getSourceDataName() {
        return this.sourceDataName;
    }

    public String getAlias() {
        return this.alias;
    }
}
