package FindCircleComponent;

import AnimationComponent.SkinsUtils;
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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FindCircleController {

    @FXML private ChoiceBox<String> targetList;
    @FXML private Button findCircleButton;
    private List<Button> myButtons = new ArrayList<>();
    @FXML private TextArea resultPrintArea;
    private BackDataManager bDM;
    private List<Target> targets;
    private Target curTarget;
    private String backroundColor = new String();
    private Paint textColor;


    public void initFindCircleController(BackDataManager bDM){

        this.bDM = bDM;

        this.myButtons = Arrays.asList(this.findCircleButton);

        this.initTargetListButton(this.bDM.getAllTargets());
    }

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

     //   this.findCircleButton.setStyle(null);
      //  this.findCircleButton.setTextFill(Color.BLACK);
    }

    @FXML
    void findCircleButtonMouseExitedAction(MouseEvent event) {

      //  this.findCircleButton.setStyle(this.backroundColor);
        //this.findCircleButton.setTextFill(this.textColor);
    }

    @FXML
    void tOnMOuseEntered(MouseEvent event) {

       // this.targetList.setStyle(null);
    }

    @FXML
    void tOnMOuseExist(MouseEvent event) {

      //  this.targetList.setStyle(this.backroundColor);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        // set my buttons skins
        SkinsUtils.changeButtonColorTo(wantedColors, this.myButtons);

        // set my kids buttons skins
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
