package component.executionResult.ioPresentation.relationPresentation;

import dd.impl.relation.RelationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

public class RelationPresentation extends VBox {
    private final TableView<Map<String, String>> tableView;

    public RelationPresentation(RelationData data) {
        tableView = createTableView(data);
        tableView.getItems().addAll(data.getRows());

        VBox.setVgrow(tableView, Priority.ALWAYS);

        getChildren().addAll(tableView);

        setFillWidth(true); // Enable filling the available width
    }

    private TableView<Map<String, String>> createTableView(RelationData data) {
        TableView<Map<String, String>> tableView = new TableView<>();
        tableView.setPrefHeight(USE_COMPUTED_SIZE);
        tableView.setPrefWidth(USE_COMPUTED_SIZE);

        // Create columns based on the column names
        ObservableList<TableColumn<Map<String, String>, ?>> columns = tableView.getColumns();
        for (String columnName : data.getColumns()) {
            TableColumn<Map<String, String>, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnName)));
            columns.add(column);
        }

        // Adjust table height based on the number of rows
        tableView.fixedCellSizeProperty().bind(tableView.prefHeightProperty().divide(data.getRows().size()));
        tableView.minHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(data.getRows().size()));
        tableView.maxHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(data.getRows().size()));

        return tableView;
    }

    public TableView<Map<String, String>> getTableView() {
        return this.tableView;
    }
}