package servlets;

import DTO.ExecuteDataDTO;
import Exceptions.*;
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
import java.util.stream.Collectors;

@WebServlet(name = "ExecuteFlowServlet", urlPatterns = "/execute_flow")
public class ExecuteFlowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        String flowId = null;

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String inputs = req.getReader().lines().collect(Collectors.joining(" "));
        ExecuteDataDTO executeDataDTO = new Gson().fromJson(inputs, ExecuteDataDTO.class);
        try {
            flowId = engineManager.executeFlow(executeDataDTO, username).getId().toString();
        } catch (MyInputMismatchException e) {
        }

        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(flowId);
    }
}