package mySchema;

import jaxb.schema.generated.*;

import java.io.Serializable;
import java.util.List;

public class stFlow implements Serializable {
    private final String name;
    private final String stFlowDescription;
    private final List<String> stFlowOutput;
    private final stStepsInFlow stStepsInFlow;
    private final stFlowLevelAliasing stFlowLevelAliasing;
    private final stCustomMappings stCustomMappings;
    private final stContinuations stContinuations;
    private final stInitialInputValues stInitialInputValues;

    public stFlow(String name, String description, List<String> output, STStepsInFlow stStepsInFlow,
                  STFlowLevelAliasing stFlowLevelAliasing, STCustomMappings stCustomMappings,
                  STContinuations stContinuation, STInitialInputValues stInitialInputValues) {
        this.name = name;
        this.stFlowDescription = description;
        this.stFlowOutput = output;
        this.stStepsInFlow = new stStepsInFlow(stStepsInFlow);
        this.stFlowLevelAliasing = new stFlowLevelAliasing(stFlowLevelAliasing);
        this.stCustomMappings = new stCustomMappings(stCustomMappings);
        this.stContinuations = stContinuation != null ? new stContinuations(stContinuation) : null;
        this.stInitialInputValues = new stInitialInputValues(stInitialInputValues);
    }

    public String getName() {
        return this.name;
    }

    public String getStFlowDescription() {
        return this.stFlowDescription;
    }

    public List<String> getStFlowOutput() {
        return this.stFlowOutput;
    }

    public stStepsInFlow getStStepsInFlow() {
        return this.stStepsInFlow;
    }

    public stFlowLevelAliasing getStFlowLevelAliasing() {
        return this.stFlowLevelAliasing;
    }

    public stCustomMappings getStCustomMappings() {
        return this.stCustomMappings;
    }

    public stContinuations getStContinuation() {
        return this.stContinuations;
    }

    public stInitialInputValues getStInitialInputValues() {
        return this.stInitialInputValues;
    }
}
