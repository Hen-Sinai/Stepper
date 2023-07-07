package mySchema;

import jaxb.schema.generated.STStepInFlow;
import jaxb.schema.generated.STStepsInFlow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class stStepsInFlow implements Serializable {
    private final List<stStepInFlow> stepsInFlowList = new ArrayList<>();
    private final Map<String, stStepInFlow> stepsInFlowMap = new HashMap<>();

    public stStepsInFlow(STStepsInFlow stepsInFlow) {
        for (STStepInFlow step : stepsInFlow.getSTStepInFlow()) {
            this.stepsInFlowList.add(new stStepInFlow(step));
        }
        for (stStepInFlow step : this.stepsInFlowList) {
            this.stepsInFlowMap.put(step.getFinalName(), step);
        }
    }

    public List<stStepInFlow> getStepsInFlowList() {
        return this.stepsInFlowList;
    }
    public Map<String, stStepInFlow> getStepsInFlowMap() {
        return this.stepsInFlowMap;
    }
}
