package DashBoard.NewJob;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;

public class NewJobController {


    @FXML private TitledPane newJobTitlePane;
    @FXML private Spinner<Integer> amountOfTargetsSpinner;
    @FXML private Spinner<Integer> amountOfThreadsSpinner;
    @FXML private Button cancelButton;
    @FXML private Button applyButton;


    @FXML
    public void initialize() {

    }

    @FXML
    void applyButtonAction(ActionEvent event) {

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }
}
