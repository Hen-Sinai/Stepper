package component.collectFlowInputs.routeField;

import DTO.FreeInputDTO;
import Exceptions.*;
import component.collectFlowInputs.CollectFlowInputsController;
import component.collectFlowInputs.inputField.InputField;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import step.api.DataNecessity;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class RouteFieldController implements InputField {
    @FXML private Label fieldLabel;
    @FXML private VBox routeInputComponent;
    @FXML private RadioButton folderRadioButton;
    @FXML private RadioButton fileRadioButton;
    @FXML private Button chooseButton;
    @FXML private TextField routeTextField;
    private final EngineManager engineManager = EngineManagerImpl.getInstance();
    private CollectFlowInputsController parentController;
    private FreeInputDTO input;
    private final SimpleStringProperty radioButtonChoseProperty;
    private final SimpleBooleanProperty isTextFieldEmptyProperty;

    public RouteFieldController() {
        radioButtonChoseProperty = new SimpleStringProperty();
        isTextFieldEmptyProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        chooseButton.disableProperty().bind(radioButtonChoseProperty.isEmpty());

        ToggleGroup toggleGroup = new ToggleGroup();
        folderRadioButton.setToggleGroup(toggleGroup);
        fileRadioButton.setToggleGroup(toggleGroup);
    }

    @FXML
    private void handleRadioButtonAction() {
        if (folderRadioButton.isSelected()) {
            radioButtonChoseProperty.set(folderRadioButton.getText());
        } else if (fileRadioButton.isSelected()) {
            radioButtonChoseProperty.set(fileRadioButton.getText());
        }
    }

    @FXML
    private void handleChooseButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        if (radioButtonChoseProperty.getValue().equals("Folder"))
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*"));
        else
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(this.parentController.getParentController().getParentController().getParentController().getPrimaryStage());
        if (selectedFile == null) {
            return;
        }
//        try {
////            engineManager.loadXmlFile(selectedFile.getAbsolutePath());
//            this.routeTextField.setText(selectedFile.getAbsolutePath());
//        } catch (JAXBException | StepNotExist | OutputNameNotUnique | FlowNameExist | NoXmlFormat | IOException |
//                 StepNameNotUnique | UserInputNotFriendly | DataNotExistCustomMapping | CustomDataNotmatch |
//                 ReferenceToForwardStep | DataNotExistFlowLevelAliasing | FlowOutputNotExist |
//                 UserInputTypeCollision | InitialInputValueNotExist | FlowNotExist | DataNotExistContinuation |
//                 InitialInputValueTypeNotMatch e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void init(CollectFlowInputsController parentController, FreeInputDTO input) {
        this.parentController = parentController;
        this.input = input;
        this.fieldLabel.setText(input.getName() + " (" + input.getNecessity().toString() + ")");

    }
    @Override
    public Label getFieldLabel() {
        return this.fieldLabel;
    }
    @Override
    public String getInputData() {
        return this.routeTextField.getText();
    }
    @Override
    public String getUserString() {
        return this.input.getUserString();
    }
    @Override
    public String getName() {
        return this.input.getName();
    }
    @Override
    public DataNecessity getNecessity() {
        return this.input.getNecessity();
    }
    @Override
    public SimpleBooleanProperty getIsInputFieldEmptyProperty() {
        return this.isTextFieldEmptyProperty;
    }
}