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
import java.util.stream.Collectors;

@WebServlet(name = "RoleServlet", urlPatterns = "/role")
public class RoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        RoleDTO role = engineManager.getRole(req.getParameter("roleName"));
        String roleStr = new Gson().toJson(role, RoleDTO.class);
        res.getWriter().write(roleStr);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String roleData = req.getReader().lines().collect(Collectors.joining(" "));
        RoleDTO roleDTO = new Gson().fromJson(roleData, RoleDTO.class);
        engineManager.addRole(roleDTO);

        res.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String roleData = req.getReader().lines().collect(Collectors.joining(" "));
        RoleDTO roleDTO = new Gson().fromJson(roleData, RoleDTO.class);
        engineManager.updateRole(roleDTO);

        res.setStatus(HttpServletResponse.SC_OK);
    }
}