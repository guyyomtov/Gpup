package taskView.compilationComponent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

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
        FileChooser fileChooser = new FileChooser();
       // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        String absolutePath = selectedFile.getAbsolutePath();
        return absolutePath;
    }
    public String getFullPathSource(){return this.fullPathSourceLabel.getText();}

    public String getFullPathDestination(){return this.saveDestinationLabel.getText();}

}
