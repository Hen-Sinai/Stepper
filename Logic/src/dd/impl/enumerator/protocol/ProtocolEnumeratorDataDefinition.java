package dd.impl.enumerator.protocol;

import dd.api.AbstractDataDefinition;
import dd.impl.enumerator.zipper.ZipperEnumerator;

public class ProtocolEnumeratorDataDefinition extends AbstractDataDefinition {
    public ProtocolEnumeratorDataDefinition() {
        super("Protocol enumerator", true, ProtocolEnumerator.class);
    }

    @Override
    public String getUserPresentation() {
        return "Set of strings";
    }
}