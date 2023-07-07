package DTO;

import java.util.Set;

public class RoleDTO {
    private final String name;
    private final String description;
    private final Set<String> allowedFlows;

    public RoleDTO(String name, String description, Set<String> allowedFlows) {
        this.name = name;
        this.description = description;
        this.allowedFlows = allowedFlows;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<String> getAllowedFlows() {
        return this.allowedFlows;
    }
}
