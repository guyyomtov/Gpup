package tableView;

import Graph.Target;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.TargetInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TableController {

    @FXML
    private TableView<TargetInfo> tableView;
    @FXML
    private TableColumn<TargetInfo, String> targetNameColumn;
    @FXML
    private TableColumn<TargetInfo, String> levelColumn;
    @FXML
    private TableColumn<TargetInfo, Integer> dependsOnColumn;
    @FXML
    private TableColumn<TargetInfo, Integer> requiredForColumn;
    @FXML
    private TableColumn<TargetInfo, String> infoColumn;
    @FXML
    private TableColumn<TargetInfo, CheckBox> selectColumn;
    @FXML
    private CheckBox selectAllCheckBox;
    private GraphInfoController graphInfoController;
    // private List<CheckBox> checkBoxes  = new ArrayList<>();

    @FXML
    void selectAllCheckBoxAction(ActionEvent event) {

        if (this.selectAllCheckBox.isSelected()) {
            for (TargetInfo targetInfo : this.tableView.getItems())
                targetInfo.getCheckBox().setSelected(true);
        } else {
            for (TargetInfo targetInfo : this.tableView.getItems())
                targetInfo.getCheckBox().setSelected(false);
        }

    }

    public void initTable(List<TargetInfo> targetsInfo) {
        ObservableList<TargetInfo> data =
                FXCollections.observableArrayList(targetsInfo);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );
        dependsOnColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalDependsOn")
        );
        requiredForColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalRequiredFor")
        );
        infoColumn.setCellValueFactory(
                new PropertyValueFactory<>("information")
        );

        selectColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkBox")
        );

        this.initCheckBox(data);
        tableView.setItems(data);

        this.makeColumsSurtable();
     /*   levelColumn.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(levelColumn);
        tableView.sort();
        levelColumn.setSortType(TableColumn.SortType.DESCENDING);*/
        //this.initActionOnCheckBoxes(targets);
    }

    private void initCheckBox(ObservableList<TargetInfo> data) {
        data.stream().forEach((targetInfo) -> targetInfo.getCheckBox());
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

    }

//    private void initActionOnCheckBoxes() {
//        for(CheckBox currCheckBox : this.checkBoxes){
//            currCheckBox.setOnAction(event -> checkBoxAction(target));
//        }
//    }
    //this action only for if what if selected.
//    private void checkBoxAction(Target target){
//
//        boolean whatIfSelected = graphInfoController.getWhatIfCheckBox().isSelected();
//        boolean dependsOnSelected = graphInfoController.getDependsOnRadioButton().isSelected();
//        boolean isAlreadySelected = target.getRemark().isSelected();
//        if(!isAlreadySelected)
//            return;
//        if(whatIfSelected){
//            Set<List<Target>> allPath;
//            try {
//                if (dependsOnSelected)
//                    allPath = this.graphInfoController.getbDM().whatIf(target.getName(), "D");
//                else // its requiredFor
//                    allPath = this.graphInfoController.getbDM().whatIf(target.getName(), "R");
//                this.updateCheckBoxesWith(allPath);
//            }catch(ErrorUtils e){}
//        }
//
//    }

    private void updateCheckBoxesWith(Set<List<Target>> allPath) {

        for (List<Target> path : allPath)
            path.forEach(target -> target.getRemark().setSelected(true));

    }

    public TableView<TargetInfo> getTableView() {
        return this.tableView;
    }

    public void setGraphInfoController(GraphInfoController graphInfoController) {
        this.graphInfoController = graphInfoController;
    }

    public List<TargetInfo> getTargetInfoThatUserSelected() {
        List<TargetInfo> targetsSelectedByUser = new ArrayList<>();
        this.tableView.getItems().forEach((item) -> {
            if (item.getCheckBox().isSelected()) {
                TargetInfo targetInfo = new TargetInfo(item);
                targetsSelectedByUser.add(targetInfo);
            }
        });
        return targetsSelectedByUser;
    }

}
