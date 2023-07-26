package component.flowDetails;

import DTO.FlowDTO;
import DTO.FlowsNameDTO;
import DTO.FreeInputDTO;
import DTO.OutputDTO;
import com.google.gson.Gson;
import component.flowDetails.stepsInFlowAccordion.StepsInFlowAccordionController;
import dataStructures.TableViewData;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.AppController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.flowsDefinition.FlowsDefinitionController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlowDetailsController {
    @FXML private TableView<TableViewData> flowDetailsTableView;
    @FXML private TableColumn<TableViewData, String> parameterColumn;
    @FXML private TableColumn<TableViewData, Object> dataColumn;

    public void init(FlowsDefinitionController parentController) {
        parentController.getAvailableFlowsController().getTitledPaneNameProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                showFlowData(newValue);
            }
        });
    }

    private void showFlowData(String flowName) {
        String finalUrl = HttpUrl
                .parse(Constants.FLOW_DETAILS)
                .newBuilder()
                .addQueryParameter("flowName", flowName)
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
                        FlowDTO flowData = new Gson().fromJson(response.body().string(), FlowDTO.class);
                        Platform.runLater(() -> {
                            createTableView(flowData);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    private void createTableView(FlowDTO flowData) {
        try {
            FXMLLoader stepsInFlowAccordion = new FXMLLoader(getClass().getResource("/component/flowDetails/stepsInFlowAccordion/StepsInFlowAccordion.fxml"));
            Parent stepsInFlowAccordionRoot = stepsInFlowAccordion.load();
            StepsInFlowAccordionController stepsInFlowAccordionController = stepsInFlowAccordion.getController();
            stepsInFlowAccordionController.init(flowData);

            // Create and populate the data for the FlowDetails component
            ObservableList<TableViewData> items = FXCollections.observableArrayList();
            items.add(new TableViewData("Name", new Label(flowData.getName())));
            items.add(new TableViewData("Description", createTextArea(flowData.getDescription())));
            items.add(new TableViewData("Formal outputs", createTextArea(flowData.getFormalOutputs().toString())));
            items.add(new TableViewData("Readonly", new Label(String.valueOf(flowData.getIsReadonly()))));
            items.add(new TableViewData("Steps", stepsInFlowAccordionRoot));
            items.add(new TableViewData("Inputs", new Label(printInputs(flowData.getInputDTO()))));
            items.add(new TableViewData("Outputs", new Label(printOutputs(flowData.getOutputDTO()))));

            flowDetailsTableView.setItems(items);

            // Set the cell value factories for the columns
            parameterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            dataColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNode()));
        } catch (Exception ignore) {}
    }


    private TextArea createTextArea(String textInput) {
        TextArea textArea = new TextArea(textInput);
        textArea.setWrapText(true);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
        textArea.setEditable(false);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);

        Text text = new Text();
        text.setFont(textArea.getFont());
        text.setText(textInput);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        double lineHeight = text.getLineSpacing();
        int prefRowCount = (int) Math.ceil((textHeight + lineHeight) / textArea.getFont().getSize());
        textArea.setPrefRowCount(prefRowCount);

        return textArea;
    }

    private String printInputs(List<FreeInputDTO> inputs) {
        StringBuilder sb = new StringBuilder();
        for (FreeInputDTO input : inputs) {
            sb.append(input.printWithAttachSteps()).append('\n');
        }
        return sb.toString();
    }

    private String printOutputs(List<OutputDTO> outputs) {
        StringBuilder sb = new StringBuilder();
        for (OutputDTO input : outputs) {
            sb.append(input.toString()).append('\n');
        }
        return sb.toString();
    }
}
