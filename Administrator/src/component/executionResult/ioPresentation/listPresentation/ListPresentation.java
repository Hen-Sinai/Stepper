package component.executionResult.ioPresentation.listPresentation;

import dd.impl.list.ListData;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ListPresentation<T> extends VBox {
    private final ListView<T> listView;

    public ListPresentation(ListData<T> data) {
        listView = createListView();
        listView.getItems().addAll(data.getData());

        VBox.setVgrow(listView, Priority.ALWAYS);

        getChildren().addAll(listView);

        setFillWidth(true); // Enable filling the available width

        listView.setFixedCellSize(30); // Set a fixed cell size for proper row height
        listView.prefHeightProperty().bind(listView.fixedCellSizeProperty().multiply(listView.getItems().size()).add(30)); // Adjust table height based on the number of items
        listView.minHeightProperty().bind(listView.prefHeightProperty());
        listView.maxHeightProperty().bind(listView.prefHeightProperty());
    }

    private ListView<T> createListView() {
        ListView<T> listView = new ListView<>();
        listView.setPrefHeight(USE_COMPUTED_SIZE);
        listView.setPrefWidth(USE_COMPUTED_SIZE);

        return listView;
    }

    public ListView<T> getListView() {
        return this.listView;
    }
}
