package DashBoardAdmin.TaskInfoTableComponent;

import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.GraphInfo;
import transferGraphData.TaskData;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class TaskListRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<TaskData>> tasksListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public TaskListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<TaskData>> tasksListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.tasksListConsumer = tasksListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        httpRequestLoggerConsumer.accept("About to invoke: " + Constants.TASKS_LIST + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(Constants.TASKS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfTaskData = response.body().string();
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfTaskData);
                TaskData[] taskDataArray = GSON_INSTANCE.fromJson(jsonArrayOfTaskData, TaskData[].class);
                if(taskDataArray != null)
                    tasksListConsumer.accept(Arrays.asList(taskDataArray));
            }
        });
    }


}