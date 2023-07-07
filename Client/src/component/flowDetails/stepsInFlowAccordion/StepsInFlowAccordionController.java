package component.flowDetails.stepsInFlowAccordion;

import DTO.FlowDTO;
import DTO.StepDTO;
import component.flowDetails.stepData.StepDataController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

import java.io.IOException;

public class StepsInFlowAccordionController {
    private FlowDTO flowData;
    @FXML private Accordion stepsInFlowAccordion;

    public void init(FlowDTO flowData) {
        this.flowData = flowData;

        for (StepDTO step : flowData.getStepsDTO()) {
            createTitledPaneWithFXML(step.getFinalName());
        }
    }

    public void handleTitledPanePress(TitledPane titledPane) {
        StepDataController controller = (StepDataController) titledPane.getProperties().get("controller");
        if (controller != null) {
            controller.setStepData(flowData, titledPane.getText());
        }
    }

    private void createTitledPaneWithFXML(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/flowDetails/stepData/StepData.fxml"));
            ScrollPane contentPane = fxmlLoader.load();

            StepDataController controller = fxmlLoader.getController();

            TitledPane titledPane = new TitledPane(title, contentPane);
            titledPane.setAnimated(false);

            // Store the controller in the TitledPane's properties
            titledPane.getProperties().put("controller", controller);

            titledPane.setOnMouseClicked(event -> handleTitledPanePress(titledPane));

            stepsInFlowAccordion.getPanes().add(titledPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
