package component.executionResult.active.flowTree;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import util.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Logic {
    private TimerTask currentRunningTask;
    private FlowTreeActiveController controller;
    private Timer timer;
    private SimpleBooleanProperty isTaskFinished; // Add a reference to the isTaskFinished property

    public Logic(FlowTreeActiveController controller) {
        this.controller = controller;
        this.isTaskFinished = new SimpleBooleanProperty(false); // Initialize the isTaskFinished property
    }

    public void fetchData(UUID flowId, UIAdapter uiAdapter, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.isTaskFinished = isTaskFinished; // Update the reference to the isTaskFinished property
        currentRunningTask = new ExecuteFlowTask(flowId, uiAdapter, currentFlowId, this.isTaskFinished); // Pass the reference to the ExecuteFlowTask
        timer = new Timer();
        new Thread(() -> timer.schedule(currentRunningTask, Constants.REFRESH_RATE, Constants.REFRESH_RATE)).start();

//        TimerTask stopTask = new TimerTask() {
//            @Override
//            public void run() {
//                if (!currentFlowId.getValue().equals(flowId.toString()) || Logic.this.isTaskFinished.get()) { // Stop the task if currentFlowId is different or isTaskFinished is true
//                    currentRunningTask.cancel();
//                }
//            }
//        };
//        timer.schedule(stopTask, Constants.REFRESH_RATE);
        //        new Thread(currentRunningTask).start();
    }
}
