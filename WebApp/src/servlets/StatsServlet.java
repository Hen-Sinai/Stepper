package servlets;

import DTO.FlowExecutedInfoDTO;
import DTO.StatsDTO;
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
import java.util.Map;

@WebServlet(name = "StatsServlet", urlPatterns = "/stats")
public class StatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null || !username.equals(engineManager.getAdminName())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Map<String, StatsDTO> stats = engineManager.getsStats();

        String statsJson = gson.toJson(stats);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(statsJson);
    }
}