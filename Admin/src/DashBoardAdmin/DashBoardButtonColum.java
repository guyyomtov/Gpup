package DashBoardAdmin;

import DataManager.BackDataManager;
import Graph.Graph;
import Graph.GraphManager;
import errors.ErrorUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class DashBoardButtonColum {


    private BackDataManager bDM = new BackDataManager();
    public static Stage primaryStage;
    private Alert alertUser;
    private Graph curGraph;
    private GraphManager graphManager;
    @FXML private Button uploadAGraphButton;
    @FXML private Button makeANewAssignmentButton;


    @FXML
    void makeANewAssignmentAction(ActionEvent event) {

    }

    @FXML
    void uploadAGraphAction(ActionEvent event) {

        //Let User Choose a file from his machine
        String absolutePath = this.letUserChooseAFile();

        synchronized (this) {

            // Check that the file is valid & get the graph
            try {
                //This function CHECKS & STARTS a graph
                this.bDM.checkFile(absolutePath);

                // get cur graph
                this.curGraph = this.bDM.getGraph();

                // check that this graph doesn't exist already in our app
                if(!this.graphManager.graphExists(this.curGraph)){

                    // add cur graph to graph manager
                    this.graphManager.addGraph(this.curGraph);

                    // show user process done successfully
                    this.showUserSuccsesAlert();
                }
                else
                    this.showUserErrorAlert("graph exists already.");

            } catch (ErrorUtils e) {
                this.showUserErrorAlert("File not valid");
                return;
            }
        }
    }

    private String letUserChooseAFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    private void showUserErrorAlert(String errorType) {

        this.alertUser = new Alert(Alert.AlertType.ERROR);
        this.alertUser.setHeaderText(errorType);
        this.alertUser.setContentText(ErrorUtils.invalidFile());
        this.alertUser.showAndWait();
    }


    private void showUserSuccsesAlert() {

        this.alertUser = new Alert(Alert.AlertType.CONFIRMATION);
        this.alertUser.setHeaderText("Graph uploaded successfully.");
        this.alertUser.showAndWait();
    }
}
