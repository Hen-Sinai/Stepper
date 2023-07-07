//package component.executionResult.flowTree;
//
//import DTO.FlowExecutedDataDTO;
//import DTO.StepResultDTO;
//import engineManager.EngineManager;
//import engineManager.EngineManagerImpl;
//import javafx.animation.FadeTransition;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.fxml.FXML;
//import javafx.scene.Node;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeView;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Text;
//import javafx.util.Duration;
//import screen.BodyController;
//import screen.flowsExecution.FlowsExecutionController;
//
//import java.util.UUID;
//
//public class FlowTreeController {
//    private final EngineManager engineManager = EngineManagerImpl.getInstance();
//    @FXML private TreeView<String> flowTreeView;
//    private BodyController bodyController;
//    private final Logic logic;
//    private boolean isActive;
//    private final SimpleBooleanProperty isTaskFinished;
//    private final SimpleObjectProperty<TreeItem<String>> selectedItem;
//    @FXML private StackPane spinnerContainer;
//    private final ProgressIndicator spinner = new ProgressIndicator();
//
//    public FlowTreeController() {
//        this.logic = new Logic(this);
//        this.isTaskFinished = new SimpleBooleanProperty(false);
//        this.selectedItem = new SimpleObjectProperty<>();
//    }
//
//    private void applySpinnerStyles() {
//        spinner.getStyleClass().add("spinner");
//    }
//
//    public void init(BodyController bodyController, boolean isActive) {
//        this.bodyController = bodyController;
//        this.isActive = isActive;
//        flowTreeView.setOnMouseClicked(event -> selectedItem.set(flowTreeView.getSelectionModel().getSelectedItem()));
//
//        if (isActive) {
//            bodyController.getFlowsExecutionComponentController().getContinuation().getContinuationButtonPressed().addListener((observable, oldValue, newValue) -> {
//                if (newValue)
//                    flowTreeView.getRoot().getChildren().clear();
//            });
//
//            bodyController.getFlowsExecutionComponentController().getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
//                    .addListener((observable, oldValue, newValue) -> {
//                        if (newValue && flowTreeView.getRoot() != null) {
//                            flowTreeView.getRoot().getChildren().clear();
//                        }
//                        flowTreeView.setRoot(null);
//                        this.isTaskFinished.set(false);
//                        this.hideSpinner();
//                    });
//            bodyController.getFlowsExecutionComponentController().getCollectFlowInputsController().getStartButtonClickProperty()
//                    .addListener((observable, oldValue, newValue) -> {
//                        if (newValue) {
//                            this.isTaskFinished.set(false);
//                            fetchData();
//                            applySpinnerStyles();
//                            if (bodyController.getSettingsComponentController().getAnimationPick().getValue())
//                                showSpinner();
//                        }
//                    });
//            bodyController.getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    flowTreeView.getRoot().getChildren().clear();
//                    flowTreeView.setRoot(null);
//                }
//            });
//
//            bodyController.getFlowsExecutionComponentController().getContinuation().getReRunButtonPressed().addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    flowTreeView.getRoot().getChildren().clear();
//                    flowTreeView.setRoot(null);
//                }
//            });
//
//        }
//        else {
//            bodyController.getExecutionHistoryController().getHistoryExecutionResult().getHistoryExecutionResultController().getChosenFlowIdProperty()
//                    .addListener((observable, oldValue, newValue) -> {
//                        if (!newValue.equals("")) {
//                            fetchData();
//                        }
//                    });
//        }
//    }
//
//
//    private void fetchData() {
//        if (isActive) {
//            TreeItem<String> root = new TreeItem<>(bodyController.getFlowsExecutionComponentController().getCurrentFlowNameProperty().getValue());
//            this.flowTreeView.setRoot(root);
//            UIAdapter uiAdapter = createUIAdapter();
//            logic.fetchData(
//                    UUID.fromString(bodyController.getFlowsExecutionComponentController().getExecutedFlowID().getValue()),
//                    uiAdapter, bodyController.getFlowsExecutionComponentController().getExecutedFlowID(), isTaskFinished);
//        }
//        else {
//            TreeItem<String> root = new TreeItem<>(
//                    this.bodyController.getExecutionHistoryController().getHistoryExecutionResult()
//                            .getHistoryExecutionResultController().getChosenFlowNameProperty().getValue()
//            );
//            this.flowTreeView.setRoot(root);
//            addExecutedFlowData(engineManager.getFlowExecutedDataDTO(UUID.fromString(this.bodyController.getExecutionHistoryController()
//                    .getHistoryExecutionResult().getHistoryExecutionResultController().getChosenFlowIdProperty().getValue())));
//        }
//        this.flowTreeView.getRoot().setExpanded(true);
//    }
//
//    private UIAdapter createUIAdapter() {
//        return new UIAdapter(
//                this::addExecutedFlowData,
//                this::hideSpinner
//        );
//    }
//
//    private void addExecutedFlowData(FlowExecutedDataDTO executedData) {
//        boolean found;
//        for (StepResultDTO step : executedData.getStepsList()) {
//            found = false;
//            for (TreeItem<String> item : this.flowTreeView.getRoot().getChildren()) {
//                if (item.getValue().equals(step.getName())) {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                TreeItem<String> newTreeItem = new TreeItem<>(step.getName());
//                newTreeItem.setGraphic(createColoredText(step.getStepResult().toString()));
//
//                // Apply fade-in effect to the newTreeItem
//                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), newTreeItem.getGraphic());
//                fadeTransition.setFromValue(0.0);
//                fadeTransition.setToValue(1.0);
//                fadeTransition.play();
//
//                this.flowTreeView.getRoot().getChildren().add(newTreeItem);
//            }
//        }
//    }
//
//
//    private Node createColoredText(String result) {
//        Text text = new Text(result);
//        text.setFill(getColorForStepResult(result));
//        return text;
//    }
//
//    private Color getColorForStepResult(String result) {
//        switch (result) {
//            case "SUCCESS":
//                return Color.GREEN;
//            case "FAILURE":
//                return Color.RED;
//            case "WARNING":
//                return Color.ORANGE;
//            default:
//                return Color.BLACK;
//        }
//    }
//
//    public SimpleBooleanProperty getIsTaskFinished() {
//        return this.isTaskFinished;
//    }
//
//    public SimpleObjectProperty<TreeItem<String>> getSelectedItem() {
//        return this.selectedItem;
//    }
//
//    private void showSpinner() {
//        spinnerContainer.getChildren().add(spinner);
//    }
//
//    private void hideSpinner() {
//        spinnerContainer.getChildren().remove(spinner);
//    }
//}
