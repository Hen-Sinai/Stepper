package roles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RolesManager {
    private final Map<String, Role> name2Role;
    private final Set<String> rolesNames;

    public RolesManager() {
        this.name2Role = new HashMap<String, Role>() {{
            put("Read Only Flows",new AllFlowsRole());
            put("All Flows",new ReadOnlyFlowsRole());
        }};
        this.rolesNames = new HashSet<String>() {{
            add("Read Only Flows");
            add("All Flows");
        }};
    }

    public void addRole(Role role) {
        this.name2Role.put(role.getName(), role);
        this.rolesNames.add(role.getName());
    }

    public void updateRole(Role role) {
        this.name2Role.remove(role.getName());
        this.rolesNames.remove(role.getName());
        addRole(role);
    }

    public Role getRole(String roleName) {
        return this.name2Role.get(roleName);
    }

    public Set<String> getRolesNames() {
        return this.rolesNames;
    }

    public Map<String, Role> getName2Role() {
        return this.name2Role;
    }
}
