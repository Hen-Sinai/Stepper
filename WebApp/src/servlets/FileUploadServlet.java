package servlets;

import Exceptions.*;
import engineManager.EngineManager;
import engineManager.EngineManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "uploadFileServlet", urlPatterns = "/upload_file")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/plain");
        Part part = req.getPart("xmlFile");

        String username = SessionUtils.getUsername(req);
        if (username == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        boolean isFirstUpload = Boolean.parseBoolean(req.getParameter("isFirstUpload"));
        EngineManager engineManager = EngineManagerImpl.getInstance();

        if (!part.getSubmittedFileName().endsWith(".xml")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("File type must be xml");
        }

        try {
            if (isFirstUpload)
                engineManager.loadXmlFile(part.getInputStream());
            else
                engineManager.updateXml(part.getInputStream());
        } catch (JAXBException | StepNotExist | OutputNameNotUnique | FlowNameExist | NoXmlFormat | IOException |
                 StepNameNotUnique | UserInputNotFriendly | DataNotExistCustomMapping | CustomDataNotmatch |
                 ReferenceToForwardStep | DataNotExistFlowLevelAliasing | FlowOutputNotExist |
                 UserInputTypeCollision | InitialInputValueNotExist | FlowNotExist | DataNotExistContinuation |
                 InitialInputValueTypeNotMatch e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(e.getMessage());
        }
    }
}
