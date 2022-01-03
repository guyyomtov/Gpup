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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Set;

public class FindCircleController {

    @FXML private ChoiceBox<String> targetList;
    @FXML private Button findCircleButton;
    @FXML private TextArea resultPrintArea;
    private BackDataManager bDM;
    private List<Target> targets;
    private Target curTarget;
    private String backroundColor = new String();
    private Paint textColor;


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

    public void setSkins(String wantedColor, Paint textColor) {

        this.backroundColor = wantedColor;
        this.textColor = textColor;

        this.findCircleButton.setStyle(wantedColor);
        this.findCircleButton.setTextFill(textColor);
        this.targetList.setStyle(wantedColor);
    }

    @FXML
    void findCircleButtonMouseEnteredAction(MouseEvent event) {

        this.findCircleButton.setStyle(null);
        this.findCircleButton.setTextFill(Color.BLACK);
    }

    @FXML
    void findCircleButtonMouseExitedAction(MouseEvent event) {

        this.findCircleButton.setStyle(this.backroundColor);
        this.findCircleButton.setTextFill(this.textColor);
    }

    @FXML
    void tOnMOuseEntered(MouseEvent event) {

        this.targetList.setStyle(null);
    }

    @FXML
    void tOnMOuseExist(MouseEvent event) {

        this.targetList.setStyle(this.backroundColor);
    }
}
