package components;

import engineManager.EngineManager;

import java.io.IOException;
import java.util.Scanner;

public class LoadState implements Runnable {
    private final EngineManager engineManager;
    private final Scanner scanner;

    public LoadState(EngineManager engineManager, Scanner scanner) {
        this.engineManager = engineManager;
        this.scanner = scanner;
    }

    public void run() {
        String path;

        System.out.println("Please enter full path");
        path = scanner.nextLine();
        try {
            this.engineManager.loadFromFile(path);
            System.out.println("File load successfully");
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
