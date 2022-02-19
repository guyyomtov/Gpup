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
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static constants.Constants.USERNAME;

@WebServlet(name = "graphViewServlet", urlPatterns = {"/graphViewResponse"})

public class GraphViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
            try {
                BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                List<TargetInfo> targetInfoList = bdm.getTargetInfoList();
                if (targetInfoList.isEmpty()) // if is the first time that the users want to interrogator the graph we make the target info list.
                    targetInfoList = bdm.convertTargetsToDtoClass();
                if (!targetInfoList.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    try (PrintWriter out = response.getWriter()) {
                        Gson gson = new Gson();
                        AllGraphInfo allGraphInfo = new AllGraphInfo(graphManager.createGraphInfo(bdm), targetInfoList);
                        String json = gson.toJson(allGraphInfo);
                        out.println(json);
                        out.flush();
                    }
                } else
                    response.setStatus(HttpServletResponse.SC_CONFLICT);

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
