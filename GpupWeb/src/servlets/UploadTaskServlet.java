package servlets;

import DataManager.BackDataManager;
import Graph.GraphManager;
import Graph.process.TaskManager;
import com.google.gson.Gson;
import errors.ErrorUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.util.List;

@WebServlet(name = "uploadTaskServlet", urlPatterns = {"/uploadTaskShortResponse"}) // todo to update the url in UI
public class UploadTaskServlet extends HttpServlet {

//todo to learn how to make doPost request
    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response){

        Gson gson = new Gson();
        response.setContentType("text/plain;charset=UTF-8");

        //get the instance of taskData
        String taskDataObjectString = request.getParameter("taskDataObject");
        TaskData taskData = gson.fromJson( taskDataObjectString ,TaskData.class);
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());

        synchronized (this){

            if(taskManager.taskDataExist(taskData.getTaskName()) || !graphManager.graphExists(taskData.getGraphName()))
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            else{
                try {
                    BackDataManager backDataManager = graphManager.getBDM(taskData.getGraphName());
                    List<ExecuteTarget> executeTargetList = backDataManager.makeExecuteTargetsBeforeAdminPressStart(taskData);
                    taskData.setExecuteTargetList(executeTargetList);
                    taskManager.addTaskData(taskData);
                    response.setStatus(HttpServletResponse.SC_OK);

                } catch (ErrorUtils errorUtils) {
                    errorUtils.printStackTrace();
                }

            }

        }

    }


}
