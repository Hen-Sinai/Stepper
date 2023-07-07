package servlets;

import DTO.FlowDTO;
import com.google.gson.Gson;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "FlowDetailsServlet", urlPatterns = "/flow_details")
public class FlowDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String flowName = req.getParameter("flowName");
        FlowDTO flowData = engineManager.getFlowData(flowName);

        String descriptionJson = gson.toJson(flowData);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(descriptionJson);
    }
}