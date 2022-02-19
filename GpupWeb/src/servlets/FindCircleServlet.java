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

@WebServlet(name = "findCircleServlet", urlPatterns = {"/findCircle"})
public class FindCircleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        String src = request.getParameter(Constants.SRC);
        if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
            try {
                BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                String res = bdm.findCircle(src);
                try (PrintWriter out = response.getWriter()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(res);
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
