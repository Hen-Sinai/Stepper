package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import dd.impl.file.FileData;
import dd.impl.list.file.FileListData;
import dd.impl.relation.RelationData;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class FilesContentExtractor extends AbstractStepDefinition {
    public FilesContentExtractor() {
        super("Files Content Extractor", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.FILES_LIST.getName(), DataNecessity.MANDATORY, "Files to extract", DataDefinitionRegistry.FILES_LIST));
        addInput(new DataDefinitionDeclarationImpl(IO.LINE.getName(), DataNecessity.MANDATORY, "Line number to extract", DataDefinitionRegistry.NUMBER));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.DATA.getName(), DataNecessity.NA, "Data extraction", DataDefinitionRegistry.RELATION));
    }

    private void extractContent(StepInfoManager stepInfo, RelationData relationData, FileListData filesList, int lineNumber) {
        for (FileData file : filesList.getData()) {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file.getFile()))); ) {
                stepInfo.addLog("About to start to work on file: " + file.getFile().getName());
                for (int i = 1; i < lineNumber; i++) {
                    in.readLine();
                }

                // read the desired line
                String lineData = in.readLine();
                if (lineData != null) {
                    file.setSelectedLineData(in.readLine());
                }
                else {
                    stepInfo.addLog("Problem extracting line number: "
                            + lineNumber + " from file: " + file.getFile().getName());
                    lineData = "No such line";
                    file.setSelectedLineData(lineData);
                }
                String finalLineData = lineData;
                relationData.addRow(new ArrayList<String>() {{
                    add(file.getFile().getName());
                    add(finalLineData);
                }});

            } catch (FileNotFoundException e) {
                relationData.addRow(new ArrayList<String>() {{
                    add(file.getFile().getName());
                    add("File not found");
                }});
                stepInfo.addLog("File doesn't found");
            } catch (IOException e) {
                stepInfo.addLog("Error occurred while trying to read the file");
            }
        }
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;

        try {
            FileListData filesList = context.getDataValue(IO.FILES_LIST.getName(), FileListData.class);
            int lineNumber = context.getDataValue(IO.LINE.getName(), Integer.class);
            RelationData relationData = new RelationData(new ArrayList<String>() {{
                add("Serial-Number");
                add("File-Name");
                add("Data");
            }});

            extractContent(stepInfo, relationData, filesList, lineNumber);
            context.storeDataValue(IO.DATA.getName(), relationData);

            if (filesList.getData().size() == 0) {
                stepInfo.addLog("No files received");
                stepInfo.setSummaryLine("No files received");
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
