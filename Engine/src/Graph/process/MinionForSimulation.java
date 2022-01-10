package Graph.process;

import DataManager.consumerData.ConsumerTaskInfo;
import Graph.Target;
import fileHandler.TaskFile;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class MinionForSimulation extends Minion{

    protected Integer timeIRun;
    protected Integer chancesISucceed;
    protected Integer chancesImAWarning;


    public MinionForSimulation(Target target, Integer maxTime, Integer chancesISucceed, Integer chancesImAWarning, boolean isSimulation) {
        super(target, isSimulation);
    }

    public List<String> runMe(Map<String,Minion> allMinions, Consumer cUI)  {

        Instant start, end;
        start = Instant.now();
        myStatus = "IN PROCESS";
        this.setStatus(myStatus);

        List<String> resData = new ArrayList<>();
        String openedParents = new String();
        ConsumerTaskInfo cTI = new ConsumerTaskInfo(this.target.getName());

        //  cTI.getInfo(cUI, "The target " + this.target.getName() + " about to run.");
        try {
            Thread.sleep(100);
        }catch (InterruptedException e) {// e.printStackTrace();
             }

        taskConsumer.accept("The target " + this.target.getName() + " about to run.");

        // Platform.runLater();
        Duration t = Duration.ofMillis(timeIRun);
        //   cTI.getInfo(cUI, "The system is going to sleep for: " + String.format("%02d:%02d:%02d" ,t.toHours(), t.toMinutes(), t.getSeconds()));

        taskConsumer.accept("The system is going to sleep for: " + String.format("%02d:%02d:%02d", t.toHours(), t.toMinutes(), t.getSeconds()));
        try {
                end = Instant.now();
                this.minionLiveData.setTimeInProcess(Duration.between(start, end).toMillis());
                Thread.sleep(timeIRun);
            } catch (InterruptedException e) {}
        taskConsumer.accept("The system finished the task on target " + this.getName());

        //     cTI.getInfo(cUI, "The system finished the task on target " + this.getName());
        //  cUI.accept("The system finished the task on target " + this.myTargetName);

        Random rand = new Random();
        if ((rand.nextInt(101)) <= this.chancesISucceed) {

            // if warning
            if ((rand.nextInt(101)) <= this.chancesImAWarning)
                this.myStatus = "WARNING"; //success with warning
            else
                this.myStatus = "SUCCESS";


            synchronized (this) {
                openedParents = this.iOpened(this.parentsNames, allMinions);
            }
        } else {
            this.myStatus = "FAILURE";
            checkAndUpdateWhoImClosedTORunning(this, true);
        }
            //openedParents = this.simulationSpecificProcess(allMinions);

        // cTI.getInfo(cUI, "the result: " + this.myStatus);

            taskConsumer.accept("The result of the target " + this.targetName + " is: " + this.myStatus);

        if(this.myStatus.equals("SUCCESS") || this.myStatus.equals("WARNING")) {
            //cTI.getInfo(cUI, "The targets that opened to run: " + (openedParents.isEmpty() ? "nobody" : openedParents));
            taskConsumer.accept("The target " + targetName +  " opened to run: " + (openedParents.isEmpty() ? "nobody" : openedParents));
        }

        // cTI.getInfo(cUI, "----------------------------------------");
        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.getName());
        resData.add(2, this.target.getGeneralInfo());
        resData.add(3, this.myStatus);
        resData.add(4, openedParents);
        // add targets names the got free
        TaskFile.closeFile();
        //  this.aTask.updateUser();

        this.setStatus(myStatus);

        return resData;
    }



}
