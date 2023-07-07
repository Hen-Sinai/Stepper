package roles;

import java.util.Set;

public interface Role {
    String getName();
    String getDescription();
    Set<String> getAllowedFlows();
    void addAllowedFlow(String flowName);
}
