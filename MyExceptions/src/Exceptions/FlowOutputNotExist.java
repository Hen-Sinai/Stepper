package Exceptions;

public class FlowOutputNotExist extends Exception {
    public FlowOutputNotExist(String flowName, String output) {
        super(output + " is not one of flow: " + flowName + " formal outputs");
    }
}
