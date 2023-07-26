package component.historyExecutionResult;

import DTO.FlowExecutedInfoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class HistoryExecutionResultRefresher extends TimerTask {
    private final Consumer<List<FlowExecutedInfoDTO>> flowExecutedInfoConsumer;
    private final SimpleStringProperty filterProperty;
    private int fromIndex;

    public HistoryExecutionResultRefresher(Consumer<List<FlowExecutedInfoDTO>> flowExecutedInfoConsumer, SimpleStringProperty filterProperty) {
        this.flowExecutedInfoConsumer = flowExecutedInfoConsumer;
        this.filterProperty = filterProperty;
        this.fromIndex = 0;
    }

    public void init(HistoryExecutionResultController historyExecutionResultController) {
        historyExecutionResultController.getParentController().getParentController().getParentController().
                getHeaderComponentController().getIsUserManagerStatusChangedToManagerProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (oldValue != null && oldValue != newValue) {
                        fromIndex = 0;
                    }
                });
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.HISTORY_EXECUTIONS)
                .newBuilder()
                .addQueryParameter("filter", filterProperty.getValue())
                .addQueryParameter("fromIndex", String.valueOf(fromIndex))
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
                        List<FlowExecutedInfoDTO> executionsInfo = new Gson().fromJson(response.body().string(),
                                new TypeToken<List<FlowExecutedInfoDTO>>() {
                                }.getType());
                        if (executionsInfo.size() > 0) {
                            fromIndex += executionsInfo.size();
                            Platform.runLater(() -> {
                                flowExecutedInfoConsumer.accept(executionsInfo);
                            });
                        }
                    } catch (IOException ignore) {}
                }
            }
        });
    }
}
