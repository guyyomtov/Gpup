package workerManager.currentTaskWorkerTable;

import DashBoard.NewJob.JobsManager;
import api.HttpStatusUpdate;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.TaskData;
import workerManager.WorkerManagerController;

import java.util.*;

public class TaskWorkerTableController {


    @FXML private TableView<MyTasksTableData> taskInfoTable;
    @FXML private TableColumn<MyTasksTableData, String> taskNameCol;
    @FXML private TableColumn<MyTasksTableData, Integer> workerAmountCol;
    @FXML private TableColumn<MyTasksTableData, String> progressCol;
    @FXML private TableColumn<MyTasksTableData, Integer> doneByMeCol;
    @FXML private TableColumn<MyTasksTableData, Integer> creditGotCol;
    private JobsManager jobsManager;
    private Timer timer;
    private TimerTask listRefresher;
    private BooleanProperty autoUpdate;
    private HttpStatusUpdate httpStatusUpdate;
    private WorkerManagerController dadComponent;



    public void initTable(JobsManager i_jobsManager, WorkerManagerController i_dadComponent){

        // start for control panel
        this.dadComponent = i_dadComponent;

        // start needed data
        this.jobsManager = i_jobsManager;

        // start columns factory
         taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
         workerAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountOfWorkers"));
         progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));
         doneByMeCol.setCellValueFactory(new PropertyValueFactory<>("amountOfTargetsDoneByMe"));
         creditGotCol.setCellValueFactory(new PropertyValueFactory<>("amountOfEarnedCredits"));
    }

    public void startTaskListRefresher() {
        listRefresher = new MyTasksTableRefresher(
                autoUpdate,
                this::updateTable, jobsManager);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    public void updateMyTaskTable(JobsManager i_jobsManager) {

        this.jobsManager = i_jobsManager;
    }

    private void updateTable(JobsManager jM) {

        Platform.runLater(() -> {

            try {
                // clear old data
                this.taskInfoTable.getItems().clear();

                // create new rows
                List<MyTasksTableData> myTasksTableDataList = this.createList(jM);
                ObservableList<MyTasksTableData> items = FXCollections.observableArrayList(myTasksTableDataList);
                this.taskInfoTable.setItems(items);

                //add listener for each row
                this.addListenerAndActionToRows();

            } catch (ErrorUtils e) {
                e.printStackTrace();
            }
        });
    }

    private void addListenerAndActionToRows() {

        this.taskInfoTable.setRowFactory(tmp -> {

            TableRow<MyTasksTableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                    // get wanted row
                    MyTasksTableData wantedRowTaskData = row.getItem();

                    // init control panel
                    try {
                        this.dadComponent.ControlPanelFor(wantedRowTaskData);
                    } catch (ErrorUtils e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    private void openWorkerControlPanelPage() {

    }

    private List<MyTasksTableData> createList(JobsManager jM) throws ErrorUtils {

        if(jM == null)
            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);

        List<MyTasksTableData> res = new ArrayList<>();

        // get all tasks & there needed data
        Map<String, JobsManager.MiniJobDataController> myTaskData = jM.getTaskNameToMiniDataHelper();

        // go over each task & create a row in the table for him
        for(String curName : myTaskData.keySet()){

            // create row
            MyTasksTableData curRow = new MyTasksTableData(myTaskData.get(curName));

            if(curRow == null)
                throw new ErrorUtils(ErrorUtils.invalidInput("Current row wasn't built correctly."));

            // add to the list
            res.add(curRow);
        }
        return res;
    }
}
