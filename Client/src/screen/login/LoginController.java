package screen.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.AppController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML public Label errorMessageLabel;
    @FXML Stage primaryStage;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    @FXML
    private void onPressLoginButton() {
        String userName = usernameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        else if (userName.equals("Admin")) {
            errorMessageProperty.set("Invalid username");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        changeSceneToMainApp();
                    });
                }
            }
        });
    }
    public void changeSceneToMainApp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/App.fxml"));
            ScrollPane scrollPane = fxmlLoader.load();
            AppController appController = fxmlLoader.getController();
            appController.setPrimaryStage(primaryStage);
            primaryStage.getScene().setRoot(scrollPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
