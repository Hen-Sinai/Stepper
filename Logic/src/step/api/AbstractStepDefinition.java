package step.api;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStepDefinition implements StepDefinition {
    private final String stepName;
    private final boolean readonly;
    private final Map<String, DataDefinitionDeclaration> inputs;
    private final Map<String, DataDefinitionDeclaration> outputs;

    public AbstractStepDefinition(String stepName, boolean readonly) {
        this.stepName = stepName;
        this.readonly = readonly;
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
    }

    protected void addInput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        this.inputs.put(dataDefinitionDeclaration.getName(), dataDefinitionDeclaration);
    }

    protected void addOutput(DataDefinitionDeclaration dataDefinitionDeclaration) {
        this.outputs.put(dataDefinitionDeclaration.getName(), dataDefinitionDeclaration);
    }

    @Override
    public String getName() {
        return this.stepName;
    }

    @Override
    public boolean isReadonly() {
        return this.readonly;
    }

    @Override
    public Map<String, DataDefinitionDeclaration> getInputs() {
        return this.inputs;
    }

    @Override
    public Map<String, DataDefinitionDeclaration> getOutputs() {
        return this.outputs;
    }
}
