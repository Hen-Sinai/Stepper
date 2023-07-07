package component.roles.rolesInfo.usersList;

import DTO.FlowsNameDTO;
import DTO.RoleDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
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

public class UsersListController {
    @FXML
    private ListView<String> usersListView;
    public void init(String roleName) {
        initUsers(roleName);
    }

    private void initUsers(String roleName) {
        String finalUrl = HttpUrl
                .parse(Constants.USERS)
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
                        Set<UserDTO> users = new Gson().fromJson(responseData, new TypeToken<Set<UserDTO>>(){}.getType());
                        Platform.runLater(() -> {
                            createListView(users, roleName);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    private void createListView(Set<UserDTO> users, String roleName) {
        for (UserDTO user : users) {
            if (user.getRoles().containsKey(roleName))
                usersListView.getItems().add(user.getName());
        }
    }
}
