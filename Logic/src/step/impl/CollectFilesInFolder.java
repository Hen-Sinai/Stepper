package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import dd.impl.file.FileData;
import dd.impl.list.file.FileListData;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class CollectFilesInFolder extends AbstractStepDefinition {
    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.FOLDER_NAME.getName(), DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.FILTER.getName(), DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.FILES_LIST.getName(), DataNecessity.NA, "Files list", DataDefinitionRegistry.FILES_LIST));
        addOutput(new DataDefinitionDeclarationImpl(IO.TOTAL_FOUND.getName(), DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    private FileListData getFiles(StepInfoManager stepInfo, File directory, String path, String filter) {
        File[] files;
        FileListData filesList = new FileListData();

        if (filter != null) {
            stepInfo.addLog("Reading folder " + path + " content with filter " + filter);
            files = directory.listFiles((dir, name) -> {
                int dotIndex = name.lastIndexOf('.');
                if (dotIndex >= 0) {
                    String fileExt = name.substring(dotIndex + 1);
                    return fileExt.equals(filter);
                } else {
                    return false;
                }
            });
        } else {
            stepInfo.addLog("Reading folder " + path + " content without filter");
            files = directory.listFiles();
        }

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesList.addData(new FileData(file.getPath()));
                }
            }
        }

        return filesList;
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;
        String path, filter;
        int totalFound;
        FileListData filesList;

        try {
            path = context.getDataValue(IO.FOLDER_NAME.getName(), String.class);
            filter = context.getDataValue(IO.FILTER.getName(), String.class);
            File directory = new File(path);

            if (!directory.exists()) {
                stepInfo.addLog("Step failed! folder doesn't exist");
                stepInfo.setSummaryLine("Step failed! folder doesn't exist");
                result = StepResult.FAILURE;
            }
            else {
                filesList = getFiles(stepInfo, directory, path, filter);

                totalFound = filesList.getData().size();
                context.storeDataValue(IO.FILES_LIST.getName(), filesList);
                context.storeDataValue(IO.TOTAL_FOUND.getName(), totalFound);

                if (totalFound == 0) { //check what if path doesnt exist or not folder (the summary line)
                    stepInfo.addLog("Warning! No files in folder");
                    stepInfo.setSummaryLine("No files in folder");
                    result = StepResult.WARNING;
                } else {
                    stepInfo.addLog("Found " + totalFound + " files in folder matching the filter");
                }
            }
        } catch (NoMatchTypeException e) {
            stepInfo.addLog(e.getMessage());
            result =  StepResult.FAILURE;
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