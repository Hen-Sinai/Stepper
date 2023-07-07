package component.users.userInfo.rolesList;

import DTO.FlowsNameDTO;
import DTO.RoleDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.users.userInfo.UserInfoController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class RolesListController {
    @FXML
    private ListView<String> rolesListView;
    private Map<String, RoleDTO> allRoles = new HashMap<>();
    private Map<String, RoleDTO> userRoles = new HashMap<>();

    public void init(UserInfoController userInfoController) {
        rolesListView.setOnMouseClicked(event -> handleRoleSelection());

        CompletableFuture<Void> allRolesFuture = initRoles();
        CompletableFuture<Void> userRolesFuture = initUserRoles(userInfoController.getParentController().getUsersListComponentController().getChosenUser().getValue());

        CompletableFuture.allOf(allRolesFuture, userRolesFuture)
                .thenRun(this::createListView);
    }

    private void createListView() {
        rolesListView.getItems().clear(); // Clear the existing items

        rolesListView.setCellFactory(param -> new ListCell<String>() {
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
            rolesListView.getItems().add(entry.getKey());
        }
    }

    private void handleRoleSelection() {
        String selectedRole = rolesListView.getSelectionModel().getSelectedItem();

        // Iterate through the selected roles and update their colors
        if (userRoles.containsKey(selectedRole))
            userRoles.remove(selectedRole);
        else
            userRoles.put(selectedRole, allRoles.get(selectedRole));

        rolesListView.setCellFactory(param -> new ListCell<String>() {
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

    private CompletableFuture<Void> initUserRoles(String username) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        String finalUrl = HttpUrl
                .parse(Constants.USER_ROLES)
                .newBuilder()
                .addQueryParameter("username", username)
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
                        userRoles = new Gson().fromJson(responseData, new TypeToken<Map<String, RoleDTO>>(){}.getType());
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

    public Map<String, RoleDTO> getUserRoles() {
        return this.userRoles;
    }
}
