//package component.executionResult.stepResult;
//
//import DTO.StepDTO;
//import DTO.StepResultDTO;
////import component.executionResult.flowResult.ExecutedFlowDataController;
//import dataStructures.TableViewData;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.scene.Node;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import screen.flowsExecution.FlowsExecutionController;
//
//import java.util.List;
//
//public class StepResult extends VBox {
//    private final TableView<TableViewData> tableView;
//    private ExecutedFlowDataController parentController;
//    private final StepResultController stepResultController = new StepResultController();
//
//    public StepResult() {
//        tableView = createTableView();
//
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
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
//        TableColumn<TableViewData, String> parameterColumn = new TableColumn<>("Name");
//        parameterColumn.setMaxWidth(150);
//        parameterColumn.setPrefWidth(150);
//        parameterColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getName()));
//
//        TableColumn<TableViewData, Node> dataColumn = new TableColumn<>("Result");
//        dataColumn.setMaxWidth(Double.MAX_VALUE);
//        dataColumn.setPrefWidth(150);
//        parameterColumn.setMaxWidth(150);
//        dataColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNode()));
//
//        tableView.getColumns().addAll(parameterColumn, dataColumn);
//
//        return tableView;
//    }
//
//    public void init(ExecutedFlowDataController parentController) {
//        this.parentController = parentController;
//        stepResultController.init(this, tableView);
//    }
//
//    public void renderSteps(List<StepResultDTO> steps) {
//        stepResultController.renderSteps(steps);
//    }
//
//    public ExecutedFlowDataController getParentController() {
//        return this.parentController;
//    }
//
//    public StepResultController geStepResultController() {
//        return this.stepResultController;
//    }
//}
