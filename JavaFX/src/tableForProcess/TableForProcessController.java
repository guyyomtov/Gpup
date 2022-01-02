package tableForProcess;

import Graph.Target;
import Graph.process.Minion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TableForProcessController {

    @FXML
    private TableView<Minion> tableForProcess;

    @FXML
    private TableColumn<Minion, String> targetNameColumn;

    @FXML
    private TableColumn<Minion, Target.Type> levelColumn;

    @FXML
    private TableColumn<Minion, String> statusColumn;


    public void initTable(List<Minion> minions){

        ObservableList<Minion> data =
                FXCollections.observableArrayList(minions);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("targetName")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<>("targetType")
        );
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("myStatus")
        );

        tableForProcess.setItems(data);

    }

}
