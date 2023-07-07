package step.impl;

import Exceptions.NoMatchTypeException;
import com.google.gson.Gson;
import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.time.Duration;
import java.time.Instant;

public class ToJson extends AbstractStepDefinition {
    public ToJson() {
        super("To Json", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.CONTENT.getName(), DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.JSON.getName(), DataNecessity.NA, "Json representation", DataDefinitionRegistry.JSON));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;
        Gson gson = new Gson();

        try {
            String content = context.getDataValue(IO.CONTENT.getName(), String.class);
            String contentJson = gson.toJson(content);
            stepInfo.addLog("Content is JSON string. Converting to json...");
            context.storeDataValue(IO.JSON.getName(), contentJson);
        } catch (Exception e) {
            stepInfo.addLog("Content is not a valid JSON representation");
            stepInfo.setSummaryLine("Content is not a valid JSON representation");
            result = StepResult.FAILURE;
        }

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(result != null ? result : StepResult.SUCCESS);
        stepInfo.setSummaryLine(stepInfo.getSummaryLine() != null ? stepInfo.getSummaryLine() : "SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        return result;
    }
}