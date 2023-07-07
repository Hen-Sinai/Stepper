package step.impl;

import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import dd.impl.relation.RelationData;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import step.api.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PropertiesExporter extends AbstractStepDefinition {
    public PropertiesExporter() {
        super("Properties Exporter", true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.SOURCE.getName(), DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.RESULT.getName(), DataNecessity.NA, "Properties export result", DataDefinitionRegistry.STRING));
    }

    private String exportProperties(RelationData data) {
        StringBuilder dataInPropertiesFormat = new StringBuilder();
        Map<String, String> row;

        for (int i = 0; i < data.getRows().size(); i++) {
            row = data.getRow(i);
            for (String column : data.getColumns()) {
                dataInPropertiesFormat.append("Row-").append(i+1).append(".").append(column).append("=").append(row.get(column));
                dataInPropertiesFormat.append("\n");
            }
        }

        return dataInPropertiesFormat.toString();
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;
        String formattedData = "";
        int totalRows;

        try {
            RelationData data = context.getDataValue(IO.SOURCE.getName(), RelationData.class);

            totalRows = data.getRows().size();
            if (totalRows == 0) {
                stepInfo.addLog("Source data is empty");
                stepInfo.setSummaryLine("Source data is empty");
                result = StepResult.WARNING;
            }
            else {
                stepInfo.addLog("About to process " + totalRows + " lines of data");
                formattedData = exportProperties(data);
                stepInfo.addLog("Extracted total of " + totalRows);
            }
            context.storeDataValue(IO.RESULT.getName(), formattedData);
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
