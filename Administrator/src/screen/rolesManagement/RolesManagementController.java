package screen.rolesManagement;

import component.roles.newRole.NewRoleController;
import component.roles.rolesInfo.RolesInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import screen.BodyController;

import java.io.IOException;

public class RolesManagementController {
    private BodyController parentController;
    @FXML private component.roles.rolesList.RolesListController rolesListComponentController;
    @FXML private RolesInfoController rolesInfoComponentController;
    private NewRoleController newRoleController;

    public void init(BodyController parentController) {
        this.parentController = parentController;
        this.rolesListComponentController.init(this);
        this.rolesInfoComponentController.init(this);
    }

    public component.roles.rolesList.RolesListController getRolesListComponentController() {
        return this.rolesListComponentController;
    }

    public RolesInfoController getRolesInfoComponentController() {
        return this.rolesInfoComponentController;
    }

    public NewRoleController getNewRoleController() {
        return this.newRoleController;
    }

    @FXML
    public void onPressNewRole(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/roles/newRole/newRole.fxml"));
            GridPane root = fxmlLoader.load();
            newRoleController = fxmlLoader.getController();
            newRoleController.init(this);
            // Create a new stage for the new window
            Stage newStage = new Stage();
            newStage.setTitle("New Role");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
