package menu;

import components.ExhibitFlowsToActivate;
import components.LoadState;
import components.LoadXml;
import components.SaveState;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static final int FIRST_MENU_OPTION = 1;
    private static final int LAST_MENU_OPTION = 8;
    private final EngineManager engineManager = EngineManagerImpl.getInstance();
    private final List<Runnable> menuOptions = new ArrayList<>();
    private final Scanner scanner;

    public MainMenu() {
        scanner = new Scanner(System.in);
        menuOptions.add(new LoadXml(this.engineManager, this.scanner));
        menuOptions.add(new ExhibitFlows(this.engineManager, this.scanner));
        menuOptions.add(new ExhibitFlowsToActivate(this.engineManager, this.scanner));
        menuOptions.add(new ExhibitExecutionInfo(this.engineManager, this.scanner));
        menuOptions.add(new ExhibitStats(this.engineManager));
        menuOptions.add(new SaveState(this.engineManager, this.scanner));
        menuOptions.add(new LoadState(this.engineManager, this.scanner));
    }

    public void run() {
        int userInput;

        do {
            try {
                printMainMenuOptions();
                userInput = this.scanner.nextInt();
                scanner.nextLine();

                if (userInput >= FIRST_MENU_OPTION && userInput < LAST_MENU_OPTION) {
                    if (userInput == 1) {
                        menuOptions.get(0).run();
                    }
                    else if (userInput == 7) {
                        menuOptions.get(6).run();
                    }
                    else {
                        if (engineManager.getFlowsManager() == null) {
                            System.out.println("First choose (1) to insert data");
                        } else {
                            menuOptions.get(userInput - 1).run();
                        }
                    }
                } else if (userInput != 8)
                    System.out.println("Choose number between 1 and 8");
            } catch (InputMismatchException e) {
                System.out.println("Choose number between 1 and 8");
                this.scanner.nextLine(); // clear the scanner buffer
                userInput = -1;
            }
        } while (userInput != LAST_MENU_OPTION);

        System.out.println("Bye :)");
        this.scanner.close();
    }


    private void printMainMenuOptions() {
        System.out.println("Welcome to STEPPER!");
        System.out.println("1. choose XML file");
        System.out.println("2. Show flow definition");
        System.out.println("3. Execute flow");
        System.out.println("4. Show details of past execution");
        System.out.println("5. Show flow stats");
        System.out.println("6. Save state to file");
        System.out.println("7. Load state from file");
        System.out.println("8. Exit");
    }
}
