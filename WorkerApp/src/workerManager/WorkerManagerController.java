package workerManager;

import DashBoard.NewJob.JobsManager;
import api.HttpStatusUpdate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import transferGraphData.TaskData;
import workerManager.currentTaskWorkerTable.TaskWorkerTableController;

import java.util.function.Consumer;

public class WorkerManagerController implements Consumer, HttpStatusUpdate{

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private Label amountOfThreadsLabel;
    @FXML private TextArea textAreaForLooger;
    @FXML private Parent taskWorkerTable;
    @FXML private TaskWorkerTableController taskWorkerTableController;
    private Integer amountOfThreads;
    private TableView<TaskData> allTasksOnApp;
    private JobsManager jobsManager;


    public void init(Integer amountOfThreads, JobsManager jobsManager) {

        this.jobsManager = jobsManager;

        this.amountOfThreads = amountOfThreads;
        amountOfThreadsLabel.setText(String.valueOf(amountOfThreads));

        // start value factory
        this.taskWorkerTableController.initTable(this.jobsManager);
        this.taskWorkerTableController.startTaskListRefresher();
    }

    @FXML private void pauseButtonAction(ActionEvent actionEvent) {
    }

    @FXML private void playButtonAction(ActionEvent actionEvent) {
    }

    @FXML private void stopButtonAction(ActionEvent actionEvent) {
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
}
