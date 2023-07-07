package screen;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import main.AppController;

import screen.executionHistory.ExecutionHistoryController;
import screen.rolesManagement.RolesManagementController;
import screen.settings.SettingsController;
import screen.statistics.StatisticsController;
import screen.usersManagement.UsersManagementController;

public class BodyController {
    private AppController parentController;
    @FXML private TabPane tabPane;
    @FXML private Tab usersManagementTab;
    @FXML private UsersManagementController usersManagementComponentController;
    @FXML private Tab rolesManagementTab;
    @FXML private RolesManagementController rolesManagementComponentController;
    @FXML private Tab executionsHistoryTab;
    @FXML private ExecutionHistoryController executionHistoryComponentController;
    @FXML private Tab statisticsTab;
    @FXML private StatisticsController statisticsComponentController;
    @FXML private Tab settingsTab;
    @FXML private SettingsController settingsComponentController;
    private FadeTransition fadeTransition;
    private final SimpleBooleanProperty isUsersManagementVisible;
    private final SimpleBooleanProperty isRolesManagementVisible;
    private final SimpleBooleanProperty isExecutionsHistoryVisible;
    private final SimpleBooleanProperty isStatisticsVisible;
    private final SimpleBooleanProperty isSettingsVisible;

    public BodyController() {
        isUsersManagementVisible = new SimpleBooleanProperty(true);
        isRolesManagementVisible = new SimpleBooleanProperty(false);
        isExecutionsHistoryVisible = new SimpleBooleanProperty(false);
        isStatisticsVisible = new SimpleBooleanProperty(false);
        isSettingsVisible = new SimpleBooleanProperty(false);
    }

    public void init() {
        usersManagementComponentController.init(this);
        rolesManagementComponentController.init(this);
        executionHistoryComponentController.init(this);
        statisticsComponentController.init(this);
        settingsComponentController.init(this);

        setTabsListener();
    }

    @FXML
    public void initialize() {
        // Create a fade transition with a duration of 500 milliseconds
        fadeTransition = new FadeTransition(Duration.millis(500));
        fadeTransition.setNode(tabPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
    }

    public void setMainController(AppController mainController) {
        this.parentController = mainController;
    }

    private void setTabsListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            // Start the fade transition when switching tabs
            if (settingsComponentController.getAnimationPick().getValue())
                fadeTransition.play();

            if (newTab == usersManagementTab) {
                isUsersManagementVisible.set(true);
                isRolesManagementVisible.set(false);
                isExecutionsHistoryVisible.set(false);
                isStatisticsVisible.set(false);
                isSettingsVisible.set(false);
            } else if (newTab == rolesManagementTab) {
                isUsersManagementVisible.set(false);
                isRolesManagementVisible.set(true);
                isExecutionsHistoryVisible.set(false);
                isStatisticsVisible.set(false);
                isSettingsVisible.set(false);
            } else if (newTab == executionsHistoryTab) {
                isUsersManagementVisible.set(false);
                isRolesManagementVisible.set(false);
                isExecutionsHistoryVisible.set(true);
                isStatisticsVisible.set(false);
                isSettingsVisible.set(false);
            } else if (newTab == statisticsTab) {
                isUsersManagementVisible.set(false);
                isRolesManagementVisible.set(false);
                isExecutionsHistoryVisible.set(false);
                isStatisticsVisible.set(true);
                isSettingsVisible.set(false);
            }
            else if (newTab == settingsTab) {
                isUsersManagementVisible.set(false);
                isRolesManagementVisible.set(false);
                isExecutionsHistoryVisible.set(false);
                isStatisticsVisible.set(false);
                isSettingsVisible.set(true);
            }
        });
    }

    public SimpleBooleanProperty getIsUsersManagementVisible() {
        return this.isStatisticsVisible;
    }
    public SimpleBooleanProperty getIsRolesManagementVisible() {
        return this.isRolesManagementVisible;
    }
    public SimpleBooleanProperty getIsExecutionsHistoryVisible() {
        return this.isExecutionsHistoryVisible;
    }
    public SimpleBooleanProperty getIsStatisticsVisible() {
        return this.isStatisticsVisible;
    }
    public SimpleBooleanProperty getIsSettingsVisible() {
        return this.isSettingsVisible;
    }
}
