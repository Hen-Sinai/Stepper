package main;

import component.header.HeaderController;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.BodyController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class AppController {
    private Stage primaryStage;
    @FXML private HeaderController headerComponentController;
    @FXML private BodyController bodyComponentController;
    @FXML private ScrollPane scrollPane;
    @FXML private BorderPane elementsBorderPane;

    @FXML
    public void initialize(){
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

    public void login() {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_LOGIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    headerComponentController.init(AppController.this);
                    bodyComponentController.setMainController(AppController.this);
                    bodyComponentController.init();
                }
                else {
                    Platform.runLater(() -> {
                        elementsBorderPane.getChildren().clear();
                        elementsBorderPane.getChildren().add(new Label("Admin already logged in"));
                    });
                }
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
