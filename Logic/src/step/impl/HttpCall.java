package step.impl;

import DTO.FlowExecutedDataDTO;
import Exceptions.NoMatchTypeException;
import dd.impl.DataDefinitionRegistry;
import dd.impl.relation.RelationData;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import javafx.application.Platform;
import okhttp3.HttpUrl;
import step.api.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class HttpCall extends AbstractStepDefinition {
    public HttpCall() {
        super("HTTP Call", false);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl(IO.RESOURCE.getName(), DataNecessity.MANDATORY, "Resource Name (include query parameters)", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.ADDRESS.getName(), DataNecessity.MANDATORY, "Domain:Port", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl(IO.PROTOCOL.getName(), DataNecessity.MANDATORY, "Protocol", DataDefinitionRegistry.PROTOCOL_ENUMERATOR));
        addInput(new DataDefinitionDeclarationImpl(IO.METHOD.getName(), DataNecessity.OPTIONAL, "Method", DataDefinitionRegistry.METHOD_ENUMERATOR));
        addInput(new DataDefinitionDeclarationImpl(IO.BODY.getName(), DataNecessity.OPTIONAL, "Request Body", DataDefinitionRegistry.JSON));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl(IO.CODE.getName(), DataNecessity.NA, "Response Code", DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl(IO.RESPONSE_BODY.getName(), DataNecessity.NA, "Response Body", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        StepInfoManager stepInfo = new StepInfo();
        Instant start = Instant.now();
        stepInfo.setStartTimeStamp();
        StepResult result = null;

        try {
            String resource = context.getDataValue(IO.RESOURCE.getName(), String.class);
            String address = context.getDataValue(IO.ADDRESS.getName(), String.class);
            String protocol = context.getDataValue(IO.PROTOCOL.getName(), String.class);
            String method = context.getDataValue(IO.METHOD.getName(), String.class);
            String body = context.getDataValue(IO.BODY.getName(), String.class);


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

    private void httpCall(String resource, String address, String protocol, String method, String body) {
        String finalUrl = HttpUrl
                .parse(address)
                .newBuilder()
                .build()
                .toString();

//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) {
//                if (response.isSuccessful()) {
//                    try {
//                        String responseData = response.body().string();
//                        FlowExecutedDataDTO executedData = new Gson().fromJson(responseData, FlowExecutedDataDTO.class);
//                        Platform.runLater(() -> {
//                            createTableView(executedData);
//                        });
//                    } catch (IOException e) {}
//                }
//            }
//        });
    }
}