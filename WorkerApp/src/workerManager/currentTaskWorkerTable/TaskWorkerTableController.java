package workerManager.currentTaskWorkerTable;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TaskWorkerTableController {
//todo to chose what kind of object should be here .
    @FXML
    private TableView<?> taskInfoTable;

    @FXML
    private TableColumn<?, ?> taskNameCol;

    @FXML
    private TableColumn<?, ?> workerAmountCol;

    @FXML
    private TableColumn<?, ?> progressCol;

    @FXML
    private TableColumn<?, ?> doneByMeCol;

    @FXML
    private TableColumn<?, ?> creditGotCol;

    public void initTable() {
    }
}
