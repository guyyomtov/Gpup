package Graph.process;

import DataManager.BackDataManager;
import DataManager.consumerData.FormatAllTask;
import DataManager.consumerData.ProcessInfo;
import Graph.Target;
import com.sun.jmx.snmp.tasks.ThreadService;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class Task implements Serializable, Consumer<String>{

    private  Integer userAmountWantedThread;
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
    protected Queue<Minion> waitingList = new LinkedList<>();
    //public static int maxParallelism;
    public Integer threadCounter = 0;
    protected Map<String, Minion> namesToCurRunningMinions = new HashMap<>();
    protected Map<String, Set<Target>> serialSetsNameToTargets;
    protected BackDataManager bDM;
    protected BooleanProperty pauseTaskProperty;
    protected String infoOfLastProcess;

    public Task(DataSetupProcess dSp) throws ErrorUtils {


        this.bDM = dSp.bDM;
        this.targets = dSp.allGraphTargets;
        this.timeIRun = dSp.timeToRun;
        this.chancesISucceed = dSp.chancesToSucceed;
        this.chancesImAWarning = dSp.chancesToBeAWarning;
      //  maxParallelism = dSp.amountOfThreads;
        this.serialSetsNameToTargets = dSp.serialSets;
        this.pauseTaskProperty = dSp.pauseTask;
        this.infoOfLastProcess = dSp.lastProcessTextArea != null ? dSp.lastProcessTextArea : "";
        this.userAmountWantedThread = dSp.amountOfThreads;
        this.taskName = dSp.taskName;

        this.minionsChosenByUser = dSp.minionsChoosenByUser;

        this.startMinions(minionsChosenByUser);

        if(dSp.flagger.processIncremental){
            this.updateMinionsForIncrementalProcess(); // anotherProcess
        }


        // if random --> chances & time random
        if(dSp.flagger.chancesIsRandomInProcess){

            this.startMinionWithRandPercent();
        }

        this.AddDataOnMinions();

        this.makeQueue();
    }

    public Task(DataSetupProcess dSp, boolean isDemo) {

        this.bDM = dSp.bDM;
        this.targets = dSp.allGraphTargets;
        this.minionsChosenByUser = dSp.minionsChoosenByUser;
        this.serialSetsNameToTargets = dSp.serialSets;

        if(dSp.flagger.processIncremental){
            this.updateMinionsForIncrementalProcess(); // anotherProcess
        }
        this.startMinions(minionsChosenByUser);
        this.AddDataOnMinions();
        this.updateStatusOfUnchosenMinions();
    }

    private void startMinionWithRandPercent() {

        Random rand = new Random();
        int upperBoundSucceed = this.chancesISucceed + 1;
        int upperBoundWarning = this.chancesImAWarning + 1;
        int upperBoundTime = this.timeIRun + 1;

        // rand new values
        this.chancesISucceed =rand.nextInt(upperBoundSucceed);
        this.chancesImAWarning = rand.nextInt(upperBoundWarning);
        this.timeIRun = rand.nextInt(upperBoundTime);

        // go over all minions & give them new chance value
        for(Minion curM : this.minionsChosenByUser){
            curM.chancesISucceed = this.chancesISucceed;
            curM.chancesImAWarning = this.chancesImAWarning;
            curM.timeIRun = this.timeIRun;
        }
    }

    private void updateMinionsForIncrementalProcess() {

        for(Minion minion : minionsChosenByUser){
            String myStatus = minion.getStatus();
            if(myStatus.equals("SKIPPED") || myStatus.equals("FAILURE"))
            {
                checkIfImWaitingOrFrozen(minion, minion);
            }
        }
    }

    private void checkIfImWaitingOrFrozen(Minion minionToUpdate, Minion minionKid) {
        List<Minion> kids = minionKid.getMyKids();
        if(kids.isEmpty())
        {
            minionToUpdate.setStatus("WAITING");
            minionToUpdate.setMyStatus("WAITING");
            minionToUpdate.setiAmFinished(false);
        }
        else
        {
            for (Minion currMin : kids) {
               // if (this.minionsChosenByUser.contains(currMin)) {
                    if (!currMin.getStatus().equals("SUCCESS") || !currMin.getStatus().equals("WARNING")) {
                        minionToUpdate.setStatus("FROZEN");
                        minionToUpdate.setMyStatus("FROZEN");
                        minionToUpdate.setiAmFinished(false);
                        return;
                    } else
                        this.checkIfImWaitingOrFrozen(minionToUpdate,currMin);
                //}
            }
        }
    }

    // from scratch
    private void startMinions(){

        this.minions = Minion.makeMinionsFrom(this.targets, this.timeIRun, this.chancesISucceed, this.chancesImAWarning, minionsChosenByUser.get(0).getImSimulation());
        this.minionsChosenByUser = this.minions;
      //  this.updateMinionLiveDataSerialSet();
        this.updateMinions();
    }

    private void updateMinions() {
        for(Minion minion : minions) {
            minion.setConsumer(this);
        }
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
       // this.updateMinionLiveDataSerialSet(); // todo to add this in all the function that start minions!!!!
        this.updateMinions();
    }
// todo guy problem in the ohh no
//    private void updateMinionLiveDataSerialSet() {
//        for(Minion minion : minionsChosenByUser){
//            minion.getMinionLiveData().setNamesOfSerialSetsThatMinionInclude(this.serialSetsThatIncluded(minion));
//        }
//    }

    private List<String> serialSetsThatIncluded(Minion currMinion) {
        List<String> namesOfSerialSet = new ArrayList<>();
        Target currTarget = currMinion.getTarget();
        for(String nSerialSet : serialSetsNameToTargets.keySet()){
            if(serialSetsNameToTargets.get(nSerialSet).contains(currTarget))
                namesOfSerialSet.add(nSerialSet);
        }
        return namesOfSerialSet;
    }

    public List<Minion> getMinions() { return minions; }

    public List<Minion> getMinionsThatUserChose() { return this.minionsChosenByUser; }

    public Consumer getcUI() { return cUI; }

    public Map<String, List<String>> getLastPData(){ return this.targetNameToSummeryProcess; }

    private void AddDataOnMinions() {

        Map<String, Minion> namesToMinions = Minion.startMinionMapFrom(this.minions);

        for(Minion curM : this.minions) {
            curM.initMyKindsAndParents(this.minions);
            curM.checkAndUpdateWhoImClosedTORunning(curM, false);
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

            Minion newMin = new Minion(curOldMin.getTarget(), this.timeIRun, this.chancesISucceed, this.chancesImAWarning, curOldMin.getImSimulation());

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
        waitingList.clear();
        for(Minion curM : this.minionsChosenByUser)
            if(curM.getCanIRun() && !curM.imFinished())
                waitingList.add(curM);
    }

    protected Boolean iCanRunSerialSet(Minion minionToRun){

        Boolean canHeRun = true;
        String curMinionName = minionToRun.getName();

        // check to see if i'm a part of a serial set
        if(this.iAmInASerialSet(minionToRun)){

            // get all relevant serial sets which are relevant to me (could be a part of more than one)
            Map<String, Set<Target>> relevantSerials = this.getRelevantSerialsTo(minionToRun);

            // go over all relevant serial sets:
            for(String curSerialName : relevantSerials.keySet()){

                //get cur targets
                Set<String> targetNames = this.getTargetsNamesUsing(curSerialName);

                // remove minion name, isn't relevant for the check
                targetNames.remove(curMinionName);

                for(String curTName : targetNames){

                    if(this.targetIsCurentlyInProcess(curTName)){

                        canHeRun = false;
                        break;
                    }
                }
            }
        }

       return canHeRun;
    }

    private boolean targetIsCurentlyInProcess(String curTName) {

        Boolean curTIsRunningNow = false;

        // if he is in map of cur process
        if (this.namesToCurRunningMinions.containsKey(curTName)) {

            Minion curMinion = this.namesToCurRunningMinions.get(curTName);

            // cur minion is running & is still in process
            if (!curMinion.imFinished()) {

                curTIsRunningNow = true;
            }
            else{// cur T is in map of process, but he finished the process

                // take out of in process list
                this.namesToCurRunningMinions.remove(curTName);
            }
        }
        return curTIsRunningNow;
    }

    private Map<String, Set<Target>> getRelevantSerialsTo(Minion minionToRun) {

        Map<String, Set<Target>> res = new HashMap<>();
        String curMinionName = minionToRun.getName();

        // go over all serial sets
        for(String curSerialName : this.serialSetsNameToTargets.keySet()){

            Set<String> curTsNames =  this.getTargetsNamesUsing(curSerialName);

            // go over each set of targets
            for(String curTName : curTsNames){

                // if minion name is in cur serial set, add it to the list
                if(curMinionName == curTName)
                    res.put(curSerialName, this.serialSetsNameToTargets.get(curSerialName));
            }
        }
        return res;
    }

    private Set<String> getTargetsNamesUsing(String curSerialName){

        Set<Target> curTargets = this.serialSetsNameToTargets.get(curSerialName);

        return Target.getTargetNamesFrom(new ArrayList<Target>(curTargets));
    }

    private boolean iAmInASerialSet(Minion minionToRun) {

        boolean iAmInASerialSet = false;
        String curMinionName = minionToRun.getName();

        // go over all serial sets
        for(String curSerialName : this.serialSetsNameToTargets.keySet()){

            Set<String> curTsNames =  this.getTargetsNamesUsing(curSerialName);

            // go over all current serial sets targets
            for(String curTName : curTsNames){

                if(curMinionName == curTName){

                    iAmInASerialSet = true;
                    break;
                }
            }
        }
        return iAmInASerialSet;
    }

    //should be in the worker.
//    @Override
//    protected Object call() throws Exception {
//
//      //  updateMessage(infoOfLastProcess);
//        Instant start , end;
//        //Map<String, Minion> test = new HashMap<>();
//        ExecutorService executorService = Executors.newFixedThreadPool(this.userAmountWantedThread);
//        Minion.MinionLiveData minionLiveData;
//
//        //first case
//        Minion minion = waitingList.poll();
//        executorService.execute(minion);
//
//        threadCounter++;
//        this.namesToCurRunningMinions.put(minion.getName(), minion);
//
//        Integer totalMinionsThatFinished = updateTotalMinionsThatFinished();
//
//        // go out of while when: no thread exist & queue is empty
//        while((threadCounter != 0 || !waitingList.isEmpty()) /*&& !pauseTaskProperty.getValue()*/) {
//
//            if(pauseTaskProperty.getValue())
//                this.pauseProcess();
//
//            start = Instant.now();
//            minion = waitingList.poll();
//
//
//            totalMinionsThatFinished = updateTotalMinionsThatFinished();
//           // updateProgress(totalMinionsThatFinished, minionsChosenByUser.size());
//            //  updateMessage(message);
//
//            if (minion != null) {
//
//                if(this.iCanRunSerialSet(minion)) {
//
//                    executorService.execute(minion);
//
//                    threadCounter++;
//                    this.namesToCurRunningMinions.put(minion.getName(), minion);
//                }
//                else{// cur minion can't run because of serial set, put back in queue
//                    waitingList.add(minion);
//                    end = Instant.now();
//                    minionLiveData = minion.getMinionLiveData();
//                    this.updateHowLongMinionWaiting(minionLiveData, start, end);
//                }
//            }
//            else {
//
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {/* e.printStackTrace();*/ }
//            }
//        }
//
//        totalMinionsThatFinished = updateTotalMinionsThatFinished();
//       // updateProgress(totalMinionsThatFinished, minionsChosenByUser.size());
//        executorService.shutdown();
//        executorService.awaitTermination(60, TimeUnit.SECONDS);
//        this.namesToCurRunningMinions.clear();
//
//        return true;
//    }

    private Integer updateTotalMinionsThatFinished() {
        Integer res = 0;
        for(Minion minion : minionsChosenByUser){
            if(minion.imFinished())
                ++res;
        }
        return res;
    }

    private void updateHowLongMinionWaiting(Minion.MinionLiveData minionLiveData, Instant start, Instant end){
        Duration timeElapsed = Duration.between(start, end);
        minionLiveData.setTimeISWaiting(timeElapsed.toMillis());
    }

    @Override
    public void accept(String s) {
       // if(s != "")
          //  Platform.runLater(()-> updateMessage(getMessage() + "\n" + s));
    }

   // @Override
    public void run(){

        Instant start, end;
        this.cUI = new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        };

        start = Instant.now();

        this.makeQueue();
        end = Instant.now();
        this.makeSummaryOfProcess(Duration.between(start, end));

        FormatAllTask.sendData(cUI, this.targetNameToSummeryProcess);
        ProcessInfo.setOldTask(this);
    }

    private void updateStatusOfUnchosenMinions() {
        for(Minion minion : minions){
            if(!this.minionsChosenByUser.contains(minion))
                minion.iOpened(minion.parentsNames, minion.getAllNamesToMinions());
            else{ // minion that the user chose
                if(minion.getStatus().equals("SUCCESS") || minion.getStatus().equals("WARNING"))
                    minion.iOpened(minion.parentsNames, minion.getAllNamesToMinions());
            }

        }
    }

    private void makeSummaryOfProcess(Duration timeElapsed) {
        String summary = new String();
        Map<String, Integer> mStatusToNumber = makeMapStatusToNumber();
        String time = String.format("%02d:%02d:%02d" ,timeElapsed.toHours(), timeElapsed.toMinutes(), timeElapsed.getSeconds());
        summary = "-----------------------------------------------" + "\n";
        summary += "The result of the process: " + "\n";
        summary += "Total Time: " + time + "\n";
        summary += "Targets that ended with 'SUCCESS': " + mStatusToNumber.get("SUCCESS") + "\n";
        summary += "Targets that ended with 'WARNING': " + mStatusToNumber.get("WARNING") + "\n";
        summary += "Targets that ended with 'FAILURE': " + mStatusToNumber.get("FAILURE") + "\n";
        summary += "Targets that ended with 'SKIPPED': " + mStatusToNumber.get("SKIPPED") + "\n";
        summary += "-----------------------------------------------";
        String messageThatProcessDone = checkIfProcessDone(mStatusToNumber);
     //   updateMessage(getMessage() +  "\n" + summary + "\n" + messageThatProcessDone);

    }

    private String checkIfProcessDone(Map<String, Integer> mStatusToNumber) {
        if(mStatusToNumber.get("SUCCESS") + mStatusToNumber.get("WARNING") >= this.minionsChosenByUser.size())
            return "Process is done!!!";
        else
            return "";
    }

    private Map<String, Integer> makeMapStatusToNumber() {
            Map<String, Integer> mStatusToNumber = new HashMap<>();
            mStatusToNumber.put("SUCCESS", 0);
            mStatusToNumber.put("WARNING", 0);
            mStatusToNumber.put("FAILURE", 0);
            mStatusToNumber.put("SKIPPED", 0);
            mStatusToNumber.put("FROZEN", 0);
            for (Minion minion : this.minionsChosenByUser) {
                String myStatus = minion.getStatus();
                Integer tmp = mStatusToNumber.get(myStatus);
                if (tmp != null)
                    mStatusToNumber.put(myStatus, tmp + 1);
            }
            return mStatusToNumber;
    }

    public void pauseProcess() {
        try {
            synchronized (this){this.wait();}

        } catch (InterruptedException e) {
            //e.printStackTrace();
        }

    }

    public void resumeProcess(){
        synchronized (this){this.notify();}
    }

    public void cancelTask() {
      //  this.cancel();
    }

    public String getTaskName(){return this.taskName;}

    public Queue<Minion> getWaitingList(){return this.waitingList;}
}
