package taskView;

import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import Flagger.Flagger;
import Graph.Target;
import Graph.process.DataSetupProcess;
import Graph.process.Minion;
import Graph.process.Task;
import errors.ErrorUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TaskController {

    private List<Button> buttons = new ArrayList<>();
    @FXML private Button updateTargetListButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private GridPane gridPaneSettingTab;
    @FXML private RadioButton simulationButton;
    @FXML private RadioButton compilationButton;
    @FXML private Spinner<Integer> threadsSpinner;
    @FXML private RadioButton incrementalButton;
    @FXML private RadioButton fromScratchButton;
    @FXML private Label errorMessegeForIncremental;
    @FXML private Text infoForTarget;
    @FXML private Label summaryLabel;
    @FXML private Parent tableProcess;
    @FXML private TableForProcessController tableProcessController;
    private BackDataManager bDM;
    private SimulationComponentController simulationComponentController;
    private Parent simulationComponent;
    private List<Minion> minions = new ArrayList<>();



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

        this.buttons = Arrays.asList(this.pauseButton, this.startButton,
                this.updateTargetListButton);
    }


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
    void startButtonAction(ActionEvent event) throws ErrorUtils {

        /* ---------- EXAMPLE PROCESS FROM --------------
        // start flagger (which is a MUST part of dSP)
        Flagger flagger = new Flagger().builder()
                .processIsSimulation(true)
                .processFromScratch(true)
                .chancesIsRandomInProcess(true);

        //start more needed data
        DataSetupProcess dSP = new DataSetupProcess().builder()
                .flagger(flagger)
                .timeToRun(2);

        this.bDM.startProcess(dSP);
         */

        /* ---------- EXAMPLE PROCESS FOR RANDOM USER TARGETS --------------

        // start flagger (which is a MUST part of dSP)
        Flagger flagger = new Flagger().builder()
                .processIsSimulation(true)
                .processFromRandomTargets(true)
                .chancesIsRandomInProcess(true);

        //start more needed data
        DataSetupProcess dSP = new DataSetupProcess().builder()
                .flagger(flagger)
                .timeToRun(2);

        if(!this.minions.isEmpty())
            dSP.minionsChoosenByUser(this.minions);

        this.bDM.startProcess(dSP);

         */
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors){

        SkinsUtils.changeButtonColorTo(wantedColors, this.buttons);
    }

    @FXML
    void updateTargetListButtonAction(ActionEvent event) {

        List<Target> targets = this.bDM.getAllTargets();
        Integer maxTime = this.simulationComponentController.getMaxTime();
        Integer chancesISucceed = this.simulationComponentController.getChancesToSuccess();
        Integer chancesImAWarning = this.simulationComponentController.getChancesToSuccessWithWarning();
        boolean isRand = this.simulationComponentController.getIfRandom();

        for(Target target : targets){
            if(target.getRemark().isSelected())
            {
                minions.add(new Minion(target, maxTime, chancesISucceed, chancesImAWarning));
            }
        }
        tableProcessController.initTable(minions);
    }

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }


    private void initThreadsSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Task.maxParallelism, 1);
        this.threadsSpinner.setValueFactory(valueFactory);
    }




}
