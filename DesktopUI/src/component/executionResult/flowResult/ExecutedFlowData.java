//package component.executionResult.flowResult;
//
//import dataStructures.TableViewData;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.geometry.Insets;
//import javafx.scene.Node;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import screen.BodyController;
//import screen.flowsExecution.FlowsExecutionController;
//
//public class ExecutedFlowData extends VBox {
//    private final TableView<TableViewData> tableView;
//    private BodyController bodyController;
//    private final ExecutedFlowDataController executedFlowDataController;
//
//    public ExecutedFlowData() {
//        tableView = createTableView();
//        executedFlowDataController = new ExecutedFlowDataController(tableView);
//
//        VBox.setVgrow(this, Priority.ALWAYS);
//        getChildren().add(tableView);
//
//        setFillWidth(true); // Enable filling the available width
//    }
//
//    private TableView<TableViewData> createTableView() {
//        TableView<TableViewData> tableView = new TableView<>();
//        tableView.setPrefHeight(USE_COMPUTED_SIZE);
//        tableView.setPrefWidth(USE_COMPUTED_SIZE);
//
//        TableColumn<TableViewData, String> parameterColumn = new TableColumn<>("Parameter");
//        parameterColumn.setMaxWidth(150);
////        parameterColumn.setPrefWidth(120);
//        parameterColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
//
//        TableColumn<TableViewData, Node> dataColumn = new TableColumn<>("Data");
//        dataColumn.setMaxWidth(Double.MAX_VALUE);
//        dataColumn.setPrefWidth(360);
//        dataColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNode()));
//
//        tableView.getColumns().addAll(parameterColumn, dataColumn);
//
//        VBox.setMargin(tableView, new Insets(10, 10, 10, 10));
//
//        return tableView;
//    }
//
//
//    public void init(BodyController bodyController, boolean isActive) {
//        this.bodyController = bodyController;
//        executedFlowDataController.init(this, isActive);
//    }
//
//    public BodyController getBodyController() {
//        return this.bodyController;
//    }
//
//    public ExecutedFlowDataController getExecutedFlowDataController() {
//        return this.executedFlowDataController;
//    }
//}
