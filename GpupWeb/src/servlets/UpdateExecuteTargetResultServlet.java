package servlets;

import DataManager.BackDataManager;
import Graph.GraphManager;
import Graph.process.Task;
import Graph.process.TaskManager;
import com.google.gson.Gson;
import constants.Constants;
import errors.ErrorUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UpdateExecuteTargetResult", urlPatterns = {"/updateExecuteTargetResult"})
public class UpdateExecuteTargetResultServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        //get the executeTarget
        Gson json = new Gson();
        String executeTargetJson = request.getParameter("executeTarget");
        ExecuteTarget executeTarget = json.fromJson(executeTargetJson, ExecuteTarget.class);
        //get parameters on relevant Task
        String taskNameFromParameter = executeTarget.getTaskName();
        String graphNameFromParameter = executeTarget.getGraphName();
        //check validity parameters

        if(taskNameFromParameter != null && taskManager.taskDataExist(taskNameFromParameter)) {
            if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
                try {
                    BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                    TaskData taskData = taskManager.getNameToTaskData().get(taskNameFromParameter);
                    Task currentTask = taskManager.getNameToTask().get(taskNameFromParameter);
                    bdm.updateExecuteTargetStatus(currentTask, taskData, executeTarget);

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
