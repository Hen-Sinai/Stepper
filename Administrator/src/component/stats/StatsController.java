package component.stats;

import DTO.StatsDTO;
import flow.execution.StatsData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;

public class StatsController {
    private final TableView<Node> tableView;
    private StatsDTO stats;
    private final SimpleBooleanProperty isStatsUpdated = new SimpleBooleanProperty(false);
    private String header;

    public StatsController(TableView<Node> tableView, String header) {
        this.tableView = tableView;
        this.header = header;

        isStatsUpdated.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                init();
            }
        });
    }

    public void setStats(StatsDTO stats) {
        this.stats = stats;
        this.isStatsUpdated.setValue(true);
        this.isStatsUpdated.setValue(false);
    }

    public void init() {
        ObservableList<Node> items = FXCollections.observableArrayList();
        // Create and populate the data for the FlowDetails component
        for (Map.Entry<String, StatsData> stat : stats.getStats().entrySet()) {
            if (header.equals("Flows statistics"))
                items.add(createTextArea("Flow name: " + stat.getKey() + "\n" + stat.getValue().toString()));
            else
                items.add(createTextArea("Step name: " + stat.getKey() + "\n" + stat.getValue().toString()));
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChartBar.fxml"));
        try {
            BarChart<String, Integer> barChart = fxmlLoader.load();
            ChartBarController controller = fxmlLoader.getController();
            controller.init(stats);
            items.add(barChart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setItems(items);
    }

    private TextArea createTextArea(String textInput) {
        TextArea textArea = new TextArea(textInput);
        textArea.setWrapText(true);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);
        textArea.setEditable(false);
        textArea.setPrefHeight(Control.USE_COMPUTED_SIZE);

        Text text = new Text();
        text.setFont(textArea.getFont());
        text.setText(textInput);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        double lineHeight = text.getLineSpacing();
        int prefRowCount = (int) Math.ceil((textHeight + lineHeight) / textArea.getFont().getSize());
        textArea.setPrefRowCount(prefRowCount);

        return textArea;
    }
}
