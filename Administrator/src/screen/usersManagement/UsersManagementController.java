package screen.usersManagement;

import component.users.userInfo.UserInfoController;
import component.users.usersList.UserListController;
import javafx.fxml.FXML;
import screen.BodyController;

public class UsersManagementController {
    private BodyController parentController;
    @FXML private UserListController usersListComponentController;
    @FXML private UserInfoController userInfoComponentController;

    public void init(BodyController parentController) {
        this.parentController = parentController;
        this.usersListComponentController.init(this);
        this.userInfoComponentController.init(this);
    }

    public UserListController getUsersListComponentController() {
        return this.usersListComponentController;
    }

    public UserInfoController getUserInfoComponentController() {
        return this.userInfoComponentController;
    }
}
