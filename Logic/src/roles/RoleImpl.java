package roles;

import java.util.HashSet;
import java.util.Set;

public class RoleImpl implements Role {
    protected final String name;
    protected final String description;
    protected Set<String> allowedFlows;

    public RoleImpl(String name, String description, Set<String> allowedFlows) {
        this.name = name;
        this.description = description;
        this.allowedFlows = allowedFlows;
    }

    protected RoleImpl(String name, String description) {
        this.name = name;
        this.description = description;
        this.allowedFlows = new HashSet<>();
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
    public Set<String> getAllowedFlows() {
        return this.allowedFlows;
    }

    @Override
    public void addAllowedFlow(String flowName) {
        this.allowedFlows.add(flowName);
    }
}
