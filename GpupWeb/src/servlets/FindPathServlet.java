package servlets;

import DataManager.BackDataManager;
import Graph.GraphManager;
import com.google.gson.Gson;
import constants.Constants;
import errors.ErrorUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.AllGraphInfo;
import transferGraphData.TargetInfo;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "findPathServlet", urlPatterns = {"/findPath"})
public class FindPathServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        String src = request.getParameter(Constants.SRC);
        String dst = request.getParameter(Constants.DST);
        String relationship = request.getParameter(Constants.RELATIONSHIP);
        if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
            try {
                BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                String allPath = bdm.getPathFromTargets(src, dst, relationship);
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson = new Gson();
                        String json = gson.toJson(allPath);
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.println(json);
                        out.flush();
                    }
            } catch (ErrorUtils e) {
                e.getMessage();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }

        } else { // graph name doesnt exist or null
            System.out.println(HttpServletResponse.SC_CONFLICT);
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        }


    }


}
