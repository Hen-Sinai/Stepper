package step.api;

import flow.execution.context.StepExecutionContext;

import java.io.Serializable;
import java.util.Map;

public interface StepDefinition extends Serializable {
    String getName();
    boolean isReadonly();
    Map<String, DataDefinitionDeclaration> getInputs();
    Map<String, DataDefinitionDeclaration> getOutputs();
    StepResult invoke(StepExecutionContext context);
}
