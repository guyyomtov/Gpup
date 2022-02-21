package taskView.tableForProcess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.TargetInfo;

import java.util.ArrayList;
import java.util.List;

public class TableForProcessController {

    @FXML
    private TableView<TargetInfo> tableForProcess;

    @FXML
    private TableColumn<TargetInfo, String> targetNameColumn;

    @FXML
    private TableColumn<TargetInfo, String> levelColumn;

    @FXML
    private TableColumn<TargetInfo, String> statusColumn;


    public void initTable(List<TargetInfo> targetInfoList, TextArea textAreaForTargetInfo){

        ObservableList<TargetInfo> data =
                FXCollections.observableArrayList(targetInfoList);

        targetNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        levelColumn.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        tableForProcess.setEditable(true);
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

    public List<TargetInfo> getMinionsFromTable(){
        return new ArrayList<>(this.tableForProcess.getItems());
    }


}
