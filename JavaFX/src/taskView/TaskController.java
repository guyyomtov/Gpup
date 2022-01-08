package taskView;

import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import Flagger.Flagger;
import Graph.Target;
import Graph.process.DataSetupProcess;
import Graph.process.Minion;
import Graph.process.Task;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import tableForProcess.TableForProcessController;
import tableView.TableController;
import taskView.simulationComponent.SimulationComponentController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    @FXML private Label summaryLabel;
    @FXML private Label precentOfProgressBar;
    private List<Label> labels = new ArrayList<>();
    @FXML private Button updateTargetListButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    private List<Button> buttons = new ArrayList<>();
    @FXML private Parent tableProcess;
    @FXML private TableForProcessController tableProcessController;
    @FXML private TextArea textAreaProcessInfo;
    @FXML ProgressBar progressBar;
    private SimpleIntegerProperty precentOfProgress;
    private SimpleStringProperty updateInfoForUI;
    private SimpleStringProperty targetInfo;
    private SimpleBooleanProperty isPause;
    private BackDataManager bDM;
    private List<Minion> minions = new ArrayList<>();
    private SimulationComponentController simulationComponentController;
    private Parent simulationComponent;




    public void initTaskView()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulationComponent/simulationComponentFxml.fxml"));
            simulationComponent = loader.load();
            simulationComponentController = loader.getController();
            simulationComponentController.initSimulation();
            this.initThreadsSpinner();
            this.bDM.setTaskController(this);

        }catch (IOException e) {
            e.printStackTrace();
        }

        this.buttons = Arrays.asList(this.pauseButton, this.startButton,
                this.updateTargetListButton);

        this.labels = Arrays.asList(this.infoForTarget, this.errorMessegeForIncremental, this.precentOfProgressBar);
    }

    public TaskController(){

        precentOfProgress = new SimpleIntegerProperty(0);
        updateInfoForUI = new SimpleStringProperty("");
        isPause = new SimpleBooleanProperty(false);
        targetInfo = new SimpleStringProperty();

    }

    @FXML
    private void initialize(){
        infoForTarget.textProperty().bind(targetInfo);
        //textAreaProcessInfo.textProperty().bind(updateInfoForUI);
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


        // ---------- EXAMPLE PROCESS FROM --------------
        // start flagger (which is a MUST part of dSP)
//        Flagger flagger = new Flagger().builder()
//                .processIsSimulation(true)
//                .processFromScratch(true)
//                .chancesIsRandomInProcess(false);
//
//        //start more needed data
//        DataSetupProcess dSP = new DataSetupProcess().builder()
//                .flagger(flagger)
//                .chancesToSucceed(100)
//                .chancesToBeAWarning(100)
//                .timeToRun(3000);
//
//        this.bDM.startProcess(dSP);

        //---------- EXAMPLE PROCESS FOR RANDOM USER TARGETS --------------

        // start flagger (which is a MUST part of dSP)
        Flagger flagger = new Flagger().builder()
                .processIsSimulation(true)
                .processFromRandomTargets(true)
                .chancesIsRandomInProcess(false);

        //start more needed data
        DataSetupProcess dSP = new DataSetupProcess().builder()
                .flagger(flagger)
                .timeToRun(2000)
                .chancesToBeAWarning(100)
                .chancesToSucceed(100);
        if (!this.minions.isEmpty())
            dSP.minionsChoosenByUser(this.minions);

        this.bDM.startProcess(dSP);
    }
    /*void startButtonAction(ActionEvent event) {
        if(minions.isEmpty())
            return;

        //else
        this.startButton.setDisable(true);
        this.resumeButton.setDisable(false);
        this.pauseButton.setDisable(false);
        this.bDM.startPro(minions);

    }
*/
    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

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
                Minion minion = new Minion(target, maxTime, chancesISucceed, chancesImAWarning);
                minions.add(minion);
            }
        }
        tableProcessController.initTable(minions);
    }

    public void bindTaskToUIComponents(javafx.concurrent.Task<Object> aTask) {
        // task message
        textAreaProcessInfo.textProperty().bind(aTask.messageProperty());
        // task progress bar

        progressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        precentOfProgressBar.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

    }

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }


    private void initThreadsSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Task.maxParallelism, 1);
        this.threadsSpinner.setValueFactory(valueFactory);
    }


    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        //set buttons colors
        this.setButtonsColors(enumWantedColor);

        // set my label skins
        SkinsUtils.changeLabelsTextColorTo(enumWantedColor, this.labels);

        //set background colors
        this.setBackRoundColors(enumWantedColor);

        //set kids skins
        this.simulationComponentController.setSkins(enumWantedColor);
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {

        //set background colors
        if(enumWantedColor != SkinsUtils.Colors.DEFAULT)
            this.gridPaneSettingTab.setBackground(new Background(new BackgroundFill(Color.gray(0.2), null, null)));
        else
            this.gridPaneSettingTab.setBackground(new Background(new BackgroundFill(null, null, null)));
    }
}
