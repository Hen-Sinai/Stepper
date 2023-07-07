package screen;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.AppController;
import screen.executionHistory.ExecutionHistoryController2;
import screen.flowsDefinition.FlowsDefinitionController;
import screen.flowsExecution.FlowsExecutionController;
import screen.settings.SettingsController;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class BodyController {
    private AppController parentController;
    @FXML private TabPane tabPane;
    @FXML private Tab flowsDefinitionTab;
    @FXML private FlowsDefinitionController flowsDefinitionComponentController;
    @FXML private Tab flowsExecutionTab;
    @FXML private FlowsExecutionController flowsExecutionComponentController;
    @FXML private Tab executionsHistoryTab;
    @FXML private ExecutionHistoryController2 executionHistoryComponentController;
    @FXML private Tab settingsTab;
    @FXML private SettingsController settingsComponentController;
    private FadeTransition fadeTransition;

    public void setMainController(AppController mainController) {
        this.parentController = mainController;
    }

    private final SimpleBooleanProperty isFlowsExecutionEnabled;
    private final SimpleBooleanProperty isFlowsDefinitionVisible;
    private final SimpleBooleanProperty isFlowsExecutionVisible;
    private final SimpleBooleanProperty isExecutionsHistoryVisible;
    private final SimpleBooleanProperty isSettingsVisible;

    public BodyController() {
        isFlowsDefinitionVisible = new SimpleBooleanProperty(true);
        isFlowsExecutionVisible = new SimpleBooleanProperty(false);
        isExecutionsHistoryVisible = new SimpleBooleanProperty(false);
        isSettingsVisible = new SimpleBooleanProperty(false);

        isFlowsExecutionEnabled = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        flowsExecutionTab.disableProperty().bind(isFlowsExecutionEnabled.not());

        // Create a fade transition with a duration of 500 milliseconds
        fadeTransition = new FadeTransition(Duration.millis(500));
        fadeTransition.setNode(tabPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
    }

    public void init() {
        flowsDefinitionComponentController.init(this);
        flowsExecutionComponentController.init(this);
        executionHistoryComponentController.init(this);
        settingsComponentController.init(this);

//        tabPane.setDisable(false);
        setTabsListener();
    }

    public SimpleBooleanProperty getIsFlowsDefinitionVisible() {
        return this.isFlowsDefinitionVisible;
    }

    public SimpleBooleanProperty getIsFlowsExecutionVisible() {
        return this.isFlowsExecutionVisible;
    }

    public FlowsDefinitionController getFlowsDefinitionComponentController() {
        return this.flowsDefinitionComponentController;
    }

    public FlowsExecutionController getFlowsExecutionComponentController() {
        return this.flowsExecutionComponentController;
    }

    public ExecutionHistoryController2 getExecutionHistoryController() {
        return this.executionHistoryComponentController;
    }

    public SettingsController getSettingsComponentController() {
        return this.settingsComponentController;
    }

    public AppController getParentController() {
        return this.parentController;
    }

    public void setTabPane(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    public Tab getFlowsExecutionTab() {
        return this.flowsExecutionTab;
    }

    private void setTabsListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            // Start the fade transition when switching tabs
            if (settingsComponentController.getAnimationPick().getValue())
                fadeTransition.play();

            if (newTab == flowsDefinitionTab) {
                isFlowsDefinitionVisible.set(true);
                isExecutionsHistoryVisible.set(false);
                isFlowsExecutionVisible.set(false);
                isSettingsVisible.set(false);
            } else if (newTab == executionsHistoryTab) {
                isFlowsDefinitionVisible.set(false);
                isExecutionsHistoryVisible.set(true);
                isFlowsExecutionVisible.set(false);
                isSettingsVisible.set(false);
            } else if (newTab == flowsExecutionTab) {
                isFlowsDefinitionVisible.set(false);
                isExecutionsHistoryVisible.set(false);
                isFlowsExecutionVisible.set(true);
                isSettingsVisible.set(false);
            }
            else if (newTab == settingsTab) {
                isFlowsDefinitionVisible.set(false);
                isExecutionsHistoryVisible.set(false);
                isFlowsExecutionVisible.set(false);
                isSettingsVisible.set(true);
            }
        });

        flowsDefinitionComponentController.getIsExecuteFlowButtonClicked().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isFlowsExecutionEnabled.setValue(true);
                isFlowsExecutionVisible.set(true);
            }
        });
    }


    public SimpleBooleanProperty getIsExecutionsHistoryVisible() {
        return this.isExecutionsHistoryVisible;
    }
}
