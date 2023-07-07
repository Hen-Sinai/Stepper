package component.users.userInfo.flowsList.possibleFlows;

import DTO.FlowExecutedInfoDTO;
import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.users.userInfo.UserInfoController;
import dataStructures.TableViewData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.util.stream.Collectors;

public class PossibleFlowsListController {
    @FXML private ListView<String> flowsListView;

    public void init(UserInfoController userInfoController) {
        initFlows(userInfoController.getParentController().getUsersListComponentController().getChosenUser().getValue());
    }

    private void initFlows(String username) {
        String finalUrl = HttpUrl
                .parse(Constants.AVAILABLE_FLOWS)
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
                        FlowsNameDTO flowsNameDTO = new Gson().fromJson(responseData, FlowsNameDTO.class);
                        Platform.runLater(() -> {
                            flowsListView.getItems().addAll(flowsNameDTO.getFlowsNames());
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    public void updateFlows(String username) {
        String finalUrl = HttpUrl
                .parse(Constants.AVAILABLE_FLOWS)
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
                        FlowsNameDTO flowsNameDTO = new Gson().fromJson(responseData, FlowsNameDTO.class);
                        Platform.runLater(() -> {
                            flowsListView.getItems().clear();
                            flowsListView.getItems().addAll(flowsNameDTO.getFlowsNames());
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }
}
