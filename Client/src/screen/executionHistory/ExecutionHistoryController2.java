package screen.executionHistory;

import component.executionResult.passive.flowResult.ExecutedFlowDataPassiveController;
import component.executionResult.passive.flowTree.FlowTreePassiveController;
import component.historyExecutionResult.HistoryExecutionResultController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import screen.BodyController;

public class ExecutionHistoryController2 {
    private BodyController parentController;
    @FXML private FlowTreePassiveController flowTreePassiveComponentController;
    @FXML private Button reRunFlowButton;
    @FXML private HistoryExecutionResultController historyExecutionResultComponentController;
    @FXML private ExecutedFlowDataPassiveController executedFlowDataPassiveComponentController;
    private final SimpleBooleanProperty isReRunPressed = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        reRunFlowButton.disableProperty().bind(historyExecutionResultComponentController.getChosenFlowIdProperty().isEmpty());
    }

    @FXML
    public void onPressReRun() {
        parentController.setTabPane(parentController.getFlowsExecutionTab());
        isReRunPressed.set(true);
        isReRunPressed.set(false);
    }

    public void init(BodyController parentController) {
        this.parentController = parentController;

        this.historyExecutionResultComponentController.init(this);
        this.flowTreePassiveComponentController.init(this);
        this.executedFlowDataPassiveComponentController.init(this);
    }

    public BodyController getParentController() {
        return parentController;
    }

    public HistoryExecutionResultController getHistoryExecutionResultComponentController() {
        return this.historyExecutionResultComponentController;
    }

    public FlowTreePassiveController getFlowTreeComponentController() {
        return this.flowTreePassiveComponentController;
    }

    public ExecutedFlowDataPassiveController getExecutedFlowDataComponentController() {
        return this.executedFlowDataPassiveComponentController;
    }

    public SimpleBooleanProperty getIsReRunPressed() {
        return this.isReRunPressed;
    }
}
