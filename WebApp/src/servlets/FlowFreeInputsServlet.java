package servlets;

import DTO.FlowDTO;
import DTO.FreeInputDTO;
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

@WebServlet(name = "FlowFreeInputsServlet", urlPatterns = "/flow_free_inputs")
public class FlowFreeInputsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        EngineManager engineManager = EngineManagerImpl.getInstance();
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<FreeInputDTO> inputs;
        String type = req.getParameter("type");
        String flowId;
        String flowName = req.getParameter("flowName");

        if (type.equals("continuation")) {
            flowId = req.getParameter("flowId");
            inputs = engineManager.continueToNextFlow(UUID.fromString(flowId), flowName);

        }
        else if (type.equals("rerun")) {
            flowId = req.getParameter("flowId");
            inputs = engineManager.reRunFlow(UUID.fromString(flowId), flowName);
        }
//        else if (parentController.getContinuation().getReRunButtonPressed().getValue()) {
//            inputs = engineManager.reRunFlow(UUID.fromString(parentController.getExecutedFlowID().getValue()),
//                    parentController.getCurrentFlowNameProperty().getValue());
//        }
        else
            inputs = engineManager.getFreeInputs(flowName);

        String inputsJson = gson.toJson(inputs);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(inputsJson);
    }
}