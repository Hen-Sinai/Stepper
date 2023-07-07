package mySchema;

import jaxb.schema.generated.STContinuationMapping;

import java.io.Serializable;

public class stContinuationMapping implements Serializable {
    private final String sourceData;
    private final String targetData;

    public stContinuationMapping(STContinuationMapping stContinuationMapping) {
        this.sourceData = stContinuationMapping.getSourceData();
        this.targetData = stContinuationMapping.getTargetData();
    }

    public String getSourceData() {
        return this.sourceData;
    }

    public String getTargetData() {
        return this.targetData;
    }
}
