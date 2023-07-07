//package screen.statistics;
//
//import component.stats.Stats;
//import engineManager.EngineManager;
//import engineManager.EngineManagerImpl;
//import javafx.fxml.FXML;
//import javafx.scene.layout.GridPane;
//import screen.BodyController;
//
//public class StatisticsController {
//    private final EngineManager engineManager = EngineManagerImpl.getInstance();
//    private BodyController parentController;
//    private Stats stepsStats;
//    private Stats flowsStats;
//    @FXML
//    private GridPane statisticsPane;
//
//    public void init(BodyController bodyController) {
//        this.parentController = bodyController;
//        stepsStats = new Stats(this, engineManager.getStepsStats(), "Steps statistics");
//        flowsStats = new Stats(this, engineManager.getFlowsStats(), "Flows statistics");
//
//        statisticsPane.add(flowsStats, 1, 1);
//        statisticsPane.add(stepsStats, 3, 1);
//
//        parentController.getIsStatisticsVisible().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                stepsStats.setStats(engineManager.getStepsStats());
//                flowsStats.setStats(engineManager.getFlowsStats());
//            }
//        });
//    }
//}