package DashBoard;

import DashBoard.AllTasksTable.AllTasksInfoTableController;
import DashBoardAdmin.onlineAdminsComponent.OnlineAdminsController;
import api.HttpStatusUpdate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkerDashBoardController implements HttpStatusUpdate {

    
    @FXML private BorderPane boarderPane;
    @FXML private TextField amountOfCreditsEarnedField;
    @FXML private Button dashBoardButton;
    @FXML private Button workManagerButton;
    @FXML private TitledPane rightSideTitlePane;
    private Stage primaryStage;
    private OnlineAdminsController onlineUsersController;
    private AllTasksInfoTableController allTasksTableController;


    @FXML
    public void initialize() throws IOException {

        this.initOnlineUsersComponent();

        this.initTasksTableComponent();
    }

    private void initTasksTableComponent() throws IOException {

        // get resources
        String curP = "/DashBoard/AllTasksTable/TasksInfoTableComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        Parent allTasksTableComponent = loader.load();
        this.allTasksTableController = loader.getController();

        // start "get data all the time"
        this.allTasksTableController.setHttpStatusUpdate(this);
        this.allTasksTableController.startTaskListRefresher();
        this.allTasksTableController.initTable();

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

        // center table
    }

    @FXML
    void workManagerButtonAction(ActionEvent event) {

        // remove unwanted components from page
        this.boarderPane.setRight(null);
    }

    public void setPrimaryStage(Stage primaryStage) {this.primaryStage = primaryStage;}

    @Override
    public void updateHttpLine(String line) {

    }
}
