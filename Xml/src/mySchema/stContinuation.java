package mySchema;

import jaxb.schema.generated.STContinuation;

import java.io.Serializable;

public class stContinuation implements Serializable {
    private String flowName;
    private stContinuationMappings mappings;

    public stContinuation(STContinuation stContinuation) {
        if (stContinuation != null) {
            this.flowName = stContinuation.getTargetFlow();
            this.mappings = new stContinuationMappings(stContinuation.getSTContinuationMapping());
        }
    }

    public String getFlowName() {
        return this.flowName;
    }

    public stContinuationMappings getMappings() {
        return this.mappings;
    }
}
