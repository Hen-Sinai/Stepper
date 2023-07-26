package component.header;

import DTO.UserDTO;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.AppController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Timer;

public class HeaderController2 {
    private AppController parentController;
    @FXML private Label descLabel;
    @FXML private Label rolesLabel;
    @FXML private Button logoutButton;
    private final SimpleBooleanProperty isUserManagerStatusChangedToManagerProperty = new SimpleBooleanProperty(false);

    public void init(AppController mainController) {
        this.parentController = mainController;
        this.startListRefresher();
    }

    public AppController getParentController() {
        return this.parentController;
    }

    public void startListRefresher() {
        HeaderRefresher headerRefresher = new HeaderRefresher(this::updateUserData);
        Timer timer = new Timer();
        timer.schedule(headerRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private void updateUserData(UserDTO user) {
        isUserManagerStatusChangedToManagerProperty.set(user.getIsManager());
        Platform.runLater(() -> {
            descLabel.setText("Name: " + user.getName() + "\t\t\t" + "Is Manager: " + user.getIsManager());
            rolesLabel.setText("Assigned Roles: " + (user.getRoles() == null ? "No roles assigned" : user.getRoles().keySet()));
        });
    }

    @FXML
    public void onClickLogout(ActionEvent actionEvent) {
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncPut(finalUrl, RequestBody.create("".getBytes()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    System.exit(1);
                }
            }
        });
    }

    public SimpleBooleanProperty getIsUserManagerStatusChangedToManagerProperty() {
        return this.isUserManagerStatusChangedToManagerProperty;
    }
}
