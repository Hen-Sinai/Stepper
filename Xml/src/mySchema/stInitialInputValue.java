package mySchema;

import jaxb.schema.generated.STContinuationMapping;
import jaxb.schema.generated.STInitialInputValue;

import java.io.Serializable;

public class stInitialInputValue implements Serializable {
    private final String inputName;
    private final String initialValue;

    public stInitialInputValue(STInitialInputValue stInitialInputValue) {
        this.inputName = stInitialInputValue.getInputName();
        this.initialValue = stInitialInputValue.getInitialValue();
    }

    public String getInputName() {
        return this.inputName;
    }

    public String getInitialValue() {
        return this.initialValue;
    }
}
