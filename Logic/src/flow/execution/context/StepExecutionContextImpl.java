package flow.execution.context;

import Exceptions.NoMatchTypeException;
import dd.api.DataDefinition;
import flow.stepInfo.StepInfoManager;
import step.api.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepExecutionContextImpl implements StepExecutionContext {
    private final Map<String, Object> dataValues;
    private final List<Map<String, String>> stepsAlias;
    private final Map<String, Map<String, String>> step2inputAlias;
    private final Map<String, Map<String, String>> step2outputAlias;
    private final List<StepInfoManager> stepsInfo;
    private final Map<String, StepInfoManager> step2info;
    private final Map<String, Object> initialInputs;

    public StepExecutionContextImpl(Map<String, Object> dataValues,
                                    List<Map<String, String>> stepsAlias,
                                    Map<String, Map<String, String>> step2inputAlias,
                                    Map<String, Map<String, String>> step2outputAlias,
                                    Map<String, Object> initialInputs
    ) {
        this.dataValues = dataValues;
        this.stepsAlias = new ArrayList<>(stepsAlias);
        this.step2inputAlias = step2inputAlias;
        this.step2outputAlias = step2outputAlias;
        this.initialInputs = initialInputs;
        this.stepsInfo = new ArrayList<>();
        this.step2info = new HashMap<>();
    }

    @Override
    public Map<String, Object> getDataValues() {
        return this.dataValues;
    }

    @Override
    public void dropStep() {
        this.stepsAlias.remove(0);
    }

    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) throws NoMatchTypeException {
        String originalStepName = this.stepsAlias.get(0).keySet().iterator().next();
        String aliasStepName = this.stepsAlias.get(0).values().iterator().next();
        String grabName = this.step2inputAlias.get(aliasStepName).get(dataName);
        DataDefinition theExpectedDataDefinition = IO.getIOByNameAndStep(dataName, originalStepName).getDataDefinition();

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue;
            if (initialInputs.containsKey(grabName))
                aValue = this.initialInputs.get(grabName);
            else
                aValue = dataValues.get(grabName);

            return expectedDataType.cast(aValue);
        } else {
            throw new NoMatchTypeException();
        }
    }

    @Override
    public void storeDataValue(String dataName, Object value) throws NoMatchTypeException {
        String originalStepName = this.stepsAlias.get(0).keySet().iterator().next();
        String aliasStepName = this.stepsAlias.get(0).values().iterator().next();
        String storeName = this.step2outputAlias.get(aliasStepName).get(dataName);
//        String storeName = this.step2outputAlias.get(this.stepsAlias.get(0).get(stepName)).get(dataName);
        // assuming that from the data name we can get to its data definition
        DataDefinition theData = IO.getIOByNameAndStep(dataName, originalStepName).getDataDefinition();

        // we have the DD type so we can make sure that its from the same type
        if (theData.getType().isAssignableFrom(value.getClass())) {
            this.dataValues.put(storeName, value);
        } else {
            throw new NoMatchTypeException();
        }
    }

    @Override
    public boolean isDataValueExist(String dataName) {
        return this.dataValues.containsKey(dataName);
    }

    @Override
    public List<StepInfoManager> getStepsInfoList() {
        return this.stepsInfo;
    }

    @Override
    public Map<String, StepInfoManager> getStepsInfoMap() {
        return this.step2info;
    }

    @Override
    public StepInfoManager getLastStepInfo() {
        return stepsInfo.get(stepsInfo.size() - 1);
    }

        @Override
    public StepInfoManager getStepInfo(String stepName) {
        return this.step2info.get(stepName);
    }

    @Override
    public void addStepInfo(StepInfoManager stepInfo) {
        String stepName = this.stepsAlias.get(0).values().iterator().next();
        stepInfo.setName(stepName);
        this.stepsInfo.add(stepInfo);
        this.step2info.put(stepName, stepInfo);
    }
}
