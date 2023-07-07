package component.historyExecutionResult;

import DTO.FlowDTO;
import DTO.FlowExecutedInfoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataStructures.HistoryFlowsData;
import dataStructures.TableViewData;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import flow.execution.FlowExecutionResult;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.executionHistory.ExecutionHistoryController2;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;

public class HistoryExecutionResultController {
    @FXML private TableView<HistoryFlowsData> historyExecutionResultsTableView;
    @FXML private ComboBox<String> filterComboBox;
    private final SimpleStringProperty chosenFlowIdProperty = new SimpleStringProperty();
    private final SimpleStringProperty chosenFlowNameProperty = new SimpleStringProperty();

    public void init(ExecutionHistoryController2 parentController) {
        filterComboBox.getItems().addAll("All", FlowExecutionResult.SUCCESS.toString()
                , FlowExecutionResult.WARNING.toString(), FlowExecutionResult.FAILURE.toString());
        filterComboBox.setValue("All");

        parentController.getParentController().getIsExecutionsHistoryVisible()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        showFlowData();
                    }
                });

        filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            showFlowData();
        });
    }

    private void showFlowData() {
        String finalUrl = HttpUrl
                .parse(Constants.HISTORY_EXECUTIONS)
                .newBuilder()
                .addQueryParameter("filter", filterComboBox.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    List<FlowExecutedInfoDTO> executionsInfo = new Gson().fromJson(response.body().charStream(),
                            new TypeToken<List<FlowExecutedInfoDTO>>(){}.getType());
                    Platform.runLater(() -> {
                        createTableView(executionsInfo);
                    });
                }
            }
        });


    }

    private void createTableView(List<FlowExecutedInfoDTO> executionsInfo) {
        historyExecutionResultsTableView.getItems().clear();
        ObservableList<HistoryFlowsData> items = FXCollections.observableArrayList();
        for (FlowExecutedInfoDTO executedInfo : executionsInfo) {
            if (executedInfo.getResult() != null) {
                items.add(new HistoryFlowsData(executedInfo.getId(), executedInfo.getName(), executedInfo.getTimeStamp(), executedInfo.getResult().toString()));
            }
        }
        historyExecutionResultsTableView.setItems(items);

        TableColumn<HistoryFlowsData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<HistoryFlowsData, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));

        TableColumn<HistoryFlowsData, String> resultColumn = new TableColumn<>("Result");
        resultColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResult()));

        // Add the columns to the table view
        historyExecutionResultsTableView.getColumns().setAll(nameColumn, timeColumn, resultColumn);

        // Add action to TableView rows
        historyExecutionResultsTableView.setOnMouseClicked(event -> {
            HistoryFlowsData selectedFlow = historyExecutionResultsTableView.getSelectionModel().getSelectedItem();
            chosenFlowNameProperty.set(selectedFlow.getName());
            chosenFlowIdProperty.set(selectedFlow.getId().toString());
        });
    }

    public SimpleStringProperty getChosenFlowIdProperty() {
        return chosenFlowIdProperty;
    }

    public SimpleStringProperty getChosenFlowNameProperty() {
        return chosenFlowNameProperty;
    }
}
