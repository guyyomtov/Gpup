package DashBoard.NewJob;

import errors.ErrorUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.ExecuteTarget;
import transferGraphData.TaskData;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class JobsManager implements Runnable{


    private List<TaskData> taskThatWorkerJoined;
    private Integer maxThreads;
    private Integer freeThreads;
    private Integer threadCounter;
    private ExecutorService executorService;
    private Queue<ExecuteTarget> waitingList;
    private Consumer consumerForLogs;


    public JobsManager(Integer maxThreads){

        this.taskThatWorkerJoined = new ArrayList<>();
        this.waitingList = new LinkedList<>();
        this.maxThreads = maxThreads;
        this.freeThreads = maxThreads;
        this.threadCounter = new Integer(0);
        this.executorService = Executors.newFixedThreadPool(this.maxThreads);
    }

    synchronized public void addToWaitingList(List<ExecuteTarget> executeTargetList) throws ErrorUtils {

        this.freeThreads -= executeTargetList.size();

        for(ExecuteTarget executeTarget : executeTargetList){

            if(thisTargetExist(executeTarget.getTargetName()))
                throw new ErrorUtils(ErrorUtils.invalidInput("double target giving back end problem"));

            this.waitingList.add(executeTarget);
        }
    }

    private Boolean thisTargetExist(String targetName) {
        Set<ExecuteTarget> executeTargets = new HashSet<>(this.waitingList);
        // to make the set of the names
        Set<String> allTargetNames = this.makeSetOfNames();
        return allTargetNames.contains(targetName);


    }

    private Set<String> makeSetOfNames() {
        Set<String> res = new HashSet<>();
        for(ExecuteTarget executeTarget : waitingList){
            res.add(executeTarget.getTargetName());
        }
        return res;
    }

    public void addNewTask(TaskData taskData){
        this.taskThatWorkerJoined.add(taskData);
    }

    //always going to run in another thread!
    @Override
    public void run() {

        while( true /*!this.taskThatWorkerJoined.isEmpty() && !this.waitingList.isEmpty()*/){
            int x;

            //call to server
            //get execute target from tasks
            // put in the waiting list
            if(!waitingList.isEmpty())
                 x = 5;
            this.createWaitingList();

            ExecuteTarget executeTarget = this.waitingList.poll();

            if(executeTarget != null) {

                executeTarget.setConsumerForLog(this.consumerForLogs);

                executorService.execute(executeTarget);

                ++threadCounter;
            }
            // slow down the requests
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWaitingList() {

        for(TaskData taskData : this.taskThatWorkerJoined)
            this.callToServer(taskData);
    }

    private void callToServer(TaskData taskData) {

        String finalUrl = HttpUrl
                .parse(Constants.NEW_JOB)
                .newBuilder()
                .addQueryParameter("taskname", taskData.getTaskName())
                .addQueryParameter("graphname", taskData.getGraphName())
                .addQueryParameter("amountOfThreads", String.valueOf(this.freeThreads))
                .build()
                .toString();

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

//                Platform.runLater(() ->
//                        ErrorUtils.makeJavaFXCutomAlert("We failed, server problem")
//                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            ErrorUtils.makeJavaFXCutomAlert(responseBody)
//                    );
                    System.out.println("we failed " + response.code());
                } else { // the code is 200
                    try {
                        String jsonString = response.body().string();
                        ExecuteTarget[] executeTargets = GSON_INSTANCE.fromJson(jsonString, ExecuteTarget[].class);
                        List<ExecuteTarget> executeTargetList = Arrays.asList(executeTargets);
                        addToWaitingList(executeTargetList);
                    } catch (ErrorUtils errorUtils) {
                        errorUtils.printStackTrace();
                    }
                }
            }
        });

    }


    public void setConsumerForLogs(Consumer consumerForLogs) {
        this.consumerForLogs = consumerForLogs;
    }
}
