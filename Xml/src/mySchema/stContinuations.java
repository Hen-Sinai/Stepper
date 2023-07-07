package mySchema;

import jaxb.schema.generated.STContinuation;
import jaxb.schema.generated.STContinuations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class stContinuations implements Serializable {
    private final List<stContinuation> continuations = new ArrayList<>();

    public stContinuations(STContinuations stContinuations) {
        if (stContinuations != null) {
            for (STContinuation stContinuation : stContinuations.getSTContinuation()) {
                this.continuations.add(new stContinuation(stContinuation));
            }
        }
    }

    public List<stContinuation> getContinuations() {
        return this.continuations;
    }
}
