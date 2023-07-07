package Exceptions;

public class DataNotExistFlowLevelAliasing extends Exception{
    public DataNotExistFlowLevelAliasing(String flowName, String step, String data) {
        super("Under flow: " + flowName + ", Under flow level aliasing: " + step + " does not contain data: " + data);
    }
}
