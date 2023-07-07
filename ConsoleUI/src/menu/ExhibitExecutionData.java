package menu;

import DTO.*;
import engineManager.EngineManager;
import flow.stepInfo.log.Log;

import java.util.List;

public class ExhibitExecutionData implements Runnable{
    private final EngineManager engineManager;
    private final String flowName;

    public ExhibitExecutionData(EngineManager engineManager, String flowName) {
        this.engineManager = engineManager;
        this.flowName = flowName;
    }

    @Override
    public void run() {
//        FlowExecutedDataDTO executedData = engineManager.getFlowExecutedDataDTO(this.flowName);
//        printExecutionsData(executedData);
    }

    private void printExecutionsData(FlowExecutedDataDTO executedData) {
        printFlowData(executedData);
        printInputData(executedData.getInputs());
        printOutputData(executedData.getOutputs());
        printStepData(executedData.getStepsList());
    }

    private void printFlowData(FlowExecutedDataDTO executedData) {
        System.out.println("        Flow Info:");
        System.out.println("            ID: " + executedData.getId());
        System.out.println("            Name: " + executedData.getFlowName());
        System.out.println("            Result: " + executedData.getResult());
        System.out.println("            Duration: " + executedData.getDuration().toMillis() + "ms");
    }

    private void printInputData(List<FreeInputDTO> inputs) {
        System.out.println("            Flow Inputs:");
        if (inputs.size() == 0) {
            System.out.println("                No inputs");
            System.out.println();
        }
        else {
            for (FreeInputDTO input : inputs) {
                System.out.println("                Name: " + input.getName());
                System.out.println("                Type: " + input.getType());
                System.out.println("                Content:");
                System.out.println("                    " + input.getData());
                System.out.println("                Necessity: " + input.getNecessity());
                System.out.println();
            }
        }
    }

    private void printOutputData(List<OutputDTO> outputs) {
        System.out.println("            Flow Outputs:");
        if (outputs.size() == 0) {
            System.out.println("                No outputs");
            System.out.println();
        }
        else {
            for (OutputDTO output : outputs) {
                System.out.println("                Name: " + output.getName());
                System.out.println("                Type: " + output.getType());
                System.out.println("                Content:");
                System.out.println("                    " + output.getData());
                System.out.println();
            }
        }
    }

    private void printStepData(List<StepResultDTO> steps) {
        System.out.println("            Flow Steps:");
        for (StepResultDTO step : steps) {
            System.out.println("                Name: " + step.getName());
            System.out.println("                Duration: " + step.getDuration().toMillis() + "ms");
            System.out.println("                Result: " + step.getStepResult());
            System.out.println("                Summary: " + step.getSummary());
            System.out.println("                Logs:");
            for (Log log : step.getLogs()) {
                System.out.println("                    " + log.getTimeStamp() + " - " + log.getData());
            }
            System.out.println();
        }
    }
}
