package servlets;

import DataManager.BackDataManager;
import Graph.GraphManager;
import Graph.process.TaskManager;
import com.google.gson.Gson;
import errors.ErrorUtils;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "uploadTaskServlet", urlPatterns = {"/uploadTaskShortResponse"}) // todo to update the url in UI
public class UploadTaskServlet extends HttpServlet {

//todo to learn how to make doPost request
    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Gson gson = new Gson();
        response.setContentType("text/plain;charset=UTF-8");

        // get the body --> TaskData
        BufferedReader in = new BufferedReader(
                new InputStreamReader(request.getInputStream()));
        TaskData taskData = gson.fromJson(in, TaskData.class);

        TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
        GraphManager graphManager = ServletUtils.getGraphManager(getServletContext());
        if(!taskData.getFromScratch()) // incremental
            taskData.setLastExecuteTargetsList(taskData.getExecuteTargetList());
        if(taskData.getCountTask() > 0)
            this.updateCountAndNameToTheNewTask(taskData, taskManager);
        synchronized (this){

            if(taskManager.taskDataExist(taskData.getTaskName()) || !graphManager.graphExists(taskData.getGraphName())) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
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

    private void updateCountAndNameToTheNewTask(TaskData taskData, TaskManager taskManager) {
        String taskName = taskData.getTaskName();
        TaskData originalTask = taskManager.getNameToTaskData().get(taskName);
        Integer countNames = taskData.getCountTask();
        originalTask.setCountTask(countNames); // in the admin we had ++1
        taskData.setTaskName(taskName + " " + countNames);
        taskData.setCountTask(0);

    }


}
