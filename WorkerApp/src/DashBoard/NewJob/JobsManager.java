package DashBoard.NewJob;

import DashBoard.AllTasksTable.AllTasksInfoTableController;
import DashBoard.WorkerDashBoardController;
import errors.ErrorUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import taskView.taskControlPanel.TaskRefresherForProcess;
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

public class JobsManager implements Runnable, Consumer{


    private List<TaskData> taskThatWorkerJoined;
    private Integer maxThreads;
    private Integer freeThreads;
    private Integer threadCounter;
    private ExecutorService executorService;
    private Queue<ExecuteTarget> waitingList;
    private Consumer consumerForLogs;
    private AllTasksInfoTableController allTasksInfoTableController;
    private WorkerDashBoardController workerDashBoardController;
    private Map<String, MiniJobDataController> taskNameToMiniDataHelper;


    public JobsManager(Integer maxThreads){

        this.taskThatWorkerJoined = new ArrayList<>();
        this.waitingList = new LinkedList<>();
        this.maxThreads = maxThreads;
        this.freeThreads = maxThreads;
        this.threadCounter = new Integer(0);
        this.executorService = Executors.newFixedThreadPool(this.maxThreads);
        taskNameToMiniDataHelper = new HashMap<>();
    }

    synchronized public void addToWaitingList(List<ExecuteTarget> executeTargetList) throws ErrorUtils {

        for(ExecuteTarget executeTarget : executeTargetList){
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

    public void addNewTask(TaskData taskData) throws ErrorUtils {

        // add to jobManger
        this.taskThatWorkerJoined.add(taskData);

        // add to dataHelper
        if(this.taskNameToMiniDataHelper.containsKey(taskData.getTaskName()))
            throw new ErrorUtils(ErrorUtils.invalidInput("This task already exists, can't add it again."));

        this.taskNameToMiniDataHelper.put(taskData.getTaskName(), new MiniJobDataController(taskData));
    }

    public void removeTask(TaskData taskData) throws ErrorUtils {

        // remove from jobManger
        this.taskThatWorkerJoined.remove(taskData);

        if(!this.taskNameToMiniDataHelper.containsKey(taskData.getTaskName()))
            throw new ErrorUtils(ErrorUtils.invalidInput("This task doesn't exist in the data."));

        // remove from dataHelper
        this.taskThatWorkerJoined.remove(taskData.getTaskName());
    }

    //always going to run in another thread!
    @Override
    public void run() {

        while( true /*!this.taskThatWorkerJoined.isEmpty() && !this.waitingList.isEmpty()*/){
            int x;
            // if he doesnt aplly to a avialble task

            if(!waitingList.isEmpty())
                 x = 5;
            this.updateWaitingList();

            ExecuteTarget executeTarget = this.waitingList.poll();

            if(executeTarget != null) {

                if(this.threadCounter <= this.maxThreads) {

                    // set consumers
                    executeTarget.setConsumerForLog(this.consumerForLogs);
                    executeTarget.setConsumerThreadsBack(this);
                    executeTarget.setConsumerForAmountOfCredit(this.workerDashBoardController);

                    // execute target
                    executeTarget.getTaskName();
                    executorService.execute(executeTarget);

                    // update thread counter
                    ++threadCounter;

                    // find relevant task
                    TaskData relevantTask = this.findRelevantTask(executeTarget.getTaskName(), this.taskThatWorkerJoined);

                    // update miniDataHelper
                    try {
                        this.updateValuesInMiniDataHelper(relevantTask);
                    } catch (ErrorUtils e) {
                        e.printStackTrace();
                    }
                }
                //we don't have a free thread
                else
                    this.waitingList.add(executeTarget);

            }

            // slow down the requests
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateValuesInMiniDataHelper(TaskData relevantTask) throws ErrorUtils {

        // get relevant MiniData
        MiniJobDataController wantedMini = this.taskNameToMiniDataHelper.get(relevantTask.getTaskName());

        // update all his data
        wantedMini.updateMyDataAfterTargetExcute(relevantTask);
    }

    private TaskData findRelevantTask(String i_wantedTaskName, List<TaskData> taskThatWorkerJoined) {

        TaskData res = new TaskData();
        String curName = new String();

        for(TaskData curT : taskThatWorkerJoined){

            curName = curT.getTaskName();

            if(curName.equals(i_wantedTaskName)){

                res = curT;
                break;
            }
        }
        return res;
    }

    private void updateWaitingList() {
        if(this.allTasksInfoTableController == null)
            return;
        Map<String, TaskData> nameToTaskDataTheNewest = this.makeMapOfTaskData(this.allTasksInfoTableController.getTaskDataList()); // maybe status change
        for(TaskData taskData : this.taskThatWorkerJoined) {
            TaskData.Status status = nameToTaskDataTheNewest.get(taskData.getTaskName()).getStatus();
            // only if the status is available get minions
            if(status == TaskData.Status.AVAILABLE)
                this.callToServer(taskData);
            //delete task from worker
            else if(status == TaskData.Status.DONE || status == TaskData.Status.STOPPED){}
               // this.taskThatWorkerJoined.remove(taskData);

        }
    }

    public Map<String, TaskData> makeMapOfTaskData(List<TaskData> taskDataList) {

        Map<String, TaskData> nameToTaskData = new HashMap<>();
        for(TaskData taskData : taskDataList){
            nameToTaskData.put(taskData.getTaskName(), taskData);
        }
        return nameToTaskData;
    }

    private void callToServer(TaskData taskData) {

        this.freeThreads = this.maxThreads - this.threadCounter;
        if(this.freeThreads > 0) {
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

    }

    public void setConsumerForLogs(Consumer consumerForLogs) {
        this.consumerForLogs = consumerForLogs;
    }

    @Override
    public void accept(Object o) {
        this.threadCounter = this.threadCounter - 1;
    }


    public void setTaskRefresherForProcess(AllTasksInfoTableController allTasksInfoTableController) {
        this.allTasksInfoTableController = allTasksInfoTableController;
    }

    public List<TaskData> getTaskThatWorkerJoined(){ return this.taskThatWorkerJoined;}

    public WorkerDashBoardController getWorkerDashBoardController() {
        return workerDashBoardController;
    }

    public void setWorkerDashBoardController(WorkerDashBoardController workerDashBoardController) {
        this.workerDashBoardController = workerDashBoardController;
    }

    public Map<String, MiniJobDataController> getTaskNameToMiniDataHelper(){ return this.taskNameToMiniDataHelper;}

    // HELPER CLASS
    public class MiniJobDataController{


        public String myName;
        public Integer amountOfTotalTargets;
        public Integer amountOfTargetsDoneByMy;
        public Integer amountOfCredits;
        public TaskData generalTableTaskData;
        public Integer amountOfTotalWorkersOnTheTask;


        public MiniJobDataController(TaskData i_generalTableTaskData) throws ErrorUtils {

            if(i_generalTableTaskData == null)
                throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);

            this.generalTableTaskData = i_generalTableTaskData;

            this.updateDataFromGeneralTable();
            this.startExtraData();
        }

        private void startExtraData() {

            this.amountOfTargetsDoneByMy = new Integer(0);
            this.amountOfCredits = new Integer(0);
        }

        private void updateDataFromGeneralTable() {

            this.myName = this.generalTableTaskData.getTaskName();
            this.amountOfTotalTargets = this.generalTableTaskData.getTotalTargets();
            this.amountOfTotalWorkersOnTheTask = this.generalTableTaskData.getTotalTargets();
        }

        public void updateMyDataAfterTargetExcute(TaskData i_generalTableTaskData) throws ErrorUtils {

            if(i_generalTableTaskData.getTaskName() != this.myName)
                throw new ErrorUtils(ErrorUtils.invalidInput("Given task is different from the original."));

            this.generalTableTaskData = i_generalTableTaskData;

            this.updateDataFromGeneralTable();

            this.updateExtraData();
        }

        private void updateExtraData() throws ErrorUtils {

            // get how much each target gives me credit
            Integer amountOfCreditForTarget = this.generalTableTaskData.getPricePerTarget();

            //update my credits
            this.amountOfCredits += amountOfCreditForTarget;

            // update my amount of targets done
            this.amountOfTargetsDoneByMy += 1;

            // logic error
            if(this.amountOfTargetsDoneByMy > this.amountOfTotalTargets || this.amountOfCredits > this.generalTableTaskData.getTotalPrice())
                throw new ErrorUtils(ErrorUtils.invalidInput("Worker did more work then possible"));
        }
    }

}
