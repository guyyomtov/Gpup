package DashBoardAdmin.TaskInfoTableComponent;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import transferGraphData.TaskData;

public class TaskInfoTableController {

    @FXML
    private TableView<TaskData> taskInfoTable;

    @FXML
    private TableColumn<TaskData, String> taskNameCol;

    @FXML
    private TableColumn<TaskData, String> uploadedByCol;

    @FXML
    private TableColumn<TaskData, String> graphNameCol;

    @FXML
    private TableColumn<TaskData, Integer> totalTargetsCol;

    @FXML
    private TableColumn<TaskData, Integer> independentCol;

    @FXML
    private TableColumn<TaskData, Integer> leafCol;

    @FXML
    private TableColumn<TaskData, Integer> middleCol;

    @FXML
    private TableColumn<TaskData, Integer> rootCol;

    @FXML
    private TableColumn<TaskData, Integer> totalPriceCol;

    @FXML
    private TableColumn<TaskData, Integer> totalWorkersCol;

    @FXML
    private TableColumn<TaskData, String> statusCol;




}
