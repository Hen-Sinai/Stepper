package components;

import engineManager.EngineManager;

import java.util.*;

public class Input implements Runnable {
    private final String name;
    private final String userString;
    private final String type;
    private final HashMap<String, Object> dataValues;
    private final Scanner scanner;

    public Input(String name, String userString, String type, HashMap<String, Object> dataValues, Scanner scanner) {
        this.name = name;
        this.userString = userString;
        this.type = type;
        this.dataValues = dataValues;
        this.scanner = scanner;
    }

    @Override
    public void run() {
//        String userInput; // TODO try to check the type in the engine!!!!!
        boolean validInput = false;
        System.out.println("                Please enter value for " + this.userString + ":");
        do {
            try {
                switch (this.type) {
                    case "Number":
                        dataValues.put(this.name, scanner.nextInt());
                        validInput = true;
                        break;
                    case "Double":
                        dataValues.put(this.name, scanner.nextDouble());
                        validInput = true;
                        break;
                    case "String":
//                        dataValues.put(this.name, scanner.next());
                        dataValues.put(this.name, scanner.nextLine());
                        validInput = true;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        } while (!validInput);
    }
}
