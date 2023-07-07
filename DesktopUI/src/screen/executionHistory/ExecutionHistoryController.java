//package screen.executionHistory;
//
//import component.continuation.Continuation;
//import component.executionResult.flowResult.ExecutedFlowData;
//import component.executionResult.flowTree.FlowTreeController;
//import component.historyExecutionResult.HistoryExecutionResult;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.fxml.FXML;
//import javafx.scene.layout.GridPane;
//import screen.BodyController;
//import javafx.scene.control.Button;
//
//public class ExecutionHistoryController {
//    @FXML private GridPane historyGridPane;
//    @FXML private FlowTreeController flowTreeComponentController;
//    @FXML private Button reRunFlowButton;
//    private BodyController parentController;
//    private final HistoryExecutionResult historyExecutionResult = new HistoryExecutionResult();
//    private final ExecutedFlowData executedFlowData = new ExecutedFlowData();
//    private final SimpleBooleanProperty isReRunPressed = new SimpleBooleanProperty(false);
//
//    @FXML
//    public void initialize() {
//        historyGridPane.add(historyExecutionResult, 1, 1);
//        historyGridPane.add(executedFlowData, 3, 2);
//        // Set the row span and column span for executedFlowData
//        GridPane.setColumnSpan(historyExecutionResult, 4);
//        GridPane.setRowSpan(executedFlowData, 2);
//        GridPane.setColumnSpan(executedFlowData, 2);
//
//        reRunFlowButton.disableProperty().bind(historyExecutionResult.getHistoryExecutionResultController().getChosenFlowIdProperty().isEmpty());
//    }
//
//    @FXML
//    public void onPressReRun() {
//        parentController.setTabPane(parentController.getFlowsExecutionTab());
//        isReRunPressed.set(true);
//        isReRunPressed.set(false);
//    }
//
//    public void init(BodyController parentController) {
//        this.parentController = parentController;
//
//        this.historyExecutionResult.init(this);
//        this.flowTreeComponentController.init(parentController, false);
//        this.executedFlowData.init(this.parentController, false);
//    }
//
//    public BodyController getParentController() {
//        return parentController;
//    }
//
//    public HistoryExecutionResult getHistoryExecutionResult() {
//        return this.historyExecutionResult;
//    }
//
//    public FlowTreeController getFlowTreeComponentController() {
//        return this.flowTreeComponentController;
//    }
//
//    public ExecutedFlowData getExecutedFlowData() {
//        return this.executedFlowData;
//    }
//
//    public SimpleBooleanProperty getIsReRunPressed() {
//        return this.isReRunPressed;
//    }
//}
