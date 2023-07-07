package step.impl;

import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.time.Duration;
import java.time.Instant;

public class JsonDataExtractor extends AbstractStepDefinition {
    public JsonDataExtractor() {
        super("Json Data Extractor", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.JSON.getName(), DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JSON));
        addInput(new DataDefinitionDeclarationImpl(IO.JSON_PATH.getName(), DataNecessity.MANDATORY, "Data", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.VALUE.getName(), DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(result != null ? result : StepResult.SUCCESS);
        stepInfo.setSummaryLine(stepInfo.getSummaryLine() != null ? stepInfo.getSummaryLine() : "SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        return result;
    }
}