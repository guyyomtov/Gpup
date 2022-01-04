package FindPathComponent;

        import DataManager.BackDataManager;
        import Graph.Target;
        import errors.ErrorUtils;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.paint.Color;
        import javafx.scene.paint.Paint;

        import java.util.*;

public class FindPathController {

    @FXML private ChoiceBox<String> srcTargetButton;
    @FXML private ChoiceBox<String> dstTargetButton;
    @FXML private ChoiceBox<String> relationshipButton;
    @FXML private Button findPathButton;
    @FXML private TableView<String> pathsTableView = new TableView<>();
    @FXML private TableColumn<String, String> pathsColum;
    private BackDataManager bDM;
    private List<Target> targets;
    private List<String> resPaths = new ArrayList<>();
    private String backroundColor = new String();
    private Paint textColor;


    @FXML
    void findPathAction(ActionEvent event) throws ErrorUtils {

        this.initDataNeededForPath();

        this.initViewPathTable();
    }

    private void initViewPathTable() {

        ObservableList<String> details = FXCollections.observableArrayList(this.resPaths);

        this.pathsColum.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        this.pathsTableView.setItems(details);
    }

    private void initDataNeededForPath() throws ErrorUtils {

        String srcV = this.srcTargetButton.getValue(), dstV = this.dstTargetButton.getValue(), relV = this.relationshipButton.getValue();

        if(srcV != null && !srcV.isEmpty() && dstV != null && !dstV.isEmpty() && relV != null && !relV.isEmpty()){

            String relationShipNeededSyntax = (relV == "Depends On" ? "D" : "R");

            this.initResPaths(this.bDM.getPathFromTargets(srcV, dstV, relationShipNeededSyntax));
        }
    }

    private void initResPaths(String pathFromTargets) {

        List<String> tmp = new ArrayList<>();
        String arrowString = new String();

        if(!pathFromTargets.isEmpty() && pathFromTargets != null) {

            // separate by comas
            tmp = Arrays.asList(pathFromTargets.split(","));

            for(Integer i = 0; i < tmp.size(); i++){

                arrowString = tmp.get(i).trim();

                arrowString = arrowString.replaceAll(" ", "-->");

                this.resPaths.add(arrowString);

            }

            //this.resPaths.addAll(tmp);
        }
    }

    public void init(BackDataManager other){

        this.bDM = other;
        this.targets = other.getAllTargets();

        this.initTargetsButtons();
        this.initRelationshipButton();
    }

    private void initRelationshipButton() {

        List<String> relations = Arrays.asList("Depends On", "Required For");

        ObservableList<String> data = FXCollections.observableArrayList(relations);

        relationshipButton.setItems(data);
    }

    private void initTargetsButtons() {

        Set<String> tNames = Target.getTargetNamesFrom(this.targets);

        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        this.srcTargetButton.setItems(data);
        this.dstTargetButton.setItems(data);
    }

    public void setSkins(String wantedColor, Paint textColor) {

        this.backroundColor = wantedColor;
        this.textColor = textColor;

        this.findPathButton.setStyle(wantedColor);
        this.findPathButton.setTextFill(textColor);
        this.srcTargetButton.setStyle(wantedColor);
        this.dstTargetButton.setStyle(wantedColor);
        this.relationshipButton.setStyle(wantedColor);
    }

    @FXML
    void destOMExist(MouseEvent event) {

        this.dstTargetButton.setStyle(this.backroundColor);
    }

    @FXML
    void destTOMEnter(MouseEvent event) {

        this.dstTargetButton.setStyle(null);
    }

    @FXML
    void findOMExist(MouseEvent event) {

        this.findPathButton.setTextFill(this.textColor);
        this.findPathButton.setStyle(this.backroundColor);
    }

    @FXML
    void findTOMEnter(MouseEvent event) {

        this.findPathButton.setStyle(null);
        this.findPathButton.setTextFill(Color.BLACK);
    }

    @FXML
    void relOMExist(MouseEvent event) {

        this.relationshipButton.setStyle(this.backroundColor);
    }

    @FXML
    void relTOMEnter(MouseEvent event) {

        this.relationshipButton.setStyle(null);
    }

    @FXML
    void srcOMExist(MouseEvent event) {

        this.srcTargetButton.setStyle(this.backroundColor);
    }

    @FXML
    void srcTOMEnter(MouseEvent event) {

        this.srcTargetButton.setStyle(null);
    }

}

