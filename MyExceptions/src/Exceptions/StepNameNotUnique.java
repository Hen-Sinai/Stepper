package Exceptions;

public class StepNameNotUnique extends Exception {
    public StepNameNotUnique(String flowName, String stepName) {
        super("Under flow: " + flowName + ", Step: " + stepName + ", must be unique");
    }
}
