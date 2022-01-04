package taskView.simulationComponent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.DragEvent;

import java.awt.event.KeyEvent;

public class SimulationComponentController {

    @FXML
    private Spinner<Integer> chooseTimeSpinner;

    @FXML
    private RadioButton randomButton;

    @FXML
    private RadioButton manualButton;

    @FXML
    private Spinner<Integer> succsesSpinner;

    @FXML
    private Spinner<Integer> warningSpinner;




    @FXML
    void manualButtonAction(ActionEvent event) {
        succsesSpinner.setDisable(false);
        warningSpinner.setDisable(false);
    }

    @FXML
    void randomButtonAction(ActionEvent event) {
        succsesSpinner.setDisable(true);
        warningSpinner.setDisable(true);
    }


    public void initSimulation(){
      this.initTimeSpinner();
      this.initSpinner(this.succsesSpinner);
      this.initSpinner(this.warningSpinner);
    }

    private void initSpinner(Spinner<Integer> spinner) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100,0,5);
        spinner.setValueFactory(valueFactory);
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

    public Integer getChancesToSuccess(){return this.succsesSpinner.getValue();}

    public Integer getChancesToSuccessWithWarning(){return this.warningSpinner.getValue();}

    public boolean getIfRandom(){return this.randomButton.isSelected();}
}