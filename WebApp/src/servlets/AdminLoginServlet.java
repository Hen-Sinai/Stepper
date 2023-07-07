package servlets;

import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = "/admin_login")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        String usernameFromSession = SessionUtils.getUsername(request);
        response.setContentType("text/plain;charset=UTF-8");

        if (usernameFromSession == null) {
            String usernameFromParameter = "Admin";
            if (engineManager.getAdminName() != null) {
                String errorMessage = "Admin already logged in.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            }
            else {
                engineManager.addUser(usernameFromParameter, true);
                engineManager.setAdminName(usernameFromParameter);
                request.getSession(true).setAttribute("username", usernameFromParameter);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
