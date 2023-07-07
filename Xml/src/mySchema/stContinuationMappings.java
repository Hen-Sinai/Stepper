package mySchema;

import jaxb.schema.generated.STContinuation;
import jaxb.schema.generated.STContinuationMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class stContinuationMappings implements Serializable {
    private final List<stContinuationMapping> mappings;

    public stContinuationMappings(List<STContinuationMapping> stContinuationMappings) {
        this.mappings = new ArrayList<>();
        for (STContinuationMapping mapping : stContinuationMappings) {
            this.mappings.add(new stContinuationMapping(mapping));
        }
    }

    public List<stContinuationMapping> getMappings() {
        return this.mappings;
    }
}
