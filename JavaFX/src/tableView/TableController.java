package tableView;

import DataManager.BackDataManager;
import Graph.Target;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
                new PropertyValueFactory<>("totalSerialNum")
        );

        tableView.setItems(data);
    }

    public TableView<Target> getTableView(){ return this.tableView;}
}
