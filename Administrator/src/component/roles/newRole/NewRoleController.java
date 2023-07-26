package component.roles.newRole;

import DTO.FlowsNameDTO;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NewRoleController {
    private RolesManagementController parentController;
    @FXML private TextField nameTextField;
    @FXML private TextField descriptionTextField;
    @FXML private ListView<String> flowsListView;
    private FlowsNameDTO allFlows;
    private final Set<String> userRoles = new HashSet<>();

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

                    if (userRoles.contains(item)) {
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
        for (String flow : allFlows.getFlowsNames()) {
            flowsListView.getItems().add(flow);
        }
    }

    private void handleRoleSelection() {
        String selectedRole = flowsListView.getSelectionModel().getSelectedItem();

        // Iterate through the selected roles and update their colors
        if (userRoles.contains(selectedRole))
            userRoles.remove(selectedRole);
        else
            userRoles.add(selectedRole);

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
                    if (userRoles.contains(item)) {
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
                .parse(Constants.ALL_FLOWS)
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
                        allFlows = new Gson().fromJson(responseData, FlowsNameDTO.class);
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
                new HashSet<>(allFlows.getFlowsNames())
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
