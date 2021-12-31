package WhatIfComponent;

        import DataManager.BackDataManager;
        import Graph.Target;
        import errors.ErrorUtils;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;


        import java.util.Arrays;
        import java.util.List;
        import java.util.Set;

public class WhatIfController {

    @FXML private ChoiceBox<String> targetListButton;
    @FXML private ChoiceBox<String> dependenciesType;
    @FXML private Button findButton;
    @FXML private TableColumn<Target, String> resTable;
    @FXML private TableView<Target> tableView;
    private BackDataManager bDM;
    private List<Target> targets;
    private Set<List<Target>> resTargets;


    @FXML
    void findButtonAction(ActionEvent event) throws ErrorUtils {

        this.initFindButtonNeededData();

        tableView.setDisable(false);

        this.initResTable();
    }

    private void initResTable() {

        ObservableList<Target> data = FXCollections.observableArrayList(this.targets);

        resTable.setCellValueFactory(
                new PropertyValueFactory<>("Name")
        );

        tableView.setItems(data);
    }

    private void initFindButtonNeededData() throws ErrorUtils {

        if(dependenciesType.getValue() != null && !dependenciesType.getValue().isEmpty() && targetListButton.getValue() != null && !targetListButton.getValue().isEmpty()) {

            String relationshipType = dependenciesType.getValue() == "Depends On" ? "D" : "R";

            this.resTargets = this.bDM.whatIf(targetListButton.getValue(), relationshipType);
        }
    }

    public void init(BackDataManager other){

        this.bDM = other;
        this.targets = other.getAllTargets();

        this.initTargetListButton();
        this.initDependenciesTypeButton();
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
}
