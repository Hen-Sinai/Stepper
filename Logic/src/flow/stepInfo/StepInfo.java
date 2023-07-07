package flow.stepInfo;

import flow.stepInfo.log.LogImpl;
import flow.stepInfo.log.Log;
import step.api.StepResult;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepInfo implements StepInfoManager {
    private String name;
    private String summaryLine;
    private final List<Log> logs = new ArrayList<>();
    private final static SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss.sss");
    private String startTimeStamp;
    private String finishTimeStamp;
    private Duration duration;
    private StepResult stepResult;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSummaryLine(String summaryLine) {
        this.summaryLine = summaryLine;
    }

    @Override
    public String getSummaryLine() {
        return this.summaryLine;
    }

    @Override
    public List<Log> getLogs() {
        return this.logs;
    }

    @Override
    public void addLog(String log) {
        this.logs.add(new LogImpl(log));
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public void setStepResult(StepResult stepResult) {
        this.stepResult = stepResult;
    }

    @Override
    public StepResult getStepResult() {
        return this.stepResult;
    }

    @Override
    public String getStartTimeStamp() {
        return this.startTimeStamp;
    }

    @Override
    public void setStartTimeStamp() {
        this.startTimeStamp = sdfDate.format(new Date());
    }

    @Override
    public String getFinishTimeStamp() {
        return this.finishTimeStamp;
    }

    @Override
    public void setFinishTimeStamp() {
        this.finishTimeStamp = sdfDate.format(new Date());
    }
}
