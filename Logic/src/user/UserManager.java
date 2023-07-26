package user;

import DTO.RoleDTO;
import DTO.UserDTO;
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

    public void logout(String username) {
        users.remove(username);
    }

    public synchronized void setAdminName(String name) {
        adminName = name;
    }

    public synchronized String getAdminName() {
        return this.adminName;
    }

    public void updateUser(UserDTO userDTO) {
        User user = this.getUser(userDTO.getName());
        Map<String, Role> roles = new HashMap<>();

        user.getRoles().clear();
        for (Map.Entry<String, RoleDTO> role : userDTO.getRoles().entrySet()) {
            roles.put(role.getKey(), new RoleImpl(role.getValue().getName(), role.getValue().getDescription(), role.getValue().getAllowedFlows()));
        }

        user.setIsManager(userDTO.getIsManager());
        user.setRoles(roles);
    }
}
