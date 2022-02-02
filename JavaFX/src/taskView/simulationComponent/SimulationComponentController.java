package taskView.simulationComponent;

import AnimationComponent.SkinsUtils;
import errors.ErrorUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationComponentController {

    @FXML private Spinner<Integer> chooseTimeSpinner;
    @FXML private RadioButton randomButton;
    @FXML private RadioButton manualButton;
    private List<RadioButton> radioButtons = new ArrayList<>();
    @FXML
    private Slider succsesSpinner;

    @FXML
    private Label succesLabel;

    @FXML
    private Slider warningSpinner;

    @FXML
    private Label warningLabel;




    public void initSimulation(){
        this.initTimeSpinner();
        this.succsesSpinner.valueProperty().addListener((v, oldV, newV) -> succesLabel.setText(String.valueOf((int)this.succsesSpinner.getValue())));
        this.warningSpinner.valueProperty().addListener((v, oldV, newV) -> warningLabel.setText(String.valueOf((int)this.warningSpinner.getValue())));
        this.radioButtons = Arrays.asList(randomButton, this.manualButton);
    }

    @FXML
    void manualButtonAction(ActionEvent event) {
        succsesSpinner.setDisable(false);
        warningSpinner.setDisable(false);
    }

    @FXML
    void randomButtonAction(ActionEvent event) {
        succsesSpinner.setDisable(false);
        warningSpinner.setDisable(false);
    }

    private void initSpinner(Slider slider) {
       slider.valueProperty().addListener((v, oldV, newV) -> succesLabel.setText(slider.toString()));
    }


    public void initTimeSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0, 500);
        valueFactory.setValue(0);
        this.chooseTimeSpinner.setValueFactory(valueFactory);
    }
    @FXML
    void onDragDoneChooseTime(DragEvent event) {
        this.chooseTimeSpinner.increment(1000);
    }

    public Integer getMaxTime(){return this.chooseTimeSpinner.getValue();}

    public Integer getChancesToSuccess(){
        Integer res = (int)this.succsesSpinner.getValue();
        return res;
    }

    public Integer getChancesToSuccessWithWarning(){
        Integer res = (int)this.warningSpinner.getValue();
        return res;
    }

    public boolean getIfRandom(){return this.randomButton.isSelected();}

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        this.setButtonsColors(enumWantedColor);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        SkinsUtils.changeRadioButtonTextColorTo(wantedColors, this.radioButtons);
    }
}
