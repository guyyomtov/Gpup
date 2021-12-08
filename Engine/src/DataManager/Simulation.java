package DataManager;

import Graph.Target;
import consumerData.FormatAllTask;
import consumerData.ProcessInfo;
import fileHandler.TaskFile;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Simulation extends Task implements Serializable {

    // from beginning
    public Simulation(List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean isTimeRandom, Consumer cUI) {

        super(targets, timeToRun, chancesToSucceed, chancesToBeAWarning, isTimeRandom, cUI);

        this.setName();

        TaskFile taskFile = new TaskFile();
        taskFile.makeTaskDir(this.taskName);
    }

    // incremental
    public Simulation(List<Target> targets, Task oldTask, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean isTimeRandom, Consumer cUI) {

        super(targets, oldTask, timeToRun, chancesToSucceed, chancesToBeAWarning, isTimeRandom, cUI);

        this.setName();

        TaskFile taskFile = new TaskFile();
        taskFile.makeTaskDir(this.taskName);
    }

    public void run(){


        //setup data
        Map<String, Target> namesToTargetsMap = Target.startTargetMap(this.targets);
        Map<String, Minion> namesToMinions     = Minion.startMinionMapFrom(this.minions);
        Map<String, Map<String, Minion>>    typeOfTargetToTargetNameToHisTask   = this.startMinionMapByTargetType(namesToMinions, namesToTargetsMap);


        // setup data
        Map<String,Minion> independents = typeOfTargetToTargetNameToHisTask.get("Independent");
        Map<String,Minion> leaves = typeOfTargetToTargetNameToHisTask.get("Leaf");
        Map<String,Minion> middles = typeOfTargetToTargetNameToHisTask.get("Middle");
        Map<String,Minion> roots = typeOfTargetToTargetNameToHisTask.get("Root");


        FormatAllTask.restartMap();
        FormatAllTask.start = Instant.now();

        // start process

        // go over all targets:

        // 1) start with independents
        this.runTheseTargetsByType(independents,namesToMinions);

        // move to leaves
        this.runTheseTargetsByType(leaves,namesToMinions);

        // then to middle
        this.runTheseTargetsByType(middles,namesToMinions);

        // then to root
        this.runTheseTargetsByType(roots,namesToMinions);

        FormatAllTask.end = Instant.now();
        FormatAllTask.sendData(cUI);


        FormatAllTask.sendData(cUI, this.targetNameToSummeryProcess);
        ProcessInfo.setOldTask(this);
    }

    private Map<String, Map<String,Minion>> startMinionMapByTargetType(Map<String, Minion> namesToTasks, Map<String, Target> namesToTarget) {

        String curTargetType = new String();
        Map<String, Map<String,Minion>> resM = new HashMap<>();

        resM.put("Independent", new HashMap<>());
        resM.put("Leaf", new HashMap<>());
        resM.put("Middle", new HashMap<>());
        resM.put("Root", new HashMap<>());

        for(String curTaskName : namesToTasks.keySet()){

            curTargetType = namesToTarget.get(curTaskName).getTargetType().toString();

            resM.get(curTargetType).put(curTaskName, namesToTasks.get(curTaskName));
        }
        return resM;
    }

    private void runTheseTargetsByType(Map<String,Minion> curMinions, Map<String, Minion> namesToMinions){

        Boolean curMinionsFinished = false;

        if(!curMinions.keySet().isEmpty()) {

            while (!curMinionsFinished) {

                this.runTheseTasks(new ArrayList<>(curMinions.values()), namesToMinions); // go over all cur tasks and get the needed data back

                curMinionsFinished = checkIfWeFinished(new ArrayList<>(curMinions.values())); // go over all cur tasks and check if finished (with get
            }
        }
    }

    private void runTheseTasks(List<Minion> minions, Map<String, Minion> namesToMinions){

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,
        List<String> curTaskData = new ArrayList<>();
        List<Minion> kids = new ArrayList<Minion>();

        for(Minion curM : minions){

            if(this.iAllReadyRan(curM))
                curTaskData = this.getOldDataFrom(curM);
            else{

                kids = findMyKids(curM, namesToMinions);
                curTaskData = curM.tryToRunMe(kids, namesToMinions, this.cUI);
                if(!curTaskData.isEmpty()) {
                    this.targetNameToSummeryProcess.put(curM.getName(), curTaskData);
                    FormatAllTask.updateCounter(curTaskData.get(3));// the status
                }
            }
        }
    }

    private Boolean checkIfWeFinished(List<Minion> minions){

        Boolean finished = true;

        for(Minion curM : minions){

            if(!curM.imFinished()) {
                finished = false;
                break;
            }
        }
        return  finished;
    }

    private List<String> getOldDataFrom(Minion curM){

        List<String> resData = new ArrayList<>();

        resData.add(0, "0");
        resData.add(1, curM.getName());
        resData.add(2, curM.getMyTargetGenaralInfo());
        resData.add(3, curM.getMyStatus());
        resData.add(4, "");

        return resData;
    }

    private static List<Minion> findMyKids(Minion curT, Map<String, Minion> namesToTasks){

        List<String> kidsNames = curT.getMyKidsNames();
        List<Minion> kids = new ArrayList<Minion>();

        for(String curKidName : kidsNames)
            kids.add(namesToTasks.get(curKidName));

        return kids;
    }

    private Boolean iAllReadyRan(Minion curM){

        Boolean iRan = false;

        if(curM.getMyStatus().equals("WARNING") || curM.getMyStatus().equals("SUCCESS") || curM.getMyStatus().equals("SKIPPED"))
            iRan = true;

        return iRan;
    }
    
    public void setName(){this.taskName = "simulation";}


}
