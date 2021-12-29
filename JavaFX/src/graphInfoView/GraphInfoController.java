package graphInfoView;

import DataManager.BackDataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane tableComponent;

    @FXML
    private TableController tableComponentController;

    private BackDataManager bDM = new BackDataManager()

    public void initGraphInfo(){

        this.tableComponentController.initTable(this.bDM.getAllTargets());
        this.initSummary();;
    }

    private void initSummary() {
        summaryTable.setText("Total targets: " + bDM.getAllTargets().size()
        );
        summaryBy.setText(    "Independents: " + bDM.getNumOfIndependents() + " Leafs: " + bDM.getNumOfLeafs() +
                "Middles: " + bDM.getNumOfMiddle() + " Roots: " + bDM.getNumOfRoots() + ".");
    }

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }
}



