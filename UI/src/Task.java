import Graph.Target;

import java.util.Random;

public class Task {

    private Integer timeToRunOnEachT;
    private Integer chanceOfSuccess;
    private Integer chanceOfWarning;
    private Boolean isChanceRandom;
    private Boolean isTimeRandom;
    private String targetStatus = new String();
    private Integer howManyRequiredFor; // right name?
    private Boolean canRunTask;
    private String targetName = new String();


    public Task(Target target){

        Random rand = new Random();

        this.timeToRunOnEachT = rand.nextInt(3000); // check, did he say how much time?
        this.chanceOfSuccess = (rand.nextInt(101)/100);
        this.chanceOfWarning = (rand.nextInt(101)/100);
        this.isChanceRandom = true;
        this.isTimeRandom = true;
        this.targetStatus = this.myStartStatus(target.getClass().getSimpleName());;
        this.howManyRequiredFor = target.getRequiredFor() == null ? 0 : target.getRequiredFor().size();
        this.canRunTask = this.howManyRequiredFor > 0 ? false : true;
        this.targetName = target.getName();

    }

    public Task(Target target, Integer timeToRunOnEachT, Integer chancesOfSuccess, Integer chancesOfWarning){

        this.timeToRunOnEachT = timeToRunOnEachT;
        this.chanceOfSuccess = chancesOfSuccess;
        this.chanceOfWarning = chancesOfWarning;
        this.isChanceRandom = false;
        this.isTimeRandom = false;
        this.targetStatus = this.myStartStatus(target.getClass().getSimpleName());
        this.howManyRequiredFor = target.getRequiredFor() == null ? 0 : target.getRequiredFor().size();
        this.canRunTask = this.howManyRequiredFor > 0 ? false : true;
        this.targetName = target.getName();
    }

    private String myStartStatus(String tType){

        if(tType == "Leaf" || tType == "Independent")
            return "WAITING";
        return "FROZEN";
    }


    public Integer getTimeToRunOnEachT() {
        return timeToRunOnEachT;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void runMe(){

    }

    public void myProcessInfo(){

    }

    public Boolean getCanRunTask() {
        return canRunTask;
    }
}
