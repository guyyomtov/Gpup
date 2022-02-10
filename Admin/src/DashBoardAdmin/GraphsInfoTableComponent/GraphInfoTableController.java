package DashBoardAdmin.GraphsInfoTableComponent;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GraphInfoTableController {

    @FXML
    private TableView<?> graphInfoTable;

    @FXML
    private TableColumn<?, ?> graphNameCol;

    @FXML
    private TableColumn<?, ?> uploadedByCol;

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
    private TableColumn<?, ?> pricesCol;

}
