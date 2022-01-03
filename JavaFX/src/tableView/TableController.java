package tableView;

import DataManager.BackDataManager;
import Graph.Target;
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

public class TableController {

    @FXML
    private TableView<Target> tableView;

    @FXML
    private TableColumn<Target, String> targetNameColumn;

    @FXML
    private TableColumn<Target, Target.Type> levelColumn;

    @FXML
    private TableColumn<Target, Integer> dependsOnColumn;

    @FXML
    private TableColumn<Target, Integer> requiredForColumn;

    @FXML
    private TableColumn<Target, String> infoColumn;

    @FXML
    private TableColumn<Target, Integer> serialSetColumn;

    @FXML
    private TableColumn<Target, CheckBox> selectColumn;

    @FXML
    private CheckBox selectAllCheckBox;

    private List<Target> targets = new ArrayList<>();

    @FXML
    void selectAllCheckBoxAction(ActionEvent event) {
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
    }

    public TableView<Target> getTableView(){ return this.tableView;}

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }
}
