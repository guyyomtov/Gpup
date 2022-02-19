package graphInfoView;

import AnimationComponent.AnimationController;
import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import Graph.Target;
import errors.ErrorUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tableView.TableController;
import transferGraphData.AllGraphInfo;
import transferGraphData.GraphInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GraphInfoController {

    @FXML private ScrollPane welcomeView;
    @FXML private GridPane gridPane;
    @FXML private ChoiceBox<String> choiceBoxSerialSets;
    @FXML private BarChart<String, Integer> barChart;
    @FXML private Label resultOfChoiceBox;
    @FXML private Label summaryTable;
    @FXML private Label summaryBy;
    @FXML private TextField wantedUserPath;
    private List<Label> labels = new ArrayList<>();
    @FXML private CheckBox whatIfCheckBox;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    @FXML private RadioButton dependsOnButton;
    @FXML private RadioButton requiredForButton;
    private List<RadioButton> radioButtons = new ArrayList<>();
    private boolean tableIsFull = false;
    @FXML private Parent tableComponent;
    @FXML private TableController tableComponentController;
    private BackDataManager bDM = new BackDataManager();
    private AnimationController aniController;
    private ImageView graphizImageView;
    private AllGraphInfo allGraphInfo;


    public void initGraphInfo() throws IOException {

        // clear data from old table if exist
        if(this.tableComponentController != null) {
            this.tableComponentController.getTableView().getItems().clear();
        }

        this.tableComponentController.initTable(this.allGraphInfo.getTargetInfoList());
        this.tableComponentController.setGraphInfoController(this);
        this.initSummary();
        this.initBarChart();

        this.radioButtons = Arrays.asList(this.dependsOnButton, this.requiredForButton);
        this.checkBoxes = Arrays.asList(this.whatIfCheckBox);
        this.labels = Arrays.asList(this.resultOfChoiceBox, this.summaryBy, this.summaryTable);

        this.initAnimation();
    }

    private void initAnimation() throws IOException {

        String curP = "/AnimationComponent/Animation.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        Parent animationView = loader.load();
        this.aniController = loader.getController();

        aniController.init();

        gridPane.add(animationView, 2,2);
    }


    private void initSummary() {
        GraphInfo graphInfo = this.allGraphInfo.getGraphInfo();
        summaryTable.setText("Total targets: " + graphInfo.getTotalTargets()
        );
        summaryBy.setText(    "Independents: " + graphInfo.getTotalIndependents()+ " Leaves: " + graphInfo.getTotalLeaf() +
                " Middles: " + graphInfo.getTotalMiddles() + " Roots: " + graphInfo.getTotalRoots() + ".");
    }

    private void initBarChart()
    {
        GraphInfo graphInfo = allGraphInfo.getGraphInfo();
        barChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Target Level");
        series.getData().add(new XYChart.Data<>("Independent", graphInfo.getTotalIndependents()));
        series.getData().add(new XYChart.Data<>("Leaf", graphInfo.getTotalLeaf()));
        series.getData().add(new XYChart.Data<>("Middle", graphInfo.getTotalMiddles()));
        series.getData().add(new XYChart.Data<>("Root", graphInfo.getTotalRoots()));
        barChart.getData().addAll(series);

    }

    @FXML
    void whatIfCheckBoxAction(ActionEvent event){
        if(this.whatIfCheckBox.isSelected()) {
            this.dependsOnButton.setDisable(false);
            this.requiredForButton.setDisable(false);
        }
        else{
            this.dependsOnButton.setDisable(true);
            this.requiredForButton.setDisable(true);
        }
    }


    public TableController getTableComponentController(){ return this.tableComponentController;}

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }

    public CheckBox getWhatIfCheckBox() {
        return this.whatIfCheckBox;
    }

    public RadioButton getDependsOnRadioButton(){
        return this.dependsOnButton;
    }

    public BackDataManager getbDM(){
        return this.bDM;
    }

    public void setButtonsColors(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        // set my buttons skins
        SkinsUtils.changeRadioButtonTextColorTo(enumWantedColor, this.radioButtons);
        SkinsUtils.changeCheckBoxButtonTextColorTo(enumWantedColor, this.checkBoxes);

        // set my kids buttons skins
        this.aniController.setSkins(enumWantedColor);
    }

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        //set buttons colors
        //this.setButtonsColors(enumWantedColor);

        // set my label skins
        //SkinsUtils.changeLabelsTextColorTo(enumWantedColor, this.labels);

        //set background colors
        //this.setBackRoundColors(enumWantedColor);
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {

        if(enumWantedColor != SkinsUtils.Colors.DEFAULT)
            this.gridPane.setBackground(new Background(new BackgroundFill(Color.gray(0.2), null, null)));
        else
            this.gridPane.setBackground(new Background(new BackgroundFill(null, null, null)));
    }

    @FXML
    void getGraphizAction(ActionEvent event) throws IOException, ErrorUtils {

        try {
            this.bDM.makeGraphizImage(this.wantedUserPath.getText());

            // tell user that the process worked
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Success");
            errorAlert.setContentText("Made graph successfully :)");
            errorAlert.showAndWait();

            //make image & put in wanted place
            Image image = new Image(new FileInputStream(this.wantedUserPath.getText() + "/curGraph.viz.png"));
            this.graphizImageView = new ImageView(image);

            errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setGraphic(this.graphizImageView);
            errorAlert.showAndWait();

        } catch (IOException e) {
            ErrorUtils.makeJavaFXCutomAlert(e.getMessage() + "please try giving another dir");
        }
    }

    public AllGraphInfo getAllGraphInfo() {
        return allGraphInfo;
    }

    public void setAllGraphInfo(AllGraphInfo allGraphInfo) {
        this.allGraphInfo = allGraphInfo;
    }
}



