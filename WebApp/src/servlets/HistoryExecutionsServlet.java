package servlets;

import DTO.FlowExecutedDataDTO;
import DTO.FlowExecutedInfoDTO;
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

@WebServlet(name = "HistoryExecutionsServlet", urlPatterns = "/history_executions")
public class HistoryExecutionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String filter = req.getParameter("filter");
        int fromIndex = Integer.parseInt(req.getParameter("fromIndex"));
        if (filter == null)
            filter = "All";
        List<FlowExecutedInfoDTO> executionsInfo = engineManager.getFlowsExecutedInfoDTO(filter, username, fromIndex);

        String executedDataJson = gson.toJson(executionsInfo);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(executedDataJson);
    }
}