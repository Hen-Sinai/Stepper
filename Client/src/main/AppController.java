package main;

import component.header.HeaderController;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import screen.BodyController;

public class AppController {
    private final EngineManager engineManager = EngineManagerImpl.getInstance();
    private Stage primaryStage;
    @FXML private HeaderController headerComponentController;
    @FXML private BodyController bodyComponentController;
    @FXML private ScrollPane scrollPane;

    @FXML
    public void initialize(){
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.init(this);
            bodyComponentController.setMainController(this);
            bodyComponentController.init();
        }

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 600) {
                scrollPane.setFitToWidth(false);
            } else {
                scrollPane.setFitToWidth(true);
            }
        });

        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 800) {
                scrollPane.setFitToHeight(false);
            } else {
                scrollPane.setFitToHeight(true);
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
}
