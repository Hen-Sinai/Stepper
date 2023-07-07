package component.users.userInfo;

import DTO.FlowDTO;
import DTO.RoleDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import component.users.userInfo.flowsList.executedFlows.ExecutedFlowsListController;
import component.users.userInfo.flowsList.possibleFlows.PossibleFlowsListController;
import component.users.userInfo.rolesList.RolesListController;
import dataStructures.TableViewData;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import screen.BodyController;
import screen.usersManagement.UsersManagementController;
import user.User;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Set;

public class UserInfoController {
    private UsersManagementController parentController;
    @FXML private TableView<TableViewData> userInfoTableView;
    @FXML private TableColumn<TableViewData, String> parameterColumn;
    @FXML private TableColumn<TableViewData, Object> dataColumn;
    private final CheckBox managerCheckBox = new CheckBox();
    private RolesListController rolesListController;
    private PossibleFlowsListController possibleFlowsListController;

    public void init(UsersManagementController parentController) {
        this.parentController = parentController;
        parentController.getUsersListComponentController().getChosenUser()
                .addListener((observable, oldValue, newValue) -> {
                    if (oldValue == null || !oldValue.equals(newValue)) {
                        getUser();
                        createTableView();
                    }
                });
    }

    private void createTableView() {
        try {
            userInfoTableView.getItems().clear(); // Clear the existing items

            userInfoTableView.getItems().add(new TableViewData("Name",
                    new Label(this.parentController.getUsersListComponentController().getChosenUser().getValue())));
            userInfoTableView.getItems().add(new TableViewData("Manager", managerCheckBox));
            createPossibleList();
            createExecutedList();
            createRolesList();

            // Set the cell value factories for the columns
            parameterColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            dataColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNode()));

            // Set the table items
            ObservableList<TableViewData> tableViewData = userInfoTableView.getItems();
            userInfoTableView.setItems(tableViewData);

        } catch (Exception ignore) {}
    }

    private void createPossibleList() {
        try {
            FXMLLoader possibleFlowsList = new FXMLLoader(getClass().getResource("/component/users/userInfo/flowsList/possibleFlows/PossibleFlowsList.fxml"));
            ListView<String> possibleFlowsListRoot = possibleFlowsList.load();
            possibleFlowsListController = possibleFlowsList.getController();
            possibleFlowsListController.init(this);
            userInfoTableView.getItems().add(new TableViewData("Possible flows", possibleFlowsListRoot));
        } catch (IOException e) {}
    }

    private void createExecutedList() {
        try {
            FXMLLoader executedFlowsList = new FXMLLoader(getClass().getResource("/component/users/userInfo/flowsList/executedFlows/ExecutedFlowsList.fxml"));
            ListView<String> executedFlowsListRoot = executedFlowsList.load();
            ExecutedFlowsListController executedFlowsListController = executedFlowsList.getController();
            executedFlowsListController.init(this);
            userInfoTableView.getItems().add(new TableViewData("Executed flows", executedFlowsListRoot));
        } catch (IOException e) {}
    }

    private void createRolesList() {
        try {
            FXMLLoader rolesList = new FXMLLoader(getClass().getResource("/component/users/userInfo/rolesList/RolesList.fxml"));
            ListView<String> rolesListRoot = rolesList.load();
            rolesListController = rolesList.getController();
            rolesListController.init(this);
            userInfoTableView.getItems().add(new TableViewData("Roles", rolesListRoot));
        } catch (IOException e) {}
    }

    private void getUser() {
        String finalUrl = HttpUrl
                .parse(Constants.USER)
                .newBuilder()
                .addQueryParameter("username", this.parentController.getUsersListComponentController().getChosenUser().getValue())
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
                        UserDTO user = new Gson().fromJson(responseData, UserDTO.class);
                        Platform.runLater(() -> {
                            managerCheckBox.setSelected(user.getIsManager());
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    public UsersManagementController getParentController() {
        return this.parentController;
    }

    @FXML
    public void onClickSave(ActionEvent actionEvent) {
        UserDTO user = new UserDTO(
                parentController.getUsersListComponentController().getChosenUser().getValue(),
                this.managerCheckBox.isSelected(),
                rolesListController.getUserRoles()
                );

        String finalUrl = HttpUrl
                .parse(Constants.USER)
                .newBuilder()
                .build()
                .toString();

        String userJson = new Gson().toJson(user);
        RequestBody body = RequestBody.create(userJson.getBytes());
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    possibleFlowsListController.updateFlows(parentController.getUsersListComponentController().getChosenUser().getValue());
                }
            }
        });
    }
}
