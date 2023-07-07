package flow.definition.api;

import step.api.StepDefinition;

import java.io.Serializable;

public interface StepUsageDeclaration extends Serializable {
    String getFinalStepName();
    String getName();
    String getAlias();
    StepDefinition getStepDefinition();
    boolean skipIfFail();
}
