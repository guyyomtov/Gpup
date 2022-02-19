package uploadGraphPane;

import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import InterrogatorComponent.InterrogatorController;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
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
import transferGraphData.AllGraphInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UploadPaneController {

    @FXML private BorderPane MainBorderPane;
    @FXML private Button graphInfoButton;
    @FXML private Button InterrogatorButton;
    @FXML private Button taskButton;
    @FXML private ScrollPane welcomeView;
    @FXML private Label msgAfterUploadFile;
    public static Stage primaryStage;
    private BackDataManager bDM = new BackDataManager();
    private AllGraphInfo allGraphInfo;
    private String wantedColor = new String();
    private Paint textColor = Color.BLACK;
    private List<Button> buttons = new ArrayList<>();
    private GraphInfoController graphInfoController;
    private Parent graphInfoView;
    private InterrogatorController intController;
    private Parent interrogatorView;
    private TaskController taskController;
    private Parent taskView;
    @FXML private Button dashboardButton;

    @FXML void dashboardButtonAction(ActionEvent event) {}


    private void setDisableButtonBeforeGraph(){

        this.graphInfoButton.setDisable(false);
        this.InterrogatorButton.setDisable(false);
        this.taskButton.setDisable(false);
    }

    private void initTaskComponent(){

        try {
            if (taskController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskView/taskViewFxml.fxml"));
                taskView = loader.load(); // was null
                this.taskController = loader.getController();
                this.taskController.setbDM(this.bDM);
                this.taskController.initTaskView();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initInteragatorComponent() throws IOException {

        String circlePathName = "/InterrogatorComponent/InteragatorComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        this.interrogatorView = loader.load();
        this.intController = loader.getController();
       // this.intController.init(this.bDM);

    }
    @FXML
    void InterrogatorButtonAction(ActionEvent event) throws IOException {

        MainBorderPane.setCenter(this.interrogatorView);
    }


    void initGraphInfoComponent() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphInfoView/graphInfoFxml.fxml"));
        graphInfoView = loader.load();
        this.graphInfoController = loader.getController();
        this.graphInfoController.setAllGraphInfo(this.allGraphInfo);
        this.graphInfoController.initGraphInfo();
    }

    public void init(){
        try {
            initGraphInfoComponent();
            MainBorderPane.setCenter(this.graphInfoView);
            setDisableButtonBeforeGraph();
           // initInteragatorComponent(); todo later
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void graphInfoAction(ActionEvent event){

        MainBorderPane.setCenter(this.graphInfoView);
    }

    @FXML
    void taskButtonAction(ActionEvent event) {

        this.MainBorderPane.setCenter(this.taskView);
    }

    public AllGraphInfo getAllGraphInfo() {
        return allGraphInfo;
    }

    public void setAllGraphInfo(AllGraphInfo allGraphInfo) {
        this.allGraphInfo = allGraphInfo;
    }

    public BorderPane getMainPane() {
        return this.MainBorderPane;
    }
}
