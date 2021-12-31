package graphInfoView;

import DataManager.BackDataManager;
import Graph.Target;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import tableView.TableController;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphInfoController {

    @FXML
    private ScrollPane welcomeView;

    @FXML
    private ChoiceBox<?> choiceBoxSerialSets;

    @FXML
    private Label resultOfChoiceBox;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private Label summaryTable;

    @FXML
    private Label summaryBy;

    @FXML
    private Parent tableComponent;

    @FXML
    private TableController tableComponentController;
    private boolean tableIsFull = false;

    private BackDataManager bDM = new BackDataManager();

    public void initGraphInfo(){

        this.tableComponentController.initTable(this.bDM.getAllTargets());
        this.initSummary();

        this.tableIsFull = true;
    }

    private void initSummary() {
        summaryTable.setText("Total targets: " + bDM.getAllTargets().size()
        );
        summaryBy.setText(    "Independents: " + bDM.getNumOfIndependents() + " Leafs: " + bDM.getNumOfLeafs() +
                "Middles: " + bDM.getNumOfMiddle() + " Roots: " + bDM.getNumOfRoots() + ".");
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



