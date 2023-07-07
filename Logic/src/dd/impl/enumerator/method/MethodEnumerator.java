package dd.impl.enumerator.method;

import dd.impl.enumerator.StringEnumerator;

public class MethodEnumerator extends StringEnumerator {
    private MethodEnumerator() {
        add("GET");
        add("PUT");
        add("POST");
        add("DELETE");
    }
}
