package DashBoard;

import DashBoard.AllTasksTable.AllTasksInfoTableController;
import DashBoard.NewJob.JobsManager;
import DashBoard.NewJob.NewJobController;
import DashBoardAdmin.onlineAdminsComponent.OnlineAdminsController;
import api.HttpStatusUpdate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import workerManager.WorkerManagerController;

import java.io.IOException;
import java.util.function.Consumer;

public class WorkerDashBoardController implements HttpStatusUpdate, Consumer {

    @FXML public Label workerNameLabel;
    @FXML private TextField freeThreadsTextField;
    @FXML private BorderPane boarderPane;
    @FXML private TextField amountOfCreditsEarnedField;
    @FXML private Button dashBoardButton;
    @FXML private Button workManagerButton;
    @FXML private TitledPane rightSideTitlePane;
    private Stage primaryStage;
    private Parent allTasksTableComponent;
    private OnlineAdminsController onlineUsersController;
    private AllTasksInfoTableController allTasksTableController;
    private NewJobController newJobController;
    private Parent newJobComponent;
    private WorkerManagerController workerManagerController;
    private Parent workerManagerComponent;
    private Integer amountOfThreads;
    private JobsManager jobsManager;
    public static String workerName;


    public void init(String name){
        //name of the worker
        workerName = name;
        this.workerNameLabel.setText("Hello " + workerName);

        this.jobsManager = new JobsManager(amountOfThreads);
        new Thread(this.jobsManager).start();

        try {
            this.initOnlineUsersComponent();

            this.initTasksTableComponent();

            this.initWorkManagerComponent();

            this.amountOfCreditsEarnedField.setText("0");
            this.freeThreadsTextField.setText(String.valueOf(this.amountOfThreads));
            this.jobsManager.setConsumerForLogs(this.workerManagerController);
            this.jobsManager.setTaskRefresherForProcess(this.allTasksTableController);
            this.jobsManager.setWorkerDashBoardController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWorkManagerComponent() {
        try {
            String curP = "/workerManager/workerManagerFxml.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
            this.workerManagerComponent = loader.load();
            this.workerManagerController = loader.getController();

            this.workerManagerController.init(amountOfThreads, this.jobsManager);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTasksTableComponent() throws IOException {

        // get resources
        String curP = "/DashBoard/AllTasksTable/TasksInfoTableComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        Parent allTasksTableComponent = loader.load();
        this.allTasksTableComponent = allTasksTableComponent;
        this.allTasksTableController = loader.getController();

        // start "get data all the time"
        this.allTasksTableController.setHttpStatusUpdate(this);
        this.allTasksTableController.startTaskListRefresher();
        this.allTasksTableController.initTable(this.jobsManager);

        // set component on page
        this.boarderPane.setCenter(allTasksTableComponent);
    }

    private void initOnlineUsersComponent() throws IOException {

        // get resources
        String curP = "/DashBoardAdmin/onlineAdminsComponent/OnlineAdmins.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        Parent onlineUsersComponent = loader.load();
        this.onlineUsersController = loader.getController();

        // start "get data all the time"
        this.onlineUsersController.setHttpStatusUpdate(this);
        this.onlineUsersController.startListRefresher();

        // set component on page
        this.rightSideTitlePane = new TitledPane();
        this.rightSideTitlePane.setText("Online Users:");
        this.rightSideTitlePane.setContent(onlineUsersComponent);
        this.boarderPane.setRight(this.rightSideTitlePane);
    }

    @FXML
    void dashBoardButtonAction(ActionEvent event) throws IOException {

        // right side table
        this.initOnlineUsersComponent();
        this.boarderPane.setCenter(allTasksTableComponent);
        // center table
    }

    @FXML
    void workManagerButtonAction(ActionEvent event) {

        // remove unwanted components from page
        this.boarderPane.setCenter(this.workerManagerComponent);
    }

    public void setPrimaryStage(Stage primaryStage) {this.primaryStage = primaryStage;}

    @Override
    public void updateHttpLine(String line) {

    }

    public Integer getAmountOfThreads() {
        return amountOfThreads;
    }

    public void setAmountOfThreads(Integer amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }

    @Override
    public void accept(Object amountOfCredit) {
        Platform.runLater(()->
        {
            // for amount of credit
            Integer amountOfCreditTillNow = Integer.valueOf(this.amountOfCreditsEarnedField.getText());
            Integer amountOfCreditFromCurrentTarget = (Integer) amountOfCredit;
            this.amountOfCreditsEarnedField.setText(String.valueOf(amountOfCreditFromCurrentTarget + amountOfCreditTillNow));
            //for freeThreads to up!
            Integer freeThreads = Integer.valueOf(this.freeThreadsTextField.getText());
            this.freeThreadsTextField.setText(String.valueOf(freeThreads + 1));
        });
    }


    public void updateFreeThreadsField() {
        Platform.runLater(() -> {
            Integer temp = Integer.valueOf(this.freeThreadsTextField.getText());
            this.freeThreadsTextField.setText(String.valueOf(temp - 1));
        });
    }

}
