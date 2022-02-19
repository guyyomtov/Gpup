package DashBoardAdmin.TaskInfoTableComponent;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TaskInfoTableController {

    @FXML
    private TableView<?> taskInfoTable;

    @FXML
    private TableColumn<?, ?> taskNameCol;

    @FXML
    private TableColumn<?, ?> uploadedByCol;

    @FXML
    private TableColumn<?, ?> graphNameCol;

    @FXML
    private TableColumn<?, ?> totalTargetsCol;

    @FXML
    private TableColumn<?, ?> independentCol;

    @FXML
    private TableColumn<?, ?> leafCol;

    @FXML
    private TableColumn<?, ?> middleCol;

    @FXML
    private TableColumn<?, ?> rootCol;

    @FXML
    private TableColumn<?, ?> totalPriceCol;

    @FXML
    private TableColumn<?, ?> pricePerTargetCol;

    @FXML
    private TableColumn<?, ?> totalWorkersCol;

    @FXML
    private TableColumn<?, ?> statusCol;

}
