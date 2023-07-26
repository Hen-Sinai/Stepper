package servlets;

import DTO.ExecuteDataDTO;
import DTO.RoleDTO;
import DTO.UserDTO;
import Exceptions.MyInputMismatchException;
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
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (username.equals("Admin"))
            username = req.getParameter("username");

        UserDTO user = engineManager.getUser(username);
        String userStr = new Gson().toJson(user, UserDTO.class);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(userStr);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (!username.equals("Admin")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String inputs = req.getReader().lines().collect(Collectors.joining(" "));
        UserDTO userData = new Gson().fromJson(inputs, UserDTO.class);
        engineManager.updateUser(userData);

        res.setStatus(HttpServletResponse.SC_OK);
    }
}
