package workerManager.currentTaskWorkerTable;

import DashBoard.NewJob.JobsManager;
import errors.ErrorUtils;

public class MyTasksTableData {

    private Integer amountOfEarnedCredits;
    private Integer amountOfWorkers;
    private Integer amountOfTargetsDoneByMe;
    private String taskName;
    private String progress;


    public MyTasksTableData(JobsManager.MiniJobDataController miniJobDataController) throws ErrorUtils {

        this.amountOfEarnedCredits = miniJobDataController.amountOfCredits;
        this.amountOfWorkers = miniJobDataController.amountOfTotalWorkersOnTheTask;
        this.amountOfTargetsDoneByMe = miniJobDataController.amountOfTargetsDoneByMy;
        this.taskName = miniJobDataController.myName;

        if(this.amountOfTargetsDoneByMe > miniJobDataController.amountOfTotalTargets
                || amountOfEarnedCredits < 0)
            throw new ErrorUtils(ErrorUtils.invalidInput("Table given values invalid"));

        //create process
        String tDoneByMe = String.valueOf(this.amountOfTargetsDoneByMe), tTotal = String.valueOf(miniJobDataController.amountOfTotalTargets);
        this.progress =  tDoneByMe + "/" + tTotal;
    }

    public Integer getAmountOfEarnedCredits(){
        return amountOfEarnedCredits;
    }

    public void setAmountOfEarnedCredits(Integer amountOfEarnedCredits){
        this.amountOfEarnedCredits = amountOfEarnedCredits;
    }

    public Integer getAmountOfTargetsDoneByMe(){
        return amountOfTargetsDoneByMe;
    }

    public void setAmountOfTargetsDoneByMe(Integer amountOfTargetsDoneByMe){
        this.amountOfTargetsDoneByMe = amountOfTargetsDoneByMe;
    }

    public Integer getAmountOfWorkers() {
        return amountOfWorkers;
    }

    public void setAmountOfWorkers(Integer amountOfWorkers) {
        this.amountOfWorkers = amountOfWorkers;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
