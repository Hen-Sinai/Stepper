package component.availableFlows;

import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AvailableFlowsRefresher extends TimerTask {
    private final Consumer<FlowsNameDTO> availableFlowsConsumer;

    public AvailableFlowsRefresher(Consumer<FlowsNameDTO> availableFlowsConsumer) {
        this.availableFlowsConsumer = availableFlowsConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.AVAILABLE_FLOWS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    FlowsNameDTO names = new Gson().fromJson(response.body().charStream(), FlowsNameDTO.class);
                    availableFlowsConsumer.accept(names);
                }
            }
        });
    }
}
