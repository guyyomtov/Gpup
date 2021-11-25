package DataManager;

import Graph.Target;

import java.util.*;

public class Task {

    private Integer timeIRun;
    private Integer chancesISucceed;
    private Integer chancesImAWarning;
    private Boolean isChanceRandom;
    private Boolean isTimeRandom;
    private String myTargetName;
    private String myTargetGenaralInfo;
    private String myStatus = new String();
    private Boolean canIRun;
    private Boolean iAmFinished;
    private List<String> parentsNames = new ArrayList<>();
    private List<String> myKidsNames = new ArrayList<>();

    public Task(Target target){

        Random rand = new Random();
        this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
        this.chancesISucceed = rand.nextInt(101);
        this.chancesImAWarning = rand.nextInt(101);
        this.isChanceRandom = true;
        this.isTimeRandom = true;

        this.myStatus = this.myStartStatus(target.getTargetType().toString());
        this.canIRun = this.myStatus == "WAITING" ? true : false;
        this.iAmFinished = false;

        this.myTargetName = target.getName();
        this.myTargetGenaralInfo = target.getGeneralInfo();
        this.myKidsNames = this.setMyKidsNames(target);
        this.parentsNames = this.setMyParentsNames(target);
    }

    public Task(Target target, String oldStatus){

        if(this.iSucceededLastTime(oldStatus))
            this.startMeWithSucceeded(target, oldStatus);
        else
            this.genarateMeAgain(target, oldStatus);

    }

    private void genarateMeAgain(Target target, String oldStatus){

        Random rand = new Random();
        this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
        this.chancesISucceed = rand.nextInt(101);
        this.chancesImAWarning = rand.nextInt(101);
        this.isChanceRandom = true;
        this.isTimeRandom = true;

        this.myStatus = this.genarateNewStatusFrom(oldStatus,target.getTargetType().toString());
        this.canIRun = this.myStatus == "WAITING" ? true : false;
        this.iAmFinished = false;

        this.myTargetName = target.getName();
        this.myTargetGenaralInfo = target.getGeneralInfo();
        this.myKidsNames = this.setMyKidsNames(target);
        this.parentsNames = this.setMyParentsNames(target);
    }

    private void startMeWithSucceeded(Target target, String oldStatus){

        this.timeIRun = 0;
        this.chancesISucceed = 100;
        this.chancesImAWarning = oldStatus == "WARNING" ? 100 : 0;
        this.isChanceRandom = false;
        this.isTimeRandom = false;

        this.myStatus = oldStatus;
        this.canIRun = true;
        this.iAmFinished = true;

        this.myTargetName = target.getName();
        this.myTargetGenaralInfo = target.getGeneralInfo();
        this.myKidsNames = this.setMyKidsNames(target);
        this.parentsNames = this.setMyParentsNames(target);
    }

    private Boolean iSucceededLastTime(String oldStatus){

        Boolean iSucceeded = false;

        if(oldStatus == "WARNING" || oldStatus == "SUCCESS")
            iSucceeded = true;

        return iSucceeded;
    }

    private Boolean checkIfImFinishedBasedOn(String myStatus){

        Boolean imFinished = false;

        if(myStatus == "WARNING" || myStatus == "SUCCESS")
            imFinished = true;
        // else false

        return  imFinished;
    }

    private String genarateNewStatusFrom(String oldStatus, String targetType){

        String resStatus = new String();

        if(oldStatus == "WARNING" || oldStatus == "SUCCESS")
            resStatus = oldStatus;
        else if(oldStatus == "SKIPPED")
            resStatus = "FROZEN";
        else if(oldStatus == "FAILURE"){

            if(targetType == "Leaf" || targetType == "Independent")
                resStatus = "WAITING";
            else // middle or root
                resStatus = "FROZEN";
        }

        return resStatus;
    }

    public Task(Target target, Integer timeToRun, Integer chancesToSucceed, Integer chancesToBeAWarning){

        Random rand = new Random();
        this.timeIRun = timeToRun;
        this.chancesISucceed = chancesToSucceed;
        this.chancesImAWarning = chancesToBeAWarning;
        this.isChanceRandom = false;
        this.isTimeRandom = false;

        this.myStatus = this.myStartStatus(target.getTargetType().toString());
        this.canIRun = this.myStatus == "WAITING" ? true : false;
        this.iAmFinished = false;

        this.myTargetName = target.getName();
        this.myTargetGenaralInfo = target.getGeneralInfo();
        this.myKidsNames = this.setMyKidsNames(target);
    }

    public String getMyTargetGenaralInfo(){ return this.myTargetGenaralInfo;}

    private List<String> setMyParentsNames(Target t){

        List<String> parentsNames = new ArrayList<>();

        for(Target curT : t.getRequiredFor())
            parentsNames.add(curT.getName());

        return parentsNames;
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

    public List<String> tryToRunMe(List<Task> myKids, Map<String,Task> allTasks){

        List<String> myPData = new ArrayList<>();

        if(this.myStatus == "SKIPPED") // return nothing
            return this.giveMyData();

        if(this.myStatus != "WAITING") { // i'm not a leaf\independent but maybe i can start

            this.myStatus = checkStatusOfMyKidsAndUpdateMe(myKids);

            myPData = this.giveMyData();
        }

        if(this.myStatus == "WAITING")
            myPData = this.runMe(allTasks);

        return myPData;
    }

    private List<String> runMe(Map<String,Task> allTasks){

        List<String> resData = new ArrayList<>();
        String openedParents = new String();

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

        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.myTargetName);
        resData.add(2, this.myTargetGenaralInfo);
        resData.add(3, this.myStatus);
        resData.add(4, openedParents);
        // add targets names the got free

        return resData;
    }

    private String iOpened(List<String> parentsNames, Map<String,Task> allTasks){

        String resNames = new String();
        Boolean addThisDad = true;

        Map<String, List<String>> dadsNamesToTheirKidsName = new HashMap<>();
        for(String curPName : parentsNames)
            dadsNamesToTheirKidsName.put(curPName, new ArrayList<>());

        // start the map --> all relevant dads and their kids
        for(String curDadName :  dadsNamesToTheirKidsName.keySet()){

            Task curDad = allTasks.get(curDadName);

            dadsNamesToTheirKidsName.get(curDadName).addAll(curDad.getMyKidsNames());
        }

        // go over all parents
        for(String curDadName :  dadsNamesToTheirKidsName.keySet()){

            addThisDad = true;

            // go over each kid
            for(String curKidName : dadsNamesToTheirKidsName.get(curDadName)){

                Task curKid = allTasks.get(curKidName);

                if(curKid.getMyStatus() != "SUCCESS" && curKid.getMyStatus() != "WARNING")
                    addThisDad = false;
            }
            if(addThisDad)
                resNames +=  " " + curDadName;
        }
        return resNames;
    }

    private List<String> giveMyData(){

        List<String> resData = new ArrayList<>();

        resData.add(0, String.valueOf(this.timeIRun));
        resData.add(1, this.myTargetName);
        resData.add(2, this.myTargetGenaralInfo);
        resData.add(3, this.myStatus);
        resData.add(4, new String());

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

                this.timeIRun = 0; this.chancesISucceed = 0; this.chancesImAWarning = 0; this.iAmFinished = true;

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
