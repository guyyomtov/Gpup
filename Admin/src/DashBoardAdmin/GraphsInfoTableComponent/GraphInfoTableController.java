package DashBoardAdmin.GraphsInfoTableComponent;

import api.HttpStatusUpdate;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import transferGraphData.GraphInfo;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class GraphInfoTableController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
    private HttpStatusUpdate httpStatusUpdate;
    private final IntegerProperty totalGraph;
    private BooleanProperty autoUpdate;
    @FXML private TableView<GraphInfo> graphInfoTable;
    @FXML private TableColumn<GraphInfo, String> graphNameCol;
    @FXML private TableColumn<GraphInfo, String> uploadedByCol;
    @FXML private TableColumn<GraphInfo, Integer> totalTargetsCol;
    @FXML private TableColumn<GraphInfo, Integer> independentCol;
    @FXML private TableColumn<GraphInfo, Integer> leafCol;
    @FXML private TableColumn<GraphInfo, Integer> middleCol;
    @FXML private TableColumn<GraphInfo, Integer> rootCol;
    @FXML private TableColumn<GraphInfo, String> pricesCol;
    @FXML private TableColumn<GraphInfo, RadioButton> checkBoxCol;
    private ToggleGroup toggleGroup = new ToggleGroup();
    private Map<String, RadioButton> nameOfGraphToRadioButton = new HashMap<>();
    public GraphInfoTableController(){
        autoUpdate = new SimpleBooleanProperty(true);
        totalGraph = new SimpleIntegerProperty();
    }

    public void setAutoUpdateToTrue(Boolean autoUpdate){
        this.autoUpdate.set(autoUpdate);
    }

    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
        this.httpStatusUpdate = httpStatusUpdate;
    }

    public void startGraphInfoRefresher() {
        listRefresher = new GraphListRefresher(
                autoUpdate,
                httpStatusUpdate::updateHttpLine,
                this::updateGraphsList);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    private void updateGraphsList(List<GraphInfo> graphInfoList) {
        Platform.runLater(() -> {
            ObservableList<GraphInfo> items = graphInfoTable.getItems();
            if(items.size() != graphInfoList.size()) {
                items.clear();
                items = FXCollections.observableArrayList(graphInfoList);
                items.stream().forEach((item)-> nameOfGraphToRadioButton.put(item.getName(), item.getRadioButton()));
                this.initToggleGroup();
                graphInfoTable.setItems(items);
                totalGraph.set(graphInfoList.size());
            }
        });
    }


    public void initTable(){

        graphNameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        uploadedByCol.setCellValueFactory(
                new PropertyValueFactory<>("byWhoUpload")
        );
        totalTargetsCol.setCellValueFactory(
                new PropertyValueFactory<>("totalTargets")
        );
        independentCol.setCellValueFactory(
                new PropertyValueFactory<>("totalIndependents")
        );
        leafCol.setCellValueFactory(
                new PropertyValueFactory<>("totalLeaf")
        );
        middleCol.setCellValueFactory(
                new PropertyValueFactory<>("totalMiddles")
        );
        rootCol.setCellValueFactory(
                new PropertyValueFactory<>("totalRoots")
        );
        pricesCol.setCellValueFactory(
                new PropertyValueFactory<>("taskInfo")
        );
        checkBoxCol.setCellValueFactory(
                new PropertyValueFactory<>("radioButton")
        );
    }

    private void initToggleGroup() {

        int i = 0;
         for(RadioButton currRadioButton : this.nameOfGraphToRadioButton.values()){
            currRadioButton.setToggleGroup(this.toggleGroup);
            if(i == 0)
                currRadioButton.setSelected(true);
            ++i;
        }
    }

    @Override
    public void close() throws IOException {


    }

    public int getSize() {
        return this.graphInfoTable.getItems().size();
    }

    public String getNameOfTheSelectedGraph() {
        String graphName = new String("");
       Toggle radioButton =  toggleGroup.getSelectedToggle();
        for(String name : this.nameOfGraphToRadioButton.keySet()){
            if(this.nameOfGraphToRadioButton.get(name).isPressed())
                graphName = name;
        }
        return graphName;
    }
}
