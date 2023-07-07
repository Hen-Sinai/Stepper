package dd.impl.json;

import dd.api.AbstractDataDefinition;

public class JsonDataDefinition extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("JSON", true, String.class);
    }

    @Override
    public String getUserPresentation() {
        return "JSON string";
    }
}
