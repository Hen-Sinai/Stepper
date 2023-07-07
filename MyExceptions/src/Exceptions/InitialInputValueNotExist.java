package Exceptions;

public class InitialInputValueNotExist extends Exception {
    public InitialInputValueNotExist(String flowName, String inputName) {
        super("Under flow: " + flowName + ", Under initial input value: " + inputName + " does not exist");
    }
}
