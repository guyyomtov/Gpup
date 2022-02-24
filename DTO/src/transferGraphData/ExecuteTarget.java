package transferGraphData;

import DataManager.consumerData.ConsumerTaskInfo;
import Graph.process.Minion;
import jdk.nashorn.internal.ir.LexicalContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

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
        String outPut  = "";
        Instant start, end;
        start = Instant.now();
        this.status = "IN PROCESS";
        ConsumerTaskInfo cTI = new ConsumerTaskInfo();
        cTI.openFile(this.targetName);
        outPut = "The target " + this.targetName + " about to run.";
        cTI.getInfo(outPut);
        if(whatKindOfTask.toLowerCase().equals(SIMULATION))
            runForSimulation(cTI);

        else // its compilation
            runForCompilation(cTI);

        outPut = "The system finished the task on target " + this.targetName;
        cTI.getInfo(outPut);
        outPut = "The result of the target " + this.targetName + " is: " + this.status;
        cTI.getInfo(outPut);
        cTI.closeFile();

    }

    // todo handle with the files tomorrow to see what we need to had
    private void runForSimulation(ConsumerTaskInfo cTI) {

        String outPut = "";
        Duration t = Duration.ofMillis(timeIRun);
        outPut = "The system is going to sleep for: " + String.format("%02d:%02d:%02d", t.toHours(), t.toMinutes(), t.getSeconds());
        cTI.getInfo(outPut);
        try {
            //end = Instant.now();
           // this.minionLiveData.setTimeInProcess(Duration.between(start, end).toMillis());
            Thread.sleep(timeIRun);
        } catch (InterruptedException e) {}
        this.checkTheResultAndUpdateStatus();

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

    private void runForCompilation(ConsumerTaskInfo cTI) {
        String outPut = "";
        Instant start, end;
        String openedParents = "";
        String resSrcArg = new String();
        int res = 0;
        // append second needed part
        resSrcArg += this.fullPathSource + "/" + this.generalInfo.replace('.', '/' ) + ".java";

        String[] c = {"javac", "-d", this.fullPathDestination, "-cp", this.fullPathDestination, resSrcArg};
        outPut = "Full command: javac-d"+ " " + this.fullPathDestination +  "-cp " +  this.fullPathDestination + resSrcArg;
        cTI.getInfo(outPut);

        try {
            start = Instant.now();
            Process process = Runtime.getRuntime().exec(c);
            process.waitFor();
            end = Instant.now();
            outPut = "The compiler finish the task on target " + targetName + " in: " + Duration.between(start, end).toMillis() + " milli second.";
            cTI.getInfo(outPut);

            res = process.exitValue();
            if(res == 0) // of res == 0 == > process on curr target success.
                this.status = "SUCCESS";
            else {
                this.status = "FAILURE";
                String errorMessage = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())).readLine();
                outPut = "The target is failed, output from javac: " + errorMessage;

                cTI.getInfo(outPut);

            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


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
}
