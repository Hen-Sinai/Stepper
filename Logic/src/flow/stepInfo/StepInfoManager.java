package flow.stepInfo;

import flow.stepInfo.log.Log;
import step.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

public interface StepInfoManager extends Serializable {
    String getName();
    void setName(String name);
    void setSummaryLine(String summaryLine);
    String getSummaryLine();
    List<Log> getLogs();
    void addLog(String log);
    void setDuration(Duration duration);
    Duration getDuration();
    void setStepResult(StepResult stepResult);
    StepResult getStepResult();
    void setStartTimeStamp();
    String getStartTimeStamp();
    void setFinishTimeStamp();
    String getFinishTimeStamp();
}
