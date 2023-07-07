package mySchema;

import jaxb.schema.generated.STFlow;
import jaxb.schema.generated.STFlows;

import java.io.Reader;
import java.io.Serializable;
import java.util.*;

public class stFlows implements Serializable {
    private final Map<String, stFlow> flowsMap = new HashMap<>();
    private final List<stFlow> listOfFlows = new ArrayList<>();
    private int numOfFlows = 0;

    public stFlows(STFlows flows) {
        for (STFlow flow: flows.getSTFlow()) {
            stFlow myFlow = new stFlow(flow.getName(), flow.getSTFlowDescription(),
                    getOutputs(flow.getSTFlowOutput()),
                    flow.getSTStepsInFlow(), flow.getSTFlowLevelAliasing(), flow.getSTCustomMappings(),
                    flow.getSTContinuations(),
                    flow.getSTInitialInputValues());
            this.flowsMap.put(myFlow.getName(), myFlow);
            this.listOfFlows.add(myFlow);
            this.numOfFlows++;
        }
    }

    public int getNumOfFlows() {
        return this.numOfFlows;
    }

    public Map<String, stFlow> getFlowsMap() {
        return this.flowsMap;
    }

    public List<stFlow> getFlowsList() {
        return this.listOfFlows;
    }

    private List<String> getOutputs(String outputsStr) {
        List<String> outputs = Arrays.asList(outputsStr.split(","));
        if (outputs.get(0).equals("")) {
            return new ArrayList<>();
        }
        else {
            return outputs;
        }
    }
}
