package workerManager;

import DashBoard.NewJob.JobsManager;
import api.HttpStatusUpdate;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import transferGraphData.TaskData;
import workerManager.currentTaskWorkerTable.MyTasksTableData;
import workerManager.currentTaskWorkerTable.TaskWorkerTableController;

import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

import static Utils.Constants.*;

public class WorkerManagerController implements Consumer, HttpStatusUpdate{

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private TextArea textAreaForLooger;
    @FXML private Parent taskWorkerTable;
    @FXML private TaskWorkerTableController taskWorkerTableController;
    @FXML private TextField taskNameControlPanelField;
    private Integer amountOfThreads;
    private TableView<TaskData> allTasksOnApp;
    private JobsManager jobsManager;
    private MyTasksTableData chosenRowTasksTable;


    public void init(Integer amountOfThreads, JobsManager jobsManager) {

        this.jobsManager = jobsManager;

        this.amountOfThreads = amountOfThreads;


        // start value factory
        this.taskWorkerTableController.initTable(this.jobsManager, this);
        this.taskWorkerTableController.startTaskListRefresher();

        this.controlPanelButtonLogic(true, true, true);
    }

    @FXML private void pauseButtonAction(ActionEvent actionEvent) throws ErrorUtils {

        // get current wanted task
        // get worker status
        String curTaskName = this.chosenRowTasksTable.getTaskName();
        String workerTaskStatus = this.getWorkerTaskStatusFrom(curTaskName);

        // check that the status fits logic
        if(!workerTaskStatus.equals(AVAILABLE_TASK))
            throw new ErrorUtils(ErrorUtils.invalidInput("Status of task worker doesn't fit pause button logic."));

        // change buttons accordingly
        this.controlPanelButtonLogic(true, false, false);

        // change status
        JobsManager.MiniJobDataController wantedMini = this.getRelevantMiniJobDataHelper(curTaskName);
        wantedMini.myWorkerTaskStatus = PAUSED_TASK;
    }

    @FXML private void playButtonAction(ActionEvent actionEvent) throws ErrorUtils {

        // get current wanted task
        // get worker status
        String curTaskName = this.chosenRowTasksTable.getTaskName();
        String workerTaskStatus = this.getWorkerTaskStatusFrom(curTaskName);

        // check that the status fits logic
        if(!workerTaskStatus.equals(PAUSED_TASK))
            throw new ErrorUtils(ErrorUtils.invalidInput("Status of task worker doesn't fit pause button logic."));

        // change buttons accordingly
        this.controlPanelButtonLogic(false, true, false);

        // change status
        JobsManager.MiniJobDataController wantedMini = this.getRelevantMiniJobDataHelper(curTaskName);
        wantedMini.myWorkerTaskStatus = AVAILABLE_TASK;
    }

    @FXML private void stopButtonAction(ActionEvent actionEvent) throws ErrorUtils {

        // get current wanted task
        // get worker status
        String curTaskName = this.chosenRowTasksTable.getTaskName();
        String workerTaskStatus = this.getWorkerTaskStatusFrom(curTaskName);

        // change buttons accordingly
        this.controlPanelButtonLogic(true, true, true);

        // change status
        JobsManager.MiniJobDataController wantedMini = this.getRelevantMiniJobDataHelper(curTaskName);
        wantedMini.myWorkerTaskStatus = STOPPED_TASK;

        // remove from workers tasks
        this.jobsManager.removeTask(wantedMini.generalTableTaskData);
    }

    @Override
    public void accept(Object newLog) {
        Platform.runLater(()->{
            String logsBefore = this.textAreaForLooger.getText();
            this.textAreaForLooger.setText(  logsBefore + newLog);
        });
    }

    @Override
    public void updateHttpLine(String line) {

    }

    public void updateJobManager(JobsManager jobsManager) {

        this.taskWorkerTableController.updateMyTaskTable(jobsManager);
    }

    private void controlPanelButtonLogic(boolean disablePause, boolean disablePlay, boolean disableStop) {

        this.pauseButton.setDisable(disablePause);
        this.playButton.setDisable(disablePlay);
        this.stopButton.setDisable(disableStop);
    }

    public void ControlPanelFor(MyTasksTableData i_wantedRowTaskData) throws ErrorUtils {

        String taskName = i_wantedRowTaskData.getTaskName();

        // set label name
        taskNameControlPanelField.setText(taskName);

        // set member current chosen row
        this.chosenRowTasksTable = i_wantedRowTaskData;

        // get worker status of current process
        String curTaskStatus = this.getWorkerTaskStatusFrom(taskName);

        // based on the status of the process show buttons
        this.showButtonsDependingOn(curTaskStatus);
    }

    private void showButtonsDependingOn(String curTaskStatus) throws ErrorUtils {

        // curTaskStatus == status made by worker
        switch (curTaskStatus){

            case AVAILABLE_TASK:
                this.controlPanelButtonLogic(false,true,false);
                break;
            case PAUSED_TASK:
                this.controlPanelButtonLogic(true,false,false);
                break;
            case STOPPED_TASK:
                this.controlPanelButtonLogic(true,true,true);
                break;
            default:
                throw new ErrorUtils(ErrorUtils.invalidInput("Status got miniJobManger is invalid."));
        }
    }

    private String getWorkerTaskStatusFrom(String taskName) throws ErrorUtils {

        // get the wanted task
        JobsManager.MiniJobDataController relevantJob = this.getRelevantMiniJobDataHelper(taskName);

        //send its status back
        return relevantJob.myWorkerTaskStatus;
    }

    private JobsManager.MiniJobDataController getRelevantMiniJobDataHelper(String taskName) throws ErrorUtils {

        // get all my tasks
        Map<String, JobsManager.MiniJobDataController>  nameToData = this.jobsManager.getTaskNameToMiniDataHelper();

        if(!nameToData.containsKey(taskName))
            throw new ErrorUtils(ErrorUtils.invalidInput("got a task that isn't in the miniJob manager."));

        // get the wanted task
        JobsManager.MiniJobDataController relevantJob = nameToData.get(taskName);

        return relevantJob;
    }
}
