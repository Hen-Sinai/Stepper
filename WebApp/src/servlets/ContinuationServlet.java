package servlets;

import DTO.FlowExecutedDataDTO;
import com.google.gson.Gson;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "ContinuationServlet", urlPatterns = "/continuations")
public class ContinuationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String flowName = req.getParameter("flowName");
        List<String> continuations = engineManager.getFlowData(flowName).getContinuations();

        String continuationsJson = gson.toJson(continuations);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(continuationsJson);
    }
}