package flow.execution.context;

import Exceptions.NoMatchTypeException;
import flow.stepInfo.StepInfoManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface StepExecutionContext extends Serializable {
    void dropStep();
    Map<String, Object> getDataValues();
    <T> T getDataValue(String dataName, Class<T> expectedDataType) throws NoMatchTypeException;
    void storeDataValue(String dataName, Object value) throws NoMatchTypeException;
    boolean isDataValueExist(String dataName);
    // some more utility methods:
    // allow step to store log lines
    List<StepInfoManager> getStepsInfoList();
    Map<String, StepInfoManager> getStepsInfoMap();
    StepInfoManager getLastStepInfo();
    StepInfoManager getStepInfo(String stepName);
    void addStepInfo(StepInfoManager stepInfoManager);
}
