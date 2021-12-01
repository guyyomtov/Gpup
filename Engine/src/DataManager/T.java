package DataManager;

import Graph.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public abstract class T {

    protected String taskName;
    protected Integer timeIRun;
    protected Integer chancesISucceed;
    protected Integer chancesImAWarning;
    protected Boolean isRandom;
    protected List<Target> targets = new ArrayList<>();
    protected List<Minion> minions = new ArrayList<>();

    public T(List<Target> targets, T oldTask,int timeToRun, int chancesToSucceed, int chancesToBeAWarning, boolean isRandom)
    {
        this.targets = targets;
        if(!isRandom) {
            this.timeIRun = timeToRun;
            this.chancesISucceed = chancesToSucceed;
            this.chancesImAWarning = chancesToBeAWarning;
            this.isRandom = false;
        }
        else {
            Random rand = new Random();
            this.timeIRun = rand.nextInt(3000); // check, did he say how much time?
            this.chancesISucceed = rand.nextInt(101);
            this.chancesImAWarning = rand.nextInt(101);
            this.isRandom = true;
        }
        this.minions = oldTask.getMinions();
    }

    public List<Minion> getMinions() {
        return minions;
    }

    public abstract void run();



//    public Map<String, List<String>> run(Consumer cUI, List<Target> targets, Map<String, Simulation> oldNamesToTasks){
//
//        Map<String, Target>                     namesToTargetsMap                   = startTargetMap(targets);
//        Map<String, Simulation>                 namesToTasks                        = oldNamesToTasks.isEmpty() == true? startTaskMap(targets) : oldNamesToTasks;
//        Map<String, Map<String, Simulation>>    typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);
//
//        // start
//        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
//    }
//
//    public Map<String,List<String>> run(Consumer cUI, List<Target> targets,  Map<String, Simulation> oldNamesToTasks, int timeToRun, int chancesToSucceed, int chancesToBeAWarning ){
//
//        //setup data
//        Map<String, Target>                     namesToTargetsMap                   = startTargetMap(targets);
//        Map<String, Simulation>                 namesToTasks                        = oldNamesToTasks.isEmpty() == true? startTaskMap(targets, timeToRun, chancesToSucceed, chancesToBeAWarning) : oldNamesToTasks;
//        Map<String, Map<String, Simulation>>    typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);
//
//        // start
//        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
//    }


}
