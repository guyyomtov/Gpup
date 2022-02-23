package servlets;

import DataManager.BackDataManager;
import Graph.GraphManager;
import Graph.process.Task;
import Graph.process.TaskManager;
import constants.Constants;
import errors.ErrorUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "startTaskServlet", urlPatterns = {"/startTaskServlet"})
public class StartTaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        String taskNameFromParameter = request.getParameter(Constants.TASKNAME);
        //check validity parameters
        if(taskNameFromParameter != null && taskManager.taskDataExist(taskNameFromParameter)) {
            if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
                try {
                    BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                    TaskData taskData = taskManager.getNameToTaskData().get(taskNameFromParameter);
                    //making original Task like it was
                    Task newTask = bdm.makeNewTask(taskData);
                    List<ExecuteTarget> executeTargetList = bdm.transferFromMinionToExecuteTarget(newTask, taskData);
                    taskData.setExecuteTargetList(executeTargetList);
                    //after the admin press start the task is available to workers
                    taskData.setStatus(TaskData.Status.AVAILABLE);
                    //adding the original task to task manager
                    taskManager.addTask(newTask);
                    //code response
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (ErrorUtils e) {
                    e.getMessage();
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }

            }
        }else { // graph name doesnt exist or null
            System.out.println(HttpServletResponse.SC_CONFLICT);
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        }


    }


}
