package menu;

import DTO.FlowExecutedInfoDTO;
import engineManager.EngineManager;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ExhibitExecutionInfo implements Runnable {
    private final EngineManager engineManager;
    private List<Runnable> menuOptions;
    private final Scanner scanner;

    public ExhibitExecutionInfo(EngineManager engineManager, Scanner scanner) {
        this.engineManager = engineManager;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        int userInput;
        List<FlowExecutedInfoDTO> executionsInfo = engineManager.getFlowsExecutedInfoDTO();
        addOptionSToMenu(executionsInfo);

        do {
            try {
                printFlowsName(executionsInfo);
                userInput = scanner.nextInt();
                scanner.nextLine();

                if (userInput >= 1 && userInput <= menuOptions.size())
                    menuOptions.get(userInput - 1).run();
                else if (userInput != 0)
                    System.out.println("    Please pick a valid option");
            } catch (InputMismatchException e) {
                System.out.println("    Choose number between 0 and " + menuOptions.size());
                this.scanner.nextLine(); // clear the scanner buffer
                userInput = -1;
            }
        } while (userInput != 0);
    }

    private void addOptionSToMenu(List<FlowExecutedInfoDTO> executionsInfo) {
        menuOptions = new ArrayList<>();
        for (FlowExecutedInfoDTO executionInfo : executionsInfo) {
            menuOptions.add(new ExhibitExecutionData(this.engineManager, executionInfo.getName()));
        }
    }

    private void printFlowsName(List<FlowExecutedInfoDTO> executionsInfo) {
        int counter = 1;
        for (FlowExecutedInfoDTO executionInfo : executionsInfo) {
            System.out.println("    " + counter + ". " + executionInfo.getName() + ": " + executionInfo.getId() + " (" +
                    executionInfo.getTimeStamp() + ")");
            counter++;
        }
        System.out.println("    " + 0 + ". Go Back");
    }
}

