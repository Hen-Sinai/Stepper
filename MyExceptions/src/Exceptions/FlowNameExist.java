package Exceptions;

public class FlowNameExist extends Exception {
    public FlowNameExist(String flowName) {
        super("Flow name: '" + flowName + "' already exist");
    }
}
