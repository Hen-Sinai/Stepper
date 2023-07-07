package servlets;

import DTO.RoleDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "UserRoleServlet", urlPatterns = "/user_roles")
public class UserRoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        username = req.getParameter("username");

        Map<String, RoleDTO> roles = engineManager.getUserRoles(username);
        String rolesStr = new Gson().toJson(roles, new TypeToken<Map<String, RoleDTO>>(){}.getType());
        res.getWriter().write(rolesStr);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}