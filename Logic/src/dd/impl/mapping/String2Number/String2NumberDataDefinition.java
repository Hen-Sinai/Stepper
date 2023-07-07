package dd.impl.mapping.String2Number;

import dd.api.AbstractDataDefinition;

public class String2NumberDataDefinition extends AbstractDataDefinition {
    public String2NumberDataDefinition() {
        super("String to number", false, String2Number.class);
    }

    @Override
    public String getUserPresentation() {
        return "Mapping between string and number";
    }
}
