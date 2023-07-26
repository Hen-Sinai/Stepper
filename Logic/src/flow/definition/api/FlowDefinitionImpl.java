package flow.definition.api;

import DTO.ExecuteDataDTO;
import Exceptions.*;
import mySchema.stCustomMapping;
import mySchema.stFlow;
import step.api.*;

import java.util.*;

public class FlowDefinitionImpl implements FlowDefinition {
    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final Set<String> flowFormalOutputsMap;
    private final boolean isReadonly;
    private final List<StepUsageDeclaration> stepsList;
    private final Map<String, StepUsageDeclaration> stepsMap;
    private final List<Map<String, String>> stepAliasList;
    private final Map<String, Map<String, String>> dataAlias;
    private final Map<String, Map<String, String>> step2inputMapping;
    private Map<String, Map<String, String>> step2inputAlias;
    private final Map<String, Map<String, String>> step2outputMapping;
    private Map<String, Map<String, String>> step2outputAlias;
    private final List<stCustomMapping> customMapping;
    private final Map<String, Map<DataDefinitionDeclaration, List<String>>> userInputs;
    private final Map<String, Map<String, String>> connectedInputs;
    private final Map<String, Map<String, Map<String, String>>> connectedOutputs;
    private final Map<String, Object> initialInputValues;
    private final List<Continuation> continuations;

    public FlowDefinitionImpl(String name, String description, List<String> outputs,
                              List<StepUsageDeclaration> stepsList,
                              Map<String, StepUsageDeclaration> stepsMap,
                              List<Map<String, String>> stepAliasList,
                              Map<String, Map<String, String>> dataAlias,
                              List<stCustomMapping> customMapping,
                              Map<String, Object> initialInputValues,
                              List<Continuation> continuations) throws OutputNameNotUnique, UserInputNotFriendly,
            DataNotExistCustomMapping, CustomDataNotmatch, ReferenceToForwardStep, DataNotExistFlowLevelAliasing,
            FlowOutputNotExist, UserInputTypeCollision, InitialInputValueNotExist, InitialInputValueTypeNotMatch {
        this.name = name;
        this.description = description;
        this.flowOutputs = outputs;
        this.flowFormalOutputsMap = new HashSet<>(this.flowOutputs);
        this.stepsList = stepsList;
        this.stepsMap = stepsMap;
        this.isReadonly = isFlowReadonly();
        this.stepAliasList = stepAliasList;
        this.step2inputMapping = new HashMap<>();
        this.step2outputMapping = new HashMap<>();
        this.dataAlias = dataAlias;
        this.customMapping = customMapping;
        this.userInputs = new HashMap<>();
        this.connectedInputs = new HashMap<>();
        this.connectedOutputs = new HashMap<>();
        this.initialInputValues = initialInputValues;
        this.continuations = continuations;
        setUserInputs();
        validateFlowStructure();
    }

    public void addFlowOutput(String outputName) {
        this.flowOutputs.add(outputName);
    }

    @Override
    public void addStepToFlow(StepUsageDeclaration stepUsageDeclaration) {
        this.stepsList.add(stepUsageDeclaration);
    }

    @Override
    public List<Map<String, String>> getStepsAlias() {
        return this.stepAliasList;
    }

    private void validateFlowStructure() throws UserInputNotFriendly, OutputNameNotUnique, DataNotExistFlowLevelAliasing,
            FlowOutputNotExist, InitialInputValueNotExist, InitialInputValueTypeNotMatch {
        validateMandatoryInputAccess();
        validateOutputAppearOnce();
        validateFlowAliasingDataRef();
        validateFlowOutput();
        validateInitialInputNameExist();
//        validateInitialInputType();
    }

    @Override
    public Map<String, Map<DataDefinitionDeclaration, List<String>>> getFlowFreeInputs() {
        return this.userInputs;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean getIsReadonly() {
        return this.isReadonly;
    }

    @Override
    public List<StepUsageDeclaration> getFlowStepsList() {
        return this.stepsList;
    }

    @Override
    public Map<String, StepUsageDeclaration> getFlowStepsMap() {
        return this.stepsMap;
    }

    @Override
    public Map<String, Map<String, String>> getStep2inputMapping() {
        return this.step2inputMapping;
    }

    @Override
    public Map<String, Map<String, String>> getStep2outputMapping() {
        return this.step2outputMapping;
    }

    @Override
    public Map<String, Map<String, String>> getStep2inputAlias() {
        return this.step2inputAlias;
    }

    @Override
    public Map<String, Map<String, String>> getStep2outputAlias() {
        return this.step2outputAlias;
    }

    @Override
    public Map<String, Object> getInitialInputValues() {
        return this.initialInputValues;
    }

    @Override
    public List<stCustomMapping> getCustomMapping() {
        return this.customMapping;
    }

    @Override
    public Map<String, Map<String, String>> getConnectedInputs() {
        return this.connectedInputs;
    }

    @Override
    public Map<String, Map<String, Map<String, String>>> getConnectedOutputs() {
        return this.connectedOutputs;
    }

    @Override
    public List<Continuation> getContinuation() {
        return this.continuations;
    }

    private boolean isFlowReadonly() {
        for (StepUsageDeclaration step : this.stepsList) {
            if (!step.getStepDefinition().isReadonly())
                return false;
        }
        return true;
    }

    private void setStep2Alias(boolean processInputs) {
        Map<String, Map<String, String>> aliasMap = processInputs ? step2inputMapping : step2outputMapping;
        String key, alias;

        for (StepUsageDeclaration step : this.stepsList) {
            HashMap<String, String> aliases = new HashMap<>();
            Map<String, DataDefinitionDeclaration> definitions = processInputs ? step.getStepDefinition().getInputs() : step.getStepDefinition().getOutputs();

            for (Map.Entry<String, DataDefinitionDeclaration> entry : definitions.entrySet()) {
                Map<String, String> stepAlias = this.dataAlias.get(step.getFinalStepName());
                key = entry.getKey();
                alias = stepAlias != null && stepAlias.containsKey(key) ? stepAlias.get(key) : key;
                aliases.put(key, alias);
            }

            aliasMap.put(step.getFinalStepName(), aliases);
        }
    }

    private void extractUserInputs() throws UserInputTypeCollision {
        Map<String, String> foundOutputs = new HashMap<>(); // outputName 2 stepName
        Map<String, String> foundInputs = new HashMap<>();
        String inputAlias, inputType, outputAlias;
        DataDefinitionDeclaration inputDefinition;

        for (StepUsageDeclaration step : this.stepsList) {
            for (Map.Entry<String, String> entry : this.step2inputMapping.get(step.getFinalStepName()).entrySet()) {
                inputAlias = entry.getValue();
                inputDefinition = step.getStepDefinition().getInputs().get(entry.getKey());
                inputType = inputDefinition.getDataDefinition().getName();

                if (foundInputs.containsKey(inputAlias) && !foundInputs.get(inputAlias).equals(inputType)) {
                    throw new UserInputTypeCollision(inputAlias, foundInputs.get(inputAlias));
                }

                if (!foundOutputs.containsKey(inputAlias)) {
                    if (this.userInputs.containsKey(inputAlias)) {
                        this.userInputs.get(inputAlias).values().iterator().next().add(step.getFinalStepName());
                        if (this.userInputs.get(inputAlias).keySet().iterator().next().getNecessity() != DataNecessity.MANDATORY &&
                                inputDefinition.getNecessity() == DataNecessity.MANDATORY) {
                            List<String> steps = this.userInputs.get(inputAlias).values().iterator().next();
                            DataDefinitionDeclaration finalInputDefinition1 = inputDefinition;
                            this.userInputs.put(inputAlias, new HashMap<DataDefinitionDeclaration, List<String>>() {{
                                put(finalInputDefinition1, steps);
                            }});
                            foundInputs.put(inputAlias, finalInputDefinition1.getDataDefinition().getName());
                        }
                    } else {
                        DataDefinitionDeclaration finalInputDefinition = inputDefinition;
                        this.userInputs.put(inputAlias, new HashMap<DataDefinitionDeclaration, List<String>>() {{
                            put(finalInputDefinition, new ArrayList<String>() {{
                                add(step.getFinalStepName());
                            }});
                        }});
                        foundInputs.put(inputAlias, finalInputDefinition.getDataDefinition().getName());
                    }
                } else {
                    if (this.connectedInputs.containsKey(step.getFinalStepName())) { // If the step is in, insert input name and his source
                        this.connectedInputs.get(step.getFinalStepName()).put(inputAlias, foundOutputs.get(inputAlias));
                    } else {
                        String finalInputAlias = inputAlias;
                        this.connectedInputs.put(step.getFinalStepName(), new HashMap<String, String>() {{
                            put(finalInputAlias, foundOutputs.get(finalInputAlias));
                        }});
                    }

                    String inputName = this.step2inputAlias.get(step.getFinalStepName()).get(entry.getKey());
                    if (this.connectedOutputs.containsKey(foundOutputs.get(inputAlias))) {
                        this.connectedOutputs.get(foundOutputs.get(inputAlias)).get(inputAlias).put(step.getFinalStepName(), inputName);
                    } else {
                        String finalInputAlias1 = inputAlias;
                        this.connectedOutputs.put(foundOutputs.get(inputAlias), new HashMap<String, Map<String, String>>() {{
                            put(finalInputAlias1, new HashMap<String, String>() {{
                                put(step.getFinalStepName(), inputName);
                            }});
                        }});
                    }
                }
            }

            for (Map.Entry<String, String> entry : this.step2outputMapping.get(step.getFinalStepName()).entrySet()) {
                outputAlias = entry.getValue();
//                outputDefinition = step.getStepDefinition().getOutputs().get(entry.getKey());
//                outputType = outputDefinition.getDataDefinition().getName();

                foundOutputs.put(outputAlias, step.getFinalStepName()); // output 2 his step
            }
        }
    }

    private void setUserInputs() throws DataNotExistCustomMapping, CustomDataNotmatch, ReferenceToForwardStep, UserInputTypeCollision {
        setStep2Alias(true);
        this.step2inputAlias = deepCopy(this.step2inputMapping);
        setStep2Alias(false);
        this.step2outputAlias = deepCopy(this.step2outputMapping);

        // custom mapping
        boolean found;
        validateRefToStepBefore();
        validateCustomMappingDataConnection();
        for (stCustomMapping mapping : this.customMapping) {
            if (!this.initialInputValues.containsKey(mapping.getTargetData())) {
                found = false;
                for (Map.Entry<String, String> entry : step2inputMapping.get(mapping.getTargetStep()).entrySet()) {
                    if (entry.getValue().equals(mapping.getTargetData())) {
                        this.step2inputMapping.get(mapping.getTargetStep()).put(entry.getKey(), mapping.getSourceData());
                        found = true;
                    }
                }
                if (!found)
                    this.step2inputMapping.get(mapping.getTargetStep()).put(mapping.getTargetData(), mapping.getSourceData());
            }
        }

        this.extractUserInputs();
    }

    private Map<String, Map<String, String>> deepCopy(Map<String, Map<String, String>> copyFrom) {
        Map<String, Map<String, String>> deepCopiedMappings = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : copyFrom.entrySet()) {
            String key = entry.getKey();
            Map<String, String> originalValue = entry.getValue();
            Map<String, String> deepCopiedValue = new HashMap<>();
            for (Map.Entry<String, String> subEntry : originalValue.entrySet()) {
                String subKey = subEntry.getKey();
                String subValue = subEntry.getValue();
                deepCopiedValue.put(subKey, subValue);
            }
            deepCopiedMappings.put(key, deepCopiedValue);
        }
        return deepCopiedMappings;
    }



    @Override
    public Map<String, StepUsageDeclaration> getFlowOutputs() {
        Map<String, StepUsageDeclaration> outputs = new HashMap<>();
        for (StepUsageDeclaration step : this.stepsList) {
            for (Map.Entry<String, String> entry : this.step2outputMapping.get(step.getFinalStepName()).entrySet()) {
                outputs.put(entry.getKey() + "." + entry.getValue(), step);
            }
        }
        return outputs;
    }

    @Override
    public Map<String, DataDefinitionDeclaration> getFlowFormalOutputs() {
        Map<String, DataDefinitionDeclaration> outputs = new HashMap<>();
        for (StepUsageDeclaration step : this.stepsList) {
            for (Map.Entry<String, String> entry : this.step2outputMapping.get(step.getFinalStepName()).entrySet()) {
                if (this.flowFormalOutputsMap.contains(entry.getValue())) {
                    outputs.put(entry.getValue(), step.getStepDefinition().getOutputs().get(entry.getKey()));
                }
            }
        }
        return outputs;
    }

    private void validateMandatoryInputAccess() throws UserInputNotFriendly {
        for (Map<DataDefinitionDeclaration, List<String>> entry : this.userInputs.values()) {
            for (Map.Entry<DataDefinitionDeclaration, List<String>> data : entry.entrySet()) {
                if (data.getKey().getNecessity() == DataNecessity.MANDATORY && !data.getKey().getDataDefinition().isUserFriendly()) {
                    int lastDotIndex = data.getKey().getDataDefinition().getType().getName().lastIndexOf(".");
                    String substring = data.getKey().getDataDefinition().getUserPresentation();
                    throw new UserInputNotFriendly(this.name, data.getKey().getName(), substring);
                }
            }

        }
    }

    private StepUsageDeclaration getStepUsageDeclarationByName(String name) {
        for (StepUsageDeclaration step : this.stepsList) {
            if (step.getFinalStepName().equals(name)) {
                return step;
            }
        }
        return null;
    }

    private String findKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void validateCustomMappingDataConnection() throws DataNotExistCustomMapping, CustomDataNotmatch { // check if source is not in the step
        String typeOfSource, typeOfTarget, input2alias, output2alias;
        for (stCustomMapping mapping : this.customMapping) {
            if (!this.step2inputMapping.get(mapping.getTargetStep()).containsValue(mapping.getTargetData())) {
                throw new DataNotExistCustomMapping(this.name, "target-data", mapping.getTargetData(), mapping.getTargetStep());
            }
            else if (!this.step2outputMapping.get(mapping.getSourceStep()).containsValue(mapping.getSourceData())) {
                throw new DataNotExistCustomMapping(this.name, "source-data", mapping.getSourceData(), mapping.getSourceStep());
            }
            else {
                input2alias = findKeyByValue(this.step2inputMapping.get(mapping.getTargetStep()), mapping.getTargetData());
                typeOfTarget = getStepUsageDeclarationByName(mapping.getTargetStep()).getStepDefinition().getInputs().get(input2alias).getDataDefinition().getName();
                output2alias = findKeyByValue(this.step2outputMapping.get(mapping.getSourceStep()), mapping.getSourceData());
                typeOfSource = getStepUsageDeclarationByName(mapping.getSourceStep()).getStepDefinition().getOutputs().get(output2alias).getDataDefinition().getName();
                if (!typeOfSource.equals(typeOfTarget)) {
                    throw new CustomDataNotmatch(this.name, "custom mapping", mapping.getSourceData(), typeOfSource, mapping.getTargetData(), typeOfTarget);
                }
            }
        }
    }

    private void validateOutputAppearOnce() throws OutputNameNotUnique {
        HashSet<String> foundOutputs = new HashSet<>();

        for (Map.Entry<String, Map<String, String>> entryStep : this.step2outputMapping.entrySet()) {
            for (Map.Entry<String, String> entryData : entryStep.getValue().entrySet()) {
                if (foundOutputs.contains(entryData.getValue())) {
                    throw new OutputNameNotUnique(this.name, entryData.getValue());
                }
                foundOutputs.add(entryData.getValue());
            }
        }
    }

    private void validateRefToStepBefore() throws ReferenceToForwardStep {
        HashMap<String, Integer> stepsOrder = new HashMap<>();
        int counter = 0;

        for (StepUsageDeclaration step : this.stepsList) {
            stepsOrder.put(step.getFinalStepName(), counter);
            counter++;
        }

        for (stCustomMapping mapping : this.customMapping) {
            if (stepsOrder.get(mapping.getSourceStep()) > stepsOrder.get(mapping.getTargetStep())) {
                throw new ReferenceToForwardStep(this.name, mapping.getSourceStep(), mapping.getTargetStep());
            }
        }
    }

    private void validateFlowOutput() throws FlowOutputNotExist {
        for (String output : this.flowOutputs) {
            boolean found = false;
            for (Map<String, String> stepOutputAlias : this.step2outputMapping.values()) {
                if (stepOutputAlias.containsValue(output)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new FlowOutputNotExist(this.name, output);
            }
        }
    }


    private void validateFlowAliasingDataRef() throws DataNotExistFlowLevelAliasing {
        for (Map.Entry<String, Map<String, String>> stepEntry : this.dataAlias.entrySet()) {
            for (Map.Entry<String, String> dataEntry : stepEntry.getValue().entrySet()) {
                if (IO.getIOByNameAndStep(dataEntry.getKey(), getOriginalStepNameByAlias(stepEntry.getKey())) == null) {
                    throw new DataNotExistFlowLevelAliasing(this.name, stepEntry.getKey(), dataEntry.getKey());
                }
            }
        }
    }

    private String getOriginalStepNameByAlias(String aliasName) {
        for (Map<String, String> stepEntry : this.stepAliasList) {
            if (stepEntry.values().iterator().next().equals(aliasName))
                return stepEntry.keySet().iterator().next();
        }
        return null;
    }

    private void validateInitialInputNameExist() throws InitialInputValueNotExist {
        boolean found;
        for (Map.Entry<String, Object> initialInputEntry : this.initialInputValues.entrySet()) {
            found = false;
            for (Map.Entry<String, Map<String, String>> stepEntry : this.step2inputMapping.entrySet()) {
                if (stepEntry.getValue().containsKey(initialInputEntry.getKey())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new InitialInputValueNotExist(this.name, initialInputEntry.getKey());
        }
    }

    private void validateInitialInputType() throws InitialInputValueTypeNotMatch {
        Object initialInputValue;
        DataDefinitionDeclaration dataDefinitionDeclaration;
        for (Map.Entry<String, Object> entry : this.initialInputValues.entrySet()) {
            initialInputValue = entry.getValue();
            dataDefinitionDeclaration = this.userInputs.get(entry.getKey()).keySet().iterator().next();
            if (!dataDefinitionDeclaration.getDataDefinition().getType().isAssignableFrom(initialInputValue.getClass())) {
                throw new InitialInputValueTypeNotMatch(this.name, entry.getKey(),
                        dataDefinitionDeclaration.getDataDefinition().getName());
            }
        }
    }
}
