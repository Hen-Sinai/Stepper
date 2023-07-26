package servlets;

import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AllFlowsServlet", urlPatterns = "/all_flows")
public class AllFlowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();
        FlowsNameDTO names;

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        names = engineManager.getFlowsNames(username);

        String namesJson = gson.toJson(names);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(namesJson);
    }
}
