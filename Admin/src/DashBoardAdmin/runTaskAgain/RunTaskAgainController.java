package DashBoardAdmin.runTaskAgain;

import DashBoardAdmin.MainDashboardController2;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import taskView.NewTask.NewTaskController;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;

import java.util.List;

public class RunTaskAgainController {

    @FXML
    private Button incrementalButton;

    @FXML
    private Button fromScratchButton;

    private TaskData taskData;
    private NewTaskController newTaskController;

    @FXML
    void fromScratchButtonAction(ActionEvent event) {
        this.updateTheRelevantInformation();
        this.taskData.setFromScratch(true);
        this.newTaskController.sendTaskDataToServer(this.taskData);
    }

    private void updateTheRelevantInformation() {
        Integer countTask = this.taskData.getCountTask();
        String taskName = this.taskData.getTaskName();
        this.taskData.setUploadedBy(MainDashboardController2.userName);
        this.taskData.setCountTask(countTask + 1);
        this.taskData.setTaskName(taskName + " " + String.valueOf(this.taskData.getCountTask()));
        this.taskData.setStatus(TaskData.Status.CREATED);
    }

    @FXML
    void incrementalButtonAction(ActionEvent event) {
        this.updateTheRelevantInformation();
        this.taskData.setFromScratch(false);
        this.taskData.setLastExecuteTargetsList(this.taskData.getExecuteTargetList());
        this.newTaskController.sendTaskDataToServer(this.taskData);
    }

    public void init(TaskData taskData, NewTaskController newTaskController){
        this.taskData = taskData;
        this.incrementalButton.setDisable(true);
        this.newTaskController = newTaskController;
        this.checkIfUserCanChoseIncremental();
    }

    private void checkIfUserCanChoseIncremental() {
        // if one of the targets failure or skipped he can chose incremental
        List<ExecuteTarget> executeTargetList = taskData.getExecuteTargetList();
        for(ExecuteTarget executeTarget : executeTargetList){
            if(executeTarget.getStatus().equals("FAILURE") || executeTarget.getStatus().equals("SKIPPED")) {
                this.incrementalButton.setDisable(false);
                return;
            }
        }
    }

    public void setTaskData(TaskData taskData) { this.taskData = taskData;}

}

