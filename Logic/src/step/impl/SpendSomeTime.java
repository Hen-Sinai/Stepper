package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.time.Duration;
import java.time.Instant;

public class SpendSomeTime extends AbstractStepDefinition {
    public SpendSomeTime() {
        super("Spend Some Time", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.TIME_TO_SPEND.getName(), DataNecessity.MANDATORY, "Total sleeping time (sec)", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        int timeToSpend;
        StepResult result = null;

        try {
            timeToSpend = context.getDataValue(IO.TIME_TO_SPEND.getName(), Integer.class);
            if (timeToSpend <= 0) {
                stepInfo.addLog("Step failed! Entered data must be positive integer");
                stepInfo.setSummaryLine("Step failed! Entered data must be positive integer");
                result = StepResult.FAILURE;
            }
            else {
                stepInfo.addLog("About to sleep for " + timeToSpend + " seconds...");
                Thread.sleep(timeToSpend * 1000L);
                stepInfo.addLog("Done sleeping");
            }
        } catch (NoMatchTypeException e) {
            stepInfo.addLog(e.getMessage());
            result = StepResult.FAILURE;
        } catch (InterruptedException e) {
            stepInfo.addLog("Step failed!");
            result = StepResult.FAILURE;
        }

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(result != null ? result : StepResult.SUCCESS);
        stepInfo.setSummaryLine(stepInfo.getSummaryLine() != null ? stepInfo.getSummaryLine() : "SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        return stepInfo.getStepResult();
    }
}