package dd.impl.enumerator.protocol;

import dd.impl.enumerator.StringEnumerator;

public class ProtocolEnumerator extends StringEnumerator {
    private ProtocolEnumerator() {
        add("http");
        add("https");
    }
}
