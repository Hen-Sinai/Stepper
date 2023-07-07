package Exceptions;

import java.util.InputMismatchException;

public class MyInputMismatchException extends Exception {
    public MyInputMismatchException(String inputName) {
        super("Input: " + inputName + " type doe's not match!");
    }
}
