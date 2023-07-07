package Exceptions;

public class StepNotExist extends Exception{
    public StepNotExist(String flowName, String section, String stepName) {
        super("Under flow: " + flowName + ", section: " + section + ", Step: '" + stepName + "' does not exist");
    }
}
