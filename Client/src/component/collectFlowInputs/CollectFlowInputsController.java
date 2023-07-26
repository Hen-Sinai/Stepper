package component.collectFlowInputs;

import DTO.ExecuteDataDTO;
import DTO.FreeInputDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.collectFlowInputs.inputField.InputField;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import screen.flowsExecution.FlowsExecutionController;
import step.api.DataNecessity;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

public class CollectFlowInputsController {
    private FlowsExecutionController parentController;
    @FXML private HBox inputsHBox;
    @FXML private Label errorLabel;
    private List<FreeInputDTO> inputs;
    private final List<VBox> vBoxList = new ArrayList<>();
    private int amountOfInputField;
    private Button button;
    private final SimpleBooleanProperty startButtonClickProperty = new SimpleBooleanProperty(false);


    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    public void init(FlowsExecutionController collectFlowInputs) {
        this.parentController = collectFlowInputs;

        initListeners();
    }

    public void createInputsField(SimpleStringProperty flowName) {
        String finalUrl;
        if (parentController.getContinuationController().getContinuationButtonPressed().getValue()) {
            finalUrl = HttpUrl
                    .parse(Constants.FLOW_FREE_INPUTS)
                    .newBuilder()
                    .addQueryParameter("type", "continuation")
                    .addQueryParameter("flowId", parentController.getExecutedFlowID().getValue())
                    .addQueryParameter("flowName", parentController.getCurrentFlowNameProperty().getValue())
                    .build()
                    .toString();
        }
        else if (parentController.getParentController().getExecutionHistoryController().getIsReRunPressed().getValue()) {
            finalUrl = HttpUrl
                    .parse(Constants.FLOW_FREE_INPUTS)
                    .newBuilder()
                    .addQueryParameter("type", "rerun")
                    .addQueryParameter("flowId", parentController.getParentController().getExecutionHistoryController().
                            getHistoryExecutionResultComponentController().getChosenFlowIdProperty().getValue())
                    .addQueryParameter("flowName", parentController.getParentController().getExecutionHistoryController().
                            getHistoryExecutionResultComponentController().getChosenFlowNameProperty().getValue())
                    .build()
                    .toString();
        }
        else
            finalUrl = HttpUrl
                    .parse(Constants.FLOW_FREE_INPUTS)
                    .newBuilder()
                    .addQueryParameter("flowName", flowName.getValue())
                    .build()
                    .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        inputs = new Gson().fromJson(response.body().string(), new TypeToken<List<FreeInputDTO>>() {
                        }.getType());
                        Platform.runLater(() -> {
                            setInput(inputs);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    private void setInput(List<FreeInputDTO> inputs) {
        this.amountOfInputField = 0;
        for (FreeInputDTO input : inputs) {
            if (!input.getIsInitialInput())
                createInputFieldsWithFXML(input);
        }

        createButton();  // Add the button after creating the input fields
    }

    private void createInputFieldsWithFXML(FreeInputDTO input) {
        try {
            FXMLLoader fxmlLoader;
            VBox inputField;
            String inputOriginalName = input.getOriginalName();
            if (inputOriginalName.equals("OPERATION") || inputOriginalName.equals("PROTOCOL") || inputOriginalName.equals("METHOD"))
                fxmlLoader = new FXMLLoader(getClass().getResource("/component/collectFlowInputs/choiceField/ChoiceField.fxml"));
            else if (inputOriginalName.equals("FOLDER_NAME") || inputOriginalName.equals("FILE_NAME") ||
                    inputOriginalName.equals("SOURCE"))
                fxmlLoader = new FXMLLoader(getClass().getResource("/component/collectFlowInputs/routeField/RouteField.fxml"));
            else
                fxmlLoader = new FXMLLoader(getClass().getResource("/component/collectFlowInputs/textField/TextField.fxml"));

            inputField = fxmlLoader.load();
            InputField controller = fxmlLoader.getController();
            controller.init(this, input);
            inputField.getProperties().put("controller", controller);

            VBox.setVgrow(inputField, Priority.NEVER);  // Prevent vertical expansion
            VBox.setMargin(inputField, new Insets(10.0));  // Add margin if desired

            vBoxList.add(inputField);

            if (input.getNecessity() == DataNecessity.MANDATORY && !input.getIsInitialInput() && input.getData() == null)
                this.amountOfInputField++;

            inputsHBox.getChildren().add(inputField);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createButton() {
        button = new Button("Start!");
        VBox.setMargin(button, new Insets(10.0));  // Add margin if desired

        VBox buttonContainer = new VBox(button);
        buttonContainer.setAlignment(Pos.BOTTOM_CENTER);  // Center the button vertically and horizontally
        VBox.setVgrow(buttonContainer, Priority.ALWAYS);  // Make the button container take available vertical space

        button.setOnAction(event -> handleButtonClick());
        if (amountOfInputField > 0)
            button.setDisable(true);

        for (VBox input : this.vBoxList) {
            InputField controller = (InputField) input.getProperties().get("controller");
            if (controller.getNecessity() == DataNecessity.MANDATORY) {
                SimpleBooleanProperty isInputFieldEmpty = controller.getIsInputFieldEmptyProperty();
                isInputFieldEmpty.addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        amountOfInputField--;
                        if (amountOfInputField == 0)
                            button.setDisable(false);
                    }
                    else {
                        amountOfInputField++;
                        button.setDisable(true);
                    }
                });
            }
        }
        inputsHBox.getChildren().add(buttonContainer);
    }

    private void handleButtonClick() {
        Map<String, Object> dataValues = getDataValues();
        ExecuteDataDTO executeDataDTO = new ExecuteDataDTO(parentController.getCurrentFlowNameProperty().getValue(),
                dataValues);

        String finalUrl = HttpUrl
                .parse(Constants.EXECUTE_FLOW)
                .newBuilder()
                .build()
                .toString();
        String executionDataJson = new Gson().toJson(executeDataDTO);
        RequestBody body = RequestBody.create(executionDataJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorLabel.setText(e.getMessage());
                    errorLabel.visibleProperty().set(true);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    if (response.isSuccessful()) {
                        String flowId = response.body().string();
                        Platform.runLater(() -> {
                            parentController.setExecutedFlowID(UUID.fromString(flowId));
                            startButtonClickProperty.set(true);
                            errorLabel.visibleProperty().set(false);
                            button.setDisable(true);
                        });
                    }
                    else if (response.code() == 400) {
                        Platform.runLater(() -> {
                            try {
                                errorLabel.setText(response.body().string());
                                errorLabel.visibleProperty().set(true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, Object> getDataValues() {
        InputField controller;
        Map<String, Object> dataValues = new HashMap<>();

        for (VBox input : vBoxList) {
            controller = (InputField) input.getProperties().get("controller");
            String labelValue = controller.getName();
            Object value = controller.getInputData();
            dataValues.put(labelValue, value.equals("") ? null : value);
        }
        for (FreeInputDTO input : inputs) {
            if (input.getIsInitialInput())
                dataValues.put(input.getName(), input.getData());
        }

        return dataValues;
    }

    public FlowsExecutionController getParentController() {
        return this.parentController;
    }

    public List<VBox> getVBoxList() {
        return this.vBoxList;
    }

    public SimpleBooleanProperty getStartButtonClickProperty() {
        return this.startButtonClickProperty;
    }

    private void initListeners() {
        parentController.getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        vBoxList.clear();
                        inputsHBox.getChildren().clear();
                        createInputsField(parentController.getCurrentFlowNameProperty());
                        startButtonClickProperty.set(false);
                    }
                });

        parentController.getContinuationController().getContinuationButtonPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vBoxList.clear();
                inputsHBox.getChildren().clear();
                createInputsField(parentController.getCurrentFlowNameProperty());
                startButtonClickProperty.set(false);
            }
        });

        parentController.getParentController().getExecutionHistoryController().getIsReRunPressed().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vBoxList.clear();
                inputsHBox.getChildren().clear();
                createInputsField(parentController.getCurrentFlowNameProperty());
                startButtonClickProperty.set(false);
            }
        });
    }
}
