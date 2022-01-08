package Graph.process;

import DataManager.consumerData.FormatAllTask;
import Graph.Target;
import DataManager.consumerData.ConsumerTaskInfo;
import fileHandler.TaskFile;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import javafx.concurrent.Task;

public class Minion implements Serializable, Runnable {

    private Target target;
    private String targetName;
    private Target.Type targetType;
    private String myStatus = new String();
    private StringProperty status;
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
    private Consumer<String> simulationConsumer;



    public Minion(Target target, Integer maxTime, Integer chancesISucceed, Integer chancesImAWarning){

        this.target = target;
        this.targetName = target.getName();
        this.targetType = target.getTargetType();
        this.myStatus = this.myStartStatus(target.getTargetType().toString());
        this.canIRun = this.myStatus.equals("WAITING") ? true : false;
        this.status = new SimpleStringProperty(this, "status", myStatus );
        this.iAmFinished = false;
        this.chancesISucceed = chancesISucceed;
        this.chancesImAWarning = chancesImAWarning;
        this.setMyKidsNames();
        this.setMyParentsNames();
        this.timeIRun = maxTime;
    }

    public Minion(Target target){

        this.target = target;
        this.targetName = target.getName();
        this.targetType = target.getTargetType();
        this.myStatus = "SUCCESS";
        this.iAmFinished = true;
        this.chancesISucceed = 100;
        this.chancesImAWarning = 0;
        this.setMyKidsNames();
        this.setMyParentsNames();
        this.timeIRun = 0;
    }

    public static Minion startMinionWithSuccessFrom(Target target){return new Minion(target);}

    public static List<Minion> getMinionsByName(List<String> minionsNames, Map<String, Minion> namesToMinions) {

        List<Minion> res = new ArrayList<>();

        for(String curMinName : minionsNames)
            res.add(namesToMinions.get(curMinName));

        return res;
    }

    public static List<Minion> makeMinionsFrom(List<Target> targets, Integer timeIRun, Integer chancesISucceed, Integer chancesImAWarning) {

        List<Minion> res = new ArrayList<>();

        for(Target curT : targets){

            Minion newM = new Minion(curT, timeIRun, chancesISucceed, chancesImAWarning);

            res.add(newM);
        }

        return res;
    }

    public static List<Minion> returnMinionsWhoAreWaitingFrom(List<Minion> allMinios){

        List<Minion> res = new ArrayList<>();

        for(Minion curM : allMinios){

            if(curM.getMyStatus().equals("WAITING"))
                res.add(curM);
        }
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

    public List<String> tryToRunMe(List<Minion> myKids, Map<String,Minion> allMinions, Consumer cUI) {

        String myS = this.myStatus;

        if(myS.equals("SKIPPED")  || myS.equals("FAILURE")  || myS.equals("WARNING") || myS.equals("SUCCESS")) { // return nothing
            return this.myPData;
        }
        else if(!this.myStatus.equals("WAITING")) { // i'm not a leaf\independent but maybe i can start

            this.myStatus = checkStatusOfMyKidsAndUpdateMe(myKids);
            this.myPData = this.giveMyData();
        }

        if(this.myStatus.equals("WAITING")) {
            try {
                this.runMe(allMinions, cUI);//this.myPData = (List<String>) this.call();
            }catch (Exception e){}
        }

        return this.myPData;
    }

    public List<String> runMe(Map<String,Minion> allMinions, Consumer cUI)  {

        this.myStatus = "IN PROCESS";
        this.setStatus(myStatus);

        List<String> resData = new ArrayList<>();
        String openedParents = new String();
        ConsumerTaskInfo cTI = new ConsumerTaskInfo(this.target.getName());

        cTI.getInfo(cUI, "The target " + this.target.getName() + " about to run.");
        try {
            Thread.sleep(100);
        }catch (InterruptedException e) {
           // e.printStackTrace();
        }

        simulationConsumer.accept("The target " + this.target.getName() + " about to run.");

       // Platform.runLater();
        Duration t = Duration.ofMillis(timeIRun);
        cTI.getInfo(cUI, "The system is going to sleep for: " + String.format("%02d:%02d:%02d" ,t.toHours(), t.toMinutes(), t.getSeconds()));
        simulationConsumer.accept("The system is going to sleep for: " + String.format("%02d:%02d:%02d" ,t.toHours(), t.toMinutes(), t.getSeconds()));
        try {
            Thread.sleep(timeIRun);
        }catch(InterruptedException e){}

        cTI.getInfo(cUI, "The system finished the task on target " + this.getName());
        simulationConsumer.accept("The system finished the task on target " + this.getName());
        //  cUI.accept("The system finished the task on target " + this.myTargetName);

        Random rand = new Random();
        if((rand.nextInt(101)) <= this.chancesISucceed){

            // if warning
            if((rand.nextInt(101)) <= this.chancesImAWarning)
                this.myStatus = "WARNING"; //success with warning
            else
                this.myStatus = "SUCCESS";


            synchronized(this){
            openedParents = this.iOpened(this.parentsNames, allMinions);}

        }
        else
            this.myStatus = "FAILURE";

        this.iAmFinished = true;
        cTI.getInfo(cUI, "the result: " + this.myStatus);
        simulationConsumer.accept("The result of the target " + this.targetName + " is: " + this.myStatus);
        if(this.myStatus.equals("SUCCESS") || this.myStatus.equals("WARNING")) {
            cTI.getInfo(cUI, "The targets that opened to run: " + (openedParents.isEmpty() ? "nobody" : openedParents));
            simulationConsumer.accept("The target " + targetName +  " opened to run: " + (openedParents.isEmpty() ? "nobody" : openedParents));
        }

        cTI.getInfo(cUI, "----------------------------------------");


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

    public Boolean imFinished(){return this.iAmFinished;}

    public void setMyParentsNames(){

        for(Target curT : this.target.getRequiredFor())
            this.parentsNames.add(curT.getName());

    }

    public void setMyKidsNames(){

        for(Target curT : this.target.getDependsOn())
            this.myKidsNames.add(curT.getName());
    }

    public Boolean getCanIRun() {return canIRun;}

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

    static public List<String> getMinionsNames(List<Minion> minions){

        List<String> res = new ArrayList<>();

        for(Minion curM : minions)
            res.add(curM.getName());

        return res;
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

    private String iOpened(List<String> parentsNames, Map<String,Minion> allMinions)
    {

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
            if(addThisDad) {

                //make res string
                resNames += " " + curDadName;

                //update status of dad
                Minion curDad = allMinions.get(curDadName);
                curDad.setMyStatus("WAITING");
                curDad.setCanIRun(true);
            }
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

        this.cUI = new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        };
        this.tryToRunMe(myKids, allNamesToMinions,this.cUI);

        this.checkIfToAddMyParents();

        if(!myPData.isEmpty()) {

           // this.targetNameToSummeryProcess.put(curM.getName(), curTaskData);

            FormatAllTask.updateCounter(myPData.get(3));// the status
        }
        Simulation.threadCounter = Simulation.threadCounter - 1 ;
        this.iAmFinished = true;
    }

    private void checkIfToAddMyParents() {

        if(this.ISucceeded()) {

            for(Minion curD : this.parents) {
                if(curD.getCanIRun()) {

                    if(!Graph.process.Task.waitingList.contains(curD))
                        Graph.process.Task.waitingList.add(curD);

  //                  curD.run();
                }
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

    public List<String> getMyPData() {return myPData;}

    public void setcUI(Consumer cUI) { this.cUI = cUI;}

    public String getTargetName() {return targetName;}

    public Target.Type getTargetType() {return targetType;}

    public void setConsumer(Consumer<String> simulationConsumer){this.simulationConsumer = simulationConsumer;}

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

}
