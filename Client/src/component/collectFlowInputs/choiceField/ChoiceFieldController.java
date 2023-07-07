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
    private final List<String> choices;
    private final SimpleBooleanProperty isChoiceBoxEmptyProperty;

    public ChoiceFieldController() {
        this.choices = new ArrayList<String>() {{
            add("ZIP");
            add("UNZIP");
        }};
        isChoiceBoxEmptyProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            isChoiceBoxEmptyProperty.set(newValue.isEmpty());
        });
    }

    @Override
    public void init(CollectFlowInputsController parentController, FreeInputDTO input) {
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
