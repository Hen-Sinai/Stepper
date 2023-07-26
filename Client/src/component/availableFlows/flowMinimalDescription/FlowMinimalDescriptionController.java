package component.availableFlows.flowMinimalDescription;

import DTO.FlowDTO;
import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import component.availableFlows.AvailableFlowsController;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FlowMinimalDescriptionController {
    private AvailableFlowsController parentController;
    @FXML private Label nameLabel;
    @FXML private TextArea DescriptionTextArea;
    @FXML private Label AmountOfStepsLabel;
    @FXML private Label AmountOfInputsLabel;
    @FXML private Label AmountOfContinuationLabel;

    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty amountOfSteps;
    private final SimpleIntegerProperty amountOfInputs;
    private final SimpleIntegerProperty amountOfContinuation;

    public FlowMinimalDescriptionController() {
        name = new SimpleStringProperty();
        description = new SimpleStringProperty();
        amountOfSteps = new SimpleIntegerProperty();
        amountOfInputs = new SimpleIntegerProperty();
        amountOfContinuation = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        nameLabel.textProperty().bind(name);
        DescriptionTextArea.textProperty().bind(description);
        AmountOfStepsLabel.textProperty().bind(Bindings.format("%d", amountOfSteps));
        AmountOfInputsLabel.textProperty().bind(Bindings.format("%d", amountOfInputs));
        AmountOfContinuationLabel.textProperty().bind(Bindings.format("%d", amountOfContinuation));
    }

    public void init(AvailableFlowsController parentController) {
        this.parentController = parentController;
        this.parentController.getTitledPaneNameProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                handleTitledPanePressed(newValue);
            }
        });
    }

    private void handleTitledPanePressed(String name) {
        String finalUrl = HttpUrl
                .parse(Constants.FLOW_MINIMAL_DESCRIPTION)
                .newBuilder()
                .addQueryParameter("flowName", name)
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
                        FlowDTO flow = new Gson().fromJson(response.body().string(), FlowDTO.class);
                        Platform.runLater(() -> {
                            setData(flow);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void setData(FlowDTO flow) {
        this.name.set(flow.getName());
        this.description.set(flow.getDescription());
        this.amountOfSteps.set(flow.getStepsDTO().size());
        this.amountOfInputs.set(flow.getInputDTO().size());
        this.amountOfContinuation.set(flow.getContinuations().size());
    }
}
