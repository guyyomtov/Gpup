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

    private final String START_PROCESS = "start";
    private final String PAUSE_PROCESS = "pause";
    private final String STOP_PROCESS = "stop";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        // get data from backEnd
        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());

        // get data from request
        String graphNameFromParameter = request.getParameter(Constants.GRAPHNAME);
        String taskNameFromParameter = request.getParameter(Constants.TASKNAME);
        String status = request.getParameter(Constants.TASK_STATUS);

        //check validity parameters
        if(taskNameFromParameter != null && taskManager.taskDataExist(taskNameFromParameter)) {
            if (graphNameFromParameter != null && graphManager.graphExists(graphNameFromParameter)) {
                try {

                    BackDataManager bdm = graphManager.getBDM(graphNameFromParameter);
                    TaskData taskData = taskManager.getNameToTaskData().get(taskNameFromParameter);

                    switch (status) {

                        case START_PROCESS:
                            this.startTask(bdm, taskData, taskManager);
                            break;
                        case STOP_PROCESS:
                            this.stopProcess(taskData);
                            break;
                        case PAUSE_PROCESS:
                            this.pauseProcess(taskData);
                            break;
                    }

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

    private void pauseProcess(TaskData taskData) {

        // make taskData status stop
        taskData.setStatus(TaskData.Status.PAUSED);
    }

    private void stopProcess(TaskData taskData) {

        // make taskData status stop
        taskData.setStatus(TaskData.Status.STOPPED);
    }

    private void startTask(BackDataManager bdm, TaskData taskData, TaskManager taskManager) throws ErrorUtils {

        //making original Task like it was
        Task newTask = bdm.makeNewTask(taskData);

        List<ExecuteTarget> executeTargetList = bdm.transferFromMinionToExecuteTarget(newTask, taskData);
        taskData.setExecuteTargetList(executeTargetList);

        //after the admin press start the task is available to workers
        taskData.setStatus(TaskData.Status.AVAILABLE);

        //adding the original task to task manager
        taskManager.addTask(newTask);
    }
}
