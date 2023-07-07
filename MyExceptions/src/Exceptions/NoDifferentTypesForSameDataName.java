package Exceptions;

public class NoDifferentTypesForSameDataName extends Exception {
    public NoDifferentTypesForSameDataName(){
        super("Can't have two inputs with the same name but different types");
    }
}
