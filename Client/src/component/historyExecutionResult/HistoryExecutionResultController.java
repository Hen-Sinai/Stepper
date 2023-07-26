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
import java.util.Timer;
import java.util.UUID;

public class HistoryExecutionResultController {
    private ExecutionHistoryController2 parentController;
    @FXML private TableView<HistoryFlowsData> historyExecutionResultsTableView;
    @FXML private ComboBox<String> filterComboBox;
    private final SimpleStringProperty filterProperty = new SimpleStringProperty();

    private final SimpleStringProperty chosenFlowIdProperty = new SimpleStringProperty();
    private final SimpleStringProperty chosenFlowNameProperty = new SimpleStringProperty();

    public void init(ExecutionHistoryController2 parentController) {
        this.parentController = parentController;
        filterComboBox.getItems().addAll("All", FlowExecutionResult.SUCCESS.toString()
                , FlowExecutionResult.WARNING.toString(), FlowExecutionResult.FAILURE.toString());
        this.initTableView();
        filterComboBox.setValue("All");
        filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterProperty.setValue(newValue);
        });
        filterProperty.setValue(filterComboBox.getValue());
        startListRefresher();

        parentController.getParentController().getParentController().
                getHeaderComponentController().getIsUserManagerStatusChangedToManagerProperty().
                addListener((observable, oldValue, newValue) -> {
                    if (oldValue != null && oldValue != newValue) {
                        historyExecutionResultsTableView.getItems().clear();
                    }
                });
    }

    public void startListRefresher() {
        HistoryExecutionResultRefresher historyExecutionResultRefresher = new HistoryExecutionResultRefresher(
                this::createTableView, filterProperty);
        historyExecutionResultRefresher.init(this);
        Timer timer = new Timer();
        timer.schedule(historyExecutionResultRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private void createTableView(List<FlowExecutedInfoDTO> executionsInfo) {
        ObservableList<HistoryFlowsData> items = FXCollections.observableArrayList();
        for (FlowExecutedInfoDTO executedInfo : executionsInfo) {
            if (executedInfo.getResult() != null) {
                items.add(new HistoryFlowsData(executedInfo.getId(), executedInfo.getName(), executedInfo.getTimeStamp(),
                        executedInfo.getResult().toString(), executedInfo.getRanByUser(), executedInfo.isRanByManager()));
            }
        }

        // Add the columns to the table view
        historyExecutionResultsTableView.getItems().addAll(items);

        // Add action to TableView rows
        historyExecutionResultsTableView.setOnMouseClicked(event -> {
            HistoryFlowsData selectedFlow = historyExecutionResultsTableView.getSelectionModel().getSelectedItem();
            chosenFlowNameProperty.set(selectedFlow.getName());
            chosenFlowIdProperty.set(selectedFlow.getId().toString());
        });
    }

    private void initTableView() {
        TableColumn<HistoryFlowsData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<HistoryFlowsData, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));

        TableColumn<HistoryFlowsData, String> resultColumn = new TableColumn<>("Result");
        resultColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResult()));

        TableColumn<HistoryFlowsData, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<HistoryFlowsData, String> managerColumn = new TableColumn<>("Is Manager");
        managerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsRanByManager()));

        // Add the columns to the table view
        historyExecutionResultsTableView.getColumns().setAll(nameColumn, timeColumn, resultColumn, usernameColumn, managerColumn);
    }

    public SimpleStringProperty getChosenFlowIdProperty() {
        return chosenFlowIdProperty;
    }

    public SimpleStringProperty getChosenFlowNameProperty() {
        return chosenFlowNameProperty;
    }

    public ExecutionHistoryController2 getParentController() {
        return this.parentController;
    }
}
