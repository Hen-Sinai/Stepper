package flow.execution.runner;

import flow.definition.api.StepUsageDeclaration;
import flow.execution.FlowExecution;
import flow.execution.FlowExecutionResult;
import flow.execution.context.StepExecutionContext;
import flow.execution.context.StepExecutionContextImpl;
import step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class FlowExecutor implements Runnable, Serializable {
    private final FlowExecution flowExecution;
    private final Map<String, Object> dataValues;
    private final Map<String, Object> initialInputs;

    public FlowExecutor(FlowExecution flowExecution, Map<String, Object> dataValues, Map<String, Object> initialInputs) {
        this.flowExecution = flowExecution;
        this.dataValues = dataValues;
        this.initialInputs = initialInputs;
    }

    @Override
    public void run() {
        FlowExecutionResult flowExecutionResult = null;
        StepExecutionContext context = new StepExecutionContextImpl(
                dataValues,
                flowExecution.getFlowDefinition().getStepsAlias(),
                flowExecution.getFlowDefinition().getStep2inputMapping(),
                flowExecution.getFlowDefinition().getStep2outputMapping(),
                initialInputs);

        flowExecution.setInput2data(dataValues);
        Instant start = Instant.now();

        for (StepUsageDeclaration stepUsageDeclaration : flowExecution.getFlowDefinition().getFlowStepsList()) {
            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);
            flowExecution.addExecutedStep(stepUsageDeclaration);
            flowExecution.addStepData(context.getLastStepInfo());
            flowExecution.setOutput2data(context.getDataValues());

            if (stepResult == StepResult.FAILURE) {
                if (!stepUsageDeclaration.skipIfFail()) {
                    flowExecutionResult = FlowExecutionResult.FAILURE;
                    break;
                }
                else {
                    flowExecutionResult = FlowExecutionResult.WARNING;
                }
            }
            else if (stepResult == StepResult.WARNING) {
                flowExecutionResult = FlowExecutionResult.WARNING;
            }
        }

        flowExecution.setTotalTime(Duration.between(start, Instant.now()));
        flowExecution.setFlowExecutionResult(flowExecutionResult != null ? flowExecutionResult : FlowExecutionResult.SUCCESS);
    }

    public FlowExecution getFlowExecution() {
        return this.flowExecution;
    }

    public Map<String, Object> getDataValues() {
        return this.dataValues;
    }
}
