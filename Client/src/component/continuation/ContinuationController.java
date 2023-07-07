package component.continuation;

import DTO.FlowDTO;
import DTO.FlowExecutedDataDTO;
import DTO.FreeInputDTO;
import DTO.StepResultDTO;
//import component.executionResult.expendedStepResult.ExpendedStepResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.BodyController;
import screen.flowsExecution.FlowsExecutionController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;

public class ContinuationController {
    private final EngineManager engineManager = EngineManagerImpl.getInstance();
    private FlowsExecutionController parentController;
    @FXML private ListView<String> continuationListView;
    @FXML private Button continueButton;
    private final SimpleBooleanProperty continuationButtonPressed = new SimpleBooleanProperty(false);
    private final SimpleStringProperty chosenFlowProperty = new SimpleStringProperty();

    public void init(FlowsExecutionController parentController) {
        this.parentController = parentController;
        continueButton.disableProperty().bind(chosenFlowProperty.isEmpty());

        parentController.getParentController().getFlowsExecutionComponentController().getFlowTreeComponentController().getIsTaskFinished()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue)
                        editData();
                });

        parentController.getParentController().getFlowsDefinitionComponentController().getIsExecuteFlowButtonClicked()
                .addListener((observable, oldValue, newValue) -> {
                    chosenFlowProperty.set("");
                });
    }

    private void editData() {
        String finalUrl = HttpUrl
                .parse(Constants.CONTINUATIONS)
                .newBuilder()
                .addQueryParameter("flowName",
                        parentController.getParentController().getFlowsExecutionComponentController().getCurrentFlowNameProperty().getValue())
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
                        String responseData = response.body().string();
                        List<String> continuations = new Gson().fromJson(responseData, new TypeToken<List<String>>(){}.getType());
                        Platform.runLater(() -> {
                            ObservableList<String> items = FXCollections.observableArrayList(continuations);
                            continuationListView.setItems(items);
                            addOnClick();
                        });
                    } catch (IOException e) {}
                }
            }
        });
    }

    private void addOnClick() {
        continuationListView.setCellFactory(param -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    chosenFlowProperty.set(cell.getItem());
                }
            });

            return cell;
        });
    }

    @FXML
    private void handleContinuationClick() {
        parentController.getParentController().getFlowsExecutionComponentController().setCurrentFlowNameProperty(chosenFlowProperty.getValue());
        chosenFlowProperty.set("");
        continuationListView.getItems().clear();

        continuationButtonPressed.set(true);
        continuationButtonPressed.set(false);
    }

    public SimpleBooleanProperty getContinuationButtonPressed() {
        return this.continuationButtonPressed;
    }

    public SimpleStringProperty getChosenFlowProperty() {
        return this.chosenFlowProperty;
    }
}
