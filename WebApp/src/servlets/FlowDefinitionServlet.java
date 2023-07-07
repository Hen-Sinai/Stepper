package servlets;

import DTO.FlowDTO;
import DTO.FlowsNameDTO;
import com.google.gson.Gson;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FlowDefinitionServlet", urlPatterns = "/flow_minimal_description")
public class FlowDefinitionServlet extends HttpServlet {
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
        FlowDTO flow = engineManager.getFlowData(flowName);

        String descriptionJson = gson.toJson(flow);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(descriptionJson);
    }
}