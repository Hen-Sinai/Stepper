package user;

import flow.execution.StatsData;
import roles.Role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {
    private final String name;
    private boolean isManager;
    private final Map<String, Role> roles;

    public User(String name, boolean isManager) {
        this.name = name;
        this.isManager = isManager;
        this.roles = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public boolean getIsManager() {
        return this.isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public Map<String, Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Map<String, Role> roles) {
        for (Map.Entry<String, Role> role : roles.entrySet()) {
            if (!this.roles.containsKey(role.getKey()))
                this.roles.put(role.getKey(), role.getValue());
        }
    }
}
