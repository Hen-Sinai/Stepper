package dd.impl.enumerator.zipper;

import dd.api.AbstractDataDefinition;

public class ZipperEnumeratorDataDefinition extends AbstractDataDefinition {
    public ZipperEnumeratorDataDefinition() {
        super("Zipper enumerator", true, ZipperEnumerator.class);
    }

    @Override
    public String getUserPresentation() {
        return "Set of strings";
    }
}