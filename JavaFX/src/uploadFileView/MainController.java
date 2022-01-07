package uploadFileView;

import AnimationComponent.AnimationController;
import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import FindPathComponent.FindPathController;
import InterrogatorComponent.InterrogatorController;
import WhatIfComponent.WhatIfController;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import taskView.TaskController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainController {

    @FXML private BorderPane MainBorderPane;
    @FXML private Button graphInfoButton;
    @FXML private Button InterrogatorButton;
    @FXML private Button taskButton;
    @FXML private Button changeSkins;
    @FXML private MenuItem UploadFile;
    @FXML private ScrollPane welcomeView;
    @FXML private Label msgAfterUploadFile;
    public static Stage primaryStage;
    private BackDataManager bDM = new BackDataManager();
    private TaskController taskController;
    private Parent taskView;
    @FXML private ChoiceBox<String> skinsChoiceBox;
    private String wantedColor = new String();
    private Paint textColor = Color.BLACK;
    private List<Button> buttons = new ArrayList<>();

    private GraphInfoController graphInfoController;
    private Parent graphInfoView;

    private InterrogatorController intController;
    private Parent interrogatorView;



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
        this.setDisableButtonBeforeGraph();

        this.msgAfterUploadFile.setText("The file " + selectedFile.getAbsolutePath() + " upload successfully");
        this.taskController = null;

        this.buttons = Arrays.asList(this.changeSkins, this.graphInfoButton,
                this.taskButton, this.InterrogatorButton);

        // SKINS
      //  SkinsUtils.initSkinsButton(this.skinsChoiceBox, this.changeSkins);

        // start child components
        this.initInteragatorComponent();
        this.initGraphInfoComponent();
        this.initTaskComponent();

        //    this.msgAfterUploadFile.setVisible(true);
    }

    private void setDisableButtonBeforeGraph(){

        this.graphInfoButton.setDisable(false);
        this.InterrogatorButton.setDisable(false);
        this.taskButton.setDisable(false);
    }

    private void initTaskComponent(){

        try {
            if (taskController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../taskView/taskViewFxml.fxml"));
                taskView = loader.load();
                this.taskController = loader.getController();
                this.taskController.setbDM(this.bDM);
                this.taskController.initTaskView();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initInteragatorComponent() throws IOException {

        String circlePathName = "../InterrogatorComponent/InteragatorComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        this.interrogatorView = loader.load();
        this.intController = loader.getController();
        this.intController.init(this.bDM);

    }
    @FXML
    void InterrogatorButtonAction(ActionEvent event) throws IOException {

        MainBorderPane.setCenter(this.interrogatorView);
    }


    void initGraphInfoComponent() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../graphInfoView/graphInfoFxml.fxml"));
        graphInfoView = loader.load();
        this.graphInfoController = loader.getController();
        this.graphInfoController.setbDM(this.bDM);
        this.graphInfoController.initGraphInfo();
        int size = this.bDM.getAllTargets().size();
    }

    @FXML
    void graphInfoAction(ActionEvent event){

        MainBorderPane.setCenter(this.graphInfoView);
    }

    @FXML
    void taskButtonAction(ActionEvent event) {

        this.MainBorderPane.setCenter(this.taskView);
    }


//    @FXML
//    void changeSkins(ActionEvent event) {
//
//        //get wanted colors
//        this.wantedColor = this.skinsChoiceBox.getValue();
//        SkinsUtils.Colors enumWantedColor = SkinsUtils.makeColorsEnum(this.wantedColor);
//
//        // change in all child components
//        this.setButtonsColors(enumWantedColor);
//        this.intController.setButtonsColors(enumWantedColor);
//        this.taskController.setButtonsColors(enumWantedColor);
//    }

//    public void setButtonsColors(SkinsUtils.Colors enumWantedColor){
//
//        SkinsUtils.changeButtonColorTo(enumWantedColor, this.buttons);
//    }

    @FXML
    void redSkinAction(ActionEvent event) {

    }
    @FXML
    void blueSkinAction(ActionEvent event) {

    }

    @FXML
    void defualtSkinAction(ActionEvent event) {

    }

    @FXML
    void changeSkinfOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void changeSkinfOnMouseExist(MouseEvent event) {

    }
}
