package dd.impl.number;

import dd.api.AbstractDataDefinition;

public class NumberDataDefinition extends AbstractDataDefinition {
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }

    @Override
    public String getUserPresentation() {
        return "Integer";
    }
}
