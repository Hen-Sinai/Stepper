package component.roles.rolesInfo;

import DTO.RoleDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import component.roles.rolesInfo.flowsList.FlowsListController;
import dataStructures.TableViewData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import screen.rolesManagement.RolesManagementController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

public class RolesInfoController {
    private RolesManagementController parentController;
    @FXML private TableView<TableViewData> rolesInfoTableView;
    @FXML private TableColumn<TableViewData, String> parameterColumn;
    @FXML private TableColumn<TableViewData, Object> dataColumn;
    private RoleDTO role;
    private FlowsListController flowsListController;

    public void init(RolesManagementController parentController) {
        this.parentController = parentController;
        parentController.getRolesListComponentController().getChosenRole()
                .addListener((observable, oldValue, newValue) -> {
                    if (oldValue == null || !oldValue.equals(newValue)) {
                        getRole();
                    }
                });
    }

    private void createTableView(RoleDTO role) {
        try {
            rolesInfoTableView.getItems().clear(); // Clear the existing items

            rolesInfoTableView.getItems().add(new TableViewData("Name", new Label(role.getName())));
            rolesInfoTableView.getItems().add(new TableViewData("Description", new Label(role.getDescription())));
            createRolesList(role);
            createUsersList(role.getName());

            // Set the cell value factories for the columns
            parameterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            dataColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNode()));

            // Set the table items
            ObservableList<TableViewData> tableViewData = rolesInfoTableView.getItems();
            rolesInfoTableView.setItems(tableViewData);

        } catch (Exception ignore) {}
    }

    private void createRolesList(RoleDTO role) {
        try {
            FXMLLoader rolesList = new FXMLLoader(getClass().getResource("/component/roles/rolesInfo/flowsList/FlowsList.fxml"));
            ListView<String> rolesListRoot = rolesList.load();
            flowsListController = rolesList.getController();
            flowsListController.init(role.getAllowedFlows());
            rolesInfoTableView.getItems().add(new TableViewData("Flows", rolesListRoot));
        } catch (IOException e) {}
    }

    private void createUsersList(String roleName) {
        try {
            FXMLLoader usersList = new FXMLLoader(getClass().getResource("/component/roles/rolesInfo/usersList/UsersList.fxml"));
            ListView<String> usersListRoot = usersList.load();
            component.roles.rolesInfo.usersList.UsersListController usersListController = usersList.getController();
            usersListController.init(roleName);
            rolesInfoTableView.getItems().add(new TableViewData("Users", usersListRoot));
        } catch (IOException e) {}
    }

    public RolesManagementController getParentController() {
        return this.parentController;
    }

    private void getRole() {
        String finalUrl = HttpUrl
                .parse(Constants.ROLE)
                .newBuilder()
                .addQueryParameter("roleName", this.parentController.getRolesListComponentController().getChosenRole().getValue())
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
                        role = new Gson().fromJson(responseData, RoleDTO.class);
                        createTableView(role);
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    @FXML
    public void onClickSave(ActionEvent actionEvent) {
        RoleDTO updateRole = new RoleDTO(
                role.getName(),
                role.getDescription(),
                flowsListController.getRoleFlows()
        );

        String finalUrl = HttpUrl
                .parse(Constants.ROLE)
                .newBuilder()
                .build()
                .toString();

        String userJson = new Gson().toJson(updateRole);
        RequestBody body = RequestBody.create(userJson.getBytes());
        HttpClientUtil.runAsyncPut(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
            }
        });
    }
}
