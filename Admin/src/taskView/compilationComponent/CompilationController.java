package taskView.compilationComponent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class CompilationController {

    @FXML
    private Label fullPathSourceLabel;

    @FXML
    private Label saveDestinationLabel;

    @FXML
    void saveDestinationAction(ActionEvent event) {
        String fullPath = this.uploadFile();
        saveDestinationLabel.setText(fullPath);
    }
    @FXML
    void uploadFileSourceCodeAction(ActionEvent event) {
        String fullPath = this.uploadFile();
        fullPathSourceLabel.setText(fullPath);
    }

    public String uploadFile(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        String absolutePath = selectedDirectory.getAbsolutePath();
        return absolutePath;
    }
    public String getFullPathSource(){return this.fullPathSourceLabel.getText();}

    public String getFullPathDestination(){return this.saveDestinationLabel.getText();}

}
