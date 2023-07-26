package component.collectFlowInputs.routeField;

import DTO.FreeInputDTO;
import component.collectFlowInputs.CollectFlowInputsController;
import component.collectFlowInputs.inputField.InputField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import step.api.DataNecessity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class RouteFieldController implements InputField {
    @FXML private Label fieldLabel;
    @FXML private TextField routeTextField;
    private CollectFlowInputsController parentController;
    private FreeInputDTO input;
    private final SimpleBooleanProperty isTextFieldEmptyProperty = new SimpleBooleanProperty(true);
    private File selectedFile;

    @FXML
    private void handleChooseButtonAction() {
        JFileChooser fileChooser = new JFileChooser();
        this.routeTextField.setText(input.getData() != null ? input.getData().toString() : "");
        FileNameExtensionFilter allFilesFilter = new FileNameExtensionFilter("All Files", "*.*");
        fileChooser.setFileFilter(allFilesFilter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int ret = fileChooser.showOpenDialog(null);

        if (ret == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (selectedFile != null) {
                isTextFieldEmptyProperty.set(false);
                routeTextField.setText(selectedFile.getPath());
            }
        }
    }

    @Override
    public void init(CollectFlowInputsController parentController, FreeInputDTO input) {
        this.parentController = parentController;
        this.input = input;
        this.fieldLabel.setText(input.getName() + " (" + input.getNecessity().toString() + ")");
        this.routeTextField.setEditable(false);
        this.routeTextField.setText(input.getData() != null ? input.getData().toString() : "");
    }
    @Override
    public Label getFieldLabel() {
        return this.fieldLabel;
    }
    @Override
    public Object getInputData() {
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