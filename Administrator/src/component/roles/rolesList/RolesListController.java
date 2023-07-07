package component.roles.rolesList;

import DTO.RoleDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.rolesManagement.RolesManagementController;
import screen.usersManagement.UsersManagementController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RolesListController {
    private RolesManagementController parentController;
    @FXML private ListView<String> rolesListView;
    private final SimpleStringProperty chosenRole = new SimpleStringProperty();

    public void init(RolesManagementController parentController) {
        this.parentController = parentController;
        initRoles();
    }

    public SimpleStringProperty getChosenRole() {
        return this.chosenRole;
    }

    private void initRoles() {
        String finalUrl = HttpUrl
                .parse(Constants.ROLES)
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
                    try {
                        String responseData = response.body().string();
                        Map<String, RoleDTO> roles = new Gson().fromJson(responseData, new TypeToken<Map<String, RoleDTO>>(){}.getType());
                        Platform.runLater(() -> {
                            createListView(roles);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    private void createListView(Map<String, RoleDTO> roles) {
        for (Map.Entry<String, RoleDTO> entry : roles.entrySet()) {
            rolesListView.getItems().add(entry.getKey());
        }

        // Set the chosen user when an item is selected
        rolesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chosenRole.set(newValue);
        });
    }

    public ListView<String> getRolesListView() {
        return this.rolesListView;
    }
}
