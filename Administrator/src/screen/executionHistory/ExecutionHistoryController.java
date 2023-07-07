package screen.executionHistory;

import component.executionResult.flowResult.ExecutedFlowDataController;
import component.executionResult.flowTree.FlowTreeController;
import component.historyExecutionResult.HistoryExecutionResultController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import screen.BodyController;

public class ExecutionHistoryController {
    private BodyController parentController;
    @FXML private GridPane historyGridPane;
    @FXML private HistoryExecutionResultController historyExecutionResultComponentController;
    @FXML private FlowTreeController flowTreeComponentController;
    @FXML private ExecutedFlowDataController executedFlowDataComponentController;

    public void init(BodyController parentController) {
        this.parentController = parentController;

        this.historyExecutionResultComponentController.init(this);
        this.flowTreeComponentController.init(this);
        this.executedFlowDataComponentController.init(this);
    }

    public BodyController getParentController() {
        return parentController;
    }

    public HistoryExecutionResultController getHistoryExecutionResultComponentController() {
        return this.historyExecutionResultComponentController;
    }
    public FlowTreeController getFlowTreeComponentController() {
        return this.flowTreeComponentController;
    }

    public ExecutedFlowDataController getExecutedFlowDataComponentController() {
        return this.executedFlowDataComponentController;
    }
}
