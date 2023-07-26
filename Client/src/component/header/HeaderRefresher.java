package component.header;

import DTO.FlowsNameDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import javafx.beans.property.SimpleBooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class HeaderRefresher extends TimerTask {
    private final Consumer<UserDTO> userConsumer;

    public HeaderRefresher(Consumer<UserDTO> userConsumer) {
        this.userConsumer = userConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.USER)
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
                        UserDTO user = new Gson().fromJson(responseData, UserDTO.class);
                        userConsumer.accept(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
