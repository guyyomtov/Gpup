package DashBoardAdmin.TaskInfoTableComponent;

import DashBoardAdmin.MainDashboardController;
import DashBoardAdmin.runTaskAgain.RunTaskAgainController;
import api.HttpStatusUpdate;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import taskView.NewTask.NewTaskController;
import taskView.taskControlPanel.TaskControlPanelController;
import transferGraphData.TaskData;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskInfoTableController implements Closeable {

    @FXML private TableView<TaskData> taskInfoTable;
    @FXML private TableColumn<TaskData, String> taskNameCol;
    @FXML private TableColumn<TaskData, String> taskTypeCol;
    @FXML private TableColumn<TaskData, String> uploadedByCol;
    @FXML private TableColumn<TaskData, String> graphNameCol;
    @FXML private TableColumn<TaskData, Integer> totalTargetsCol;
    @FXML private TableColumn<TaskData, Integer> independentCol;
    @FXML private TableColumn<TaskData, Integer> leafCol;
    @FXML private TableColumn<TaskData, Integer> middleCol;
    @FXML private TableColumn<TaskData, Integer> rootCol;
    @FXML private TableColumn<TaskData, Integer> totalPriceCol;
    @FXML private TableColumn<TaskData, Integer> totalWorkersCol;
    @FXML private TableColumn<TaskData, TaskData.Status> statusCol;
    private NewTaskController newTaskController;

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalGraph;
    private BooleanProperty autoUpdate;
    private HttpStatusUpdate httpStatusUpdate;

    private TaskControlPanelController taskControlPanelController;
    private Parent taskControlPanelComponent;

    private RunTaskAgainController runTaskAgainController;
    private Parent runTaskAgainComponent;

    public TaskInfoTableController(){
        autoUpdate = new SimpleBooleanProperty(true);
        totalGraph = new SimpleIntegerProperty();
    }
    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;
    }

    public void startTaskListRefresher() {
        listRefresher = new TaskListRefresher(
                autoUpdate,
                httpStatusUpdate::updateHttpLine,
                this::updateTasksList);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    private void updateTasksList(List<TaskData> taskDataList) {
        Platform.runLater(() -> {
            ObservableList<TaskData> items = taskInfoTable.getItems();
                items.clear();
                items = FXCollections.observableArrayList(taskDataList);
                taskInfoTable.setItems(items);
                totalGraph.set(taskDataList.size());
                addListenerToRows();
                // todo maybe here to add the addLisener on every item in the table
        });
    }

    private void addListenerToRows() {

        this.taskInfoTable.setRowFactory(tmp -> {
            TableRow<TaskData> row = new TableRow<>();
           // String nameOfMakeTask = row.getItem().getUploadedBy();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TaskData taskData = row.getItem();
                    TaskData.Status status = taskData.getStatus();
                    // if task is done let the user run incremental / from scratch
                    if(status.equals(TaskData.Status.DONE) || status.equals(TaskData.Status.STOPPED)){
                        this.initAndUploadRunNewTaskComponent(taskData);
                        this.makeNewSceneForComponent(this.runTaskAgainComponent);
                    }
                    // if the admin press on task that he created.
                    else if(MainDashboardController.userName.equals(taskData.getUploadedBy())) {
                        this.initAndUploadTaskControlPanel(taskData);
                        this.makeNewSceneForComponent(this.taskControlPanelComponent);
                    }
                }
            });
            return row;
        });
    }

    private void initAndUploadRunNewTaskComponent(TaskData taskData) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoardAdmin/runTaskAgain/runTaskAgainFxml.fxml"));
                this.runTaskAgainComponent = loader.load();
                this.runTaskAgainController = loader.getController();
                this.runTaskAgainController.init(taskData, this.newTaskController);

            }
            catch (IOException e) {
            }
    }


    private void makeNewSceneForComponent(Parent parent) {
        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 800, 800));
        stage.show();
    }

    private void initAndUploadTaskControlPanel(TaskData taskData) {
        //open with fxml loader the control panel
        this.initControlPanelComponent(taskData);
    }

    private void initControlPanelComponent(TaskData taskData) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskView/taskControlPanel/TaskControlPanelFxml.fxml"));
            this.taskControlPanelComponent = loader.load();
            this.taskControlPanelController = loader.getController();
            this.taskControlPanelController.init(taskData);
            this.initButtonsAccordinglyToStatus(taskData);
        }
        catch (IOException e) {
        }
    }

    private void initButtonsAccordinglyToStatus(TaskData taskData) {
        // user already been in control panel
        if(taskData.getStatus().equals(TaskData.Status.AVAILABLE))
            this.taskControlPanelController.setControlButtons(true, false, false, true);
        else if(taskData.getStatus().equals(TaskData.Status.PAUSED))
            this.taskControlPanelController.setControlButtons(true, true, false, false);
    }


    public void initTable(){

        taskNameCol.setCellValueFactory(
                new PropertyValueFactory<>("taskName")
        );
        taskTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("whatKindOfTask")
        );
        graphNameCol.setCellValueFactory(
                new PropertyValueFactory<>("graphName")
        );
        uploadedByCol.setCellValueFactory(
                new PropertyValueFactory<>("uploadedBy")
        );
        totalTargetsCol.setCellValueFactory(
                new PropertyValueFactory<>("totalTargets")
        );
        independentCol.setCellValueFactory(
                new PropertyValueFactory<>("totalIndependent")
        );
        leafCol.setCellValueFactory(
                new PropertyValueFactory<>("totalLeaf")
        );
        middleCol.setCellValueFactory(
                new PropertyValueFactory<>("totalMiddles")
        );
        rootCol.setCellValueFactory(
                new PropertyValueFactory<>("totalRoots")
        );
        totalPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("totalPrice")
        );
        totalWorkersCol.setCellValueFactory(
                new PropertyValueFactory<>("totalWorker")
        );

        statusCol.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
    }


    //todo to handle with all the close!
    @Override
    public void close() throws IOException {

    }

    public void setNewTaskController(NewTaskController newTaskController) {this.newTaskController = newTaskController;}
}
