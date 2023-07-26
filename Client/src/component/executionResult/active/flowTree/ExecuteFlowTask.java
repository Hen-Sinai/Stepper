package component.executionResult.active.flowTree;

import DTO.FlowExecutedDataDTO;
import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import flow.stepInfo.log.Log;
import flow.stepInfo.log.LogImpl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.UUID;

public class ExecuteFlowTask extends TimerTask {
    private final UIAdapter uiAdapter;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;

    public ExecuteFlowTask(UUID flowId, UIAdapter uiAdapter, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.uiAdapter = uiAdapter;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }

    @Override
    public void run() {
        if (currentFlowId.getValue().equals(flowId.toString())) {
            String finalUrl = HttpUrl
                    .parse(Constants.FLOW_PROGRESS)
                    .newBuilder()
                    .addQueryParameter("flowId", this.flowId.toString())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            FlowExecutedDataDTO executedData = new Gson().fromJson(responseData, FlowExecutedDataDTO.class);
                            uiAdapter.update(executedData);
                            if (executedData.getResult() != null) {
                                isTaskFinished.set(true);
                                uiAdapter.removeSpinner();
                                cancel();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }
}
