package uploadFileView;

import DataManager.BackDataManager;
import FindPathComponent.FindPathController;
import InterrogatorComponent.InterrogatorController;
import WhatIfComponent.WhatIfController;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import taskView.TaskController;

import java.io.File;
import java.io.IOException;


public class MainController {

    @FXML
    private BorderPane MainBorderPane;

    @FXML
    private Button graphInfoButton;

    @FXML
    private Button InterrogatorButton;

    @FXML
    private Button taskButton;

    @FXML
    private MenuItem UploadFile;

    @FXML
    private ScrollPane welcomeView;

    @FXML
    private Label msgAfterUploadFile;

    public static Stage primaryStage;

    private BackDataManager bDM = new BackDataManager();

    private GraphInfoController graphInfoController;

    private Parent graphInfoView;
   // FXMLLoader graphInfoLoader;

    private TaskController taskController;

    private Parent taskView;

    @FXML
    void graphInfoAction(ActionEvent event){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphInfoView/graphInfoFxml.fxml"));
            graphInfoView = loader.load();
            graphInfoController = loader.getController();
            graphInfoController.setbDM(this.bDM);
            graphInfoController.initGraphInfo();
            int size = this.bDM.getAllTargets().size();
            MainBorderPane.setCenter(graphInfoView);

        }catch (IOException e) {
           //e.printStackTrace();
        }
    }

    @FXML
    void UploadFileAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        String absolutePath = selectedFile.getAbsolutePath();
        this.msgAfterUploadFile.setVisible(true);
        try {
            this.bDM.checkFile(absolutePath);
        }catch (ErrorUtils e){
             this.msgAfterUploadFile.setText(e.getMessage());
             return;
       }
        this.graphInfoButton.setDisable(false);
        this.InterrogatorButton.setDisable(false);
        this.taskButton.setDisable(false);
        this.msgAfterUploadFile.setText("The file " + selectedFile.getAbsolutePath() + " upload successfully");
        if(graphInfoController != null) {
            graphInfoController.setbDM(this.bDM);
            graphInfoController.initGraphInfo();
        }
        this.taskController = null;
    //    this.msgAfterUploadFile.setVisible(true);

    }

    @FXML
    void InterrogatorButtonAction(ActionEvent event) throws IOException {

        String circlePathName = "../InterrogatorComponent/InteragatorComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent interrogatorView = loader.load();
        InterrogatorController intController = loader.getController();
        intController.init(this.bDM);

        MainBorderPane.setCenter(interrogatorView);
    }

    @FXML
    void taskButtonAction(ActionEvent event) {
        try {
            if (taskController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../taskView/taskViewFxml.fxml"));
                taskView = loader.load();
                taskController = loader.getController();
                taskController.setbDM(this.bDM);
                taskController.initTaskView();
            }
            MainBorderPane.setCenter(taskView);



        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
