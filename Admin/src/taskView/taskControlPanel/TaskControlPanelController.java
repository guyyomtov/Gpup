package taskView.taskControlPanel;

import DashBoardAdmin.TaskInfoTableComponent.TaskListRefresher;
import api.HttpStatusUpdate;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import taskView.tableForProcess.TableForProcessController;
import transferGraphData.AllGraphInfo;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.GSON_INSTANCE;

public class TaskControlPanelController {

    @FXML private Label taskNameLabel;
    @FXML private Label graphNameLabel;
    @FXML private Label totalWorkerLabel;
    @FXML private Label totalTargetsLabel;
    @FXML private Label totalIndependentsLabel;
    @FXML private Label totalLeafLabel;
    @FXML private Label totalRootsLabel;
    @FXML private Label middleLabel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Label taskIsFinishedLabel;
    @FXML private Label precentOfProgressBar;
    @FXML private ProgressBar progressBar;
    @FXML private TextArea textAreaProcessInfo;
    @FXML private TextArea textAreaProcessInfo1;
    @FXML private TextArea textAreaTargetInfo;
    @FXML private Parent tableForProcess;
    @FXML private TableForProcessController tableForProcessController;
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate = new SimpleBooleanProperty(false);
    private HttpStatusUpdate httpStatusUpdate;
    private final String START_PROCESS = "start";
    private final String PAUSE_PROCESS = "pause";
    private final String STOP_PROCESS = "stop";
    private final String RESUME_PROCESS = "resume";
    private TaskData taskData;


    public void init(TaskData taskData) {

        this.taskData = taskData;
        // init all label that related to task
        this.initInformation();
        // init table
        this.initTableForProcess();
    }

    private void initTableForProcess() {

        this.tableForProcessController.initTable(taskData.getExecuteTargetList(), textAreaTargetInfo);
    }

    private void initInformation() {
        this.taskNameLabel.setText(taskData.getTaskName());
        this.graphNameLabel.setText(taskData.getGraphName());
        this.totalWorkerLabel.setText(String.valueOf(taskData.getTotalWorker()));
        this.totalTargetsLabel.setText(String.valueOf(taskData.getTotalTargets()));
        this.totalIndependentsLabel.setText(String.valueOf(taskData.getTotalIndependent()));
        this.totalLeafLabel.setText(String.valueOf(taskData.getTotalLeaf()));
        this.middleLabel.setText(String.valueOf(taskData.getTotalMiddles()));
        this.totalRootsLabel.setText(String.valueOf(taskData.getTotalRoots()));
    }

    @FXML
    void pauseButtonAction(ActionEvent event) throws ErrorUtils {

        // set process logic
        this.changeStatusOfProcessTo(PAUSE_PROCESS);

        // change buttons accordingly
        this.setControlButtons(true, true, false, false);
    }

    @FXML
    void resumeButtonAction(ActionEvent event) throws ErrorUtils {

        // set process logic
        this.changeStatusOfProcessTo(RESUME_PROCESS);

        // change buttons accordingly
        this.setControlButtons(true, false, false, true);
    }

    @FXML
    void stopButtonAction(ActionEvent event) throws ErrorUtils {

        // set process logic
        this.changeStatusOfProcessTo(STOP_PROCESS);

        // change buttons accordingly
        this.setControlButtons(true, true, true, true);

        // close current task window
        Stage stage = (Stage) resumeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void startButtonAction(ActionEvent event) throws ErrorUtils {

        // to send request to the server that the mission going to run with name of the task
        this.changeStatusOfProcessTo(START_PROCESS);

        //to start the list refresher
        // change buttons accordingly
        this.setControlButtons(true, false, false, true);
    }

    public void setControlButtons(boolean disableStart, boolean disablePause, boolean disableStop, boolean disableResume) {

        this.startButton.setDisable(disableStart);
        this.pauseButton.setDisable(disablePause);
        this.stopButton.setDisable(disableStop);
        this.resumeButton.setDisable(disableResume);
    }

    private void changeStatusOfProcessTo(String wantedStatus) throws ErrorUtils {

        if(wantedStatus != START_PROCESS && wantedStatus != PAUSE_PROCESS && wantedStatus != STOP_PROCESS && wantedStatus != RESUME_PROCESS)
            throw new ErrorUtils(ErrorUtils.invalidInput("Status given invalid."));

        String finalUrl = this.makeUrlForControlProcessServlet(wantedStatus);

        this.makeRequestToStartTheCurrentTask(finalUrl);
    }

    private String makeUrlForControlProcessServlet(String wantedStatus) throws ErrorUtils {

        if(wantedStatus != START_PROCESS && wantedStatus != PAUSE_PROCESS && wantedStatus != STOP_PROCESS && wantedStatus != RESUME_PROCESS)
            throw new ErrorUtils(ErrorUtils.invalidInput("Status given invalid."));

        String finalUrl = HttpUrl
                .parse(Constants.START_TASK)
                .newBuilder()
                .addQueryParameter("taskname", taskData.getTaskName())
                .addQueryParameter("graphname", taskData.getGraphName())
                .addQueryParameter("status", wantedStatus)
                .build()
                .toString();

        return finalUrl;
    }

    private void makeRequestToStartTheCurrentTask(String finalUrl) {

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Platform.runLater(() ->
                        AlertMessage.showUserErrorAlert("We failed, server problem")
                );
                System.out.println("We failed, server problem");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {

                    Platform.runLater(() ->
                            AlertMessage.showUserErrorAlert("Task failed something went wrong.")
                    );
                    System.out.println( response.code());
                } else {

                    autoUpdate.set(true);
                    startTaskListRefresher();
                }
            }
        });
    }

    public void startTaskListRefresher() {
        listRefresher = new TaskRefresherForProcess(
                autoUpdate,
                this::updateTasksList);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    private void updateTasksList(List<TaskData> taskDataList) {
        Platform.runLater(() -> {
            for(TaskData currTaskData : taskDataList) {
                if(currTaskData.getTaskName().equals(taskData.getTaskName())) {
                    tableForProcessController.initTable(currTaskData.getExecuteTargetList(), textAreaTargetInfo);
                    updateProgressBar(currTaskData.getExecuteTargetList());
                    return;
                }
            }
        });
    }

    private void updateProgressBar(List<ExecuteTarget> executeTargetList) {
        double countTargetsThatFinished = 0;
        double totalTargets = executeTargetList.size();
        for(ExecuteTarget executeTarget : executeTargetList){
            String status = executeTarget.getStatus();
            if(status.equals("SUCCESS") || status.equals("WARNING") || status.equals("FAILURE") || status.equals("SKIPPED"))
                countTargetsThatFinished = countTargetsThatFinished + 1;
        }
        //the result
        double res = countTargetsThatFinished/totalTargets;
        this.progressBar.setProgress(res);
        this.precentOfProgressBar.setText(String.valueOf(res*100) + " %");

    }

}
