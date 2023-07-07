package Exceptions;

public class FileCreationFailed extends Exception{
    public FileCreationFailed(){
        super("Fail occurred while trying to create file");
    }
}
