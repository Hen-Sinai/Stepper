package dd.impl.relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationData implements Serializable {
    private final List<String> columns;
    private final List<Map<String, String>> rows;

    public RelationData(List<String> columns) {
        this.columns = columns;
        this.rows = new ArrayList<>();
    }

    public RelationData(List<String> columns, List<Map<String, String>> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public List<Map<String, String>> getRows() {
        return this.rows;
    }

    public Map<String, String> getRow(int rowId) {
        return this.rows.get(rowId);
    }

    public void addRow(List<String> data) {
        Map<String, String> newRow = new HashMap<>();
        newRow.put(columns.get(0), String.valueOf(rows.size() + 1));
        for (int i = 1; i < columns.size(); i++)
            newRow.put(columns.get(i), data.get(i - 1));
        rows.add(newRow);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String column : columns) {
            sb.append(column);
            sb.append(", ");
        }
        if (!columns.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Remove the last ", "
        }
        sb.append("\n");
        sb.append("Amount of lines: ").append(rows.size());
        return sb.toString();
    }
}
