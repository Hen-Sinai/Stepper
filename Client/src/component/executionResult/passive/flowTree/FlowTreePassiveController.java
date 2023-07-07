package component.executionResult.passive.flowTree;

import DTO.FlowExecutedDataDTO;
import DTO.StepResultDTO;
import com.google.gson.Gson;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.executionHistory.ExecutionHistoryController2;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class FlowTreePassiveController {
    @FXML private TreeView<String> flowTreeView;
    private ExecutionHistoryController2 parentController;
    private final SimpleObjectProperty<TreeItem<String>> selectedItem = new SimpleObjectProperty<>();

    public void init(ExecutionHistoryController2 parentController) {
        this.parentController = parentController;
        flowTreeView.setOnMouseClicked(event -> selectedItem.set(flowTreeView.getSelectionModel().getSelectedItem()));

        parentController.getHistoryExecutionResultComponentController().getChosenFlowIdProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("")) {
                        fetchData();
                    }
                });
    }


    private void fetchData() {
        TreeItem<String> root = new TreeItem<>(
                this.parentController.getHistoryExecutionResultComponentController().getChosenFlowNameProperty().getValue()
        );
        this.flowTreeView.setRoot(root);
        String finalUrl = HttpUrl
                .parse(Constants.FLOW_FINAL_DATA)
                .newBuilder()
                .addQueryParameter("flowId",
                        this.parentController.getHistoryExecutionResultComponentController().getChosenFlowIdProperty().getValue())
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
                        FlowExecutedDataDTO executedData = new Gson().fromJson(responseData, FlowExecutedDataDTO.class);
                        Platform.runLater(() -> {
                            addExecutedFlowData(executedData);
                        });
                    } catch (IOException e) {}
                }
            }
        });

        this.flowTreeView.getRoot().setExpanded(true);
    }


    private void addExecutedFlowData(FlowExecutedDataDTO executedData) {
        boolean found;
        for (StepResultDTO step : executedData.getStepsList()) {
            found = false;
            for (TreeItem<String> item : this.flowTreeView.getRoot().getChildren()) {
                if (item.getValue().equals(step.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> newTreeItem = new TreeItem<>(step.getName());
                newTreeItem.setGraphic(createColoredText(step.getStepResult().toString()));

                // Apply fade-in effect to the newTreeItem
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), newTreeItem.getGraphic());
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();

                this.flowTreeView.getRoot().getChildren().add(newTreeItem);
            }
        }
    }


    private Node createColoredText(String result) {
        Text text = new Text(result);
        text.setFill(getColorForStepResult(result));
        return text;
    }

    private Color getColorForStepResult(String result) {
        switch (result) {
            case "SUCCESS":
                return Color.GREEN;
            case "FAILURE":
                return Color.RED;
            case "WARNING":
                return Color.ORANGE;
            default:
                return Color.BLACK;
        }
    }
    public SimpleObjectProperty<TreeItem<String>> getSelectedItem() {
        return this.selectedItem;
    }
}
