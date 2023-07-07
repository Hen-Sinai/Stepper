package component.roles.newRole;

import DTO.RoleDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import screen.rolesManagement.RolesManagementController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NewRoleController {
    private RolesManagementController parentController;
    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;
    @FXML private ListView<String> flowsListView;
    private Map<String, RoleDTO> allRoles = new HashMap<>();
    private final Map<String, RoleDTO> userRoles = new HashMap<>();

    public void init(RolesManagementController parentController) {
        this.parentController = parentController;
        flowsListView.setOnMouseClicked(event -> handleRoleSelection());

        CompletableFuture<Void> allRolesFuture = initRoles();

        CompletableFuture.allOf(allRolesFuture)
                .thenRun(this::createListView);
    }

    private void createListView() {
        flowsListView.getItems().clear(); // Clear the existing items

        flowsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setFont(javafx.scene.text.Font.font("Arial", 12)); // Set the font style

                    if (userRoles.containsKey(item)) {
                        // Role is present in userRoles, color it green
                        setTextFill(Color.GREEN);
                    } else {
                        // Role is not present in userRoles, color it red
                        setTextFill(Color.RED);
                    }
                }
            }
        });

        // Add allRoles to the ListView
        for (Map.Entry<String, RoleDTO> entry : allRoles.entrySet()) {
            flowsListView.getItems().add(entry.getKey());
        }
    }

    private void handleRoleSelection() {
        String selectedRole = flowsListView.getSelectionModel().getSelectedItem();

        // Iterate through the selected roles and update their colors
        if (userRoles.containsKey(selectedRole))
            userRoles.remove(selectedRole);
        else
            userRoles.put(selectedRole, allRoles.get(selectedRole));

        flowsListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setFont(javafx.scene.text.Font.font("Arial", 12));
                    if (userRoles.containsKey(item)) {
                        setTextFill(Color.GREEN);
                    }
                    else {
                        setTextFill(Color.RED);
                    }
                }
            }
        });
    }


    private CompletableFuture<Void> initRoles() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String finalUrl = HttpUrl
                .parse(Constants.ROLES)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.complete(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        allRoles = new Gson().fromJson(responseData, new TypeToken<Map<String, RoleDTO>>(){}.getType());
                        future.complete(null);
                    } catch (IOException ignore) {
                        future.complete(null);
                    }
                } else {
                    future.complete(null);
                }
            }
        });

        return future;
    }

    @FXML
    public void onPressCreateRole(ActionEvent actionEvent) {
        RoleDTO newRole = new RoleDTO(
                this.nameTextField.getText(),
                this.descriptionTextField.getText(),
                allRoles.keySet()
                );

        String finalUrl = HttpUrl
                .parse(Constants.ROLE)
                .newBuilder()
                .build()
                .toString();
        String roleDTOJson = new Gson().toJson(newRole);
        RequestBody body = RequestBody.create(roleDTOJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        parentController.getRolesListComponentController().getRolesListView().getItems().add(nameTextField.getText());
                        Stage stage = (Stage) nameTextField.getScene().getWindow();
                        stage.close();
                    });
                }
            }
        });
    }
}
