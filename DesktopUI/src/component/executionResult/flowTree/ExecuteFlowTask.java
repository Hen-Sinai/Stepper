//package component.executionResult.flowTree;
//
//import DTO.FlowExecutedDataDTO;
//import engineManager.EngineManager;
//import engineManager.EngineManagerImpl;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.concurrent.Task;
//
//import java.util.UUID;
//import java.util.function.Consumer;
//
//public class ExecuteFlowTask extends Task<Boolean> {
//    private final EngineManager engineManager = EngineManagerImpl.getInstance();
//    private final UIAdapter uiAdapter;
//    private final UUID flowId;
//    private final SimpleStringProperty currentFlowId;
//    private final SimpleBooleanProperty isTaskFinished;
//
//    public ExecuteFlowTask(UUID flowId, UIAdapter uiAdapter, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
//        this.flowId = flowId;
//        this.uiAdapter = uiAdapter;
//        this.currentFlowId = currentFlowId;
//        this.isTaskFinished = isTaskFinished;
//    }
//
//    @Override
//    protected Boolean call() {
//        int SLEEP_TIME = 200;
//        FlowExecutedDataDTO executedData = engineManager.getFlowExecutedDataDTO(this.flowId);
//        uiAdapter.update(executedData);
//
//        while (executedData.getResult() == null) {
//            executedData = engineManager.getFlowExecutedDataDTO(this.flowId);
//            if (currentFlowId.getValue().equals(flowId.toString()))
//                uiAdapter.update(executedData);
//
//            try {
//                Thread.sleep(SLEEP_TIME);
//            } catch (InterruptedException ignored) {}
//        }
//
//        if (currentFlowId.getValue().equals(flowId.toString()))
//            uiAdapter.removeSpinner();
//
//        engineManager.setStats(flowId);
//        engineManager.setFinishTimeStamp(flowId);
//        isTaskFinished.set(true);
//        return Boolean.TRUE;
//    }
//}
