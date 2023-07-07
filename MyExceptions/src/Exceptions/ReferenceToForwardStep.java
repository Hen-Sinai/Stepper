package Exceptions;

public class ReferenceToForwardStep extends Exception {
    public ReferenceToForwardStep(String flowName, String sourceStep, String targetStep) {
        super("Under flow: " + flowName + ", under custom mappings: " + sourceStep + " can't come earlier than: " + targetStep);
    }
}
