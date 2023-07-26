package component.header.xmlLoader;

import DTO.RoleDTO;
import Exceptions.*;
import com.google.gson.Gson;
import component.header.HeaderController;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class XmlLoaderController {
    private HeaderController parentController;
    @FXML private Button loadXml;
    @FXML private Label pathLabel;
    @FXML private Label errorLabel;
    private final SimpleStringProperty chosenPath;
    private final SimpleBooleanProperty isFileSelected;
    private boolean isFirstFile = true;

    public XmlLoaderController() {
        chosenPath = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        pathLabel.textProperty().bind(chosenPath);
    }

    public void setHeaderController(HeaderController headerController) {
        this.parentController = headerController;
    }

    public void LoadFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(parentController.getParentController().getPrimaryStage());
        if (selectedFile == null) {
            return;
        }

        String finalUrl;
        if (isFirstFile) {
            finalUrl = HttpUrl
                    .parse(Constants.XML_LOADER)
                    .newBuilder()
                    .addQueryParameter("isFirstUpload", "true")
                    .build()
                    .toString();
            isFirstFile = false;
        }
        else {
            finalUrl = HttpUrl
                    .parse(Constants.XML_LOADER)
                    .newBuilder()
                    .addQueryParameter("isFirstUpload", "false")
                    .build()
                    .toString();
        }
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("xmlFile", selectedFile.getAbsolutePath(),
                        RequestBody.create(selectedFile, MediaType.parse("text/plain")))
                .build();
        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorLabel.setText(e.getMessage());
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        errorLabel.setText("");
                        isFileSelected.set(true);
                        chosenPath.set(selectedFile.getAbsolutePath());
                        if (parentController.getParentController().getBodyComponentController().getTabPane().isDisabled())
                            parentController.getParentController().getBodyComponentController().getTabPane().setDisable(false);
                    });
                }
                else {
                    try {
                        String responseData = response.body().string();
                        Platform.runLater(() -> {
                            errorLabel.setText(responseData);
                        });
                    } catch (IOException ignore) {}
                }
            }
        });
    }

    public SimpleBooleanProperty getIsFileSelected() {
        return this.isFileSelected;
    }
}
