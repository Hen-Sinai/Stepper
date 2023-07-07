package component.header;

import main.AppController;

public class HeaderController {
    private AppController parentController;

    public void init(AppController mainController) {
        this.parentController = mainController;
    }

    public AppController getParentController() {
        return this.parentController;
    }

}
