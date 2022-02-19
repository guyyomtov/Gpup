package servlets;

import DataManager.BackDataManager;
import Graph.Graph;
import Graph.GraphManager;
import com.google.gson.Gson;
import errors.ErrorUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.AllGraphInfo;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.io.IOException;

import static constants.Constants.GRAPH_PATH_NAME;
@WebServlet(name = "uploadGraphServlet", urlPatterns = {"/uploadTaskShortResponse"}) // todo to update the url in UI
public class UploadTaskServlet extends HttpServlet {




    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/plain;charset=UTF-8");
        //get the instance of taskData
        Gson gson = new Gson();
        TaskData taskData = gson.fromJson( "TaskData" ,TaskData.class);

        //to check if is a new name

        //to get the relevant back data manager from graph manager and to generate the to a real task.

        //to create task manager map name to task





    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String userName = request.getParameter("username");
        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());

        synchronized (this) {

            BackDataManager bDM = new BackDataManager();

            bDM.setUserNameThatUploadTheCurGraph(userName);
            // Check that the file is valid & add graph to DB
            try {
                //This function CHECKS & STARTS a graph
             //   bDM.checkFile(absolutePath);

                // get cur graph
                Graph curGraph = bDM.getGraph();

                // check if graph was uploaded already
                if(graphManager.graphExists(curGraph)) {

                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
                else{

                    // add cur graph to graph manager
                    graphManager.addGraph(bDM);

                    response.setStatus(HttpServletResponse.SC_OK);
                }

            } catch (ErrorUtils e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }

}
