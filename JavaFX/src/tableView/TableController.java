package tableView;

import DataManager.BackDataManager;
import Graph.Target;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TableController {

    @FXML private TableView<Target> tableView;
    @FXML private TableColumn<Target, String> targetNameColumn;
    @FXML private TableColumn<Target, Target.Type> levelColumn;
    @FXML private TableColumn<Target, Integer> dependsOnColumn;
    @FXML private TableColumn<Target, Integer> requiredForColumn;
    @FXML private TableColumn<Target, String> infoColumn;
    @FXML private TableColumn<Target, Integer> serialSetColumn;
    @FXML private TableColumn<Target, CheckBox> selectColumn;
    @FXML private CheckBox selectAllCheckBox;
    private GraphInfoController graphInfoController;

    @FXML
    void selectAllCheckBoxAction(ActionEvent event) {

        List<Target> targets = this.graphInfoController.getbDM().getAllTargets();

        if(this.selectAllCheckBox.isSelected()) {
            for(Target target : targets)
                target.getRemark().setSelected(true);
        }
        else
            for(Target target : targets)
                target.getRemark().setSelected(false);

    }

    public void initTable(List<Target> targets) {
        ObservableList<Target> data =
                FXCollections.observableArrayList(targets);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<>("targetType")
        );
        dependsOnColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalDependsOn")
        );
        requiredForColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalRequiredFor")
        );
        infoColumn.setCellValueFactory(
                new PropertyValueFactory<>("generalInfo")
        );
        serialSetColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalSerialSets")
        );

        selectColumn.setCellValueFactory(
                new PropertyValueFactory<>("remark")
        );

        tableView.setItems(data);

        this.makeColumsSurtable();
     /*   levelColumn.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(levelColumn);
        tableView.sort();
        levelColumn.setSortType(TableColumn.SortType.DESCENDING);*/
        this.initActionOnCheckBoxes(targets);
    }

    private void makeColumsSurtable() {

        this.targetNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.targetNameColumn.setSortType(TableColumn.SortType.DESCENDING);

        this.levelColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.levelColumn.setSortType(TableColumn.SortType.DESCENDING);

        this.dependsOnColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.dependsOnColumn.setSortType(TableColumn.SortType.DESCENDING);

        this.requiredForColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.requiredForColumn.setSortType(TableColumn.SortType.ASCENDING);

        this.serialSetColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.serialSetColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    private void initActionOnCheckBoxes(List<Target> targets) {
        for(Target target : targets){
            CheckBox currCheckBox = target.getRemark();
            currCheckBox.setOnAction(event -> checkBoxAction(target));
        }
    }
    //this action only for if what if selected.
    private void checkBoxAction(Target target){

        boolean whatIfSelected = graphInfoController.getWhatIfCheckBox().isSelected();
        boolean dependsOnSelected = graphInfoController.getDependsOnRadioButton().isSelected();
        boolean isAlreadySelected = target.getRemark().isSelected();
        if(!isAlreadySelected)
            return;
        if(whatIfSelected){
            Set<List<Target>> allPath;
            try {
                if (dependsOnSelected)
                    allPath = this.graphInfoController.getbDM().whatIf(target.getName(), "D");
                else // its requiredFor
                    allPath = this.graphInfoController.getbDM().whatIf(target.getName(), "R");
                this.updateCheckBoxesWith(allPath);
            }catch(ErrorUtils e){}
        }

    }

    private void updateCheckBoxesWith(Set<List<Target>> allPath) {

        for(List<Target> path : allPath)
            path.forEach(target -> target.getRemark().setSelected(true));

    }

    public TableView<Target> getTableView(){ return this.tableView;}

    public void setGraphInfoController(GraphInfoController graphInfoController) {
        this.graphInfoController = graphInfoController;
    }
}
