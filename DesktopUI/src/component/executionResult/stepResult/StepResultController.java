//package component.executionResult.stepResult;
//
//import DTO.StepResultDTO;
//import dataStructures.TableViewData;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TablePosition;
//import javafx.scene.control.TableView;
//import javafx.scene.input.MouseButton;
//
//import java.util.List;
//
//public class StepResultController {
//    private StepResult parentController;
//    private TableView<TableViewData> tableView;
//    private final SimpleStringProperty selectedStepProperty = new SimpleStringProperty();
//
//    public void init(StepResult parentController, TableView<TableViewData> tableView) {
//        this.parentController = parentController;
//        this.tableView = tableView;
//
//        parentController.getParentController().getParentController().getParentController()
//                .getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
//                .addListener((observable, oldValue, newValue) -> {
//                    if (newValue) {
//                        tableView.getItems().clear();
//                    }
//                });
//    }
//
//    public void renderSteps(List<StepResultDTO> steps) {
//        ObservableList<TableViewData> items = FXCollections.observableArrayList();
//        for (StepResultDTO step : steps) {
//            items.add(new TableViewData(step.getName(), new Label(step.getStepResult().toString())));
//        }
//        tableView.setItems(items);
//
//        tableView.setFixedCellSize(30); // Set a fixed cell size for proper row height
//        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(items.size()).add(30)); // Adjust table height based on the number of items
//        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
//        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
//
//        setupCellClickListener();
//    }
//
//    private void setupCellClickListener() {
//        tableView.setOnMouseClicked(event -> {
//            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
//                TablePosition<TableViewData, String> selectedCell = tableView.getSelectionModel().getSelectedCells().get(0);
//                int rowIndex = selectedCell.getRow();
//                TableColumn<TableViewData, String> column = selectedCell.getTableColumn();
//                TableViewData rowData = tableView.getItems().get(rowIndex);
//                String cellValue = column.getCellData(rowData);
//
//                selectedStepProperty.setValue(cellValue);
//            }
//        });
//    }
//
//    public SimpleStringProperty getSelectedStepProperty() {
//        return this.selectedStepProperty;
//    }
//}
