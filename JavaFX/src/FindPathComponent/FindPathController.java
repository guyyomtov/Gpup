package FindPathComponent;

        import AnimationComponent.SkinsUtils;
        import DataManager.BackDataManager;
        import Graph.Target;
        import errors.ErrorUtils;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
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
    private List<Button> buttons = new ArrayList<>();
    @FXML private ListView<String> pathListView;
    @FXML private Label thereIsNoPathMessege;
    private BackDataManager bDM;
    private List<Target> targets;
    private List<String> resPaths = new ArrayList<>();
    private String backroundColor = new String();
    private Paint textColor;



    public void init(BackDataManager other){

        this.bDM = other;
        this.targets = other.getAllTargets();

        this.buttons = Arrays.asList(this.findPathButton);

        this.initTargetsButtons();
        this.initRelationshipButton();
    }

    @FXML
    void findPathAction(ActionEvent event) throws ErrorUtils {

        this.initDataNeededForPath();

        this.initViewPathTable();
    }

    private void initViewPathTable() {

       // this.pathsTableView.getSelectionModel().selectAll();

        ObservableList<String> details = FXCollections.observableArrayList(this.resPaths);

      //  this.pathsColum.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        this.pathListView.setItems(details);
    }

    private void initDataNeededForPath() throws ErrorUtils {

        // clean last data
        this.resPaths.clear();

        String srcV = this.srcTargetButton.getValue(), dstV = this.dstTargetButton.getValue(), relV = this.relationshipButton.getValue();

        if(srcV != null && !srcV.isEmpty() && dstV != null && !dstV.isEmpty() && relV != null && !relV.isEmpty()){

            String relationShipNeededSyntax = (relV == "Depends On" ? "D" : "R");
            String allPath = this.bDM.getPathFromTargets(srcV, dstV, relationShipNeededSyntax);

            if(allPath.equals("")){

                this.resPaths.clear();
                ErrorUtils.makeJavaFXCutomAlert("There is no path between " + srcV + " to " + dstV + "\n" +  " with the given connection " + this.relationshipButton.getValue());
                return;
            }
            else {
                this.thereIsNoPathMessege.setText("");
                this.initResPaths(allPath);
            }
        }
    }

    private void initResPaths(String pathFromTargets) {

        List<String> tmp = new ArrayList<>();
        String arrowString = new String();
        this.resPaths.clear();

        if(!pathFromTargets.isEmpty() && pathFromTargets != null) {

            // separate by comas
            tmp = Arrays.asList(pathFromTargets.split(","));

                for (Integer i = 0; i < tmp.size(); i++) {

                    arrowString = tmp.get(i).trim();

                    if(this.relationshipButton.getValue().equals("Required For"))
                        arrowString = this.reverseString(arrowString);

                    arrowString = arrowString.replaceAll(" ", "-->");

                    this.resPaths.add(arrowString);
                }
            //this.resPaths.addAll(tmp);
        }
    }

    private String reverseString(String arrowString) {
        char ch;
        String res = new String();
        for (int i=0; i < arrowString.length(); i++)
        {
            ch = arrowString.charAt(i); //extracts each character
            res= ch + res; //adds each character in front of the existing string
        }

        return res;
    }

    private void initRelationshipButton() {

        List<String> relations = Arrays.asList("Depends On", "Required For");

        ObservableList<String> data = FXCollections.observableArrayList(relations);

        relationshipButton.setItems(data);
        relationshipButton.setValue(relations.get(0));
    }

    private void initTargetsButtons() {

        Set<String> tNames = Target.getTargetNamesFrom(this.targets);

        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        this.srcTargetButton.setItems(data);
        this.dstTargetButton.setItems(data);
        if(data.size() > 2) {
            this.srcTargetButton.setValue(data.get(0));
            this.dstTargetButton.setValue(data.get(1));
        }
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

        //this.dstTargetButton.setStyle(this.backroundColor);
    }

    @FXML
    void destTOMEnter(MouseEvent event) {

      //  this.dstTargetButton.setStyle(null);
    }

    @FXML
    void findOMExist(MouseEvent event) {

      //  this.findPathButton.setTextFill(this.textColor);
      //  this.findPathButton.setStyle(this.backroundColor);
    }

    @FXML
    void findTOMEnter(MouseEvent event) {

      //  this.findPathButton.setStyle(null);
       //  this.findPathButton.setTextFill(Color.BLACK);
    }

    @FXML
    void relOMExist(MouseEvent event) {

       // this.relationshipButton.setStyle(this.backroundColor);
    }

    @FXML
    void relTOMEnter(MouseEvent event) {

      //  this.relationshipButton.setStyle(null);
    }

    @FXML
    void srcOMExist(MouseEvent event) {

     //   this.srcTargetButton.setStyle(this.backroundColor);
    }

    @FXML
    void srcTOMEnter(MouseEvent event) {

     //   this.srcTargetButton.setStyle(null);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        SkinsUtils.changeButtonColorTo(wantedColors, this.buttons);
    }

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {


        //set buttons colors
        this.setButtonsColors(enumWantedColor);

        // set my label skins

        //set background colors
        this.setBackRoundColors(enumWantedColor);

        // set my kids skins
    
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {
    }
}

