package dd.impl.enumerator.method;

import dd.api.AbstractDataDefinition;

public class MethodEnumeratorDataDefinition extends AbstractDataDefinition {
    public MethodEnumeratorDataDefinition() {
        super("Method enumerator", true, MethodEnumerator.class);
    }

    @Override
    public String getUserPresentation() {
        return "Set of strings";
    }
}