package DTO;

public abstract class IoAbstract {
    protected String name;
    protected String type;
    protected Object data;

    public String getName() {
        return this.name;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return this.data;
    }
}
