package DataManager.consumerData;

import Graph.process.Simulation;

public class ProcessInfo {

    private static Simulation oldTask;

    public static Simulation getOldTask() {
        return oldTask;
    }

    public static void setOldTask(Simulation oldT) {
        ProcessInfo.oldTask = oldT;
    }


}
