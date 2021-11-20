package DataManager;

import Graph.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Task {

    private Integer timeIRun;
    private Integer chancesISucceed;
    private Integer chancesImAWarning;
    final private Boolean isChanceRandom;
    final private Boolean isTimeRandom;
    final private String myTargetName;
    final private String myTargetGenaralInfo;
    private String myStatus = new String();
    private Boolean canIRun;
    private Boolean iAmFinished;
    final private List<String> myKidsNames;


    public Task(Target target){

        Random rand = new Random();
        this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
        this.chancesISucceed = (rand.nextInt(101)/100);
        this.chancesImAWarning = (rand.nextInt(101)/100);
        this.isChanceRandom = true;
        this.isTimeRandom = true;

        this.myStatus = this.myStartStatus(target.getClass().getSimpleName());;
        this.canIRun = this.myStatus == "WAITING" ? true : false;
        this.iAmFinished = true;

        this.myTargetName = target.getName();
        this.myTargetGenaralInfo = target.getGeneralInfo();
        this.myKidsNames = this.setMyKidsNames(target);
    }

    private List<String> setMyKidsNames(Target t){

        List<String> kidsNames = new ArrayList<>();

        for(Target curT : t.getDependsOn())
            kidsNames.add(curT.getName());

        return kidsNames;
    }

    public List<String> getMyKidsNames(){ return this.myKidsNames;}

    private String myStartStatus(String tType){

        if(tType == "Leaf" || tType == "Independent")
            return "WAITING";
        return "FROZEN";
    }

    public List<String> tryToRunMe(List<Task> myKids){

        List<String> myPData = new ArrayList<>();

        if(this.myStatus == "SKIPPED") // return nothing
            return  myPData;

        if(this.myStatus != "WAITING") // i'm not a leaf\independent but maybe i can start
            this.myStatus = checkStatusOfMyKidsAndUpdateMe(myKids);

        if(this.myStatus == "WAITING")
            myPData = this.runMe();

        return myPData;
    }

    private List<String> runMe(){

        List<String> resData = new ArrayList<>();

        Random rand = new Random();
        if((rand.nextInt(101)/100) <= this.chancesISucceed){

            // if warning
            if((rand.nextInt(101)/100) <= this.chancesImAWarning)
                this.myStatus = "Warning"; //success with warning
            else
                this.myStatus = "Success";
        }
        else
            this.myStatus = "Failure";

        this.iAmFinished = true;

        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.myTargetName);
        resData.add(2, this.myTargetGenaralInfo);
        resData.add(3, this.myStatus);
        // add targets names the got free

        return resData;
    }

    public String getMyName(){return this.myTargetName;}

    public String getMyStatus(){return this.myStatus;}

    private String checkStatusOfMyKidsAndUpdateMe(List<Task> myKids){

        String resStatus = new String();
        Boolean iCanRun = true;

        for(Task curKid : myKids) {

            if (curKid.getMyStatus() == "FAILURE" || curKid.getMyStatus() == "SKIPPED") {

                resStatus = "SKIPPED";

                this.timeIRun = 0; this.chancesISucceed = 0; this.chancesImAWarning = 0;

                iCanRun = false;
                break;
            }

            else if(curKid.getMyStatus() == "IN PROCESS" || curKid.getMyStatus() == "WAITING" || curKid.getMyStatus() == "FROZEN") {
                resStatus = "FROZEN";
                iCanRun = false;
            }
        }
        if(iCanRun)
            resStatus = "WAITING";

        this.canIRun = iCanRun;

        return resStatus;
    }

    public Boolean imFinished(){return this.iAmFinished;}

}
