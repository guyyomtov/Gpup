package DataManager;

import Graph.Target;
import fileHandler.TaskFile;

import java.io.Serializable;
import java.util.*;
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

    // from beginning
    public Task(List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean isRandom, Consumer cUI) {

        this.targets = targets;

        this.cUI = cUI;

        if (!isRandom) {
            this.timeIRun = timeToRun;
            this.chancesISucceed = chancesToSucceed;
            this.chancesImAWarning = chancesToBeAWarning;
            this.isRandom = false;
        }
        else {
            Random rand = new Random();
            this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
            this.chancesISucceed = rand.nextInt(101);
            this.chancesImAWarning = rand.nextInt(101);
            this.isRandom = true;
        }

        this.startMinions(null);
    }

    // incremental
    public Task(List<Target> targets, Task oldTask, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean isRandom, Consumer cUI) {

        this.targets = targets;

        this.cUI = cUI;

        if (!isRandom) {
            this.timeIRun = timeToRun;
            this.chancesISucceed = chancesToSucceed;
            this.chancesImAWarning = chancesToBeAWarning;
            this.isRandom = false;
        }
        else {
            Random rand = new Random();
            this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
            this.chancesISucceed = rand.nextInt(101);
            this.chancesImAWarning = rand.nextInt(101);
            this.isRandom = true;
        }

        this.startMinions(oldTask);
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public abstract void run();

    public Consumer getcUI() {
        return cUI;
    }

    public Map<String, List<String>> getLastPData(){ return this.targetNameToSummeryProcess; }

    public void startMinions(Task oldTask){

        // from beginning
        if(oldTask == null){

            for(Target curTarget : this.targets)
                this.minions.add(new Minion(curTarget, this.timeIRun, this.chancesISucceed, this.chancesImAWarning));
        }
        else// incremental
            this.initCurMinFrom(oldTask.getMinions());
    }

    public void initCurMinFrom(List<Minion> oldMins){

        String oldStatus;
        String targetType;

        // go over all old
        for(Minion curOldMin : oldMins){

            oldStatus =  curOldMin.getMyStatus();
            targetType = curOldMin.getTarget().getTargetType().toString();

            Minion newMin = new Minion(curOldMin.getTarget(), this.timeIRun, this.chancesISucceed, this.chancesImAWarning);

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
}
