package consumerData;

import DataManager.Simulation;
import DataManager.Task;

public class ProcessInfo {

    private static Simulation oldTask;

    public static Simulation getOldTask() {
        return oldTask;
    }

    public static void setOldTask(Simulation oldT) {
        ProcessInfo.oldTask = oldT;
    }


}
