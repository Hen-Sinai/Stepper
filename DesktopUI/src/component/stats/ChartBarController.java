//package component.stats;
//
//import DTO.StatsDTO;
//import flow.execution.StatsData;
//import javafx.fxml.FXML;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.XYChart;
//
//import java.util.Map;
//
//public class ChartBarController {
//    @FXML BarChart<String, Integer> chartBar;
//
//    public void init(StatsDTO stats) {
//        XYChart.Series series = new XYChart.Series();
//
//        for (Map.Entry<String, StatsData> stat : stats.getStats().entrySet()) {
//            series.getData().add(new XYChart.Data(stat.getKey(), stat.getValue().getAmountOfExecutions()));
//        }
//
//        chartBar.getData().add(series);
//    }
//}
