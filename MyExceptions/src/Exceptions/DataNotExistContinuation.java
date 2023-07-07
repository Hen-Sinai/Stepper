package Exceptions;

public class DataNotExistContinuation extends Exception {
    public DataNotExistContinuation(String flowName, String data) {
        super("Under flow: " + flowName + ", Under Continuation: " + data + " does not exist");
    }
}
