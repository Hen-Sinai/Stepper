package components;

import engineManager.EngineManager;

import java.io.IOException;
import java.util.Scanner;

public class SaveState implements Runnable {
    private final EngineManager engineManager;
    private final Scanner scanner;

    public SaveState(EngineManager engineManager, Scanner scanner) {
        this.engineManager = engineManager;
        this.scanner = scanner;
    }

    public void run() {
        String path;

        System.out.println("Please enter full path");
        path = scanner.nextLine();
        try {
            this.engineManager.saveToFile(path);
            System.out.println("File saved successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
