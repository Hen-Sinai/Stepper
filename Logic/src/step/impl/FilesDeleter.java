package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import dd.impl.file.FileData;
import dd.impl.list.file.FileListData;
import dd.impl.list.string.StringListData;
import dd.impl.mapping.String2Number.String2Number;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.time.Duration;
import java.time.Instant;

public class FilesDeleter extends AbstractStepDefinition {
    public FilesDeleter() {
        super("Files Deleter", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.FILES_LIST.getName(), DataNecessity.MANDATORY, "Files to delete", DataDefinitionRegistry.FILES_LIST));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.DELETED_LIST.getName(), DataNecessity.NA, "Files failed to be deleted", DataDefinitionRegistry.STRING_LIST));
        addOutput(new DataDefinitionDeclarationImpl(IO.DELETION_STATS.getName(), DataNecessity.NA, "Deletion summary result", DataDefinitionRegistry.STRING2NUMBER_MAP));
    }

    private StringListData getNotDeletedFiles(StepInfoManager stepInfo, FileListData filesList) {
        StringListData notDeletedList = new StringListData();
        for (FileData file : filesList.getData()) {
            if (!file.getFile().delete()) {
                notDeletedList.addData(file.getFile().getPath());
                stepInfo.addLog("Failed to delete file " + file.getFile().getName());
            }
        }

        return notDeletedList;
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        int success, fail, totalFiles;
        StringListData notDeletedList;
        String2Number successOrFail = new String2Number();
        StepResult result = null;

        try {
            FileListData filesList = context.getDataValue(IO.FILES_LIST.getName(), FileListData.class);
            totalFiles = filesList.getData().size();

            stepInfo.addLog("About to start delete " + totalFiles + " files");
            notDeletedList = getNotDeletedFiles(stepInfo, filesList);

            fail = notDeletedList.getData().size();
            success = filesList.getData().size() - notDeletedList.getData().size();

            successOrFail.add("Successfully deleted", success);
            successOrFail.add("Fail to delete", fail);

            context.storeDataValue(IO.DELETED_LIST.getName(), notDeletedList);
            context.storeDataValue(IO.DELETION_STATS.getName(), successOrFail);

            if (totalFiles == 0) {
                stepInfo.addLog("File list is empty");
                stepInfo.setSummaryLine("File list is empty");
            }
            else if (totalFiles == fail) {
                stepInfo.addLog("No file got deleted");
                stepInfo.setSummaryLine("No file got deleted");
                result = StepResult.FAILURE;
            }
            else if (totalFiles == success) {
                stepInfo.addLog("All files got deleted");
                stepInfo.setSummaryLine("All files got deleted");
            }
            else {
                stepInfo.addLog("Not all files got deleted");
                stepInfo.setSummaryLine("Not all files got deleted");
                result = StepResult.WARNING;
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
