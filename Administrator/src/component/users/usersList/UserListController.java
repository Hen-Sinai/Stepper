package component.users.usersList;

import DTO.FlowExecutedDataDTO;
import DTO.FlowsNameDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import screen.usersManagement.UsersManagementController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;

public class UserListController {
    private UsersManagementController parentController;
    @FXML private ListView<String> usersListView;
    private final SimpleStringProperty chosenUser = new SimpleStringProperty();
    private final Set<String> currentUsers = new HashSet<>();

    public void init(UsersManagementController parentController) {
        this.parentController = parentController;
        usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chosenUser.set(newValue);
        });
        getDataFromEngine();
    }

    private void getDataFromEngine() {
        usersListView.getItems().clear();
        startListRefresher();
    }

    private void updateUsers(Set<String> users) {
        synchronized (currentUsers) {
            Set<String> newUsers = new HashSet<>(users);
            Set<String> removedUsers = new HashSet<>();

            Platform.runLater(() -> {
                usersListView.getItems().removeIf(user -> {
                    if (!newUsers.contains(user)) {
                        removedUsers.add(user);
                        return true;
                    }
                    return false;
                });
            });

            for (String user : newUsers) {
                if (!currentUsers.contains(user) && !removedUsers.contains(user)) {
                    currentUsers.add(user);
                    Platform.runLater(() -> usersListView.getItems().add(user));
                }
            }
        }
    }

    public void startListRefresher() {
        UserListRefresher userListRefresher = new UserListRefresher(
                this::updateUsers);
        Timer timer = new Timer();
        timer.schedule(userListRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public SimpleStringProperty getChosenUser() {
        return this.chosenUser;
    }
}
