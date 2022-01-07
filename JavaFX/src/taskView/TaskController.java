package taskView;

import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import Flagger.Flagger;
import Graph.Target;
import Graph.process.DataSetupProcess;
import Graph.process.Minion;
import Graph.process.Task;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

    @FXML private GridPane gridPaneSettingTab;
    @FXML private RadioButton simulationButton;
    @FXML private RadioButton compilationButton;
    @FXML private Spinner<Integer> threadsSpinner;
    @FXML private RadioButton incrementalButton;
    @FXML private RadioButton fromScratchButton;
    @FXML private Label errorMessegeForIncremental;
    @FXML private Label infoForTarget;
    @FXML private Button updateTargetListButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Label summaryLabel;
    @FXML private Parent tableProcess;
    @FXML private TableForProcessController tableProcessController;
    @FXML private TextArea textArea;
    @FXML ProgressBar progressBar;
    @FXML private Label precentOfProgressBar;


    private SimpleIntegerProperty precentOfProgress;
    private SimpleStringProperty updateInfoForUI;
    private SimpleStringProperty targetInfo;
    private SimpleBooleanProperty isPause;

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
    private List<Minion> minions = new ArrayList<>();
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

    public TaskController(){
        precentOfProgress = new SimpleIntegerProperty(0);
        updateInfoForUI = new SimpleStringProperty();
        isPause = new SimpleBooleanProperty(false);
        targetInfo = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        infoForTarget.textProperty().bind(targetInfo);
    }

    @FXML
    void compilationButtonAction(ActionEvent event) {
        try {
            gridPaneSettingTab.getChildren().removeAll(simulationComponent);
            gridPaneSettingTab.add(summaryLabel, 0, 1);
        }catch (Exception e){}
    }

    @FXML
    void resumeButtonAction(ActionEvent event) {

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
    void startButtonAction(ActionEvent event) {
        if(minions.isEmpty())
            return;

        //else
        this.startButton.setDisable(true);
        this.resumeButton.setDisable(false);
        this.pauseButton.setDisable(false);
        this.bDM.startPro(minions);

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
