package menu;

import DTO.StatsDTO;
import engineManager.EngineManager;
import flow.execution.StatsData;

import java.util.*;

public class ExhibitStats implements Runnable {
    private final EngineManager engineManager;

    public ExhibitStats(EngineManager engineManager) {
        this.engineManager = engineManager;
    }

    @Override
    public void run() {
//        StatsDTO stats = engineManager.getStats();
//        printFlowsStats(stats.getFlowStats());
//        printStepsStats(stats.getStepsStats());
    }

    public void printFlowsStats(Map<String, StatsData> flowStats) {
        for (Map.Entry<String, StatsData> entry : flowStats.entrySet()) {
            System.out.println("    Flow name: " + entry.getKey());
            System.out.println("    Amount of executions: " + entry.getValue().getAmountOfExecutions());
            System.out.println("    Average duration: " + entry.getValue().getAverageDuration().toMillis() + "ms");
            System.out.println();
        }
    }

    public void printStepsStats(Map<String, StatsData> stepsStats) {
        for (Map.Entry<String, StatsData> entry : stepsStats.entrySet()) {
            System.out.println("    Step name: " + entry.getKey());
            System.out.println("    Amount of executions: " + entry.getValue().getAmountOfExecutions());
            System.out.println("    Average duration: " + entry.getValue().getAverageDuration().toMillis() + "ms");
            System.out.println();
        }
    }
}
