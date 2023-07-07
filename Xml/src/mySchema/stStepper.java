package mySchema;

import jaxb.schema.generated.STStepper;

import java.io.Serializable;

public class stStepper implements Serializable {
    private final stFlows flows;
    private final int threadPoolSize;

    public stStepper(STStepper StStepper) {
        this.flows = new stFlows(StStepper.getSTFlows());
        this.threadPoolSize = StStepper.getSTThreadPool();
    }

    public stFlows getFlows() {
        return this.flows;
    }

    public int getThreadPoolSize() {
        return this.threadPoolSize;
    }
}
