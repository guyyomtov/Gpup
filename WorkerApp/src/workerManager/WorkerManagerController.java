package workerManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import workerManager.currentTaskWorkerTable.TaskWorkerTableController;

public class WorkerManagerController {

    @FXML private Button pauseButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private Label amountOfThreadsLabel;
    @FXML private TextArea textAreaForLooger;

    @FXML private Parent taskWorkerTable;
    @FXML private TaskWorkerTableController taskWorkerTableController;


    @FXML private void pauseButtonAction(ActionEvent actionEvent) {
    }

    @FXML private void playButtonAction(ActionEvent actionEvent) {
    }

    @FXML private void stopButtonAction(ActionEvent actionEvent) {
    }
}
