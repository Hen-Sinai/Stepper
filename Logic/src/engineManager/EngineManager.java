package engineManager;

import DTO.*;
import Exceptions.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface EngineManager {
    void loadXmlFile(InputStream inputStream) throws JAXBException, IOException, NoXmlFormat, ReferenceToForwardStep,
            FlowOutputNotExist, StepNotExist, StepNameNotUnique, OutputNameNotUnique, DataNotExistFlowLevelAliasing,
            UserInputNotFriendly, FlowNameExist, DataNotExistCustomMapping, CustomDataNotmatch, UserInputTypeCollision,
            InitialInputValueNotExist, FlowNotExist, DataNotExistContinuation, InitialInputValueTypeNotMatch;
    void updateXml(InputStream inputStream) throws JAXBException, IOException, NoXmlFormat, ReferenceToForwardStep,
            FlowOutputNotExist, StepNotExist, StepNameNotUnique, OutputNameNotUnique, DataNotExistFlowLevelAliasing,
            UserInputNotFriendly, FlowNameExist, DataNotExistCustomMapping, CustomDataNotmatch, UserInputTypeCollision,
            InitialInputValueNotExist, FlowNotExist, DataNotExistContinuation, InitialInputValueTypeNotMatch;
    FlowsNameDTO getFlowsNames(String username);
    List<FlowDTO> getAllFlowsData();
    FlowDTO getFlowData(String name);
    List<FreeInputDTO> getFreeInputs(String flowName);
    List<FreeInputDTO> continueToNextFlow(UUID currentFlowID, String chosenFlow);
    List<FreeInputDTO> reRunFlow(UUID currentFlowID, String chosenFlow);
    ExecutionResultDTO executeFlow(ExecuteDataDTO executeDataDTO, String userName) throws MyInputMismatchException;
//    ExecutionResultDTO getExecutionResult();
    List<FlowExecutedInfoDTO> getFlowsExecutedInfoDTO(String filter, String userName, int fromIndex);
    FlowExecutedDataDTO getFlowExecutedDataDTO(UUID flowId);
    Map<String,StatsDTO> getsStats();
    StatsDTO getStepsStats();
    StatsDTO getFlowsStats();
    void setStats(UUID id);
    void setFinishTimeStamp(UUID id);
    Map<String, RoleDTO> getRoles();
    RoleDTO getRole(String roleName);
    Map<String, RoleDTO> getUserRoles(String username);
    void addRole(RoleDTO role);
    void updateRole(RoleDTO role);
    boolean deleteRoleIfPossible(String roleName);
    String getAdminName();
    void setAdminName(String username);
    boolean isUserExists(String username);
    void logout(String username);
    void addUser(String username, boolean isManager);
    Set<UserDTO> getUsers();
    UserDTO getUser(String username);
    void updateUser(UserDTO userData);
}
