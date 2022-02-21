package taskView.taskControlPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import taskView.tableForProcess.TableForProcessController;
import transferGraphData.TaskData;

import java.io.IOException;

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

    private TaskData taskData;

    @FXML
    void pauseButtonAction(ActionEvent event) {

    }

    @FXML
    void resumeButtonAction(ActionEvent event) {

    }

    @FXML
    void startButtonAction(ActionEvent event) {

    }

    @FXML
    void stopButtonAction(ActionEvent event) {

    }

    public void init(TaskData taskData) {

        this.taskData = taskData;
        // init all label that related to task
        this.initInformation();
        // init table
        this.initTableForProcess();
    }

    private void initTableForProcess() {

        this.tableForProcessController.initTable(taskData.getTargetInfoList(), textAreaTargetInfo);

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
}
