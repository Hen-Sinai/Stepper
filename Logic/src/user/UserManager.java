package user;

import DTO.RoleDTO;
import DTO.UserDTO;
import roles.AllFlowsRole;
import roles.ReadOnlyFlowsRole;
import roles.Role;
import roles.RoleImpl;

import java.util.*;

public class UserManager {
    private String adminName;
    private final Map<String, User> users = new HashMap<>();

    public synchronized void addUser(String username, boolean isManager) {
        users.put(username, new User(username, isManager));
    }

    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    public synchronized User getUser(String username) {
        return this.users.get(username);
    }
    public synchronized Map<String, User> getUsers() {
        return users;
    }

//    public Map<String, Role> getUserRoles(String userName) {
//        return this.users.get(userName).getRoles();
//    }

    public Map<String, Role> getUserRoles(String userName) {
        return this.users.get(userName).getRoles();
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    public synchronized void setAdminName(String name) {
        adminName = name;
    }

    public synchronized String getAdminName() {
        return this.adminName;
    }

    public void updateUser(UserDTO user) {
        Map<String, Role> roles = new HashMap<>();
        for (Map.Entry<String, RoleDTO> role : user.getRoles().entrySet()) {
            roles.put(role.getKey(), new RoleImpl(role.getValue().getName(), role.getValue().getDescription(), role.getValue().getAllowedFlows()));
        }

        User userToUpdate = this.getUser(user.getName());
        userToUpdate.setIsManager(user.getIsManager());
        userToUpdate.setRoles(roles);
    }
}
