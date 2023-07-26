package component.collectFlowInputs.inputField;

import DTO.FreeInputDTO;
import component.collectFlowInputs.CollectFlowInputsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import step.api.DataNecessity;

public interface InputField {
    void init(CollectFlowInputsController parentController, FreeInputDTO input);
    Label getFieldLabel();
    String getUserString();
    String getName();
    DataNecessity getNecessity();
    SimpleBooleanProperty getIsInputFieldEmptyProperty();
    Object getInputData();
}
