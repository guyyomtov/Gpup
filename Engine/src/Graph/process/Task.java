package Graph.process;

import Graph.Target;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public abstract class Task implements Serializable {

    protected String taskName;
    protected Integer timeIRun;
    protected Integer chancesISucceed;
    protected Integer chancesImAWarning;
    protected Boolean isRandom;
    protected List<Target> targets = new ArrayList<>();
    protected List<Minion> minions = new ArrayList<>();
    protected Consumer cUI;
    protected Map<String, List<String>> targetNameToSummeryProcess = new HashMap<>();
    protected static Queue<Minion> waitingList = new LinkedList<>();
    public static int maxParallelism;


    // from beginning
    public Task(List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean timeIsRandom, Consumer cUI) {

        this.targets = targets;
        this.cUI = cUI;
        this.timeIRun = timeToRun;
        this.chancesISucceed = chancesToSucceed;
        this.chancesImAWarning = chancesToBeAWarning;

        if(timeIsRandom){

            Integer maxTime = this.timeIRun;

            this.isRandom = true;

            this.startMinions(null, maxTime);
        }
        else{

            this.isRandom = false;

            this.startMinions(null);
        }
    }

    // incremental
    public Task(List<Target> targets, Task oldTask, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean timeIsRandom, Consumer cUI) {

        this.targets = targets;
        this.cUI = cUI;
        this.timeIRun = timeToRun;
        this.chancesISucceed = chancesToSucceed;
        this.chancesImAWarning = chancesToBeAWarning;

        if(timeIsRandom){

            Integer maxTime = this.timeIRun;

            this.isRandom = true;

            this.startMinions(oldTask, maxTime);
        }
        else{

            this.isRandom = false;

            this.startMinions(oldTask);
        }
    }

    public List<Minion> getMinions() { return minions; }

    public abstract void run();

    public Consumer getcUI() { return cUI; }

    public Map<String, List<String>> getLastPData(){ return this.targetNameToSummeryProcess; }

    public void startMinions(Task oldTask){

        // from beginning
        if(oldTask == null){

            for(Target curTarget : this.targets)
                this.minions.add(new Minion(curTarget, this.timeIRun, this.chancesISucceed, this.chancesImAWarning, false));
        }
        else// incremental
            this.initCurMinFrom(oldTask.getMinions());
        AddDataOnMinions();


    }

    private void AddDataOnMinions() {

        Map<String, Minion> namesToMinions = Minion.startMinionMapFrom(this.minions);

        for(Minion curM : this.minions) {
            curM.initMyKindsAndParents(this.minions);
            curM.setAllNamesToMinions(namesToMinions);
            curM.setcUI(this.cUI);
        }
    }

    // when time random
    public void startMinions(Task oldTask, Integer maxPossibleTime){

        // from beginning
        if(oldTask == null){

            for(Target curTarget : this.targets)
                this.minions.add(new Minion(curTarget, maxPossibleTime, this.chancesISucceed, this.chancesImAWarning, true));
        }
        else// incremental
            this.initCurMinFrom(oldTask.getMinions(), maxPossibleTime);
    }

    //when time random
    public void initCurMinFrom(List<Minion> oldMins, Integer maxPossibleTime){

        String oldStatus;
        String targetType;

        // go over all old
        for(Minion curOldMin : oldMins){

            oldStatus =  curOldMin.getMyStatus();
            targetType = curOldMin.getTarget().getTargetType().toString();

            Minion newMin = new Minion(curOldMin.getTarget(), maxPossibleTime, this.chancesISucceed, this.chancesImAWarning, true);

            // if success || warning --> give it to new minions
            if(oldStatus.equals("WARNING") || oldStatus.equals("SUCCESS")){
                newMin.setMyStatus(oldStatus);
                newMin.setiAmFinished(true);
                newMin.setCanIRun(true);
            }
            else if(oldStatus.equals("SKIPPED")) {

                newMin.setMyStatus("FROZEN");
                newMin.setiAmFinished(false);
                newMin.setCanIRun(false);
            }
            else if(oldStatus.equals("FAILURE") ){

                if(targetType.equals("Leaf") || targetType.equals("Independent")) {

                    newMin.setMyStatus("WAITING");
                    newMin.setiAmFinished(false);
                    newMin.setCanIRun(true);
                }
                else { // middle or root

                    newMin.setMyStatus("FROZEN");
                    newMin.setiAmFinished(false);
                    newMin.setCanIRun(false);
                }
            }
            this.minions.add(newMin);
        }
    }

    public void initCurMinFrom(List<Minion> oldMins){

        String oldStatus;
        String targetType;

        // go over all old
        for(Minion curOldMin : oldMins){

            oldStatus =  curOldMin.getMyStatus();
            targetType = curOldMin.getTarget().getTargetType().toString();

            Minion newMin = new Minion(curOldMin.getTarget(), this.timeIRun, this.chancesISucceed, this.chancesImAWarning, false);

            // if success || warning --> give it to new minions
            if(oldStatus.equals("WARNING") || oldStatus.equals("SUCCESS")){
                newMin.setMyStatus(oldStatus);
                newMin.setiAmFinished(true);
                newMin.setCanIRun(true);
            }
            else if(oldStatus.equals("SKIPPED")) {

                newMin.setMyStatus("FROZEN");
                newMin.setiAmFinished(false);
                newMin.setCanIRun(false);
            }
            else if(oldStatus.equals("FAILURE") ){

                if(targetType.equals("Leaf") || targetType.equals("Independent")) {

                    newMin.setMyStatus("WAITING");
                    newMin.setiAmFinished(false);
                    newMin.setCanIRun(true);
                }
                else { // middle or root

                    newMin.setMyStatus("FROZEN");
                    newMin.setiAmFinished(false);
                    newMin.setCanIRun(false);
                }
            }
            this.minions.add(newMin);
        }
    }

    public abstract void setName();

    public void makeQueue() {
        for(Minion curM : this.minions)
            if(curM.getCanIRun())
                waitingList.add(curM);

    }

}
