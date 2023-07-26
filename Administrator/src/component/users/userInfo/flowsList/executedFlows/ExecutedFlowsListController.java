package component.users.userInfo.flowsList.executedFlows;

import DTO.FlowExecutedInfoDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.historyExecutionResult.HistoryExecutionResultRefresher;
import component.users.userInfo.UserInfoController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.usersManagement.UsersManagementController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

public class ExecutedFlowsListController {
    @FXML
    private ListView<String> flowsListView;

    public void init(UserInfoController userInfoController) {
        initFlows(userInfoController.getParentController().getUsersListComponentController().getChosenUser().getValue());
        startListRefresher();
    }

    public void startListRefresher() {
        ExecutedFlowsListRefresher executedFlowsListRefresher = new ExecutedFlowsListRefresher(this::createListView);
        Timer timer = new Timer();
        timer.schedule(executedFlowsListRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private void initFlows(String username) {
        String finalUrl = HttpUrl
                .parse(Constants.HISTORY_EXECUTIONS)
                .newBuilder()
                .addQueryParameter("username", username)
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
                        List<FlowExecutedInfoDTO> executedFlows = new Gson().fromJson(responseData, new TypeToken<List<FlowExecutedInfoDTO>>(){}.getType());
                        Platform.runLater(() -> {
                            createListView(executedFlows);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    private void createListView(List<FlowExecutedInfoDTO> executedFlows) {
        List<String> userNamesList = executedFlows.stream()
                .map(FlowExecutedInfoDTO::getName)
                .collect(Collectors.toList());

        flowsListView.getItems().addAll(userNamesList);
    }
}
