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

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class FilesRenamer extends AbstractStepDefinition {
    public FilesRenamer() {
        super("Files Renamer", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.FILES_TO_RENAME.getName(), DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.FILES_LIST));
        addInput(new DataDefinitionDeclarationImpl(IO.PREFIX.getName(), DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.SUFFIX.getName(), DataNecessity.OPTIONAL, "Add this suffix", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.RENAME_RESULT.getName(), DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));
    }

    private StepResult editName(StepInfoManager stepInfo, RelationData relationData, FileListData filesToRename,
                          int totalFiles, String prefix, String suffix) {
        File originalFile, newFile;
        String originalFileName, newFileName;
        int dotIndex;
        boolean isSuccess;
        StepResult result = null;
        StringBuilder sb = new StringBuilder("Files failed to rename:\n");

        for (FileData file : filesToRename.getData()) {
            originalFile = new File(file.getFile().getPath());
            originalFileName = originalFile.getName();
            dotIndex = originalFileName.indexOf(".");

            if (prefix != null && suffix != null) {
                stepInfo.addLog("About to start rename " + totalFiles + " files. Adding prefix: " + prefix + "and suffix: " + suffix + ";");
                newFileName = prefix + originalFileName.substring(0, dotIndex) + suffix + originalFileName.substring(dotIndex);
            }
            else if (prefix != null) {
                stepInfo.addLog("About to start rename " + totalFiles + " files. Adding prefix: " + prefix + ";");
                newFileName = prefix + originalFileName;
            }
            else if (suffix != null) {
                stepInfo.addLog("About to start rename " + totalFiles + " files. Adding suffix: " + suffix + ";");
                newFileName = originalFileName.substring(0, dotIndex) + suffix + originalFileName.substring(dotIndex);
            }
            else {
                stepInfo.addLog("File: " + originalFileName + " didn't change name");
                newFileName = originalFileName;
            }

            newFile = new File(originalFile.getParent(), newFileName);
            isSuccess = originalFile.renameTo(newFile);

            String finalOriginalFileName = originalFileName;
            String finalNewFileName = newFileName;
            relationData.addRow(new ArrayList<String>() {{
                add(finalOriginalFileName);
                add(finalNewFileName);
            }});

            if (!isSuccess) {
                stepInfo.addLog("Problem renaming file " + originalFileName); // TODO - CHECK
                result = StepResult.WARNING;
                sb.append("Problem renaming file " + originalFileName + "\n");
            }
        }

        if (result != null)
            stepInfo.setSummaryLine(sb.toString());
        return result;
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;

        try {
            FileListData filesToRename = context.getDataValue(IO.FILES_TO_RENAME.getName(), FileListData.class);
            int totalFiles = filesToRename.getData().size();
            String prefix = context.getDataValue(IO.PREFIX.getName(), String.class);
            String suffix = context.getDataValue(IO.SUFFIX.getName(), String.class);
            RelationData relationData = new RelationData(new ArrayList<String>() {{
                add("Serial-Number");
                add("Original-File-Name");
                add("Updated-File-Name");
            }});

            result = editName(stepInfo, relationData, filesToRename, totalFiles, prefix, suffix);

            context.storeDataValue(IO.RENAME_RESULT.getName(), relationData);

            if (totalFiles == 0) {
                stepInfo.addLog("No files in folder");
                stepInfo.setSummaryLine("No files in folder");
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
        return result;
    }
}
