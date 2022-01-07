package graphInfoView;

import AnimationComponent.AnimationController;
import DataManager.BackDataManager;
import Graph.Target;
import Graph.Target;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import tableView.TableController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class GraphInfoController {

    @FXML private ScrollPane welcomeView;
    @FXML private GridPane gridPane;
    @FXML private ChoiceBox<String> choiceBoxSerialSets;
    @FXML private Label resultOfChoiceBox;
    @FXML private BarChart<String, Integer> barChart;
    @FXML private Label summaryTable;
    @FXML private Label summaryBy;
    @FXML private Parent tableComponent;
    @FXML private TableController tableComponentController;
    @FXML private CheckBox whatIfCheckBox;
    @FXML private RadioButton dependsOnButton;
    @FXML private RadioButton requiredForButton;
    private boolean tableIsFull = false;
    private AnimationController aniController;

    private BackDataManager bDM = new BackDataManager();

    public void initGraphInfo(){

        this.tableComponentController.initTable(this.bDM.getAllTargets());
        this.tableComponentController.setGraphInfoController(this);
        this.initSummary();;
        this.initChoiceBox();
        this.initBarChart();
        try {
            this.initAnimation();
        } catch (IOException e) {
           // to handle with exception e.printStackTrace();
        }
    }

    private void initAnimation() throws IOException {

        String curP = "../AnimationComponent/Animation.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(curP));
        Parent animationView = loader.load();
        this.aniController = loader.getController();

        aniController.init();

        gridPane.add(animationView, 2,2);
    }

    private void initChoiceBox() {
        this.choiceBoxSerialSets.getItems().clear();
        Map<String, Set<Target>> serialSets = this.bDM.getSerialSets();
        Set<String> namesOfSerialSets = serialSets.keySet();
        String[] names = namesOfSerialSets.toArray(new String[0]);
        this.choiceBoxSerialSets.getItems().addAll(names);
        this.choiceBoxSerialSets.setOnAction(this::getNames);
    }

    public void getNames(ActionEvent event){
        Map<String, Set<Target>> serialSets = this.bDM.getSerialSets();
        Set<Target> inSerial = serialSets.get(this.choiceBoxSerialSets.getValue());
        for(Target target: inSerial)
        {
            this.resultOfChoiceBox.setText(this.resultOfChoiceBox.getText() + target.getName() + " ");
            this.aniController.setSquareText(this.resultOfChoiceBox.getText());
        }

    }

    private void initSummary() {
        summaryTable.setText("Total targets: " + bDM.getAllTargets().size()
        );
        summaryBy.setText(    "Independents: " + bDM.getNumOfIndependents() + " Leaves: " + bDM.getNumOfLeafs() +
                " Middles: " + bDM.getNumOfMiddle() + " Roots: " + bDM.getNumOfRoots() + ".");
    }

    private void initBarChart()
    {
        barChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Target Level");
        series.getData().add(new XYChart.Data<>("Independent", this.bDM.getNumOfIndependents()));
        series.getData().add(new XYChart.Data<>("Leaf", this.bDM.getNumOfLeafs()));
        series.getData().add(new XYChart.Data<>("Middle", this.bDM.getNumOfMiddle()));
        series.getData().add(new XYChart.Data<>("Root", this.bDM.getNumOfRoots()));
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


    public TableView<Target> getTable(){

        if(tableIsFull)
            return this.tableComponentController.getTableView();
        else
            return null;
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
}



