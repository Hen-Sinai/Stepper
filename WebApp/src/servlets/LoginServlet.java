package servlets;

import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        String usernameFromSession = SessionUtils.getUsername(request);
        response.setContentType("text/plain;charset=UTF-8");

        if (usernameFromSession == null) {
            String usernameFromParameter = request.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (engineManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        engineManager.addUser(usernameFromParameter, false);
                        request.getSession(true).setAttribute("username", usernameFromParameter);

                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        engineManager.logout(username);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}