package component.executionResult.active.flowResult;

import DTO.*;
import com.google.gson.Gson;
import component.executionResult.ioPresentation.listPresentation.ListPresentation;
import component.executionResult.ioPresentation.mapPresentation.MapPresentation;
import component.executionResult.ioPresentation.relationPresentation.RelationPresentation;
import dataStructures.TableViewData;
import dd.impl.list.ListData;
import dd.impl.list.file.FileListData;
import dd.impl.list.string.StringListData;
import dd.impl.mapping.String2Number.String2Number;
import dd.impl.relation.RelationData;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.flowsExecution.FlowsExecutionController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

public class ExecutedFlowDataActiveController {
    private FlowsExecutionController parentController;
    @FXML private TableView<TableViewData> flowDataTableView;
    @FXML private TableColumn<TableViewData, String> parameterColumn;
    @FXML private TableColumn<TableViewData, Object> dataColumn;
    private final SimpleBooleanProperty isTaskFinished = new SimpleBooleanProperty(false);

    public void init(FlowsExecutionController executedFlowData) {
        this.parentController = executedFlowData;
        this.isTaskFinished.set(false);

        parentController.getParentController().getFlowsDefinitionComponentController()
                .init(this.parentController.getParentController());

        parentController.getParentController().getFlowsExecutionComponentController().getContinuationController().getContinuationButtonPressed()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue)
                        flowDataTableView.getItems().clear();
                });

        parentController.getFlowTreeComponentController().getSelectedItem().addListener((observable, oldValue, newValue) -> {
            flowDataTableView.getItems().clear();
                    showExecutedFlowData();
                });

        parentController.getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
                .addListener((observable, oldValue, newValue) -> {
                    flowDataTableView.getItems().clear();
                });

        parentController.getParentController().getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                flowDataTableView.getItems().clear();
            }
        });
    }

    public FlowsExecutionController getParentController() {
        return this.parentController;
    }

    private void showExecutedFlowData() {
        String finalUrl = HttpUrl
                .parse(Constants.FLOW_FINAL_DATA)
                .newBuilder()
                .addQueryParameter("flowId", parentController.getExecutedFlowID().getValue())
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
                        Platform.runLater(() -> {
                            createTableView(executedData);
                        });
                    } catch (IOException e) {}
                }
            }
        });
    }

    private void createTableView(FlowExecutedDataDTO executedData) {
        TreeItem<String> selectedTreeItemItem;
        StepResultDTO stepResult;
        selectedTreeItemItem = parentController.getFlowTreeComponentController().getSelectedItem().get();
        boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);

        ObservableList<TableViewData> items = FXCollections.observableArrayList();
        if (isRootSelected) {
            items.add(new TableViewData("ID", new Label(executedData.getId().toString())));
            items.add(new TableViewData("Name", new Label(executedData.getFlowName())));
            items.add(new TableViewData("Result", new Label(executedData.getResult() == null ? "Pending" : executedData.getResult().toString())));
            items.add(new TableViewData("Times", createTextArea(executedData.getDuration() == null ? "Pending" :
                    getTimes(executedData.getStartTimeStamp(), executedData.getDuration(), executedData.getFinishTimeStamp()))));
            items.add(new TableViewData("Inputs", createTextArea(printInputs(executedData.getInputs()))));
            printOutputs(executedData.getOutputs(), items);
        }
        else {
            stepResult = executedData.getStepsMap().get(selectedTreeItemItem.getValue().toString());
            items.add(new TableViewData("Name", new Label(stepResult.getName())));
            items.add(new TableViewData("Result", new Label(stepResult.getStepResult().toString())));
            items.add(new TableViewData("Summary", new Label(stepResult.getSummary())));
            items.add(new TableViewData("Times",createTextArea(executedData.getDuration() == null ? "Pending" :
                    getTimes(executedData.getStartTimeStamp(), executedData.getDuration(), executedData.getFinishTimeStamp()))));
            addItemsToTableView(stepResult.getInputs(), items, "Inputs");
            addItemsToTableView(stepResult.getOutputs(), items, "Outputs");
            items.add(new TableViewData("Logs", createTextArea(stepResult.getLogs().toString())));
        }
        flowDataTableView.setItems(items);

        // Set the cell value factories for the columns
        parameterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dataColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNode()));
    }

    private void addItemsToTableView(List<? extends IoAbstract> items, ObservableList<TableViewData> tableViewItems, String header) {
        VBox vbox = new VBox();
        for (IoAbstract item : items) {
            if (item.getType().equals("File List") || item.getType().equals("String List")) {
                ListView<? extends ListData<?>> listView = createListView(item);
                if (listView != null) {
                    addListViewToItems(item, listView, vbox);
                }
            }
            else if (item.getType().equals("String to number")) {
                MapPresentation<String, Integer> mapView = new MapPresentation<>((String2Number)item.getData());
                addMapViewToItems(item, mapView, vbox);
            }
            else if (item.getType().equals("Relation")) {
                RelationPresentation tablePresentation = new RelationPresentation((LinkedHashMap) item.getData());
                addRelationViewToItems(item, tablePresentation, vbox);
            }
            else {
                vbox.getChildren().add(createTextArea("Name: " + item.getName() + "\n" +
                        "Type: " + item.getType() + "\n" +
                        "Content:\n" + item.getData()));
            }
        }

        tableViewItems.add(new TableViewData(header, vbox));
    }

    private void addRelationViewToItems(IoAbstract item, RelationPresentation relationView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
                "Type: " + item.getType());
        vbox.getChildren().addAll(textArea, relationView);
    }

    private void addMapViewToItems(IoAbstract item, MapPresentation<String,Integer> mapView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
                "Type: " + item.getType());
        vbox.getChildren().addAll(textArea, mapView);
    }

    private void addListViewToItems(IoAbstract item, ListView<? extends ListData<?>> listView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
                "Type: " + item.getType());
        vbox.getChildren().addAll(textArea, listView);
    }

    private ListView<? extends ListData<?>> createListView(IoAbstract item) {
        try {
            if (item.getName().equals("FILES_LIST")) {
                ListData<FileListData> listData = (ListData<FileListData>) item.getData();
                return new ListPresentation<>(listData).getListView();
            } else if (item.getName().equals("STRING_LIST")) {
                ListData<StringListData> listData = (ListData<StringListData>) item.getData();
                return new ListPresentation<>(listData).getListView();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void printOutputs(List<OutputDTO> outputs, ObservableList<TableViewData> items) {
        VBox vbox = new VBox();
        for (OutputDTO output : outputs) {
            if (output.getType().equals("File List") || output.getName().equals("String List")) {
                ListView<? extends ListData<?>> listView = createListView(output);
                if (listView != null) {
                    addListViewToItems(output, listView, vbox);
                }
            }
            else if (output.getType().equals("String to number")) {
                MapPresentation<String, Integer> mapView = new MapPresentation<>((String2Number)output.getData());
                addMapViewToItems(output, mapView, vbox);
            }
            else if (output.getType().equals("Relation")) {
                RelationPresentation tablePresentation = new RelationPresentation((LinkedHashMap) output.getData());
                addRelationViewViewToItems(output, tablePresentation, vbox);
            }
            else {
                vbox.getChildren().add(createTextArea("Name: " + output.getName() + "\n" +
                        "Type: " + output.getType() + "\n" +
                        "Content:\n" + output.getData()));
            }
        }

        items.add(new TableViewData("Outputs", vbox));
    }

    private void addRelationViewViewToItems(OutputDTO output, RelationPresentation relationView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
                "Type: " + output.getType());
        vbox.getChildren().addAll(textArea, relationView);
    }

    private void addMapViewToItems(OutputDTO output, MapPresentation<String,Integer> mapView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
                "Type: " + output.getType());
        vbox.getChildren().addAll(textArea, mapView);
    }

    private void addListViewToItems(OutputDTO output, ListView<? extends ListData<?>> listView, VBox vbox) {
        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
                "Type: " + output.getType());
        vbox.getChildren().addAll(textArea, listView);
    }

    private ListView<? extends ListData<?>> createListView(OutputDTO output) {
        try {
            if (output.getName().equals("FILES_LIST")) {
                ListData<FileListData> listData = (ListData<FileListData>) output.getData();
                return new ListPresentation<>(listData).getListView();
            } else if (output.getName().equals("STRING_LIST")) {
                ListData<StringListData> listData = (ListData<StringListData>) output.getData();
                return new ListPresentation<>(listData).getListView();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private TextArea createTextArea(String textInput) {
        TextArea textArea = new TextArea(textInput);
        textArea.setWrapText(true);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
        textArea.setEditable(false);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);

        Text text = new Text();
        text.setFont(textArea.getFont());
        text.setText(textInput);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        double lineHeight = text.getLineSpacing();
        int prefRowCount = (int) Math.ceil((textHeight + lineHeight) / textArea.getFont().getSize());
        textArea.setPrefRowCount(prefRowCount);

        return textArea;
    }

    private String printInputs(List<FreeInputDTO> inputs) {
        StringBuilder sb = new StringBuilder();
        for (FreeInputDTO input : inputs) {
            if (input.getData() != null)
                sb.append(input.printWithData()).append("\n");
        }
        return sb.toString();
    }

    private String getTimes(String startTime, Duration Duration, String finishTime) {
        return "Start: " + startTime + "\n" +
                "Duration: " + Duration.toMillis() + "ms"+ "\n" +
                "Finish: " + finishTime;
    }
}