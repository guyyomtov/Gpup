package DataManager;

import Graph.Target;
import consumerData.ConsumerTaskInfo;
import fileHandler.TaskFile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class Minion {

    private Target target;
    private String myStatus = new String();
    private Boolean canIRun;
    private Boolean iAmFinished;
    private List<String> parentsNames = new ArrayList<>();
    private List<String> myKidsNames = new ArrayList<>();


    public Minion(Target target){

        this.target = target;

        this.myStatus = this.myStartStatus(target.getTargetType().toString());
        this.canIRun = this.myStatus.equals("WAITING") ? true : false;
        this.iAmFinished = false;

        this.myKidsNames = this.setMyKidsNames(target);
        this.parentsNames = this.setMyParentsNames(target);
    }

    private void startMeWithSucceeded(Target target, String oldStatus){

        this.myStatus = oldStatus;
        this.canIRun = true;
        this.iAmFinished = true;
        this.myKidsNames = this.setMyKidsNames(target);
        this.parentsNames = this.setMyParentsNames(target);
    }

    private Boolean iSucceededLastTime(String oldStatus){

        Boolean iSucceeded = false;

        if(oldStatus.equals("WARNING") || oldStatus.equals("SUCCESS"))
            iSucceeded = true;

        return iSucceeded;
    }

    private String genarateNewStatusFrom(String oldStatus, String targetType){

        String resStatus = new String();

        if(oldStatus.equals("WARNING")   || oldStatus.equals("SUCCESS") )
            resStatus = oldStatus;
        else if(oldStatus.equals("SKIPPED"))
            resStatus = "FROZEN";
        else if(oldStatus.equals("FAILURE") ){

            if(targetType.equals("Leaf") || targetType.equals("Independent"))
                resStatus = "WAITING";
            else // middle or root
                resStatus = "FROZEN";
        }

        return resStatus;
    }


    public String getName(){return target.getName();}

    public String getMyTargetGenaralInfo(){ return this.target.getGeneralInfo();}

    public String getMyStatus(){
        String res = new String();

        return res;
    }

    public List<String> getMyKidsNames(){return this.myKidsNames();}

    private String myStartStatus(String tType){

        if(tType == "Leaf" || tType == "Independent")
            return "WAITING";
        return "FROZEN";
    }

    public List<String> tryToRunMe(List<Minion> myKids, Map<String,Minion> allMinions, Consumer cUI){

        List<String> myPData = new ArrayList<>();
        String myS = this.myStatus;


        if(myS == "SKIPPED" || myS == "SKIPPED" || myS == "FAILURE" || myS == "WARNING" || myS == "SUCCESS") { // return nothing
            return myPData;
        }
        else if(this.myStatus != "WAITING") { // i'm not a leaf\independent but maybe i can start

            this.myStatus = checkStatusOfMyKidsAndUpdateMe(myKids);
            myPData = this.giveMyData();
        }

        if(this.myStatus == "WAITING")
            myPData = this.runMe(allMinions, cUI);

        return myPData;
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

        cTI.getInfo(cUI, "The system finished the task on target " + this.myTargetName);
        //  cUI.accept("The system finished the task on target " + this.myTargetName);

        Random rand = new Random();
        if((rand.nextInt(101)) <= this.chancesISucceed){

            // if warning
            if((rand.nextInt(101)) <= this.chancesImAWarning)
                this.myStatus = "WARNING"; //success with warning
            else
                this.myStatus = "SUCCESS";
            openedParents = this.iOpened(this.parentsNames, allTasks);
        }
        else
            this.myStatus = "FAILURE";

        this.iAmFinished = true;
        cTI.getInfo(cUI, "the result: " + this.myStatus);
        if(this.myStatus.equals("SUCCESS") || this.myStatus.equals("WARNING"))
            cTI.getInfo(cUI, "The targets that opened to run: " + (openedParents.isEmpty() ? "nobody": openedParents ));

        cTI.getInfo(cUI, "----------------------------------------");


        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.myTargetName);
        resData.add(2, this.myTargetGenaralInfo);
        resData.add(3, this.myStatus);
        resData.add(4, openedParents);
        // add targets names the got free
        TaskFile.closeFile();
        return resData;
    }



    public Boolean imFinished(){

        Boolean res = false;

        return res;
    }
}
