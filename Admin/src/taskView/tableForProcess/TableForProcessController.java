package taskView.tableForProcess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.ExecuteTarget;
import transferGraphData.TargetInfo;

import java.util.ArrayList;
import java.util.List;

public class TableForProcessController {

    @FXML
    private TableView<ExecuteTarget> tableForProcess;

    @FXML
    private TableColumn<ExecuteTarget, String> targetNameColumn;

    @FXML
    private TableColumn<ExecuteTarget, String> levelColumn;

    @FXML
    private TableColumn<ExecuteTarget, String> statusColumn;


    public void initTable(List<ExecuteTarget> targetInfoList, TextArea textAreaForTargetInfo){

        ObservableList<ExecuteTarget> data =
                FXCollections.observableArrayList(targetInfoList);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("targetName")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        tableForProcess.setEditable(true);
        tableForProcess.getItems().clear();
        tableForProcess.setItems(data);

     //   this.addListener(targetInfo); // todo to see what object return to me the relevant data.

    }

//    private void addListener(TextArea targetInfo) {
//
//        taskView.tableForProcess.getSelectionModel().selectedItemProperty().addListener((v, oldV, newV) ->
//                targetInfo.setText(taskView.tableForProcess.getSelectionModel().getSelectedItem()
//                        .getMinionLiveData().getMinionLiveDataToString()));
//
//    }

    public List<ExecuteTarget> getMinionsFromTable(){
        return new ArrayList<>(this.tableForProcess.getItems());
    }


}
