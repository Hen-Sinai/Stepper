//package component.stats;
//
//import DTO.StatsDTO;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.scene.Node;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import screen.statistics.StatisticsController;
//
//public class Stats extends VBox {
//    private final TableView<Node> tableView;
//    private final StatisticsController parentController;
//    private final StatsController statsController;
//
//    public Stats(StatisticsController statisticsController, StatsDTO stats, String header) {
//        this.parentController = statisticsController;
//        tableView = createTableView(header);
//        statsController = new StatsController(this, tableView, stats, header);
//
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        getChildren().add(tableView);
//
//        setFillWidth(true); // Enable filling the available width
//    }
//
//    private TableView<Node> createTableView(String header) {
//        TableView<Node> tableView = new TableView<>();
//        tableView.setPrefHeight(USE_COMPUTED_SIZE);
//        tableView.setPrefWidth(USE_COMPUTED_SIZE);
//
//        TableColumn<Node, Node> dataColumn = new TableColumn<>(header);
//        dataColumn.setMaxWidth(Double.MAX_VALUE);
//        dataColumn.setPrefWidth(475);
//        dataColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
//
//        tableView.getColumns().addAll(dataColumn);
//
//        return tableView;
//    }
//
//    public StatisticsController getParentController() {
//        return this.parentController;
//    }
//
//    public void setStats(StatsDTO stats) {
//        statsController.setStats(stats);
//    }
//}
