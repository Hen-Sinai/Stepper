package Exceptions;

public class OutputNameNotUnique extends Exception{
    public OutputNameNotUnique(String flowName, String outputName){
        super("Under flow: " + flowName + ", output: " + outputName + " already exists");
    }
}
