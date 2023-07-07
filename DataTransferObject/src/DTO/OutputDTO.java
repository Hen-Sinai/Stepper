package DTO;

public class OutputDTO extends IoAbstract{
    private String attachedStep;

    public OutputDTO(String name, String type, String attachedStep) {
        this.name = name;
        this.type = type;
        this.attachedStep = attachedStep;
    }

    public OutputDTO(String name, String type, Object data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getAttachedStep() {
        return this.attachedStep;
    }

    @Override
    public String toString() {
        return "Name: " + name + '\n' +
                "Type: " + type + '\n' +
                "Attached step: " + attachedStep + '\n';
    }
}
