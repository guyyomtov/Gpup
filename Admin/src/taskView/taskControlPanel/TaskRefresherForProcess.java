package taskView.taskControlPanel;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.TaskData;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class TaskRefresherForProcess extends TimerTask {

    private final Consumer<List<TaskData>> tasksListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

// todo to make refresh with query parameter and get only the relevant task, now we get all task because Im so lazy!
    public TaskRefresherForProcess(BooleanProperty shouldUpdate, Consumer<List<TaskData>> tasksListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.tasksListConsumer = tasksListConsumer;
        requestNumber = 0;
    }
    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
      //  httpRequestLoggerConsumer.accept("About to invoke: " + Constants.TASK_DATA + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(Constants.TASKS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertMessage.showUserErrorAlert("server failed!"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonTaskData = response.body().string();
                TaskData[] taskDataArray = GSON_INSTANCE.fromJson(jsonTaskData, TaskData[].class);
                if(jsonTaskData != null) {
                    tasksListConsumer.accept(Arrays.asList(taskDataArray));
                }
            }
        });
    }

}

