package workerManager.currentTaskWorkerTable;

public class MyTasksTableData {

    private Integer amountOfEarnedCredits;
    private Integer amountOfWorkers;
    private Integer amountOfTargetsDoneByMe;
    private String taskName;
    private String progress;



    public MyTasksTableData(String taskName, Integer amountOfWorkers, String progress, Integer amountOfEarnedCredits, Integer amountOfTargetsDoneByMe) {

        this.taskName = taskName;
        this.amountOfWorkers = amountOfWorkers;
        this.progress = progress;
        this.amountOfEarnedCredits = amountOfEarnedCredits;
        this.amountOfTargetsDoneByMe = amountOfTargetsDoneByMe;
    }
}
