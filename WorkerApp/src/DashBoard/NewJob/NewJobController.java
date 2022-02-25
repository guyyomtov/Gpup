package DashBoard.NewJob;

import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static util.Constants.GSON_INSTANCE;

import java.util.logging.ErrorManager;

public class NewJobController {


    @FXML private TitledPane newJobTitlePane;
    @FXML private Spinner<Integer> amountOfTargetsSpinner;
    @FXML private Spinner<Integer> amountOfThreadsSpinner;
    @FXML private Button cancelButton;
    @FXML private Button applyButton;
    private TaskData taskData;
    private JobsManager jobsManager;

    @FXML
    public void initialize() {

    }

    @FXML
    void applyButtonAction(ActionEvent event) {

        jobsManager.addNewTask(taskData);

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    public void setJobsManager(JobsManager jobsManager) {
        this.jobsManager = jobsManager;
    }
}
