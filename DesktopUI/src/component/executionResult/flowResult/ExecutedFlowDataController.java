//package component.executionResult.flowResult;
//
//import DTO.*;
//import component.executionResult.ioPresentation.listPresentation.ListPresentation;
//import component.executionResult.ioPresentation.mapPresentation.MapPresentation;
//import component.executionResult.ioPresentation.relationPresentation.RelationPresentation;
////import component.executionResult.stepResult.StepResult;
//import dataStructures.TableViewData;
//import dd.impl.list.ListData;
//import dd.impl.list.file.FileListData;
//import dd.impl.list.string.StringListData;
//import dd.impl.mapping.String2Number.String2Number;
//import dd.impl.relation.RelationData;
//import engineManager.EngineManager;
//import engineManager.EngineManagerImpl;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import screen.flowsExecution.FlowsExecutionController;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.UUID;
//
//public class ExecutedFlowDataController {
//    private final EngineManager engineManager = EngineManagerImpl.getInstance();
//    private ExecutedFlowData parentController;
//    private final TableView<TableViewData> tableView;
//    private final SimpleBooleanProperty isTaskFinished;
//    private boolean isActive;
//
//    public ExecutedFlowDataController(TableView<TableViewData> tableView) {
//        this.tableView = tableView;
//        this.isTaskFinished = new SimpleBooleanProperty(false);
//    }
//
//    public void init(ExecutedFlowData executedFlowData, boolean isActive) {
//        this.parentController = executedFlowData;
//        this.isTaskFinished.set(false);
//        this.isActive = isActive;
//        parentController.getBodyController().getFlowsDefinitionComponentController()
//                .init(this.parentController.getBodyController());
//
//        if (isActive) {
//            parentController.getBodyController().getFlowsExecutionComponentController().getContinuation().getContinuationButtonPressed()
//                    .addListener((observable, oldValue, newValue) -> {
//                        if (newValue)
//                            tableView.getItems().clear();
//                    });
//
//            parentController.getBodyController().getFlowsExecutionComponentController().getFlowTreeComponentController().getSelectedItem()
//                    .addListener((observable, oldValue, newValue) -> {
//                        tableView.getItems().clear();
//                        showExecutedFlowData();
//                    });
//
//            parentController.getBodyController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
//                    .addListener((observable, oldValue, newValue) -> {
//                        tableView.getItems().clear();
//                    });
//
//            parentController.getBodyController().getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    tableView.getItems().clear();
//                }
//            });
//        }
//        else {
//            parentController.getBodyController().getExecutionHistoryController().getFlowTreeComponentController().getSelectedItem()
//                    .addListener((observable, oldValue, newValue) -> {
//                        tableView.getItems().clear();
//                        showExecutedFlowData();
//                    });
//
//            parentController.getBodyController().getExecutionHistoryController().getHistoryExecutionResult()
//                    .getHistoryExecutionResultController().getChosenFlowIdProperty().addListener((observable, oldValue, newValue) -> {
//                        if (!newValue.equals("")) {
//                            tableView.getItems().clear();
//                        }
//                    });
//        }
//    }
//
//    public ExecutedFlowData getParentController() {
//        return this.parentController;
//    }
//
//    private void showExecutedFlowData() {
//        TreeItem<String> selectedTreeItemItem;
//        FlowExecutedDataDTO executedData;
//        StepResultDTO stepResult;
//        if (isActive) {
//            selectedTreeItemItem = parentController.getBodyController().getFlowsExecutionComponentController().getFlowTreeComponentController().getSelectedItem().get();
//            executedData = engineManager.getFlowExecutedDataDTO(
//                    UUID.fromString(parentController.getBodyController().getFlowsExecutionComponentController().getExecutedFlowID().getValue()));
//        }
//        else {
//            selectedTreeItemItem = parentController.getBodyController().getExecutionHistoryController().getFlowTreeComponentController().getSelectedItem().get();
//            executedData = engineManager.getFlowExecutedDataDTO(
//                    UUID.fromString(parentController.getBodyController().getExecutionHistoryController()
//                            .getHistoryExecutionResult().getHistoryExecutionResultController().getChosenFlowIdProperty().getValue()));
//        }
//        boolean isRootSelected = (selectedTreeItemItem != null && selectedTreeItemItem.getParent() == null);
//
//        ObservableList<TableViewData> items = FXCollections.observableArrayList();
//        if (isRootSelected) {
//            items.add(new TableViewData("ID", new Label(executedData.getId().toString())));
//            items.add(new TableViewData("Name", new Label(executedData.getFlowName())));
//            items.add(new TableViewData("Result", new Label(executedData.getResult() == null ? "Pending" : executedData.getResult().toString())));
//            items.add(new TableViewData("Times", createTextArea(executedData.getDuration() == null ? "Pending" :
//                    getTimes(executedData.getStartTimeStamp(), executedData.getDuration(), executedData.getFinishTimeStamp()))));
//            items.add(new TableViewData("Inputs", createTextArea(printInputs(executedData.getInputs()))));
//            printOutputs(executedData.getOutputs(), items);
//        }
//        else {
//            stepResult = executedData.getStepsMap().get(selectedTreeItemItem.getValue().toString());
//            items.add(new TableViewData("Name", new Label(stepResult.getName())));
//            items.add(new TableViewData("Result", new Label(stepResult.getStepResult().toString())));
//            items.add(new TableViewData("Summary", new Label(stepResult.getSummary())));
//            items.add(new TableViewData("Times",createTextArea(executedData.getDuration() == null ? "Pending" :
//                    getTimes(executedData.getStartTimeStamp(), executedData.getDuration(), executedData.getFinishTimeStamp()))));
//            addItemsToTableView(stepResult.getInputs(), items, "Inputs");
//            addItemsToTableView(stepResult.getOutputs(), items, "Outputs");
//            items.add(new TableViewData("Logs", createTextArea(stepResult.getLogs().toString())));
//        }
//        tableView.setItems(items);
//    }
//
//    private void addItemsToTableView(List<? extends IoAbstract> items, ObservableList<TableViewData> tableViewItems, String header) {
//        VBox vbox = new VBox();
//        for (IoAbstract item : items) {
//            if (item.getType().equals("File List") || item.getType().equals("String List")) {
//                ListView<? extends ListData<?>> listView = createListView(item);
//                if (listView != null) {
//                    addListViewToItems(item, listView, vbox);
//                }
//            }
//            else if (item.getType().equals("String to number")) {
//                MapPresentation<String, Integer> mapView = new MapPresentation<>((String2Number)item.getData());
//                addMapViewToItems(item, mapView, vbox);
//            }
//            else if (item.getType().equals("Relation")) {
//                RelationPresentation tablePresentation = new RelationPresentation((RelationData) item.getData());
//                addRelationViewToItems(item, tablePresentation, vbox);
//            }
//            else {
//                vbox.getChildren().add(createTextArea("Name: " + item.getName() + "\n" +
//                        "Type: " + item.getType() + "\n" +
//                        "Content:\n" + item.getData()));
//            }
//        }
//
//        tableViewItems.add(new TableViewData(header, vbox));
//    }
//
//    private void addRelationViewToItems(IoAbstract item, RelationPresentation relationView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
//                "Type: " + item.getType());
//        vbox.getChildren().addAll(textArea, relationView);
//    }
//
//    private void addMapViewToItems(IoAbstract item, MapPresentation<String,Integer> mapView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
//                "Type: " + item.getType());
//        vbox.getChildren().addAll(textArea, mapView);
//    }
//
//    private void addListViewToItems(IoAbstract item, ListView<? extends ListData<?>> listView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + item.getName() + "\n" +
//                "Type: " + item.getType());
//        vbox.getChildren().addAll(textArea, listView);
//    }
//
//    private ListView<? extends ListData<?>> createListView(IoAbstract item) {
//        try {
//            if (item.getName().equals("FILES_LIST")) {
//                ListData<FileListData> listData = (ListData<FileListData>) item.getData();
//                return new ListPresentation<>(listData).getListView();
//            } else if (item.getName().equals("STRING_LIST")) {
//                ListData<StringListData> listData = (ListData<StringListData>) item.getData();
//                return new ListPresentation<>(listData).getListView();
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private void printOutputs(List<OutputDTO> outputs, ObservableList<TableViewData> items) {
//        VBox vbox = new VBox();
//        for (OutputDTO output : outputs) {
//            if (output.getType().equals("File List") || output.getName().equals("String List")) {
//                ListView<? extends ListData<?>> listView = createListView(output);
//                if (listView != null) {
//                    addListViewToItems(output, listView, vbox);
//                }
//            }
//            else if (output.getType().equals("String to number")) {
//                MapPresentation<String, Integer> mapView = new MapPresentation<>((String2Number)output.getData());
//                addMapViewToItems(output, mapView, vbox);
//            }
//            else if (output.getType().equals("Relation")) {
//                RelationPresentation tablePresentation = new RelationPresentation((RelationData) output.getData());
//                addRelationViewViewToItems(output, tablePresentation, vbox);
//            }
//            else {
//                vbox.getChildren().add(createTextArea("Name: " + output.getName() + "\n" +
//                        "Type: " + output.getType() + "\n" +
//                        "Content:\n" + output.getData()));
//            }
//        }
//
//        items.add(new TableViewData("Outputs", vbox));
//    }
//
//    private void addRelationViewViewToItems(OutputDTO output, RelationPresentation relationView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
//                "Type: " + output.getType());
//        vbox.getChildren().addAll(textArea, relationView);
//    }
//
//    private void addMapViewToItems(OutputDTO output, MapPresentation<String,Integer> mapView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
//                "Type: " + output.getType());
//        vbox.getChildren().addAll(textArea, mapView);
//    }
//
//    private void addListViewToItems(OutputDTO output, ListView<? extends ListData<?>> listView, VBox vbox) {
//        TextArea textArea = createTextArea("Name: " + output.getName() + "\n" +
//                "Type: " + output.getType());
//        vbox.getChildren().addAll(textArea, listView);
//    }
//
//    private ListView<? extends ListData<?>> createListView(OutputDTO output) {
//        try {
//            if (output.getName().equals("FILES_LIST")) {
//                ListData<FileListData> listData = (ListData<FileListData>) output.getData();
//                return new ListPresentation<>(listData).getListView();
//            } else if (output.getName().equals("STRING_LIST")) {
//                ListData<StringListData> listData = (ListData<StringListData>) output.getData();
//                return new ListPresentation<>(listData).getListView();
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private TextArea createTextArea(String textInput) {
//        TextArea textArea = new TextArea(textInput);
//        textArea.setWrapText(true);
//        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
//        textArea.setEditable(false);
//        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
//
//        Text text = new Text();
//        text.setFont(textArea.getFont());
//        text.setText(textInput);
//        double textWidth = text.getLayoutBounds().getWidth();
//        double textHeight = text.getLayoutBounds().getHeight();
//        double lineHeight = text.getLineSpacing();
//        int prefRowCount = (int) Math.ceil((textHeight + lineHeight) / textArea.getFont().getSize());
//        textArea.setPrefRowCount(prefRowCount);
//
//        return textArea;
//    }
//
//    private String printInputs(List<FreeInputDTO> inputs) {
//        StringBuilder sb = new StringBuilder();
//        for (FreeInputDTO input : inputs) {
//            if (input.getData() != null)
//                sb.append(input.printWithData()).append("\n");
//        }
//        return sb.toString();
//    }
//
//    private String getTimes(String startTime, Duration Duration, String finishTime) {
//        return "Start: " + startTime + "\n" +
//                "Duration: " + Duration.toMillis() + "ms"+ "\n" +
//                "Finish: " + finishTime;
//    }
//}