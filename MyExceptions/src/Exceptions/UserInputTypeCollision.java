package Exceptions;

public class UserInputTypeCollision extends Exception {
    public UserInputTypeCollision(String inputName, String inputType) {
        super(inputName + " already located in the user inputs and his type is: " + inputType);
    }
}
