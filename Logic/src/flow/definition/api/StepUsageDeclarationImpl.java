package flow.definition.api;

import step.api.StepDefinition;

public class StepUsageDeclarationImpl implements StepUsageDeclaration {
    private final StepDefinition stepDefinition;
    private final boolean skipIfFail;
    private final String stepName;
    private final String alias;

    public StepUsageDeclarationImpl(StepDefinition stepDefinition, boolean skipIfFail, String stepName, String alias) {
        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
        this.alias = alias;
    }

    @Override
    public String getName() {
        return this.stepName;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getFinalStepName() {
        return this.alias != null ? this.alias : this.stepName;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return this.stepDefinition;
    }

    @Override
    public boolean skipIfFail() {
        return this.skipIfFail;
    }
}
