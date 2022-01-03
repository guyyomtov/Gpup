package taskView;

import DataManager.BackDataManager;
import Graph.Target;
import Graph.process.Minion;
import Graph.process.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import tableForProcess.TableForProcessController;
import tableView.TableController;
import taskView.simulationComponent.SimulationComponentController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskController {

    @FXML
    private GridPane gridPaneSettingTab;

    @FXML
    private RadioButton simulationButton;

    @FXML
    private RadioButton compilationButton;

    @FXML
    private Spinner<Integer> threadsSpinner;

    @FXML
    private RadioButton incrementalButton;

    @FXML
    private RadioButton fromScratchButton;

    @FXML
    private Label errorMessegeForIncremental;

    @FXML
    private Text infoForTarget;

    @FXML
    private Button updateTargetListButton;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Label summaryLabel;

    @FXML
    private Parent tableProcess;

    @FXML
    private TableForProcessController tableProcessController;


    private BackDataManager bDM;

    private SimulationComponentController simulationComponentController;

    private Parent simulationComponent;


    @FXML
    void compilationButtonAction(ActionEvent event) {
        try {
            gridPaneSettingTab.getChildren().removeAll(simulationComponent);
            gridPaneSettingTab.add(summaryLabel, 0, 1);
        }catch (Exception e){}
    }

    @FXML
    void pauseButtonAction(ActionEvent event) {

    }

    @FXML
    void simulationButtonAction(ActionEvent event) {
        try {
            gridPaneSettingTab.getChildren().removeAll(summaryLabel);
            gridPaneSettingTab.add(simulationComponent, 0, 1);
        }catch (Exception e){}
    }

    @FXML
    void startButtonAction(ActionEvent event) {

    }

    @FXML
    void updateTargetListButtonAction(ActionEvent event) {

        List<Target> targets = this.bDM.getAllTargets();
        List<Minion> minions = new ArrayList<>();
        Integer maxTime = this.simulationComponentController.getMaxTime();
        Integer chancesISucceed = this.simulationComponentController.getChancesToSuccess();
        Integer chancesImAWarning = this.simulationComponentController.getChancesToSuccessWithWarning();
        boolean isRand = this.simulationComponentController.getIfRandom();


        for(Target target : targets){
            if(target.getRemark().isSelected())
            {
                minions.add(new Minion(target, maxTime, chancesISucceed, chancesImAWarning, isRand));
            }
        }

        tableProcessController.initTable(minions);

    }

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }

    public void initTaskView()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulationComponent/simulationComponentFxml.fxml"));
            simulationComponent = loader.load();
            simulationComponentController = loader.getController();
            simulationComponentController.initSimulation();
            this.initThreadsSpinner();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initThreadsSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Task.maxParallelism, 1);
        this.threadsSpinner.setValueFactory(valueFactory);
    }




}
