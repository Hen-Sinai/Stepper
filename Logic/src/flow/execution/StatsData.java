package flow.execution;

import java.io.Serializable;
import java.time.Duration;

public class StatsData implements Serializable {
    private int amountOfExecutions = 0;
    private Duration totalDuration = Duration.ZERO;

    public int getAmountOfExecutions() {
        return this.amountOfExecutions;
    }

    public Duration getTotalDuration() {
        return this.totalDuration;
    }

    public Duration getAverageDuration() {
        if (amountOfExecutions == 0)
            return Duration.ZERO;
        return totalDuration.dividedBy(amountOfExecutions);
    }

    public void addAmountOfExecutions() {
        this.amountOfExecutions += 1;
    }

    public void addDuration(Duration duration) {
        this.totalDuration = this.totalDuration.plus(duration);
    }

    @Override
    public String toString() {
        return "Amount of executions: " + amountOfExecutions + '\n' +
                "Average duration: " + this.totalDuration.toMillis() + "ms";
    }
}
