package flow.definition.api;

import mySchema.stCustomMapping;
import step.api.DataDefinitionDeclaration;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FlowDefinition extends Serializable {
    String getName();
    String getDescription();
    boolean getIsReadonly();
    List<StepUsageDeclaration> getFlowStepsList();
    Map<String, StepUsageDeclaration> getFlowStepsMap();
    List<Map<String, String>> getStepsAlias();
    Map<String, Map<String, String>> getStep2inputMapping();
    Map<String, Map<String, String>> getStep2inputAlias();
    Map<String, Map<String, String>> getStep2outputMapping();
    Map<String, Map<String, String>> getStep2outputAlias();
    Map<String, DataDefinitionDeclaration> getFlowFormalOutputs();
//    HashMap<String, DataDefinition> getData2dataDefinition();
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    Map<String, Map<DataDefinitionDeclaration, List<String>>> getFlowFreeInputs();
    Map<String, Map<String, String>> getConnectedInputs();
    Map<String, Map<String, Map<String, String>>> getConnectedOutputs();
    Map<String, StepUsageDeclaration> getFlowOutputs();
    Map<String, Object> getInitialInputValues();
    List<Continuation> getContinuation();
    List<stCustomMapping> getCustomMapping();
}
