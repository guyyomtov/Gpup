package workerManager.currentTaskWorkerTable;

import DashBoard.NewJob.JobsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.TaskData;

import java.util.ArrayList;
import java.util.List;

public class TaskWorkerTableController {


    @FXML private TableView<MyTasksTableData> taskInfoTable;
    @FXML private TableColumn<MyTasksTableData, String> taskNameCol;
    @FXML private TableColumn<MyTasksTableData, Integer> workerAmountCol;
    @FXML private TableColumn<MyTasksTableData, String> progressCol;
    @FXML private TableColumn<MyTasksTableData, Integer> doneByMeCol;
    @FXML private TableColumn<MyTasksTableData, Integer> creditGotCol;
    private JobsManager jobsManager;


    public void initTable(){

         taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
         workerAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountOfWorkers"));
         progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));
         doneByMeCol.setCellValueFactory(new PropertyValueFactory<>("amountOfTargetsDoneByMe"));
         creditGotCol.setCellValueFactory(new PropertyValueFactory<>("amountOfEarnedCredits"));
    }

    public void updateMyTaskTable(JobsManager i_jobsManager) {

        this.jobsManager = i_jobsManager;

        this.updateTable();
    }

    private void updateTable() {

        //clear last data
        if(this.taskInfoTable != null && !this.taskInfoTable.getItems().isEmpty())
            this.taskInfoTable.getItems().clear();

        //get all my tasks
        List<TaskData> allMyTasks = this.jobsManager.getTaskThatWorkerJoined();

        // create new row for each of my tasks
        List<MyTasksTableData> myTasksTableDataList = this.createList(allMyTasks);
        ObservableList<MyTasksTableData> items = FXCollections.observableArrayList(myTasksTableDataList);
        this.taskInfoTable.setItems(items);

    }

    private List<MyTasksTableData> createList(List<TaskData> allMyTasks) {
        List<MyTasksTableData> myTasksTableDataList = new ArrayList<>();
        for(TaskData curTask : allMyTasks) {
            MyTasksTableData myTasksTableData = this.createRowFrom(curTask);
            myTasksTableDataList.add(myTasksTableData);
        }
        return myTasksTableDataList;
    }

    private MyTasksTableData createRowFrom(TaskData curTask) {

        return null;
    }
}
