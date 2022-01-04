package graphInfoView;

import DataManager.BackDataManager;
import Graph.Target;
import Graph.Target;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import tableView.TableController;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class GraphInfoController {

    @FXML
    private ScrollPane welcomeView;

    @FXML
    private ChoiceBox<String> choiceBoxSerialSets;

    @FXML
    private Label resultOfChoiceBox;

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private Label summaryTable;

    @FXML
    private Label summaryBy;

    @FXML
    private Parent tableComponent;

    @FXML
    private TableController tableComponentController;

    @FXML
    private CheckBox whatIfCheckBox;

    private boolean tableIsFull = false;

    private BackDataManager bDM = new BackDataManager();

    public void initGraphInfo(){

        this.tableComponentController.initTable(this.bDM.getAllTargets());
        this.tableComponentController.setTargets(this.bDM.getAllTargets());
        this.initSummary();;
        this.initChoiceBox();
        this.initBarChart();

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
}



