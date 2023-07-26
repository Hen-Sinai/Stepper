package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class FileDumper extends AbstractStepDefinition {
    public FileDumper() {
        super("File Dumper", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.CONTENT.getName(), DataNecessity.MANDATORY, "Content", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.FILE_NAME.getName(), DataNecessity.MANDATORY, "Target file path", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.RESULT.getName(), DataNecessity.NA, "File creation result", DataDefinitionRegistry.STRING));
    }

    private boolean dumpData(StepInfoManager stepInfo, String fileName, String content) {
        try (Writer out1 = new BufferedWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(Paths.get(fileName)), StandardCharsets.UTF_8))) {
            stepInfo.addLog("About to create file named " + fileName);
            out1.write(content);

            return true;
        } catch (IOException e) {
            stepInfo.addLog(e.getMessage());
            stepInfo.setSummaryLine(e.getMessage());
            return false;
        }
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;

        try {
            String content = context.getDataValue(IO.CONTENT.getName(), String.class);
            String fileName = context.getDataValue(IO.FILE_NAME.getName(), String.class);

            if (!dumpData(stepInfo, fileName, content)) {
                result = StepResult.FAILURE;
            }
            else {
                if (content.equals("")) {
                    stepInfo.addLog("File created with no content");
                    stepInfo.setSummaryLine("File created with no content");
                    result = StepResult.WARNING;
                }
                context.storeDataValue(IO.RESULT.getName(), "SUCCESS");
            }
        } catch (NoMatchTypeException e) {
            stepInfo.addLog(e.getMessage());
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
