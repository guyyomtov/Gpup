package tableForProcess;

import Graph.Target;
import Graph.process.Minion;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class TableForProcessController {

    @FXML
    private TableView<Minion> tableForProcess;

    @FXML
    private TableColumn<Minion, String> targetNameColumn;

    @FXML
    private TableColumn<Minion, Target.Type> levelColumn;

    @FXML
    private TableColumn<Minion, StringProperty> statusColumn;


    public void initTable(List<Minion> minions, TextArea targetInfo){

        ObservableList<Minion> data =
                FXCollections.observableArrayList(minions);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<Minion, String>("targetName")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<Minion, Target.Type>("targetType")
        );
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<Minion, StringProperty>("status")
        );

        tableForProcess.setEditable(true);
        tableForProcess.setItems(data);
        this.addListener(targetInfo);

    }

    private void addListener(TextArea targetInfo) {

        tableForProcess.getSelectionModel().selectedItemProperty().addListener((v, oldV, newV) ->
                targetInfo.setText(tableForProcess.getSelectionModel().getSelectedItem()
                        .getMinionLiveData().getMinionLiveDataToString()));

    }

    public List<Minion> getMinionsFromTable(){
        return new ArrayList<>(this.tableForProcess.getItems());
    }


}
