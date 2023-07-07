//package component.executionResult.flowTree;
//
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.concurrent.Task;
//
//import java.util.UUID;
//import java.util.function.Consumer;
//
//public class Logic {
//    private Task<Boolean> currentRunningTask;
//    private FlowTreeController controller;
//
//    public Logic(FlowTreeController controller) {
//        this.controller = controller;
//    }
//
//    public void fetchData(UUID flowId, UIAdapter uiAdapter, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
//        currentRunningTask = new ExecuteFlowTask(flowId, uiAdapter, currentFlowId, isTaskFinished);
//
//        new Thread(currentRunningTask).start();
//    }
//}
