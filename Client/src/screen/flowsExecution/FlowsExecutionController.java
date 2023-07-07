package screen.flowsExecution;

import component.collectFlowInputs.CollectFlowInputsController;
import component.continuation.ContinuationController;
import component.executionResult.active.flowResult.ExecutedFlowDataActiveController;
import component.executionResult.active.flowTree.FlowTreeActiveController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import screen.BodyController;

import java.util.UUID;

public class FlowsExecutionController {
    private BodyController parentController;
    @FXML private CollectFlowInputsController collectFlowInputsComponentController;
    @FXML private ContinuationController continuationComponentController;
    @FXML private FlowTreeActiveController flowTreeComponentController;
    @FXML private ExecutedFlowDataActiveController executedFlowDataComponentController;
//    @FXML private GridPane flowsExecutionPane;
    private final SimpleStringProperty executedFlowIDProperty = new SimpleStringProperty();
    private final SimpleStringProperty currentFlowNameProperty = new SimpleStringProperty();


    public void init(BodyController bodyController) {
        this.parentController = bodyController;

        this.collectFlowInputsComponentController.init(this);
        this.flowTreeComponentController.init(this);
        this.executedFlowDataComponentController.init(this);
        this.continuationComponentController.init(this);

        bodyController.getFlowsDefinitionComponentController().getAvailableFlowsController()
                .getTitledPaneNameProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                currentFlowNameProperty.set(newValue);
            }
        });

        bodyController.getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        currentFlowNameProperty.set(
                                bodyController.getExecutionHistoryController().getHistoryExecutionResultComponentController()
                                        .getChosenFlowNameProperty().getValue());
                    }
                });
    }

    public BodyController getParentController() {
        return this.parentController;
    }

    public SimpleStringProperty getCurrentFlowNameProperty() {
        return currentFlowNameProperty;
    }
    public void setCurrentFlowNameProperty(String flowName) {
        currentFlowNameProperty.setValue(flowName);
    }

    public CollectFlowInputsController getCollectFlowInputsController() {
        return this.collectFlowInputsComponentController;
    }

    public ContinuationController getContinuationController() {
        return this.continuationComponentController;
    }

    public SimpleStringProperty getExecutedFlowID() {
        return this.executedFlowIDProperty;
    }

    public void setExecutedFlowID(UUID id) {
        this.executedFlowIDProperty.set(id.toString());
    }

    public FlowTreeActiveController getFlowTreeComponentController() {
        return this.flowTreeComponentController;
    }
}
