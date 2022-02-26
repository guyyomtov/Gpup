package workerManager.currentTaskWorkerTable;

import DashBoard.NewJob.JobsManager;
import javafx.beans.property.BooleanProperty;

import java.util.TimerTask;
import java.util.function.Consumer;

public class MyTasksTableRefresher extends TimerTask {


    private final Consumer<JobsManager> jobsManagerConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private JobsManager jobsManager;


    public MyTasksTableRefresher(BooleanProperty shouldUpdate, Consumer<JobsManager> jobsManagerConsumer, JobsManager jobsManager) {

        this.shouldUpdate = shouldUpdate;
        this.jobsManagerConsumer = jobsManagerConsumer;
        this.jobsManager = jobsManager;
        requestNumber = 0;
    }

    @Override
    public void run() {

        this.jobsManagerConsumer.accept(this.jobsManager);
    }
}
