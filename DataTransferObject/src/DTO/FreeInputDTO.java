package DTO;

import step.api.DataNecessity;

import java.util.List;
import java.util.Map;

public class FreeInputDTO {
    private final String name;
    private final String type;
    private List<String> attachedSteps;
    private final DataNecessity necessity;
    private String userString;
    private Object data;
    private final boolean isInitialInput;

    public FreeInputDTO(String name, String userString, String type, List<String> attachedSteps, DataNecessity necessity,
                        boolean isInitialInput, Object data) {
        this.name = name;
        this.type = type;
        this.attachedSteps = attachedSteps;
        this.necessity = necessity;
        this.userString = userString;
        this.isInitialInput = isInitialInput;
        this.data = data;
    }

    public FreeInputDTO(String name, String userString, String type, List<String> attachedSteps, DataNecessity necessity,
                        boolean isInitialInput) {
        this.name = name;
        this.type = type;
        this.attachedSteps = attachedSteps;
        this.necessity = necessity;
        this.userString = userString;
        this.isInitialInput = isInitialInput;
    }

    public FreeInputDTO(String name, String type, Object data, DataNecessity necessity, boolean isInitialInput) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.necessity = necessity;
        this.isInitialInput = isInitialInput;
    }

    public String getName() {
        return this.name;
    }

    public String getUserString() {
        return this.userString;
    }

    public String getType() {
        return type;
    }

    public List<String> getAttachedSteps() {
        return this.attachedSteps;
    }

    public DataNecessity getNecessity() {
        return this.necessity;
    }

    public Object getData() {
        return this.data;
    }

    public boolean getIsInitialInput() {
         return this.isInitialInput;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String printWithAttachSteps() {
        return  "Name: " + name + '\n' +
                "Type: " + type + '\n' +
                "Necessity: " + necessity + '\n' +
                "Attached step/s: " + attachedSteps + '\n';
    }

    public String printWithData() {
        return  "Name: " + name + '\n' +
                "Type: " + type + '\n' +
                "Necessity: " + necessity + '\n' +
                "Content: " + data + '\n';
    }
}
