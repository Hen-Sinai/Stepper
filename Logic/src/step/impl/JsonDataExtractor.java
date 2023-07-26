package step.impl;

import DTO.FreeInputDTO;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import dd.impl.DataDefinitionRegistry;
import dd.impl.json.JsonDataDefinition;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import net.minidev.json.JSONObject;
import step.api.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

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

        try {
            JsonElement json = context.getDataValue(IO.JSON.getName(), JsonElement.class);
            String jsonPath = context.getDataValue(IO.JSON_PATH.getName(), String.class);
            String[] dataArray = jsonPath.split("\\|");
            StringBuilder value = new StringBuilder();
            StringBuilder jsonPathStr = new StringBuilder();

            for (String path : dataArray) {
                value.append(JsonPath.read(json.toString(), path.trim()).toString()).append(", ");
            }
            stepInfo.addLog("Extracting data " + jsonPath + ". Value: " + value);

            if (value.toString().equals(""))
                stepInfo.addLog("No value found for json path jsonPath");

            context.storeDataValue(IO.VALUE.getName(), value.toString());
        } catch (Exception e) {
            stepInfo.addLog(e.getMessage());
            stepInfo.setSummaryLine(e.getMessage());
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