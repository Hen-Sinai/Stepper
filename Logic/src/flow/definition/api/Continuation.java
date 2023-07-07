package flow.definition.api;

import mySchema.stContinuationMapping;

import java.io.Serializable;
import java.util.List;

public class Continuation implements Serializable {
    private final String flowName;
    private final List<stContinuationMapping> mappings;

    public Continuation(String flowName, List<stContinuationMapping> mappings) {
        this.flowName = flowName;
        this.mappings = mappings;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public List<stContinuationMapping> getMappings() {
        return this.mappings;
    }
}
