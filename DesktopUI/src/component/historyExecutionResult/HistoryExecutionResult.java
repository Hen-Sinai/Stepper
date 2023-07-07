//package component.historyExecutionResult;
//
//import dataStructures.HistoryFlowsData;
//import flow.execution.FlowExecutionResult;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import screen.executionHistory.ExecutionHistoryController;
//
//public class HistoryExecutionResult extends VBox {
//    private final TableView<HistoryFlowsData> tableView;
//    private final ComboBox<String> filterComboBox;
//    private ExecutionHistoryController parentController;
//    private final HistoryExecutionResultController historyExecutionResultController;
//    private final ObservableList<HistoryFlowsData> data;
//
//    public HistoryExecutionResult() {
//        data = FXCollections.observableArrayList();
//        tableView = createTableView();
//        filterComboBox = createFilterComboBox();
//        historyExecutionResultController = new HistoryExecutionResultController(tableView);
//
//        VBox.setVgrow(this, Priority.ALWAYS);
//
//        getChildren().addAll(filterComboBox, tableView);
//
//        setFillWidth(true); // Enable filling the available width
//    }
//
//    private TableView<HistoryFlowsData> createTableView() {
//        TableView<HistoryFlowsData> tableView = new TableView<>();
//        tableView.setPrefHeight(USE_COMPUTED_SIZE);
//        tableView.setPrefWidth(USE_COMPUTED_SIZE);
//
//        TableColumn<HistoryFlowsData, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setMaxWidth(150);
//        nameColumn.setPrefWidth(80);
//        nameColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
//
//        TableColumn<HistoryFlowsData, String> timeColumn = new TableColumn<>("Time");
//        timeColumn.setMaxWidth(Double.MAX_VALUE);
//        timeColumn.setPrefWidth(200);
//        timeColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTime()));
//
//        TableColumn<HistoryFlowsData, String> resultColumn = new TableColumn<>("Result");
//        resultColumn.setMaxWidth(Double.MAX_VALUE);
//        resultColumn.setPrefWidth(200);
//        resultColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getResult()));
//
//        tableView.getColumns().addAll(nameColumn, timeColumn, resultColumn);
//
//        VBox.setMargin(tableView, new Insets(10, 10, 10, 10));
//
//        return tableView;
//    }
//
//    private ComboBox<String> createFilterComboBox() {
//        ComboBox<String> comboBox = new ComboBox<>();
//        comboBox.getItems().addAll("All", FlowExecutionResult.SUCCESS.toString()
//                , FlowExecutionResult.WARNING.toString(), FlowExecutionResult.FAILURE.toString());
//        comboBox.setValue("All"); // Set the default filter option
//
//        comboBox.setOnAction(event -> {
//            String selectedFilter = comboBox.getValue();
//            historyExecutionResultController.filterTableData(selectedFilter);
//        });
//
//        return comboBox;
//    }
//
//    public void init(ExecutionHistoryController parentController) {
//        this.parentController = parentController;
//        historyExecutionResultController.init(this);
//    }
//
//    public ExecutionHistoryController getParentController() {
//        return this.parentController;
//    }
//
//    public HistoryExecutionResultController getHistoryExecutionResultController() {
//        return this.historyExecutionResultController;
//    }
//
//    public void setData(ObservableList<HistoryFlowsData> data) {
//        this.data.setAll(data);
//        tableView.setItems(this.data);
//    }
//}
