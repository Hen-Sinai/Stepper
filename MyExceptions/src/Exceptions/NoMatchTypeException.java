package Exceptions;

public class NoMatchTypeException extends Exception{
    public NoMatchTypeException(){
        super("Entered data does not match the required type");
    }
}