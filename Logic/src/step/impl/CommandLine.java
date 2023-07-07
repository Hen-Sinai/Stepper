package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLine extends AbstractStepDefinition {
    public CommandLine() {
        super("Command Line", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.COMMAND.getName(), DataNecessity.MANDATORY, "Command", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.ARGUMENTS.getName(), DataNecessity.OPTIONAL, "Command arguments", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.RESULT.getName(), DataNecessity.NA, "Command output", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        List<String> commandList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            String command = context.getDataValue(IO.COMMAND.getName(), String.class);
            String args = context.getDataValue(IO.ARGUMENTS.getName(), String.class);

            commandList.add(command);
            if (args != null) {
                stepInfo.addLog("About to invoke " + command + " " + args);
                commandList.addAll(Arrays.asList(args.split(" ")));
            } else {
                stepInfo.addLog("About to invoke " + command);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            context.storeDataValue(IO.RESULT.getName(), sb.toString());
        } catch (NoMatchTypeException | IOException e) {
            stepInfo.addLog(e.getMessage());
            e.printStackTrace();
            try {
                context.storeDataValue(IO.RESULT.getName(), e.getMessage());
            } catch (NoMatchTypeException e2) {
                e2.printStackTrace();
            }
        }

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(StepResult.SUCCESS);
        stepInfo.setSummaryLine("SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        return StepResult.SUCCESS;
    }
}
