package DashBoard.AllTasksTable;

import DashBoard.NewJob.NewJobController;
import Utils.ErrorHandling.ErrorHandling;
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
import taskView.taskControlPanel.TaskControlPanelController;
import transferGraphData.TaskData;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AllTasksInfoTableController implements Closeable {

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

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalGraph;
    private BooleanProperty autoUpdate;
    private HttpStatusUpdate httpStatusUpdate;

    private NewJobController newJobController;
    private Parent newJobComponent;


    public AllTasksInfoTableController(){
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
        });
    }

    private void addListenerToRows() {

        this.taskInfoTable.setRowFactory(tmp -> {
            TableRow<TaskData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                    TaskData taskData = row.getItem();

                    try {
                        openNewJobPage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    private void openNewJobPage() throws IOException {

        //start resources
        String curP = "/DashBoard/NewJob/NewJob.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        this.newJobComponent = loader.load();
        this.newJobController = loader.getController();

        //open component in new page
        Stage stage = new Stage();
        stage.setScene(new Scene(this.newJobComponent, 800, 800));
        stage.show();
    }

    public void initTable(){

        taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        taskTypeCol.setCellValueFactory(new PropertyValueFactory<>("whatKindOfTask"));

        graphNameCol.setCellValueFactory(new PropertyValueFactory<>("graphName"));

        uploadedByCol.setCellValueFactory(new PropertyValueFactory<>("uploadedBy"));

        totalTargetsCol.setCellValueFactory(new PropertyValueFactory<>("totalTargets"));

        independentCol.setCellValueFactory(new PropertyValueFactory<>("totalIndependent"));

        leafCol.setCellValueFactory(new PropertyValueFactory<>("totalLeaf"));

        middleCol.setCellValueFactory(new PropertyValueFactory<>("totalMiddles"));

        rootCol.setCellValueFactory(new PropertyValueFactory<>("totalRoots"));

        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        totalWorkersCol.setCellValueFactory(new PropertyValueFactory<>("totalWorker"));

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status")
        );
    }

    @Override
    public void close() throws IOException {

    }
}
