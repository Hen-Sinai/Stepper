package dd.impl.list.string;

import dd.api.AbstractDataDefinition;

public class StringListDataDefinition extends AbstractDataDefinition {
    public StringListDataDefinition() {
        super("String List", false, StringListData.class);
    }

    @Override
    public String getUserPresentation() {
        return "List of strings";
    }
}