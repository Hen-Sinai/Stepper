package component.collectFlowInputs.textField;

import DTO.FreeInputDTO;
import component.collectFlowInputs.CollectFlowInputsController;
import component.collectFlowInputs.inputField.InputField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import step.api.DataNecessity;

public class TextFieldController implements InputField {
    @FXML
    private Label fieldLabel;
    @FXML
    private TextField inputTextField;
    private FreeInputDTO input;

    private final SimpleBooleanProperty isInputFieldEmptyProperty;

    public TextFieldController() {
        isInputFieldEmptyProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            isInputFieldEmptyProperty.set(newValue.isEmpty());
        });
    }

    @Override
    public void init(CollectFlowInputsController parentController, FreeInputDTO input) {
        this.input = input;
        this.fieldLabel.setText(input.getName() + " (" + input.getNecessity().toString() + ")");
        this.inputTextField.setText(input.getData() != null ? input.getData().toString() : "");

        if (input.getType().equals("Number")) {
            // Create a TextFormatter with a regular expression pattern allowing only digits
            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*")) {
                    return change;
                }
                return null;
            });

            // Set the TextFormatter to the TextField
            inputTextField.setTextFormatter(textFormatter);
        }
        if (input.getIsInitialInput())
            inputTextField.setDisable(true);
    }

    @Override
    public Label getFieldLabel() {
        return this.fieldLabel;
    }

    @Override
    public String getInputData() {
        return this.inputTextField.getText();
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
        return this.isInputFieldEmptyProperty;
    }
}
