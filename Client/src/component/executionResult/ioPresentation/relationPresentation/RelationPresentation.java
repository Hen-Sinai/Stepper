package component.executionResult.ioPresentation.relationPresentation;

import dataStructures.HistoryFlowsData;
import dd.impl.relation.RelationData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class RelationPresentation extends VBox {
    private final TableView<Map<String, String>> tableView;

    public RelationPresentation(LinkedHashMap data) {
        List<String> columns = (List<String>)(data.get("columns"));
        List<Map<String, String>> rows = (List<Map<String, String>>)(data.get("rows"));
        RelationData RelationData = new RelationData(columns, rows);

        tableView = createTableView(RelationData);
        tableView.getItems().addAll(RelationData.getRows());

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
}
