package component.users.userInfo.flowsList.executedFlows;

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

public class ExecutedFlowsListRefresher extends TimerTask {
    private final Consumer<List<FlowExecutedInfoDTO>> flowExecutedInfoConsumer;
    private int fromIndex;

    public ExecutedFlowsListRefresher(Consumer<List<FlowExecutedInfoDTO>> flowExecutedInfoConsumer) {
        this.flowExecutedInfoConsumer = flowExecutedInfoConsumer;
        this.fromIndex = 0;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.HISTORY_EXECUTIONS)
                .newBuilder()
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
