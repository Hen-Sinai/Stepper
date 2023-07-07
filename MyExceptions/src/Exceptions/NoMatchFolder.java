package Exceptions;

public class NoMatchFolder extends Exception {
    public NoMatchFolder(){
        super("Folder not found! Check the path again.");
    }
}
