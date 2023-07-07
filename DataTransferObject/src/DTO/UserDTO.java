package DTO;

import roles.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserDTO {
    private final String name;
    private final boolean isManager;
    private Map<String, RoleDTO> roles;

    public UserDTO(String name, boolean isManager) {
        this.name = name;
        this.isManager = isManager;
    }

    public UserDTO(String name, boolean isManager, Map<String, RoleDTO> roles) {
        this.name = name;
        this.isManager = isManager;
        this.roles = roles;
    }

    public String getName() {
        return this.name;
    }

    public boolean getIsManager() {
        return this.isManager;
    }

    public Map<String, RoleDTO> getRoles() {
        return this.roles;
    }
}
