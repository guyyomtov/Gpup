package taskView;

import AnimationComponent.SkinsUtils;
import DataManager.BackDataManager;
import Flagger.Flagger;
import Graph.Target;
import Graph.process.DataSetupProcess;
import Graph.process.Minion;
import Graph.process.Task;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
import tableForProcess.TableForProcessController;
import taskView.compilationComponent.CompilationController;
import taskView.simulationComponent.SimulationComponentController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskController {

    @FXML private GridPane gridPaneSettingTab;
    @FXML private RadioButton simulationButton;
    @FXML private RadioButton compilationButton;
    @FXML private Spinner<Integer> threadsSpinner;
    @FXML private RadioButton incrementalButton;
    @FXML private RadioButton fromScratchButton;
    @FXML private Label errorMessegeForIncremental;
    @FXML private TextArea textAreaTargetInfo;
    @FXML private Label summaryLabel;
    @FXML private Label precentOfProgressBar;
    @FXML private Button updateTargetListButton;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    private List<Button> buttons = new ArrayList<>();
    @FXML private Parent tableProcess;
    @FXML private TableForProcessController tableProcessController;
    @FXML private TextArea textAreaProcessInfo;
    @FXML ProgressBar progressBar;
    private CompilationController compilationController;
    private Parent compilationComponent;

    private List<Label> labels = new ArrayList<>();
    private BooleanProperty startButtonProperty;
    private BooleanProperty resumeProperty;
    private BooleanProperty pauseProperty;
    private StringProperty targetInfo;
    private BackDataManager bDM;
    private List<Minion> minions = new ArrayList<>();
    private SimulationComponentController simulationComponentController;
    private Parent simulationComponent;
    private javafx.concurrent.Task<Object> task;
    private DataSetupProcess dSP;
    private List<RadioButton> radionButtons = new ArrayList<>();

    public TaskController(){

        resumeProperty = new SimpleBooleanProperty(this, "pauseAndResume", true);
        pauseProperty =  new SimpleBooleanProperty(this, "pauseAndResume", false);
        startButtonProperty = new SimpleBooleanProperty(this, "start", true );
       // targetInfo = new SimpleStringProperty("");

    }

    public void initTaskView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulationComponent/simulationComponentFxml.fxml"));
            simulationComponent = loader.load();
            simulationComponentController = loader.getController();
            simulationComponentController.initSimulation();

            FXMLLoader loaderCom = new FXMLLoader(getClass().getResource("compilationComponent/compilationControllerFxml.fxml"));
            compilationComponent = loaderCom.load();
            compilationController = loaderCom.getController();
            this.initThreadsSpinner();
            this.bDM.setTaskController(this);
            gridPaneSettingTab.add(simulationComponent, 0, 1);
            this.initButtonsListener();

        }catch (IOException e) {
            e.printStackTrace();
        }

       // this.radionButtons = Arrays.asList(this.compilationButton, this.fromScratchButton, this.incrementalButton, this.simulationButton);

        this.buttons = Arrays.asList(this.pauseButton, this.startButton,
                this.updateTargetListButton);

        //this.labels = Arrays.asList(this.errorMessegeForIncremental, this.precentOfProgressBar, this.taskName, this.chooseTask);
    }

    private void initButtonsListener() {
        precentOfProgressBar.textProperty().addListener((v, oldV, newV) ->  {
            String[] value = precentOfProgressBar.textProperty().getValue().split(" ");
            if( Integer.valueOf(value[0]) == 100) {
                startButtonProperty.setValue(false);
                pauseProperty.setValue(true);
                resumeProperty.setValue(true);
            }
        });
        this.startButton.disableProperty().bind(this.startButtonProperty);
        this.resumeButton.disableProperty().bind(this.resumeProperty);
        this.pauseButton.disableProperty().bind(this.pauseProperty);
    }

    @FXML
    private void initialize(){
        //textAreaProcessInfo.textProperty().bind(updateInfoForUI);
    }

    @FXML
    void compilationButtonAction(ActionEvent event) {
        try {
            gridPaneSettingTab.getChildren().removeAll(simulationComponent);
            gridPaneSettingTab.add(compilationComponent, 0, 1);
        }catch (Exception e){}
    }

    @FXML
    void resumeButtonAction(ActionEvent event) {
        pauseProperty.set(false);
        resumeProperty.set(true);
        List<Minion> updateMinions = this.tableProcessController.getMinionsFromTable();
        try {
            dSP.setLastProcessTextArea(this.textAreaProcessInfo.getText());
            this.bDM.startProcess(dSP);
        }catch (ErrorUtils e) { // last update}
        }

    }

    @FXML
    void pauseButtonAction(ActionEvent event) {
        pauseProperty.set(true);
        resumeProperty.set(false);
    }

    @FXML
    void simulationButtonAction(ActionEvent event) {
        try {
            gridPaneSettingTab.getChildren().removeAll(compilationComponent);
            gridPaneSettingTab.add(simulationComponent, 0, 1);
        }catch (Exception e){}
    }

    @FXML
    void startButtonAction(ActionEvent event) throws ErrorUtils {


        this.updateTargetListButtonAction(event);
        this.pauseProperty.setValue(false);
        this.startButtonProperty.setValue(true);
        Flagger flagger = new Flagger().builder()
                .processIsSimulation(simulationButton.isSelected())
                .processIsCompilation(compilationButton.isSelected())
                .processFromRandomTargets(true)
                .thereIsSerialSets(true)
                .chancesIsRandomInProcess(this.simulationComponentController.getIfRandom());

        //start more needed data
         dSP = new DataSetupProcess().builder()
                .flagger(flagger)
                .timeToRun(this.simulationComponentController.getMaxTime())
                .chancesToBeAWarning(this.simulationComponentController.getChancesToSuccessWithWarning())
                .chancesToSucceed(this.simulationComponentController.getChancesToSuccess())
                .pauseTaskProperty(pauseProperty)
                .amountOfThreads(this.threadsSpinner.getValue())
                 .setFullPathSource(this.compilationController.getFullPathSource())
                 .setFullPathDestination(this.compilationController.getFullPathDestination());

        if (!this.minions.isEmpty())
            dSP.minionsChoosenByUser(this.minions);

        this.bDM.startProcess(dSP);

    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        SkinsUtils.changeButtonColorTo(wantedColors, this.buttons);
    }

    @FXML
    void updateTargetListButtonAction(ActionEvent event) {

        this.minions.clear();
        List<Target> targets = this.bDM.getAllTargets();
        Integer maxTime = this.simulationComponentController.getMaxTime();
        Integer chancesISucceed = this.simulationComponentController.getChancesToSuccess();
        Integer chancesImAWarning = this.simulationComponentController.getChancesToSuccessWithWarning();
        boolean isRand = this.simulationComponentController.getIfRandom();

        for(Target target : targets){
            if(target.getRemark().isSelected())
            {
                // if minion is simulation --> make simulation minion
                if(this.simulationButton.isSelected()) {
                    Minion minion = new Minion(target, maxTime, chancesISucceed, chancesImAWarning, this.simulationButton.isSelected());
                    minions.add(minion);
                }
                else{ // if minon is compilation --> make compilation minon

                    Minion minion = new Minion(target, maxTime, chancesISucceed, chancesImAWarning, this.simulationButton.isSelected(), this.compilationController.getFullPathDestination(), this.compilationController.getFullPathSource());
                    minions.add(minion);
                }
            }
        }
        tableProcessController.initTable(minions, this.textAreaTargetInfo);
        if(!minions.isEmpty())
            this.startButtonProperty.setValue(false);
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

        this.task = aTask;
        //pauseProperty.addListener((v, oldV, newV) -> aTask.cancel(true));


    }

    public void setbDM(BackDataManager bDM) {
        this.bDM = bDM;
    }

    private void initThreadsSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Task.maxParallelism, 1);
        this.threadsSpinner.setValueFactory(valueFactory);
    }

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

//        //set buttons colors
//        this.setButtonsColors(enumWantedColor);
//
//        // set my label skins
//        SkinsUtils.changeLabelsTextColorTo(enumWantedColor, this.labels);
//
//        //set background colors
//        this.setBackRoundColors(enumWantedColor);`
//
//        //set kids skins
//        this.simulationComponentController.setSkins(enumWantedColor);
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {

        //set background colors
        if(enumWantedColor != SkinsUtils.Colors.DEFAULT)
            this.gridPaneSettingTab.setBackground(new Background(new BackgroundFill(Color.gray(0.2), null, null)));
        else
            this.gridPaneSettingTab.setBackground(new Background(new BackgroundFill(null, null, null)));
    }
}
