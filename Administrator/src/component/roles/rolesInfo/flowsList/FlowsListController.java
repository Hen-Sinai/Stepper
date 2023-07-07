package component.roles.rolesInfo.flowsList;

import DTO.FlowsNameDTO;
import DTO.RoleDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FlowsListController {
    @FXML private ListView<String> rolesListView;
    private Set<String> allFlows;
    private Set<String> roleFlows;

    public void init(Set<String> roleFlows) {
        rolesListView.setOnMouseClicked(event -> handleRoleSelection());
        this.roleFlows = roleFlows;

        CompletableFuture<Void> allRolesFuture = initRoles();

        CompletableFuture.allOf(allRolesFuture)
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

                    if (roleFlows.contains(item)) {
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
        for (String role : allFlows) {
            rolesListView.getItems().add(role);
        }
    }

    private void handleRoleSelection() {
        String selectedRole = rolesListView.getSelectionModel().getSelectedItem();

        // Iterate through the selected roles and update their colors
        if (roleFlows.contains(selectedRole))
            roleFlows.remove(selectedRole);
        else
            roleFlows.add(selectedRole);

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
                    if (roleFlows.contains(item)) {
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
                .parse(Constants.AVAILABLE_FLOWS)
                .newBuilder()
                .addQueryParameter("username", "Admin")
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
                        FlowsNameDTO flows = new Gson().fromJson(responseData, FlowsNameDTO.class);
                        allFlows = new HashSet<>(flows.getFlowsNames());
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

    public Set<String> getRoleFlows() {
        return this.roleFlows;
    }
}
