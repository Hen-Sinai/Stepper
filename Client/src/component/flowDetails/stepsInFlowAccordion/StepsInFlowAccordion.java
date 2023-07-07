//package component.flowDetails.stepsInFlowAccordion;
//
//import DTO.FlowDTO;
//import javafx.scene.control.Accordion;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.layout.VBox;
//
//public class StepsInFlowAccordion extends ScrollPane {
//    private final StepsInFlowAccordionController stepsInFlowAccordionController;
//
//    public StepsInFlowAccordion(FlowDTO flowData) {
//        VBox container = new VBox();
//        container.getStyleClass().add("steps-container");
//
//        Accordion accordion = new Accordion();
//
//        container.getChildren().add(accordion);
//        setContent(container);
//
//        setFitToWidth(true);
//        setFitToHeight(true);
//
//        stepsInFlowAccordionController = new StepsInFlowAccordionController(accordion, flowData);
//    }
//
////    public void init(EngineManager engineManager) {
////        stepsInFlowAccordionController.init(engineManager);
////    }
//
//    public StepsInFlowAccordionController getStepsInFlowAccordionController() {
//        return this.stepsInFlowAccordionController;
//    }
//}