package components;

import DTO.ExecuteDataDTO;
import DTO.ExecutionResultDTO;
import DTO.OutputDTO;
import engineManager.EngineManager;

import java.util.HashMap;
import java.util.List;

public class FlowExecution implements Runnable {
    private final EngineManager engineManager;
    private final String flowName;
    private final HashMap<String, Object> dataValues;

    public FlowExecution(EngineManager engineManager, String flowName, HashMap<String, Object> dataValues) {
        this.engineManager = engineManager;
        this.flowName = flowName;
        this.dataValues = dataValues;
    }

    @Override
    public void run() {
//        ExecutionResultDTO executionResult = this.engineManager.executeFlow(new ExecuteDataDTO(this.flowName, this.dataValues));
//        printExecutionResult(executionResult);
    }

    private void printExecutionResult(ExecutionResultDTO executionResult) {
        System.out.println("                ID: " + executionResult.getId());
        System.out.println("                Name: " + executionResult.getName());
        System.out.println("                Result: " + executionResult.getResult());
        printData(executionResult.getUserString2data());
    }

    private void printData(List<OutputDTO> outputsData) {
        System.out.println("                Outputs:");
        if (outputsData.size() == 0) {
            System.out.println("                    No outputs");
            System.out.println();
        }
        else {
            for (OutputDTO data : outputsData) {
                System.out.println("                    " + data.getName() + ":");
                System.out.println("                    " + data.getData());
                System.out.println();
            }
        }
    }
}
