package servlets;

import Graph.GraphManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.GraphInfo;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GraphsListServlet", urlPatterns = {"/graphsListServlet"})
public class GraphsInfoListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
            List<GraphInfo> graphsInfo = graphManager.makeListOfGraphInfo();
            String json = gson.toJson(graphsInfo);
            out.println(json);
            out.flush();
        }
    }




}
