package taskView.NewTask;

import errors.ErrorUtils;
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
import tableView.TableController;
import taskView.compilationComponent.CompilationController;
import taskView.simulationComponent.SimulationComponentController;
import transferGraphData.AllGraphInfo;

import java.io.IOException;
import java.util.Locale;

public class NewTaskController {


    @FXML private GridPane gridPane;
    @FXML private Parent targetTable;
    @FXML private TableController targetTableController;
    @FXML private TextField taskNameTextInput;
    @FXML private RadioButton simulationRB;
    @FXML private RadioButton compilationRB;
    @FXML private Button sumbitButton;
    private BorderPane mainBorderPane;
    private AllGraphInfo curGraphInfo;
    private final String SIMULATION = "simulation";
    private final String COMPILATION = "compilation";
    // baby components
    private Parent compilationComponent;
    private CompilationController compilationController;
    private Parent simulationComponent;
    private SimulationComponentController simulationController;


    public void initGraphInfo(AllGraphInfo allGraphInfo) throws ErrorUtils, IOException {

        if(allGraphInfo == null)
            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);

        this.curGraphInfo = allGraphInfo;

        // start target table
        targetTableController.initTable(allGraphInfo.getTargetInfoList());

        // start baby components
        this.initBabyComponents();
    }

    private void initBabyComponents() throws IOException {

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

    @FXML void compilationRBOnAction(ActionEvent event) throws ErrorUtils {

        // if compilation button is on, turn him off

        // check if simulation is an option
        // if simulation so: show simulation component


        // if compilation button is on, turn him off
        this.turnTheOtherOff(this.compilationRB);

        // check if simulation is an option
        if(thisProcessIsAvailable(COMPILATION)){

            // if simulation so: show simulation component

            // save data to members
        }
        else{

            // process not available, send a message.
            ErrorUtils.showUserErrorAlertJavaFX("Process not available.");
        }

    }

    @FXML
    void simulationRBOnAction(ActionEvent event) throws ErrorUtils{

        // if compilation button is on, turn him off
        this.turnTheOtherOff(this.simulationRB);

        // check if simulation is an option
        if(thisProcessIsAvailable(SIMULATION)){

            // if simulation so: show simulation component
            this.showSimulationComponent();

            // save data to members
        }
        else{

            // process not available, send a message.
            ErrorUtils.showUserErrorAlertJavaFX("Process not available.");
        }
    }

    private void showSimulationComponent() {

        // put new component there
        this.gridPane.add(this.simulationComponent, 1,1);
    }

    private boolean thisProcessIsAvailable(String processType) {

        boolean isAvailable = false;

        // check if available
        if(this.curGraphInfo.getGraphInfo().getTaskInfo().toLowerCase(Locale.ROOT).contains(processType.toLowerCase(Locale.ROOT)))
            isAvailable = true;

        return isAvailable;
    }


    private void turnTheOtherOff(RadioButton rB) throws ErrorUtils {

        // this function makes sure that only on process check box is selected at any given time.

        if(rB == this.simulationRB){

            if(this.compilationRB.isPressed() && this.simulationRB.isPressed())
                this.compilationRB.setSelected(false);
        }
        else if(rB == this.compilationRB){

            if(this.compilationRB.isPressed() && this.simulationRB.isPressed())
                this.simulationRB.setSelected(false);
        }
        else
            throw new ErrorUtils(ErrorUtils.invalidInput("Got an invalid radio button."));
    }

    @FXML
    void sumbitButtonAction(ActionEvent event) {

        // check valid data: valid name, data for process, user choose targets

        // TODO make request of a new task
    }

    public void setMainBorderPane(BorderPane mainBorderPane) { this.mainBorderPane = mainBorderPane;}
}
