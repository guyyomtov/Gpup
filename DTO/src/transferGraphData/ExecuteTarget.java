package transferGraphData;

import DataManager.consumerData.ConsumerTaskInfo;
import Graph.process.Minion;
import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import errors.ErrorUtils;
import jdk.nashorn.internal.ir.LexicalContext;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

public class ExecuteTarget implements Runnable {

    private String targetName;
    private String type;
    private TargetInfo targetInfo;
    private String status;
    private String whatKindOfTask;
    private final String SIMULATION = "simulation";
    private final String COMPILATION = "compilation";
    //parameters for simulation
    private Boolean isRandom;
    private Integer timeIRun;
    private Integer chancesToSuccess;
    private Integer chancesToWarning;

    // parameters for compilation
    private String fullPathSource;
    private String fullPathDestination;
    private String generalInfo;

    //information for servlet
    private String taskName;
    private String graphName;
    private String workerThatDoneMe = new String("");
    private String logs;
    private Consumer consumerForLog;
    private Consumer consumerThreadIsBack;
    private Consumer consumerForAmountOfCredit;
    private Integer pricePerJob;
    private Set<String> ISkippedBecause = new HashSet<>();

    //after user press start
    public ExecuteTarget(TaskData taskData, TargetInfo currentTargetInfo, Minion minion){
        this.targetInfo = currentTargetInfo;
        this.targetName = currentTargetInfo.getName();
        this.type = currentTargetInfo.getType();
        this.status = minion.getStatus();
        this.whatKindOfTask = taskData.getWhatKindOfTask();
        this.isRandom = taskData.getRandom();
        this.timeIRun = minion.getTimeIRun();
        this.chancesToSuccess = minion.getChancesISucceed();
        this.chancesToWarning = minion.getChancesImAWarning();
        this.fullPathSource = minion.getFullPathSource();
        this.fullPathDestination = minion.getFullPathDestination();
        this.generalInfo = currentTargetInfo.getInformation();
        this.taskName = taskData.getTaskName();
        this.graphName = taskData.getGraphName();
        this.pricePerJob = taskData.getPricePerTarget();
    }
    // before the admin press start usr that
    public ExecuteTarget(TargetInfo currentTargetInfo){
        this.targetInfo = currentTargetInfo;
        this.targetName = currentTargetInfo.getName();
        this.status = "Not initialized";
        this.type = currentTargetInfo.getType();
    }

    @Override
    public void run() {

        String outPut  = new String();
        Instant start, end;
        start = Instant.now();
        this.status = "IN PROCESS";

        outPut = "The target " + this.targetName + " about to run." + "\n";
        logs += outPut;
        consumerForLog.accept(outPut);

        if(whatKindOfTask.toLowerCase().equals(SIMULATION))
            runForSimulation();
        else // its compilation
            runForCompilation();

        outPut = "The system finished the task on target " + this.targetName + "\n";
        this.logs += outPut;
        consumerForLog.accept(outPut);

        outPut = "The result of the target " + this.targetName + " is: " + this.status + "\n";
        this.logs += outPut;
        consumerForLog.accept(outPut);
        this.consumerThreadIsBack.accept(true);
        this.consumerForAmountOfCredit.accept(this.pricePerJob);
        this.sendTheResultToTheServer();
        //i think here we should update the thread counter
    }

    // todo handle with the files tomorrow to see what we need to had

    private String runForSimulation() {

        String outPut = "";
        Duration t = Duration.ofMillis(timeIRun);
        outPut = "The system is going to sleep for: " + String.format("%02d:%02d:%02d", t.toHours(), t.toMinutes(), t.getSeconds()) + "\n";
        this.logs += outPut;
        consumerForLog.accept(outPut);

        try {
            //end = Instant.now();
           // this.minionLiveData.setTimeInProcess(Duration.between(start, end).toMillis());
            Thread.sleep(timeIRun);
        } catch (InterruptedException e) {}
        this.checkTheResultAndUpdateStatus();
        return outPut;
    }
    private void checkTheResultAndUpdateStatus() {

            Random rand = new Random();
            String openedParents = "";
            if ((rand.nextInt(101)) <= this.chancesToSuccess) {

                // if warning
                if ((rand.nextInt(101)) <= this.chancesToWarning)
                    this.status = "WARNING"; //success with warning
                else
                    this.status = "SUCCESS";

            } else {
                this.status = "FAILURE";
            }
    }

    private String runForCompilation() {

        String outPut = "";
        Instant start, end;
        String openedParents = "";
        String resSrcArg = new String();
        int res = 0;

        // append second needed part
        resSrcArg += this.fullPathSource + "/" + this.generalInfo.replace('.', '/' ) + ".java";

        String[] c = {"javac", "-d", this.fullPathDestination, "-cp", this.fullPathDestination, resSrcArg};
        outPut = "Full command: javac-d"+ " " + this.fullPathDestination +  " -cp " +  this.fullPathDestination + " "+ resSrcArg + "\n";
        consumerForLog.accept(outPut);
        this.logs += outPut;

        try {
            start = Instant.now();
            Process process = Runtime.getRuntime().exec(c);
            process.waitFor();
            end = Instant.now();
            outPut = "The compiler finish the task on target " + targetName + " in: " + Duration.between(start, end).toMillis() + " milli second." + "\n";
            consumerForLog.accept(outPut);
            this.logs += outPut;

            res = process.exitValue();
            if(res == 0) // of res == 0 == > process on curr target success.
                this.status = "SUCCESS";
            else {
                this.status = "FAILURE";
                String errorMessage = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())).readLine();
                outPut = "The target is failed, output from javac: " + errorMessage + "\n";
                this.logs += outPut;
                consumerForLog.accept(outPut);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outPut;
    }

    public TargetInfo getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(TargetInfo targetInfo) {
        this.targetInfo = targetInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhatKindOfTask() {
        return whatKindOfTask;
    }

    public void setWhatKindOfTask(String whatKindOfTask) {
        this.whatKindOfTask = whatKindOfTask;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void sendTheResultToTheServer() {

        Request request = this.makeTheBodyRequest();

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                .followRedirects(false)
                .build();

        runAsync(request, okHttpClient,new Callback() {
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



                    System.out.println("we failed " + response.code());
                } else { // the code is 200

                    int x = 5;

                    }
                }
        });

    }

    private Request makeTheBodyRequest() {
        //make the gson
        this.consumerForLog = null;
        this.consumerThreadIsBack = null;
        this.consumerForAmountOfCredit = null;

        Gson gson = new Gson();
        String executeTargetJson = gson.toJson(this);
        // making the body of the request
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.MIXED)
                .addFormDataPart("executeTarget", executeTargetJson)
                .build();

        //making the url
        String finalUrl = HttpUrl
                .parse("http://localhost:8080/GpupWeb_Web_exploded" + "/updateExecuteTargetResult")
                .newBuilder()
                .addQueryParameter("executeTarget", executeTargetJson)
                //.addQueryParameter("logs", logs)
                .build()
                .toString();
        // making the request
        Request request = new Request.Builder().
                url(finalUrl)
                .post(body)
                .build();
        return request;
    }

    public void runAsync(Request request, OkHttpClient okHttpClient,Callback callback) {

        Call call = okHttpClient.newCall(request);

        call.enqueue(callback);
    }

    public void setConsumerForLog(Consumer consumerForLog) {
        this.consumerForLog = consumerForLog;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public void setConsumerThreadsBack(Consumer threadIsBack) {
        this.consumerThreadIsBack = threadIsBack;
    }

    public Consumer getConsumerForAmountOfCredit() {
        return consumerForAmountOfCredit;
    }

    public void setConsumerForAmountOfCredit(Consumer consumerForAmountOfCredit) {
        this.consumerForAmountOfCredit = consumerForAmountOfCredit;
    }

    public void addWhoClosedMe(String name) {
        this.ISkippedBecause.add(name);
    }

    public void setISkippedBecause(Set<String> ISkippedBecause){this.ISkippedBecause = ISkippedBecause;}

    public Set<String> getISkippedBecause(){return this.ISkippedBecause;}

    public String getWorkerThatDoneMe() {
        return workerThatDoneMe;
    }

    public void setWorkerThatDoneMe(String workerThatDoneMe) {
        this.workerThatDoneMe = workerThatDoneMe;
    }
}
