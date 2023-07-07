package menu;

import engineManager.EngineManager;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ExhibitFlows implements Runnable {
    private final EngineManager engineManager;
    private List<Runnable> menuOptions;
    private List<String> flowsName;
    private final Scanner scanner;

    public ExhibitFlows(EngineManager engineManager, Scanner scanner) {
        this.engineManager = engineManager;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        int userInput;
        this.flowsName = engineManager.getFlowsNames().getFlowsNames();
        addOptionSToMenu();

        do {
            try {
                printFlowsName();
                userInput = scanner.nextInt();
                scanner.nextLine();

                if (userInput >= 1 && userInput <= menuOptions.size())
                    menuOptions.get(userInput - 1).run();
                else if (userInput != 0)
                    System.out.println(     "Please pick a valid option");
            } catch (InputMismatchException e) {
                System.out.println(     "Choose number between 0 and " + menuOptions.size());
                this.scanner.nextLine(); // clear the scanner buffer
                userInput = -1;
            }
        } while (userInput != 0);
    }

    private void addOptionSToMenu() {
        menuOptions = new ArrayList<>();
        for (String name : this.flowsName) {
            menuOptions.add(new ExhibitFlowData(this.engineManager, name));
        }
    }

    private void printFlowsName() {
        int counter = 1;
        System.out.println("    Flows:");
        for (String name : this.flowsName) {
            System.out.println("    " + counter + ". " + name);
            counter++;
        }
        System.out.println("    " + 0 + ". Go Back");
    }
}
