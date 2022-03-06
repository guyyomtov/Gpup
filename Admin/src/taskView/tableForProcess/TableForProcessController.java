package taskView.tableForProcess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.ExecuteTarget;

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


    public void initTable(List<ExecuteTarget> executeTargetList, TextArea textAreaForTargetInfo){

        ObservableList<ExecuteTarget> data =
                FXCollections.observableArrayList(executeTargetList);

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

        this.addListenerToRows(executeTargetList, textAreaForTargetInfo); // todo to see what object return to me the relevant data.

    }

    private void addListenerToRows(List<ExecuteTarget> executeTargetList, TextArea textAreaForTargetInfo) {
        this.tableForProcess.setRowFactory(tmp -> {
            TableRow<ExecuteTarget> row = new TableRow<>();
            // String nameOfMakeTask = row.getItem().getUploadedBy();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    ExecuteTarget executeTarget = row.getItem();
                    String status = executeTarget.getStatus();
                    String liveData = this.showLiveData(status, executeTarget);
                    textAreaForTargetInfo.setText(liveData);
                }
            });
            return row;
        });

    }

    private String showLiveData(String status, ExecuteTarget executeTarget) {
        String liveData = new String();
        liveData = this.makeRegularLiveData(executeTarget);
        switch (status){

            case "SKIPPED":
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                liveData += "skipped because: " + executeTarget.getISkippedBecause().toString() + "\n";

                break;

            case "WAITING":
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                break;

            case "IN PROCESS":
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                break;

            case "Not initialized":
                // do nothing
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                break;

            case "FROZEN":
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                break;

            default: // is finished
                liveData += "The worker " + executeTarget.getWorkerThatDoneMe() + " finished the job." + "\n";
                liveData+= "The status is : " + executeTarget.getStatus() + "\n";
                liveData += "Logs result: " + "\n" + executeTarget.getLogs();
                break;

        }
    return liveData;
    }

    private String makeRegularLiveData(ExecuteTarget executeTarget) {
        String liveData = new String();
        liveData += "Target name: " + executeTarget.getTargetName() + "\n";
        liveData+= "Type: " + executeTarget.getType() + "\n";
        return liveData;
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
