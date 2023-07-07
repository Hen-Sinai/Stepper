package Exceptions;

public class DataNotExistCustomMapping extends Exception {
    public DataNotExistCustomMapping(String flowName, String source, String data, String step) {
        super("Under flow: " + flowName + ", Under custom mappings: " + source + " = " + data + " does not exist in step: " + step);
    }
}
