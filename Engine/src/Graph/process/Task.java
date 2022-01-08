package Graph.process;

import Flagger.Flagger;
import Graph.Target;
import errors.ErrorUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public abstract class Task extends javafx.concurrent.Task<Object> implements Serializable, Consumer<String>{

    protected String taskName;
    protected Integer timeIRun;
    protected Integer chancesISucceed;
    protected Integer chancesImAWarning;
    protected Boolean isRandom;
    protected List<Target> targets = new ArrayList<>();
    protected List<Minion> minions = new ArrayList<>();
    protected List<Minion> minionsChosenByUser;
    protected Consumer cUI;
    protected Map<String, List<String>> targetNameToSummeryProcess = new HashMap<>();
    protected static Queue<Minion> waitingList = new LinkedList<>();
    public static int maxParallelism;
    public static Integer threadCounter = 0;


    public Task(DataSetupProcess dSp) throws ErrorUtils {

        if(dSp.allGraphTargets == null || dSp.timeToRun == null
        || dSp.chancesToSucceed == null || dSp.chancesToBeAWarning == null){

            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);
        }

        this.targets = dSp.allGraphTargets;
        this.timeIRun = dSp.timeToRun;
        this.chancesISucceed = dSp.chancesToSucceed;
        this.chancesImAWarning = dSp.chancesToBeAWarning;
        maxParallelism = dSp.amountOfThreads;

        if (dSp.flagger.processFromScratch) {

            // make all minions from scratch
            this.startMinions();
        }
        else if (dSp.flagger.processIncremental) {

            // copy minions from last task
            this.startMinions(dSp.oldTask);
        }
        else if(dSp.flagger.processFromRandomTargets){

            //make on chosen minions with 100% succeed
            this.startMinions(dSp.minionsChoosenByUser);
        }
        AddDataOnMinions();
    }

    // from scratch
    private void startMinions(){

        this.minions = Minion.makeMinionsFrom(this.targets, this.timeIRun, this.chancesISucceed, this.chancesImAWarning);
        this.minionsChosenByUser = this.minions;
        this.updateMinions();
    }

    private void updateMinions() {
        for(Minion minion : minions)
            minion.setConsumer(this);
    }

    private void startMinions(Task oldTask){

        this.initCurMinFrom(oldTask.getMinions());
    }

    private void startMinions(List<Minion> userMinions){

        List<String> existingMinions = Minion.getMinionsNames(userMinions);

        //add missing minions with 100% success
        for(Target curTarget : this.targets) {

            if(!existingMinions.contains(curTarget.getName()))
                this.minions.add(Minion.startMinionWithSuccessFrom(curTarget));
        }
        // process wanted minions
        this.minions.addAll(userMinions);
        this.minionsChosenByUser = userMinions;
        this.updateMinions();
    }


    public List<Minion> getMinions() { return minions; }

    public abstract void run();

    public Consumer getcUI() { return cUI; }

    public Map<String, List<String>> getLastPData(){ return this.targetNameToSummeryProcess; }

    private void AddDataOnMinions() {

        Map<String, Minion> namesToMinions = Minion.startMinionMapFrom(this.minions);

        for(Minion curM : this.minions) {
            curM.initMyKindsAndParents(this.minions);
            curM.setAllNamesToMinions(namesToMinions);
            curM.setcUI(this.cUI);
        }
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

    public void makeQueue() {

        for(Minion curM : this.minions)
            if(curM.getCanIRun())
                waitingList.add(curM);
    }

}
