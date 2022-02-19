package taskView.NewTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tableView.TableController;

public class NewTaskController {


    @FXML private GridPane gridPane;
    @FXML private TableView targetTable;
    @FXML private TableController targetTableController;
    @FXML private TextField taskNameTextInput;
    @FXML private RadioButton simulationRB;
    @FXML private RadioButton compilationRB;
    @FXML private Button sumbitButton;


    @FXML void compilationRBOnAction(ActionEvent event) {

    }

    @FXML
    void simulationRBOnAction(ActionEvent event) {

    }

    @FXML
    void sumbitButtonAction(ActionEvent event) {

    }

}
