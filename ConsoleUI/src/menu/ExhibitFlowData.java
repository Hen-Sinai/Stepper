package menu;

import DTO.FlowDTO;
import DTO.FreeInputDTO;
import DTO.OutputDTO;
import DTO.StepDTO;
import engineManager.EngineManager;

import java.util.List;

public class ExhibitFlowData implements Runnable{
    private final EngineManager engineManager;
    private final String flowName;

    public ExhibitFlowData(EngineManager engineManager, String flowName) {
        this.engineManager = engineManager;
        this.flowName = flowName;
    }

    @Override
    public void run() {
        FlowDTO flowData = this.engineManager.getFlowData(this.flowName);
        System.out.println("        Flow:");
        printFlowData(flowData);
        System.out.println("       Steps:");
        printStepsData(flowData.getStepsDTO());
        System.out.println("       Inputs:");
        printInputsData(flowData.getInputDTO());
        System.out.println("       Outputs:");
        printOutputsData(flowData.getOutputDTO());
    }

    private void printFlowData(FlowDTO flowData) {
        System.out.println("           Name: " + flowData.getName());
        System.out.println("           Description: " + flowData.getDescription());
        printFlowFormatOutputs(flowData.getFormalOutputs());
        System.out.println("           Is flow readonly: " + flowData.getIsReadonly());
        System.out.println();
    }

    private void printFlowFormatOutputs(List<String> outputs) {
        System.out.println("           Formal outputs:");
        if (outputs.size() == 0) {
            System.out.println("               No formal outputs");
        }
        else {
            for (String output : outputs) {
                System.out.println("                " + output);
            }
        }
    }


    private void printStepsData(List<StepDTO> stepsData) {
        for (StepDTO stepData : stepsData) {
            if (stepData.getAlias() != null) {
                System.out.println("           Name: " + stepData.getName());
                System.out.println("           Alias: " + stepData.getAlias());
                System.out.println("           Is readonly: " + stepData.getIsReadonly());
            }
            else {
                System.out.println("           Name: " + stepData.getName());
                System.out.println("           Is readonly: " + stepData.getIsReadonly());
            }
            System.out.println();
        }
    }

    private void printInputsData(List<FreeInputDTO> inputsData) {
        if (inputsData.size() == 0) {
            System.out.println("           No inputs");
        }
        else {
            for (FreeInputDTO inputData : inputsData) {
                System.out.println("           Name: " + inputData.getName());
                System.out.println("           Type: " + inputData.getType());
                System.out.println("           Attached step/s: ");
                printAttachedSteps(inputData.getAttachedSteps());
                System.out.println("           Necessity: " + inputData.getNecessity());
                System.out.println();
            }
        }
    }

    private void printAttachedSteps(List<String> steps) {
        for (String stepName : steps) {
            System.out.println("                    " + stepName);
        }
    }

    private void printOutputsData(List<OutputDTO> outputsData) {
        if (outputsData.size() == 0) {
            System.out.println("           No inputs");
        }
        else {
            for (OutputDTO outputData : outputsData) {
                System.out.println("           Name: " + outputData.getName());
                System.out.println("           Type: " + outputData.getType());
                System.out.println("           Attached step: " + outputData.getAttachedStep());
                System.out.println();
            }
        }
    }
}
