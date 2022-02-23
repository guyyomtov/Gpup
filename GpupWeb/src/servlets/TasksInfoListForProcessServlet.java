package servlets;

import Graph.process.TaskManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import transferGraphData.TaskData;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TasksListForProcessServlet", urlPatterns = {"/tasksListForProcess"})
public class TasksInfoListForProcessServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            TaskManager taskManager = ServletUtils.getTaskManager(getServletContext());
            List<TaskData> taskDataList = new ArrayList<>(taskManager.getNameToTaskData().values());
            String json = gson.toJson(taskDataList);
            out.println(json);
            out.flush();
        }
    }

}
