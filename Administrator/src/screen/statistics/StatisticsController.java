package screen.statistics;

import DTO.StatsDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.stats.Stats;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.BodyController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class StatisticsController {
    private BodyController parentController;
    private Stats stepsStats;
    private Stats flowsStats;
    @FXML
    private GridPane statisticsPane;

    public void init(BodyController bodyController) {
        this.parentController = bodyController;

        stepsStats = new Stats(this, "Steps statistics");
        flowsStats = new Stats(this, "Flows statistics");

        statisticsPane.add(flowsStats, 1, 1);
        statisticsPane.add(stepsStats, 3, 1);

        parentController.getIsStatisticsVisible().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setStats();
            }
        });
    }

    private void setStats() {
        String finalUrl = HttpUrl
                .parse(Constants.STATS)
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
                    try {
                        String responseData = response.body().string();
                        Type dataType = new TypeToken<Map<String, StatsDTO>>(){}.getType();
                        Map<String, StatsDTO> stats = new Gson().fromJson(responseData, dataType);
                        Platform.runLater(() -> {
                            stepsStats.setStats(stats.get("Steps"));
                            flowsStats.setStats(stats.get("Flows"));
                        });
                    } catch (IOException e) {}
                }
            }
        });
    }
}