package servlets;

import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AvailableFlowsServlet", urlPatterns = "/available_flows")
public class AvailableFlowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();
        FlowsNameDTO names;

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (username.equals("Admin"))
            username = req.getParameter("username");

        if (req.getParameter("flowsName") != null) {
            names = engineManager.getFlowsNames(username);
        }
        else {
            names = engineManager.getFlowsNames(username);
        }

        String namesJson = gson.toJson(names);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(namesJson);
    }
}