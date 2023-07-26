package dd.impl.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dd.api.AbstractDataDefinition;

public class JsonDataDefinition extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("JSON", true, JsonElement.class);
    }

    @Override
    public String getUserPresentation() {
        return "JSON string";
    }
}
