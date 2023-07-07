package Exceptions;

public class CustomDataNotmatch extends  Exception {
    public CustomDataNotmatch(String flowName, String section, String sourceData, String sourceType, String targetData, String targetType) {
        super("Under flow: " + flowName + ", under " + section + ": " + targetData + " - " + targetType + " do not match to " +
                sourceData + " - " + sourceType);
    }
}
