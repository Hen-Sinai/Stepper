//package component.historyExecutionResult;
//
//import DTO.FlowExecutedInfoDTO;
//import dataStructures.HistoryFlowsData;
//import dataStructures.TableViewData;
//import engineManager.EngineManager;
//import engineManager.EngineManagerImpl;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableView;
//
//import java.util.List;
//import java.util.UUID;
//
//public class HistoryExecutionResultController {
//    private final EngineManager engineManager = EngineManagerImpl.getInstance();
//    private HistoryExecutionResult parentController;
//    private final TableView<HistoryFlowsData> tableView;
//    private List<FlowExecutedInfoDTO> executionsInfo;
//    private final SimpleStringProperty chosenFlowIdProperty = new SimpleStringProperty();
//    private final SimpleStringProperty chosenFlowNameProperty = new SimpleStringProperty();
//
//    public HistoryExecutionResultController(TableView<HistoryFlowsData> tableView) {
//        this.tableView = tableView;
//    }
//
//    public void init(HistoryExecutionResult executionHistory) {
//        this.parentController = executionHistory;
//
//        parentController.getParentController().getParentController().getIsExecutionsHistoryVisible()
//                .addListener((observable, oldValue, newValue) -> {
//                    if (newValue) {
//                        showFlowData();
//                    }
//                });
//
//        // Add action to TableView rows
//        tableView.setOnMouseClicked(event -> {
//            HistoryFlowsData selectedFlow = tableView.getSelectionModel().getSelectedItem();
//            chosenFlowNameProperty.set(selectedFlow.getName());
//            chosenFlowIdProperty.set(selectedFlow.getId().toString());
//        });
//    }
//
//    private void showFlowData() {
//        this.executionsInfo = engineManager.getFlowsExecutedInfoDTO();
//
//        ObservableList<HistoryFlowsData> items = FXCollections.observableArrayList();
//        for (FlowExecutedInfoDTO executedInfo : executionsInfo) {
//            if (executedInfo.getResult() != null) {
//                items.add(new HistoryFlowsData(executedInfo.getId(), executedInfo.getName(), executedInfo.getTimeStamp(), executedInfo.getResult().toString()));
//            }
//        }
//        tableView.setItems(items);
//    }
//
//    public void filterTableData(String selectedFilter) {
//        showFlowData();
//        if (!selectedFilter.equals("All")) {
//            ObservableList<HistoryFlowsData> filteredList = FXCollections.observableArrayList();
//            for (HistoryFlowsData flow : tableView.getItems()) {
//                if (flow.getResult().equals(selectedFilter)) {
//                    filteredList.add(flow);
//                }
//            }
//            tableView.setItems(filteredList);
//        }
//    }
//
//    public SimpleStringProperty getChosenFlowIdProperty() {
//        return chosenFlowIdProperty;
//    }
//
//    public SimpleStringProperty getChosenFlowNameProperty() {
//        return chosenFlowNameProperty;
//    }
//}
