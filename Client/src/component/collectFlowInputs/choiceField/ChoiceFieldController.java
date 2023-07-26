package component.collectFlowInputs.choiceField;

import DTO.FreeInputDTO;
import component.collectFlowInputs.CollectFlowInputsController;
import component.collectFlowInputs.inputField.InputField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import step.api.DataNecessity;

import java.util.ArrayList;
import java.util.List;

public class ChoiceFieldController implements InputField {
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Label fieldLabel;
    private FreeInputDTO input;
    private final List<String> choices =  new ArrayList<>();
    private final SimpleBooleanProperty isChoiceBoxEmptyProperty = new SimpleBooleanProperty(true);

    private void insertOptions(String type) {
        if (type.equals("Zipper enumerator")) {
            this.choices.add("ZIP");
            this.choices.add("UNZIP");
        }
        else if (type.equals("Protocol enumerator")) {
            this.choices.add("http");
            this.choices.add("https");
        }
        else {
            this.choiceBox.setValue("GET");
            this.choices.add("GET");
            this.choices.add("PUT");
            this.choices.add("POST");
            this.choices.add("DELETE");
        }
    }

    @FXML
    public void initialize() {
        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            isChoiceBoxEmptyProperty.set(newValue.isEmpty());
        });
    }

    @Override
    public void init(CollectFlowInputsController parentController, FreeInputDTO input) {
        this.insertOptions(input.getTypeName());
        this.choiceBox.getItems().addAll(choices);

        this.input = input;
        this.fieldLabel.setText(input.getName() + " (" + input.getNecessity().toString() + ")");
        if (input.getData() != null) {
            choiceBox.setValue(input.getData().toString());
        }
        if (input.getIsInitialInput())
            choiceBox.setDisable(true);
    }

    @Override
    public Label getFieldLabel() {
        return this.fieldLabel;
    }
    @Override
    public String getInputData() {
        return this.choiceBox.getValue();
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
        return this.isChoiceBoxEmptyProperty;
    }
}
