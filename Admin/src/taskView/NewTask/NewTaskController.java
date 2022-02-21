package taskView.NewTask;

import DashBoardAdmin.MainDashboardController2;
import Graph.process.Task;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import tableView.TableController;
import taskView.compilationComponent.CompilationController;
import taskView.simulationComponent.SimulationComponentController;
import transferGraphData.AllGraphInfo;
import transferGraphData.TargetInfo;
import transferGraphData.TaskData;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Locale;

public class NewTaskController {


    @FXML
    private GridPane gridPane;
    @FXML
    private Parent targetTable;
    @FXML
    private TableController targetTableController;
    @FXML
    private TextField taskNameTextInput;
    @FXML
    private RadioButton simulationRB;
    @FXML
    private RadioButton compilationRB;
    @FXML
    private Button sumbitButton;
    private BorderPane mainBorderPane;
    private AllGraphInfo curGraphInfo;
    private final String SIMULATION = "simulation";
    private final String COMPILATION = "compilation";
    // baby components
    private Parent compilationComponent;
    private CompilationController compilationController;
    private Parent simulationComponent;
    private SimulationComponentController simulationController;

    //todo to handle with panel controller and those boolean property!
    private BooleanProperty pauseProperty;
    private BooleanProperty stopProperty;


    public void initGraphInfo(AllGraphInfo allGraphInfo) throws ErrorUtils, IOException {

        if (allGraphInfo == null)
            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);

        this.curGraphInfo = allGraphInfo;

        // start target table
        targetTableController.initTable(allGraphInfo.getTargetInfoList());

        // start baby components
        //this.initBabyComponents();
    }

    public void initBabyComponents() throws IOException {

        FXMLLoader loader;

        // start compilation component
        loader = new FXMLLoader(getClass().getResource("/taskView/compilationComponent/compilationControllerFxml.fxml"));
        this.compilationComponent = loader.load();
        this.compilationController = loader.getController();

        //start simulation component
        loader = new FXMLLoader(getClass().getResource("/taskView/simulationComponent/simulationComponentFxml.fxml"));
        this.simulationComponent = loader.load();
        this.simulationController = loader.getController();
        this.simulationController.initSimulation();
    }

    @FXML
    void compilationRBOnAction(ActionEvent event) throws ErrorUtils {

        // check if simulation is an option
        if (thisProcessIsAvailable(COMPILATION)) {

            // if compilation so: show compilation component
            this.showCompilationComponent();
            // save data to members
        } else {

            // process not available, send a message.
            this.compilationRB.setSelected(false);
            ErrorUtils.showUserErrorAlertJavaFX("Process not available.");
        }

    }

    private void showCompilationComponent() {
        this.gridPane.getChildren().remove(this.simulationComponent);
        this.gridPane.add(this.compilationComponent, 1, 1);

    }

    @FXML
    void simulationRBOnAction(ActionEvent event) throws ErrorUtils {


        // check if simulation is an option
        if (thisProcessIsAvailable(SIMULATION)) {

            // if simulation so: show simulation component
            this.showSimulationComponent();

            // save data to members
        } else {

            // process not available, send a message.
            this.simulationRB.setSelected(false);
            ErrorUtils.showUserErrorAlertJavaFX("Process not available.");
        }
    }

    private void showSimulationComponent() {

        // put new component there
        this.gridPane.getChildren().remove(this.compilationComponent);
        this.gridPane.add(this.simulationComponent, 1, 1);
    }

    private boolean thisProcessIsAvailable(String processType) {

        boolean isAvailable = false;

        // check if available
        if (this.curGraphInfo.getGraphInfo().getTaskInfo().toLowerCase(Locale.ROOT).contains(processType.toLowerCase(Locale.ROOT)))
            isAvailable = true;

        return isAvailable;
    }


    @FXML
    void sumbitButtonAction(ActionEvent event) {
        // create new Task Data and check handle with Error
        TaskData taskData;
        try {
            // check valid data: valid name, data for process, user choose targets
            this.checkValidity();
            taskData = this.createTaskData();
            this.sendTaskDataToServer(taskData);
        } catch (ErrorUtils e) {
            ErrorUtils.makeJavaFXCutomAlert(e.getMessage());
            return;
        }


    }

    private void sendTaskDataToServer(TaskData taskData) {

        //make url
        String json = Constants.GSON_INSTANCE.toJson(taskData);
        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_TASK)
                .newBuilder()
                .addQueryParameter("taskDataObject", json)
                .build()
                .toString();

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ErrorUtils.makeJavaFXCutomAlert("server failed")
                );
                System.out.println("We failed, server problem");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            ErrorUtils.makeJavaFXCutomAlert("Something went wrong: " + responseBody)
                    );
                    System.out.println("we failed " + response.code());

                } else {
                    Platform.runLater(() -> {
                        AlertMessage.showUserSuccessAlert("Task uploaded successfully");
                    });
                    System.out.println("we succeeded " + response.code());
                }
            }
        });
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }


    private TaskData createTaskData() throws ErrorUtils {
        TaskData taskData = new TaskData();
        taskData.setTaskName(this.taskNameTextInput.getText());
        taskData.setGraphName(MainDashboardController2.currGraphName);
        taskData.setTargetInfoList(this.targetTableController.getTargetInfoThatUserSelected());
        taskData.setWhatKindOfTask(this.compilationRB.isSelected() ? COMPILATION : SIMULATION);
        taskData.setFromScratch(true);
        taskData.setPricePerTarget(this.getPricePerTarget());
        if(this.simulationRB.isSelected()){
            this.getDataForSimulation(taskData);
        }
        else
            this.getDataForCompilation(taskData);
        return taskData;
    }

    private void getDataForSimulation(TaskData taskData) {
        taskData.setChancesToSuccess(this.simulationController.getChancesToSuccess());
        taskData.setChancesToWarning(this.simulationController.getChancesToSuccessWithWarning());
        taskData.setMaxTimePerTarget(this.simulationController.getMaxTime());
        taskData.setRandom(this.simulationController.getIfRandom());
    }

    private void getDataForCompilation(TaskData taskData) {
        taskData.setFullPathSource(this.compilationController.getFullPathSource());
        taskData.setFullPathDestination(this.compilationController.getFullPathDestination());
    }

    private Integer getPricePerTarget() {
        String taskInfo = this.curGraphInfo.getGraphInfo().getTaskInfo();
        //todo handle with string i dont have a power right now!
        return 5;
    }

    private void checkValidity() throws ErrorUtils {
        try{
            this.ifUserChoseTask();
            this.checkTaskName();
            this.checkIfUserChoseTargets();

        }catch (ErrorUtils e){throw e;}

    };
    private void checkTaskName() throws ErrorUtils {
        if(this.taskNameTextInput.getText().equals(""))
            throw new ErrorUtils("Please chose name to The Task");
    }

    private Boolean ifUserChoseTask() throws ErrorUtils {
        if(this.simulationRB.isSelected() || this.compilationRB.isSelected())
            return true;
        else throw new ErrorUtils("Please chose task!");
    }

    private Boolean checkIfUserChoseTargets() throws ErrorUtils {
        for(TargetInfo targetInfo : this.targetTableController.getTableView().getItems()){
            if(targetInfo.getCheckBox().isSelected())
                return true;
        }
        throw new ErrorUtils("Please chose targets!");
    }


}
