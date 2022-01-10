package uploadFileView;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
    @FXML private MenuItem UploadFile;
    @FXML private ScrollPane welcomeView;
    @FXML private Label msgAfterUploadFile;
    public static Stage primaryStage;
    private BackDataManager bDM = new BackDataManager();
    @FXML private ChoiceBox<String> skinsChoiceBox;
    private String wantedColor = new String();
    private Paint textColor = Color.BLACK;
    private List<Button> buttons = new ArrayList<>();
    private GraphInfoController graphInfoController;
    private Parent graphInfoView;
    private InterrogatorController intController;
    private Parent interrogatorView;
    private TaskController taskController;
    private Parent taskView;



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

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText(ErrorUtils.invalidFile());
            errorAlert.showAndWait();

             this.msgAfterUploadFile.setText(e.getMessage());
             return;
       }
        this.setDisableButtonBeforeGraph();

        this.msgAfterUploadFile.setText("The file " + selectedFile.getAbsolutePath() + " upload successfully");
        this.taskController = null;

        this.buttons = Arrays.asList(this.graphInfoButton,
                this.taskButton, this.InterrogatorButton);

        // start child components
        this.initInteragatorComponent();
        this.initGraphInfoComponent();
        this.initTaskComponent();

        // refresh\ show current graph table
        MainBorderPane.setCenter(this.graphInfoView);
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

    public void setButtonsColors(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        //SkinsUtils.changeButtonColorTo(enumWantedColor, this.buttons);
    }

    public void changeSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        this.setBoarderSkins(enumWantedColor);
    }

    private void setBoarderSkins(SkinsUtils.Colors enumWantedColor) {

        switch (enumWantedColor){

            case RED:
                this.MainBorderPane.getStylesheets().add("AnimationComponent/redMode.css");
                break;
            case BLUE:
                this.MainBorderPane.getStylesheets().add("AnimationComponent/blueMode.css");
                break;
            case DEFAULT:
                this.MainBorderPane.getStylesheets().add("AnimationComponent/defaultMode.css");
                break;
        }
    }

    @FXML
    void redSkinAction(ActionEvent event) throws ErrorUtils {

        SkinsUtils.Colors enumWantedColor = SkinsUtils.makeColorsEnum("RED");

        this.changeSkins(enumWantedColor);
    }
    @FXML
    void blueSkinAction(ActionEvent event) throws ErrorUtils {

        SkinsUtils.Colors enumWantedColor = SkinsUtils.makeColorsEnum("BLUE");

        this.changeSkins(enumWantedColor);
    }

    @FXML
    void defualtSkinAction(ActionEvent event) throws ErrorUtils {

        SkinsUtils.Colors enumWantedColor = SkinsUtils.makeColorsEnum("DEFAULT");

        this.changeSkins(enumWantedColor);
    }

    @FXML
    void changeSkinfOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void changeSkinfOnMouseExist(MouseEvent event) {

    }
}
