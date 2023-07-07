package dataStructures;

import javafx.scene.Node;

public class TableViewData {
    private final String name;
    private final Node node;

    public TableViewData(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public Node getNode() {
        return node;
    }
}
