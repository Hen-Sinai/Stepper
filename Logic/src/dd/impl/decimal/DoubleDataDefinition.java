package dd.impl.decimal;

import dd.api.AbstractDataDefinition;

public class DoubleDataDefinition extends AbstractDataDefinition {
    public DoubleDataDefinition() {
        super("Double", true, Double.class);
    }

    @Override
    public String getUserPresentation() {
        return "Decimal number";
    }


}
