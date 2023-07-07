package mySchema;

import jaxb.schema.generated.STContinuationMapping;
import jaxb.schema.generated.STInitialInputValue;
import jaxb.schema.generated.STInitialInputValues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class stInitialInputValues implements Serializable {
    private final List<stInitialInputValue> inputValues;

    public stInitialInputValues(STInitialInputValues stInitialInputValues) {
        this.inputValues = new ArrayList<>();
        if (stInitialInputValues != null) {
            for (STInitialInputValue inputValue : stInitialInputValues.getSTInitialInputValue()) {
                this.inputValues.add(new stInitialInputValue(inputValue));
            }
        }
    }

    public List<stInitialInputValue> getInputValues() {
        return this.inputValues;
    }
}
