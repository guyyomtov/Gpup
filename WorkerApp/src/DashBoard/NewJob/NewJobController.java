package DashBoard.NewJob;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static util.Constants.GSON_INSTANCE;

import java.util.logging.ErrorManager;

public class NewJobController {


    @FXML private TitledPane newJobTitlePane;
    @FXML private Spinner<Integer> amountOfTargetsSpinner;
    @FXML private Spinner<Integer> amountOfThreadsSpinner;
    @FXML private Button cancelButton;
    @FXML private Button applyButton;
    private TaskData taskData;

    @FXML
    public void initialize() {

    }

    @FXML
    void applyButtonAction(ActionEvent event) {
        // to take the values from the spinner
        // to make a request with the values and the current Task name
        Integer amountOfTargets = this.amountOfTargetsSpinner.getValue();
        Integer amountOfThreads = this.amountOfThreadsSpinner.getValue();
        String finalUrl = HttpUrl
                .parse(Constants.NEW_JOB)
                .newBuilder()
                .addQueryParameter("taskname", taskData.getGraphName())
                .addQueryParameter("graphname", taskData.getGraphName())
                .addQueryParameter("amountOfThreads", String.valueOf(amountOfThreads))
                .build()
                .toString();
        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ErrorUtils.makeJavaFXCutomAlert("We failed, server problem")
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            ErrorUtils.makeJavaFXCutomAlert(responseBody)
                    );
                    System.out.println("we failed " + response.code());
                } else { // the code is 200
                    String jsonString = response.body().string();
                    ExecuteTarget[] executeTargets = GSON_INSTANCE.fromJson(jsonString, ExecuteTarget[].class);
                    List<ExecuteTarget> executeTargetList = Arrays.asList(executeTargets);

                    Platform.runLater(()-> AlertMessage.showUserSuccessAlert("you get the targets"));
                }
            }
        });

    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }
}
