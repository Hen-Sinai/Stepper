package servlets;

import DTO.FlowDTO;
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
import java.util.UUID;

@WebServlet(name = "FlowProgressServlet", urlPatterns = "/flow_progress")
public class FlowProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UUID flowId = UUID.fromString(req.getParameter("flowId"));
        FlowExecutedDataDTO executedData = engineManager.getFlowExecutedDataDTO(flowId);

        if (executedData.getResult() != null) {
            engineManager.setStats(flowId);
            engineManager.setFinishTimeStamp(flowId);
        }

        String executedDataJson = gson.toJson(executedData);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(executedDataJson);
    }
}
