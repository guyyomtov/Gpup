package WhatIfComponent;

        import AnimationComponent.SkinsUtils;
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


        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Set;

public class WhatIfController {

    @FXML private ChoiceBox<String> targetListButton;
    @FXML private ChoiceBox<String> dependenciesType;
    @FXML private Button findButton;
    private List<Button> buttons = new ArrayList<>();
    @FXML private TableColumn<String, String> resTable;
    @FXML private TableView<String> tableView;
    private BackDataManager bDM;
    private List<Target> targets;
    private Set<List<Target>> resTargets;
    private String backroundColor = new String();
    private Paint textColor;
    private List<String> resPaths = new ArrayList<>();


    public void init(BackDataManager other){

        // remove extra unneeded column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.bDM = other;
        this.targets = other.getAllTargets();

        this.buttons = Arrays.asList(this.findButton);

        this.initTargetListButton();
        this.initDependenciesTypeButton();
    }

    @FXML
    void findButtonAction(ActionEvent event) throws ErrorUtils {

        this.initFindButtonNeededData();

        tableView.setDisable(false);

        this.convertToStringPath();

        this.initResTable();
    }

    private void convertToStringPath() {

        this.resPaths.clear();
        for(List<Target> targets : this.resTargets ) {
            String path = new String();
            if(dependenciesType.getValue().equals("Depends On")) {
                for (int i = 0; i < targets.size(); i++) {
                    path += targets.get(i).getName();
                    if (i != targets.size() - 1)
                        path += "-->";
                }
            }

            else{ // print it as required for should be
                for (int i = targets.size() - 1; i >=0 ; i--) {
                    path += targets.get(i).getName();
                    if (i != 0)
                        path += "-->";
                }
            }
            this.resPaths.add(path);
        }

    }

    private void initResTable() {

        ObservableList<String> dataL = FXCollections.observableArrayList(this.resPaths);

        this.resTable.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        tableView.setItems(dataL);
    }



    private void initFindButtonNeededData() throws ErrorUtils {

        if(dependenciesType.getValue() != null && !dependenciesType.getValue().isEmpty() && targetListButton.getValue() != null && !targetListButton.getValue().isEmpty()) {

            String relationshipType = dependenciesType.getValue() == "Depends On" ? "D" : "R";

            this.resTargets = this.bDM.whatIf(targetListButton.getValue(), relationshipType);

        }
    }

    private void initDependenciesTypeButton() {

        List<String> relations = Arrays.asList("Depends On", "Required For");

        ObservableList<String> data = FXCollections.observableArrayList(relations);

        dependenciesType.setItems(data);
    }

    private void initTargetListButton() {

        Set<String> tNames = Target.getTargetNamesFrom(this.targets);

        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        targetListButton.setItems(data);
    }

    public void setSkins(String wantedColor, Paint textColor) {

        this.backroundColor = wantedColor;
        this.textColor = textColor;

        this.dependenciesType.setStyle(wantedColor);
        this.targetListButton.setStyle(wantedColor);
        this.findButton.setStyle(wantedColor);
        this.findButton.setTextFill(textColor);
    }


    @FXML
    void findOnMOuseEntered(MouseEvent event) {

      //  this.findButton.setTextFill(Color.BLACK);
      //  this.findButton.setStyle(null);
    }

    @FXML
    void findOnMOuseExist(MouseEvent event) {

      //  this.findButton.setStyle(this.backroundColor);
      //  this.findButton.setTextFill(this.textColor);
    }

    @FXML
    void relOnMOuseEntered(MouseEvent event) {

       // this.dependenciesType.setStyle(null);
    }

    @FXML
    void relOnMOuseExist(MouseEvent event) {

       //  this.dependenciesType.setStyle(this.backroundColor);
    }

    @FXML
    void tOnMOuseEntered(MouseEvent event) {

//        this.targetListButton.setStyle(null);
    }
    @FXML
    void tOnMOuseExist(MouseEvent event) {

      //  this.targetListButton.setStyle(this.backroundColor);
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
