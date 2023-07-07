package Exceptions;

public class NoXmlFormat extends Exception{
    public NoXmlFormat(){
        super("File format must be XML");
    }
}
