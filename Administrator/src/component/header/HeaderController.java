package component.header;

import component.header.xmlLoader.XmlLoaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.AppController;

public class HeaderController {
    private AppController parentController;
    @FXML private XmlLoaderController xmlLoaderController;

    public void init(AppController mainController) {
        this.parentController = mainController;
        this.xmlLoaderController.setHeaderController(this);
    }

    public AppController getParentController() {
        return this.parentController;
    }

    public XmlLoaderController getXmlLoaderController() {
        return this.xmlLoaderController;
    }
}
