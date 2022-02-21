package servlets;

import DataManager.BackDataManager;
import Graph.Graph;
import Graph.GraphManager;
import Graph.process.Task;
import Graph.process.TaskManager;
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
@WebServlet(name = "uploadTaskServlet", urlPatterns = {"/uploadTaskShortResponse"}) // todo to update the url in UI
public class UploadTaskServlet extends HttpServlet {

//todo to learn how to make doPost request
    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/plain;charset=UTF-8");
        //get the instance of taskData
        String taskDataObjectString = request.getParameter("taskDataObject");
        Gson gson = new Gson();
        TaskData taskData = gson.fromJson( taskDataObjectString ,TaskData.class);
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());

        synchronized (this){

            if(taskManager.taskExist(taskData.getTaskName()) || !graphManager.graphExists(taskData.getGraphName())){

                response.setStatus(HttpServletResponse.SC_CONFLICT);

            }
            else{
                    // BackDataManager backDataManager = graphManager.getBDM(taskData.getGraphName());
                    //Task newTask = backDataManager.makeNewTask(taskData);

                    taskManager.addTask(taskData);
                    response.setStatus(HttpServletResponse.SC_OK);

            }

        }

    }


}
