package component.flowDetails.stepData;

import DTO.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class StepDataController {
    @FXML private Label nameLabel;
    @FXML private TextArea InputsTextArea;
    @FXML private TextArea outputsTextArea;

    public void setStepData(FlowDTO flowData, String stepName) {
        nameLabel.setText(stepName);
        InputsTextArea.setText(printInputs(flowData.getStepByName(stepName).getInputs()));
        outputsTextArea.setText(printOutputs(flowData.getStepByName(stepName).getOutputs()));
    }

    private String printInputs(List<StepInputDTO> inputs) {
        StringBuilder sb = new StringBuilder();
        for (StepInputDTO input : inputs) {
            sb.append(input.toString()).append("\n");
        }
        return sb.toString();
    }

    private String printOutputs(List<StepOutputDTO> outputs) {
        StringBuilder sb = new StringBuilder();
        for (StepOutputDTO input : outputs) {
            sb.append(input.toString()).append("\n");
        }
        return sb.toString();
    }
}
