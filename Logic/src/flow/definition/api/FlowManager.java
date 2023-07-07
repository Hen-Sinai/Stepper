package flow.definition.api;

import Exceptions.*;
import mySchema.stStepper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FlowManager extends Serializable {
    Map<String, FlowDefinition> getFlows();
    FlowDefinition getFlow(String name);
    List<String> getFlowsName();
    int getTheadPoolSize();
    void updateDataUsingXml(stStepper stepper) throws OutputNameNotUnique, UserInputNotFriendly, DataNotExistCustomMapping, CustomDataNotmatch,
            ReferenceToForwardStep, DataNotExistFlowLevelAliasing, FlowOutputNotExist, UserInputTypeCollision,
            InitialInputValueNotExist, InitialInputValueTypeNotMatch, StepNotExist, StepNameNotUnique, FlowNameExist, FlowNotExist, DataNotExistContinuation;
}
