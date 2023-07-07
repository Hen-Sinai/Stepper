//package screen.settings;
//
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.fxml.FXML;
//import javafx.scene.control.ChoiceBox;
//import screen.BodyController;
//
//public class SettingsController {
//    @FXML private ChoiceBox<String> animationChoiceBox;
//    private BodyController parentController;
//    private final SimpleStringProperty skinPick = new SimpleStringProperty();
//    private final SimpleBooleanProperty animationPick = new SimpleBooleanProperty(true);
//
//    @FXML
//    public void initialize() {
//        animationChoiceBox.getItems().addAll("Enable", "Disable");
//        animationChoiceBox.setValue("Enable");
//        animationChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            boolean animationEnabled = newValue.equals("Enable");
//            animationPick.set(animationEnabled);
//        });
//    }
//
//    public void init(BodyController bodyController) {
//        this.parentController = bodyController;
//
//
//    }
//
//    public SimpleBooleanProperty getAnimationPick() {
//        return this.animationPick;
//    }
//
//    public SimpleStringProperty getSkinPick() {
//        return this.skinPick;
//    }
//}
