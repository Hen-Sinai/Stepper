package component.users.usersList;

import DTO.FlowsNameDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UserListRefresher extends TimerTask {
    private final Consumer<Set<String>> usersConsumer;

    public UserListRefresher(Consumer<Set<String>> usersConsumer) {
        this.usersConsumer = usersConsumer;
    }

    @Override
    public void run() {
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
                        Set<UserDTO> usersDTO =  new Gson().fromJson(responseData, new TypeToken<Set<UserDTO>>(){}.getType());
                        Set<String> users = usersDTO.stream().map(UserDTO::getName).collect(Collectors.toSet());
                        usersConsumer.accept(users);
                    } catch (IOException ignore) {}
                }
            }
        });
    }
}
