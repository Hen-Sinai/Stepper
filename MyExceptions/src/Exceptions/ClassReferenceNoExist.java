package Exceptions;

public class ClassReferenceNoExist extends Exception {
    public ClassReferenceNoExist(){
        super("The class referenced does not exists");
    }
}
