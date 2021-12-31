package FindCircleComponent;

import DataManager.BackDataManager;
import Graph.Target;
import errors.ErrorUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import java.util.List;
import java.util.Set;

public class FindCircleController {

    @FXML private ChoiceBox<String> targetList;
    @FXML private Button findCircleButton;
    @FXML private TextArea resultPrintArea;
    private BackDataManager bDM;
    private List<Target> targets;
    private Target curTarget;


    @FXML
    void findCircleAction(ActionEvent event) throws ErrorUtils {

        String tName = targetList.getValue(), res= "";

        if(tName != null) {
            this.curTarget = Target.getTargetByName(tName, targets);

            resultPrintArea.setDisable(false);

            try {
                res = this.bDM.findCircle(curTarget.getName());
            } catch (ErrorUtils e) {
                res = e.getMessage();
            }

            resultPrintArea.setText(res);
        }
    }

    public void initFindCircleController(BackDataManager bDM){

        this.bDM = bDM;

        this.initTargetListButton(this.bDM.getAllTargets());
    }

    public void initTargetListButton(List<Target> targets){

        this.targets = targets;

        Set<String> tNames = Target.getTargetNamesFrom(targets);
        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        targetList.setItems(data);
    }

}
