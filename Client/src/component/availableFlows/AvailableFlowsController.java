package component.availableFlows;

import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import component.availableFlows.flowMinimalDescription.FlowMinimalDescriptionController;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
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
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

public class AvailableFlowsController {
    @FXML private Accordion availableFlowsAccordion;
    private FlowsDefinitionController parentController;
    private final SimpleStringProperty titledPaneName = new SimpleStringProperty();
    private final Set<String> currentNames = new HashSet<>();
    private final FlowsNameDTO flowsName = new FlowsNameDTO(new ArrayList<>());

    public void init(FlowsDefinitionController parentController) {
        this.parentController = parentController;
        this.availableFlowsAccordion.getPanes().clear();
        getDataFromEngine();

        availableFlowsAccordion.expandedPaneProperty().addListener((obs, oldExpanded, newExpanded) -> {
            if (newExpanded != null)
                this.titledPaneName.set(newExpanded.getText());
        });
    }

    private void getDataFromEngine() {
        clearAccordion();
        startListRefresher();
    }

    private void updateAvailableFlows(FlowsNameDTO names) {
        synchronized (currentNames) {
            Set<String> newFlowNames = new HashSet<>(names.getFlowsNames());
            Set<String> removedFlowNames = new HashSet<>();

            Platform.runLater(() -> {
                availableFlowsAccordion.getPanes().removeIf(titledPane -> {
                    String flowName = titledPane.getText();
                    if (!newFlowNames.contains(flowName)) {
                        removedFlowNames.add(flowName);
                        return true;
                    }
                    return false;
                });

                // Remove flow names from currentNames
                currentNames.removeIf(removedFlowNames::contains);

                for (String flowName : newFlowNames) {
                    if (!currentNames.contains(flowName) && !removedFlowNames.contains(flowName)) {
                        currentNames.add(flowName);
                        Platform.runLater(() ->  {
                            createTitledPaneWithFXML(flowName);
                        });
                    }
                }

                if (availableFlowsAccordion.getPanes().size() == 0)
                    this.parentController.getParentComponent().getTabPane().setDisable(true);
                else
                    this.parentController.getParentComponent().getTabPane().setDisable(false);
            });
        }
    }



    public void startListRefresher() {
        AvailableFlowsRefresher availableFlowsRefresher = new AvailableFlowsRefresher(this::updateAvailableFlows);
        Timer timer = new Timer();
        timer.schedule(availableFlowsRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private void clearAccordion() {
        this.availableFlowsAccordion.getPanes().clear();
    }

    private void createTitledPaneWithFXML(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/availableFlows/flowMinimalDescription/FlowMinimalDescription.fxml"));
            ScrollPane contentPane = fxmlLoader.load();

            FlowMinimalDescriptionController controller = fxmlLoader.getController();

            TitledPane titledPane = new TitledPane(title, contentPane);

            controller.init(this);

            availableFlowsAccordion.getPanes().add(titledPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SimpleStringProperty getTitledPaneNameProperty() {
        return this.titledPaneName;
    }
}
