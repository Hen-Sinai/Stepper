package step.impl;

import DTO.FlowExecutedDataDTO;
import DTO.UserDTO;
import Exceptions.NoMatchTypeException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dd.impl.DataDefinitionRegistry;
import dd.impl.relation.RelationData;
import flow.execution.context.StepExecutionContext;
import flow.stepInfo.StepInfo;
import flow.stepInfo.StepInfoManager;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import step.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

public class HttpCall extends AbstractStepDefinition {
    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private final static String HTTP = "http";
    private final static String HTTPS = "https";
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
            JsonElement body = context.getDataValue(IO.BODY.getName(), JsonElement.class);

            stepInfo.addLog("About to invoke http request <request details:\n" +
                    protocol + " | " + (method == null ? "GET" : method) + " | " + address + " | " + resource);
            if (method == null || method.equals("GET")) {
                getCall(context, stepInfo, resource, address, protocol);
            }
            else if (method.equals("PUT")) {
                putCall(context, stepInfo, resource, address, protocol, body);
            }
            else if (method.equals("POST")) {
                postCall(context, stepInfo, resource, address, protocol, body);
            }
            else if (method.equals("DELETE")) {
                deleteCall(context, stepInfo, resource, address, protocol);
            }
        } catch (IOException | RuntimeException | NoMatchTypeException e) {
            stepInfo.addLog(e.getMessage());
            result = StepResult.FAILURE;
        }

        stepInfo.setDuration(Duration.between(start, Instant.now()));
        stepInfo.setStepResult(result != null ? result : StepResult.SUCCESS);
        stepInfo.setSummaryLine(stepInfo.getSummaryLine() != null ? stepInfo.getSummaryLine() : "SUCCESS");
        stepInfo.setFinishTimeStamp();
        context.addStepInfo(stepInfo);
        context.dropStep();
        System.out.println(stepInfo.getStepResult().toString());
        return stepInfo.getStepResult();
    }

    private void getCall(StepExecutionContext context, StepInfoManager stepInfo, String resource,
                         String address, String protocol) throws RuntimeException, IOException {
        String finalUrl = HttpUrl
                .parse((protocol.equals(HTTP) ? HTTP : HTTPS) + "://" + address + resource)
                .newBuilder()
                .build()
                .toString();

        Response response = runSyncGet(finalUrl);
        try {
            stepInfo.addLog("Received Response. Status code: " + response.code());
            context.storeDataValue(IO.CODE.getName(), response.code());
            Object res = new Gson().fromJson(response.body().string(), Object.class);
            if (res != null)
                context.storeDataValue(IO.RESPONSE_BODY.getName(), res.toString());
            else
                context.storeDataValue(IO.RESPONSE_BODY.getName(), "No Body");
        } catch (NoMatchTypeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void putCall(StepExecutionContext context, StepInfoManager stepInfo, String resource, String address, String protocol, JsonElement body) throws IOException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String finalUrl = HttpUrl
                .parse((protocol.equals(HTTP) ? HTTP : HTTPS) + "://" + address + resource)
                .newBuilder()
                .build()
                .toString();
        String jsonStr = new Gson().toJson(body);
        RequestBody data = RequestBody.create(jsonStr.getBytes());

        Response response = runSyncPut(finalUrl, data);
        try {
            stepInfo.addLog("Received Response. Status code: " + response.code());
            context.storeDataValue(IO.CODE.getName(), response.code());
            Object res = new Gson().fromJson(response.body().string(), Object.class);
            if (res != null)
                context.storeDataValue(IO.RESPONSE_BODY.getName(), res.toString());
            else
                context.storeDataValue(IO.RESPONSE_BODY.getName(), "No Body");
        } catch (NoMatchTypeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void postCall(StepExecutionContext context, StepInfoManager stepInfo, String resource, String address, String protocol, JsonElement body) throws IOException {
        String finalUrl = HttpUrl
                .parse((protocol.equals(HTTP) ? HTTP : HTTPS) + "://" + address + resource)
                .newBuilder()
                .build()
                .toString();
        String jsonStr = new Gson().toJson(body);
        RequestBody data = RequestBody.create(jsonStr.getBytes());

        Response response = runSyncPost(finalUrl, data);
        try {
            stepInfo.addLog("Received Response. Status code: " + response.code());
            context.storeDataValue(IO.CODE.getName(), response.code());
            Object res = new Gson().fromJson(response.body().string(), Object.class);
            if (res != null)
                context.storeDataValue(IO.RESPONSE_BODY.getName(), res.toString());
            else
                context.storeDataValue(IO.RESPONSE_BODY.getName(), "No Body");
        } catch (NoMatchTypeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCall(StepExecutionContext context, StepInfoManager stepInfo, String resource, String address, String protocol) throws IOException {
        String finalUrl = HttpUrl
                .parse((protocol.equals(HTTP) ? HTTP : HTTPS) + "://" + address + resource)
                .newBuilder()
                .build()
                .toString();

        Response response = runSyncDelete(finalUrl);
        try {
            stepInfo.addLog("Received Response. Status code: " + response.code());
            context.storeDataValue(IO.CODE.getName(), response.code());
            Object res = new Gson().fromJson(response.body().string(), Object.class);
            if (res != null)
                context.storeDataValue(IO.RESPONSE_BODY.getName(), res.toString());
            else
                context.storeDataValue(IO.RESPONSE_BODY.getName(), "No Body");
        } catch (NoMatchTypeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response runSyncGet(String finalUrl) throws IOException {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    public static Response runSyncPost(String finalUrl, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    public static Response runSyncPut(String finalUrl, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    public static Response runSyncDelete(String finalUrl) throws IOException {
        Request request = new Request.Builder()
                .url(finalUrl)
                .delete()
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return call.execute();
    }
}