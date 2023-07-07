package Exceptions;

public class FlowNotExist extends Exception {
    public FlowNotExist(String flowName, String section, String targetFlow) {
        super("Under Flow: '" + flowName + "' under: " + section + " flow: " + targetFlow + " not exist");
    }
}
