package Graph.process;

import DataManager.consumerData.FormatAllTask;
import Graph.Target;
import DataManager.consumerData.ConsumerTaskInfo;
import fileHandler.TaskFile;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

public class Minion implements Serializable, Runnable {

    private Target target;
    private String myStatus = new String();
    private Boolean canIRun;
    private Boolean iAmFinished;
    private List<String> parentsNames = new ArrayList<>();
    private List<String> myKidsNames = new ArrayList<>();
    private List<Minion> parents = new ArrayList<>();
    private List<Minion> myKids = new ArrayList<>();
    private List<String> myPData = new ArrayList<>();
    private Consumer cUI;
    protected Integer timeIRun;
    protected Integer chancesISucceed;
    protected Integer chancesImAWarning;


    private Map<String, Minion> allNamesToMinions;

    public Minion(Target target, Integer maxTime, Integer chancesISucceed, Integer chancesImAWarning, boolean timeIsRand ){

        this.target = target;
        this.myStatus = this.myStartStatus(target.getTargetType().toString());
        this.canIRun = this.myStatus.equals("WAITING") ? true : false;
        this.iAmFinished = false;
        this.chancesISucceed = chancesISucceed;
        this.chancesImAWarning = chancesImAWarning;
        this.setMyKidsNames();
        this.setMyParentsNames();
        if(timeIsRand){

            Random rand = new Random();

            this.timeIRun = rand.nextInt(maxTime); // gives a value from 0 to maxTime
        }
        else{
            this.timeIRun = maxTime;
        }
    }

    public static List<Minion> getMinionsByName(List<String> minionsNames, Map<String, Minion> namesToMinions) {

        List<Minion> res = new ArrayList<>();

        for(String curMinName : minionsNames)
            res.add(namesToMinions.get(curMinName));

        return res;
    }

    public void setiAmFinished(Boolean iAmFinished) { this.iAmFinished = iAmFinished; }

    public void setCanIRun(Boolean canIRun) { this.canIRun = canIRun;}

    public void setMyStatus(String newS){this.myStatus = newS;}

    public List<String> getParentsNames() { return parentsNames;}

    public String getName(){return target.getName();}

    public String getMyTargetGenaralInfo(){ return this.target.getGeneralInfo();}

    public String getMyStatus(){ return this.myStatus; }

    public List<String> getMyKidsNames(){return this.myKidsNames;}

    private String myStartStatus(String tType){

        if(tType.equals("Leaf") || tType.equals("Independent"))
            return "WAITING";
        return "FROZEN";
    }

    public List<String> tryToRunMe(List<Minion> myKids, Map<String,Minion> allMinions, Consumer cUI){

        String myS = this.myStatus;

        if(myS.equals("SKIPPED")  || myS.equals("FAILURE")  || myS.equals("WARNING") || myS.equals("SUCCESS")) { // return nothing
            return this.myPData;
        }
        else if(!this.myStatus.equals("WAITING")) { // i'm not a leaf\independent but maybe i can start

            this.myStatus = checkStatusOfMyKidsAndUpdateMe(myKids);
            this.myPData = this.giveMyData();
        }

        if(this.myStatus.equals("WAITING"))
            this.myPData = this.runMe(allMinions, cUI);


        return this.myPData;
    }

    public List<String> runMe(Map<String,Minion> allMinions, Consumer cUI)  {

        List<String> resData = new ArrayList<>();
        String openedParents = new String();
        ConsumerTaskInfo cTI = new ConsumerTaskInfo(this.target.getName());
        cTI.getInfo(cUI, "The target " + this.target.getName() + " about to run.");

        Duration t = Duration.ofMillis(timeIRun);
        cTI.getInfo(cUI, "The system is going to sleep for: " + String.format("%02d:%02d:%02d" ,t.toHours(), t.toMinutes(), t.getSeconds()));
        try {
            Thread.sleep(timeIRun);
        }catch(InterruptedException e){}

        cTI.getInfo(cUI, "The system finished the task on target " + this.getName());
        //  cUI.accept("The system finished the task on target " + this.myTargetName);

        Random rand = new Random();
        if((rand.nextInt(101)) <= this.chancesISucceed){

            // if warning
            if((rand.nextInt(101)) <= this.chancesImAWarning)
                this.myStatus = "WARNING"; //success with warning
            else
                this.myStatus = "SUCCESS";
            openedParents = this.iOpened(this.parentsNames, allMinions);
        }
        else
            this.myStatus = "FAILURE";

        this.iAmFinished = true;
        cTI.getInfo(cUI, "the result: " + this.myStatus);
        if(this.myStatus.equals("SUCCESS") || this.myStatus.equals("WARNING"))
            cTI.getInfo(cUI, "The targets that opened to run: " + (openedParents.isEmpty() ? "nobody": openedParents ));

        cTI.getInfo(cUI, "----------------------------------------");


        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.getName());
        resData.add(2, this.target.getGeneralInfo());
        resData.add(3, this.myStatus);
        resData.add(4, openedParents);
        // add targets names the got free
        TaskFile.closeFile();
        return resData;
    }

    public Boolean imFinished(){return this.iAmFinished;}

    public void setMyParentsNames(){

        for(Target curT : this.target.getRequiredFor())
            this.parentsNames.add(curT.getName());

    }

    public void setMyKidsNames(){

        for(Target curT : this.target.getDependsOn())
            this.myKidsNames.add(curT.getName());
    }

    public Boolean getCanIRun() {
        return canIRun;
    }

    private String checkStatusOfMyKidsAndUpdateMe(List<Minion> myKids){

        String resStatus = new String();
        Boolean iCanRun = true;

        for(Minion curKid : myKids) {

            if (curKid.getMyStatus().equals("FAILURE") || curKid.getMyStatus().equals("SKIPPED")) {

                resStatus = "SKIPPED";
                this.iAmFinished = true;
                iCanRun = false;
                break;
            }

            else if(curKid.getMyStatus().equals("IN PROCESS") || curKid.getMyStatus().equals("WAITING") || curKid.getMyStatus().equals("FROZEN")) {
                resStatus = "FROZEN";
                iCanRun = false;
            }
        }
        if(iCanRun)
            resStatus = "WAITING";

        this.canIRun = iCanRun;

        return resStatus;
    }

    private List<String> giveMyData(){

        List<String> resData = new ArrayList<>();

        if(this.myStatus != "WAITING" && this.myStatus != "FROZEN" && this.myStatus != "IN PROCESS") {

            resData.add(0, String.valueOf(this.timeIRun));
            resData.add(1, this.getName());
            resData.add(2, this.target.getGeneralInfo());
            resData.add(3, this.myStatus);
            resData.add(4, new String());
        }

        return resData;
    }

    private String iOpened(List<String> parentsNames, Map<String,Minion> allMinions){

        String resNames = new String();
        Boolean addThisDad = true;

        Map<String, List<String>> dadsNamesToTheirKidsName = new HashMap<>();
        for(String curPName : parentsNames)
            dadsNamesToTheirKidsName.put(curPName, new ArrayList<>());

        // start the map --> all relevant dads and their kids
        for(String curDadName :  dadsNamesToTheirKidsName.keySet()){

            Minion curDad = allMinions.get(curDadName);

            dadsNamesToTheirKidsName.get(curDadName).addAll(curDad.getMyKidsNames());
        }

        // go over all parents
        for(String curDadName :  dadsNamesToTheirKidsName.keySet()){

            addThisDad = true;

            // go over each kid
            for(String curKidName : dadsNamesToTheirKidsName.get(curDadName)){

                Minion curKid = allMinions.get(curKidName);

                if(!curKid.getMyStatus().equals("SUCCESS") && !curKid.getMyStatus().equals("WARNING"))
                    addThisDad = false;
            }
            if(addThisDad)
                resNames +=  " " + curDadName;
        }
        return resNames;
    }

    public Target getTarget(){ return this.target;}

    static public Map<String, Minion> startMinionMapFrom(List<Minion> minions){

        Map<String, Minion> resM = new HashMap<String, Minion>();

        for(Minion m : minions)
            resM.put(m.getName(), m);

        return resM;
    }


    @Override
    public void run(){

        this.tryToRunMe(myKids, allNamesToMinions,this.cUI);

        this.checkIfToAddMyParents();

        if(!myPData.isEmpty()) {

           // this.targetNameToSummeryProcess.put(curM.getName(), curTaskData);

            FormatAllTask.updateCounter(myPData.get(3));// the status
        }
    }

    private void checkIfToAddMyParents() {

        if(this.ISucceeded())
        {
            for(Minion curD : this.parents) {
                if(curD.getCanIRun())
                    Task.waitingList.add(curD);
            }
        }
    }

    public boolean ISucceeded() {

        boolean iSucceeded = false;

        if(this.myStatus.equals("WARNING") || this.myStatus.equals("SUCCESS"))
            iSucceeded = true;

        return iSucceeded;
    }

    public void initMyKindsAndParents(List<Minion> minions) {

        Map<String, Minion> nameToMinion = startMinionMapFrom(minions);
        this.parents = findMinions(this.parentsNames, nameToMinion);
        this.myKids = findMinions(this.myKidsNames, nameToMinion);

    }

    private List<Minion> findMinions(List<String> mNames ,Map<String, Minion> namesToTasks){

        List<Minion> res = new ArrayList<Minion>();

        for(String curName : mNames)
            res.add(namesToTasks.get(curName));

        return res;
    }

    public void setAllNamesToMinions(Map<String, Minion> allNamesToMinions) {
        this.allNamesToMinions = allNamesToMinions;
    }

    public List<String> getMyPData() {
        return myPData;
    }

    public void setcUI(Consumer cUI) {
        this.cUI = cUI;
    }

}
