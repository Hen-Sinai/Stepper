package dd.impl.string;

import dd.api.AbstractDataDefinition;

public class StringDataDefinition extends AbstractDataDefinition {

    public StringDataDefinition() {
        super("String", true, String.class);
    }

    @Override
    public String getUserPresentation() {
        return "String";
    }
}
