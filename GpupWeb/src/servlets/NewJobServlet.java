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

@WebServlet(name = "newJobServlet", urlPatterns = {"/newJobServlet"})
public class NewJobServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        //get the query
        String taskNameFromParameter = request.getParameter(Constants.TASKNAME);
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        String amountOFThreadsFromRequest = request.getParameter(Constants.AMOUNT_OF_THREADS);
        Integer amountOfThreads = Integer.valueOf(amountOFThreadsFromRequest);
        //check validity parameters
        if(taskNameFromParameter != null && taskManager.taskDataExist(taskNameFromParameter)) {
            if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
                try {
                    BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                    TaskData taskData = taskManager.getNameToTaskData().get(taskNameFromParameter);
                    Task currentTask = taskManager.getNameToTask().get(taskNameFromParameter);
                    List<ExecuteTarget> executeTargetListToSentToTheWorker = bdm.makeExecuteTargetsToSend(currentTask, taskData, amountOfThreads);
                    
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
