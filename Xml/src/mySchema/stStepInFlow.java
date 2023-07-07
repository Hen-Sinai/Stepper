package mySchema;

import jaxb.schema.generated.STStepInFlow;

import java.io.Serializable;

public class stStepInFlow implements Serializable {
    private final String name;
    private final String alias;
    private final boolean continueIfFailing;

    public stStepInFlow(STStepInFlow step) {
        this.name = step.getName();
        this.alias = step.getAlias();
        this.continueIfFailing = step.isContinueIfFailing() != null;
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getFinalName() {
        return this.alias != null ? this.alias : this.name;
    }

    public boolean getContinueIfFailing() {
        return this.continueIfFailing;
    }
}
