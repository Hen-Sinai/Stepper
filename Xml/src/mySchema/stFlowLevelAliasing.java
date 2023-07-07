package mySchema;

import jaxb.schema.generated.STFlowLevelAlias;
import jaxb.schema.generated.STFlowLevelAliasing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class stFlowLevelAliasing implements Serializable {
    private final List<stFlowLevelAlias> flowAlias = new ArrayList<>();

    public stFlowLevelAliasing(STFlowLevelAliasing stFlowLevelAliasing) {
        if (stFlowLevelAliasing != null) {
            for (STFlowLevelAlias flowAlias : stFlowLevelAliasing.getSTFlowLevelAlias()) {
                this.flowAlias.add(new stFlowLevelAlias(flowAlias));
            }
        }
    }

    public List<stFlowLevelAlias> getFlowAlias() {
        return this.flowAlias;
    }
}
