//package component.continuation;
//
////import component.executionResult.expendedStepResult.ExpendedStepResultController;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.geometry.Insets;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.VBox;
//import screen.BodyController;
//
//public class Continuation extends VBox {
//    private final ListView<String> listView;
//    private BodyController bodyController;
//    private final ContinuationController continuationController;
//    private final Button continueToFlowButton = new Button("Continue to flow");
//    private final Button reRunFlowButton = new Button("ReRun flow");
//    private final SimpleBooleanProperty continuationButtonPressed;
//    private final SimpleBooleanProperty reRunButtonPressed;
//    private final SimpleStringProperty chosenFlowProperty;
//
//    public Continuation() {
//        continuationButtonPressed = new SimpleBooleanProperty(false);
//        reRunButtonPressed = new SimpleBooleanProperty(false);
//        chosenFlowProperty = new SimpleStringProperty();
//        listView = createListView();
//        continuationController = new ContinuationController(listView);
//
//        continueToFlowButton.disableProperty().bind(chosenFlowProperty.isEmpty());
//
//        getChildren().add(listView);
//
//        setFillWidth(true);
//    }
//
//    private ListView<String> createListView() {
//        ListView<String> listView = new ListView<>();
//        listView.setPrefHeight(USE_COMPUTED_SIZE);
//        listView.setPrefWidth(USE_COMPUTED_SIZE);
//
//        listView.setCellFactory(param -> {
//            ListCell<String> cell = new ListCell<String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null) {
//                        setText(null);
//                    } else {
//                        setText(item);
//                    }
//                }
//            };
//
//            cell.setOnMouseClicked(event -> {
//                if (!cell.isEmpty()) {
//                    chosenFlowProperty.set(cell.getItem());
//                }
//            });
//
//            return cell;
//        });
//
//        VBox.setMargin(listView, new Insets(10, 10, 10, 10));
//        return listView;
//    }
//
//
//    public void init(BodyController parentController) {
//        this.bodyController = parentController;
//        continuationController.init(this);
//
//        createButtons();
//
//        bodyController.getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
//                .addListener((observable, oldValue, newValue) -> {
//                    chosenFlowProperty.set("");
//                });
//    }
//
//    private void createButtons() {
//        VBox buttonContainer = new VBox();
//        VBox.setMargin(buttonContainer, new Insets(10));
//
//        continueToFlowButton.setPrefWidth(120);
//        continueToFlowButton.setOnAction(event -> handleContinuationClick());
//        buttonContainer.getChildren().add(continueToFlowButton);
//
//        getChildren().add(buttonContainer);
//    }
//
//    private void handleContinuationClick() {
//        bodyController.getFlowsExecutionComponentController().setCurrentFlowNameProperty(chosenFlowProperty.getValue());
//        chosenFlowProperty.set("");
//        listView.getItems().clear();
//
//        continuationButtonPressed.set(true);
//        continuationButtonPressed.set(false);
//    }
//
//    private void handleReRunClick() {
//        chosenFlowProperty.set("");
//        listView.getItems().clear();
//
//        reRunButtonPressed.set(true);
//        reRunButtonPressed.set(false);
//    }
//
//    public BodyController getParentController() {
//        return this.bodyController;
//    }
//
//    public ContinuationController getContinuationController() {
//        return this.continuationController;
//    }
//
//    public SimpleBooleanProperty getContinuationButtonPressed() {
//        return this.continuationButtonPressed;
//    }
//    public SimpleBooleanProperty getReRunButtonPressed() {
//        return this.reRunButtonPressed;
//    }
//
//    public SimpleStringProperty getChosenFlowProperty() {
//        return this.chosenFlowProperty;
//    }
//
//    public Button getContinueToFlowButton() {
//        return this.continueToFlowButton;
//    }
//}
