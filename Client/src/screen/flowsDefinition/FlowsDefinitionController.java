package screen.flowsDefinition;

import component.availableFlows.AvailableFlowsController;
import component.flowDetails.FlowDetailsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import screen.BodyController;

public class FlowsDefinitionController {
    private BodyController parentComponent;
    @FXML private GridPane flowsDefinitionPane;
    @FXML private Button executeFlowButton;
    @FXML private AvailableFlowsController availableFlowsComponentController;
    @FXML private FlowDetailsController flowDetailsComponentController;
    private final SimpleBooleanProperty isExecuteFlowButtonShown;
    private final SimpleBooleanProperty isExecuteFlowButtonClicked;

    public FlowsDefinitionController() {
        isExecuteFlowButtonShown = new SimpleBooleanProperty(false);
        isExecuteFlowButtonClicked = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        executeFlowButton.visibleProperty().bind(isExecuteFlowButtonShown);
    }

    public void init(BodyController bodyController) {
        this.parentComponent = bodyController;
        this.availableFlowsComponentController.init(this);
        this.flowDetailsComponentController.init(this);

        availableFlowsComponentController.getTitledPaneNameProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                isExecuteFlowButtonShown.set(true);
            }
        });
    }

    public AvailableFlowsController getAvailableFlowsController() {
        return this.availableFlowsComponentController;
    }

    public FlowDetailsController getFlowDetailsController() {
        return this.flowDetailsComponentController;
    }

    public BodyController getParentComponent() {
        return parentComponent;
    }

    public SimpleBooleanProperty getIsExecuteFlowButtonShown() {
        return this.isExecuteFlowButtonShown;
    }

    public SimpleBooleanProperty getIsExecuteFlowButtonClicked() {
        return this.isExecuteFlowButtonClicked;
    }

    @FXML
    public void onPressExecuteFlow() {
        parentComponent.setTabPane(parentComponent.getFlowsExecutionTab());
        isExecuteFlowButtonClicked.set(true);
        isExecuteFlowButtonClicked.set(false);
    }
}
