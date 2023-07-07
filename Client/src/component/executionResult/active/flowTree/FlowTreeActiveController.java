package component.executionResult.active.flowTree;

import DTO.FlowExecutedDataDTO;
import DTO.StepResultDTO;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import screen.flowsExecution.FlowsExecutionController;

import java.util.UUID;

public class FlowTreeActiveController {
    @FXML private TreeView<String> flowTreeView;
    private FlowsExecutionController parentController;
    private Logic logic;
    private final SimpleBooleanProperty isTaskFinished = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<TreeItem<String>> selectedItem = new SimpleObjectProperty<>();
    @FXML private StackPane spinnerContainer;
    private final ProgressIndicator spinner = new ProgressIndicator();

    private void applySpinnerStyles() {
        spinner.getStyleClass().add("spinner");
    }

    public void init(FlowsExecutionController parentController) {
        this.parentController = parentController;
        this.logic = new Logic(this);
        flowTreeView.setOnMouseClicked(event -> selectedItem.set(flowTreeView.getSelectionModel().getSelectedItem()));

        parentController.getContinuationController().getContinuationButtonPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                flowTreeView.getRoot().getChildren().clear();
                flowTreeView.setRoot(null);
            }
        });

        parentController.getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue && flowTreeView.getRoot() != null) {
                        flowTreeView.getRoot().getChildren().clear();
                    }
                    flowTreeView.setRoot(null);
                    this.isTaskFinished.set(false);
                    this.hideSpinner();
                });

        parentController.getCollectFlowInputsController().getStartButtonClickProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        this.isTaskFinished.set(false);
                        fetchData();
                        applySpinnerStyles();
                        if (parentController.getParentController().getSettingsComponentController().getAnimationPick().getValue())
                            showSpinner();
                    }
                });

        parentController.getParentController().getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                flowTreeView.getRoot().getChildren().clear();
                flowTreeView.setRoot(null);
            }
        });
    }


    private void fetchData() {
        TreeItem<String> root = new TreeItem<>(parentController.getCurrentFlowNameProperty().getValue());
        this.flowTreeView.setRoot(root);
        UIAdapter uiAdapter = createUIAdapter();
        logic.fetchData(
                UUID.fromString(parentController.getExecutedFlowID().getValue()),
                uiAdapter, parentController.getExecutedFlowID(), isTaskFinished);
        this.flowTreeView.getRoot().setExpanded(true);
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                this::addExecutedFlowData,
                this::hideSpinner
        );
    }

    private void addExecutedFlowData(FlowExecutedDataDTO executedData) {
        boolean found;
        for (StepResultDTO step : executedData.getStepsList()) {
            found = false;
            for (TreeItem<String> item : this.flowTreeView.getRoot().getChildren()) {
                if (item.getValue().equals(step.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> newTreeItem = new TreeItem<>(step.getName());
                newTreeItem.setGraphic(createColoredText(step.getStepResult().toString()));

                // Apply fade-in effect to the newTreeItem
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), newTreeItem.getGraphic());
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();

                this.flowTreeView.getRoot().getChildren().add(newTreeItem);
            }
        }
    }


    private Node createColoredText(String result) {
        Text text = new Text(result);
        text.setFill(getColorForStepResult(result));
        return text;
    }

    private Color getColorForStepResult(String result) {
        switch (result) {
            case "SUCCESS":
                return Color.GREEN;
            case "FAILURE":
                return Color.RED;
            case "WARNING":
                return Color.ORANGE;
            default:
                return Color.BLACK;
        }
    }

    public SimpleBooleanProperty getIsTaskFinished() {
        return this.isTaskFinished;
    }

    public SimpleObjectProperty<TreeItem<String>> getSelectedItem() {
        return this.selectedItem;
    }

    private void showSpinner() {
        spinnerContainer.getChildren().add(spinner);
    }

    private void hideSpinner() {
        spinnerContainer.getChildren().remove(spinner);
    }
}
