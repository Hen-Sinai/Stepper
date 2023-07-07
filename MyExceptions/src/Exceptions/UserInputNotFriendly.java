package Exceptions;

public class UserInputNotFriendly extends Exception {
    public UserInputNotFriendly(String flowName, String name, String type) {
        super("Under flow: " + flowName + ", " + name + " type is: " + type + ", which is not user friendly input");
    }
}
