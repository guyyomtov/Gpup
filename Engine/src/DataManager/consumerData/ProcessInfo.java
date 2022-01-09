package DataManager.consumerData;

import Graph.process.Simulation;
import Graph.process.Task;

public class ProcessInfo {

    private static Task oldTask;

    public static Task getOldTask() {
        return oldTask;
    }

    public static void setOldTask(Task oldT) {
        ProcessInfo.oldTask = oldT;
    }


}
