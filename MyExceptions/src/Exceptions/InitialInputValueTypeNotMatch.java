package Exceptions;

public class InitialInputValueTypeNotMatch extends Exception {
    public InitialInputValueTypeNotMatch(String flowName, String inputName, String neededType) {
        super("Under flow: " + flowName + ", initial input: " + inputName + " type should be: " + neededType);
    }
}
