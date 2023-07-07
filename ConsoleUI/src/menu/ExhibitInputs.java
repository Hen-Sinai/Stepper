package menu;

import DTO.FreeInputDTO;
import components.FlowExecution;
import components.Input;
import engineManager.EngineManager;
import step.api.DataNecessity;

import java.util.*;

public class ExhibitInputs implements Runnable {
    private final EngineManager engineManager;
    private final String flowName;
    private List<Runnable> menuOptions;
    private HashMap<String, Object> dataValues;
    private final Scanner scanner;
    private boolean isExecuted = false;

    public ExhibitInputs(EngineManager engineManager, String name, Scanner scanner) {
        this.engineManager = engineManager;
        this.flowName = name;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        int userInput, amountOfMandatoryInputs;
        boolean isMandatoryInputFull, isExecuteAddedToMenu = false;
        List<FreeInputDTO> inputs = engineManager.getFreeInputs(this.flowName);
        dataValues = new HashMap<>();
        addOptionSToMenu(inputs);
        amountOfMandatoryInputs = getAmountOfMandatoryInputs(inputs);

        do {
            try {
                isMandatoryInputFull = isMandatoryInputFull(inputs, amountOfMandatoryInputs);
                if (!isExecuteAddedToMenu && isMandatoryInputFull) {
                    menuOptions.add(new FlowExecution(this.engineManager, this.flowName, this.dataValues));
                    isExecuteAddedToMenu = true;
                }
                printInputs(inputs, amountOfMandatoryInputs);
                userInput = this.scanner.nextInt();
                scanner.nextLine();

                if (userInput >= 1 && userInput <= menuOptions.size())
                    menuOptions.get(userInput - 1).run();
                else if (userInput != 0) {
                    System.out.println("            Please pick a valid option");
                    return;
                }
                if (isMandatoryInputFull && userInput == menuOptions.size())
                    return;
            } catch (InputMismatchException e) {
                System.out.println("            Choose number between 0 and " + menuOptions.size());
                this.scanner.nextLine(); // clear the scanner buffer
                userInput = -1;
            }
        } while (isExecuted || userInput != 0);
    }

    private void addOptionSToMenu(List<FreeInputDTO> inputs) {
        menuOptions = new ArrayList<>();
        for (FreeInputDTO input : inputs) {
            menuOptions.add(new Input(input.getName(), input.getUserString(), input.getType(),
                    this.dataValues, this.scanner));
        }
    }

    private void printInputs(List<FreeInputDTO> inputs, int amountOfMandatoryInputs) {
        int counter = 1;

        for (FreeInputDTO input : inputs) {
            System.out.println("            " + counter + ". " + input.getUserString() + " " +
                    input.getAttachedSteps() + " - " + input.getNecessity());
            counter++;
        }
        if (isMandatoryInputFull(inputs, amountOfMandatoryInputs)) {
            System.out.println("            " + counter + ". Execute flow");
        }
        System.out.println("            " + 0 + ". Go Back");
    }

    private int getAmountOfMandatoryInputs(List<FreeInputDTO> inputs) {
        int counter = 0;
        for (FreeInputDTO input : inputs) {
            if (input.getNecessity() == DataNecessity.MANDATORY)
                counter++;
        }
        return counter;
    }

    private boolean isMandatoryInputFull(List<FreeInputDTO> inputs, int amountOfMandatoryInputs) {
        for (FreeInputDTO input : inputs) {
            if (input.getNecessity() == DataNecessity.MANDATORY && dataValues.containsKey(input.getName()))
                amountOfMandatoryInputs--;
        }
        return amountOfMandatoryInputs == 0;
    }

}
