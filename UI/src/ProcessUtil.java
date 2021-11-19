import Graph.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessUtil {

    public static void run(List<Target> targets){

        //setup data
        Map<String, Target> nameToTargetMap = startTargetMap(targets);
        Map<String, Map<String, Task>> typeToTAndT = startData(nameToTargetMap); // add info

        // start
        startProcess(typeToTAndT, nameToTargetMap);
    }

    private static Map<String, Target> startTargetMap(List<Target> targets) {

        Map<String, Target> resM = new HashMap<>();

        for(Target curT : targets)
            resM.put(curT.getName(), curT);

        return resM;
    }

    private static Map<String, Map<String,Task>> startData(Map<String, Target> tMap) {

        Map<String, Map<String,Task>> resM = new HashMap<>();

        resM.put("Independent", new HashMap<>());
        resM.put("Leaf", new HashMap<>());
        resM.put("Middle", new HashMap<>());
        resM.put("Root", new HashMap<>());

        for(Target curT : tMap.values()){

            Task curTask = new Task(curT);

            resM.get(curT.getTargetType().toString()).put(curT.getName(), curTask);
        }
        return resM;
    }

    private static void startProcess(Map<String, Map<String,Task>> typeToTAndT, Map<String, Target> targets) {

        Map<String,Task> independents = typeToTAndT.get("Independent");
        Map<String,Task> leaves = typeToTAndT.get("Leaf");
        Map<String,Task> middles = typeToTAndT.get("Middle");
        Map<String,Task> roots = typeToTAndT.get("Root");

        // go over all targets

        // start with independents
        for(String indName : independents.keySet()){

            Task curTask = independents.get(indName);
            Target curTarget = targets.get(indName);

            runThisTask(curTask, curTarget, typeToTAndT);
        }

        // move to leaves
        for(String leafName : leaves.keySet()){

            Task curTask = leaves.get(leafName);
            Target curTarget = targets.get(leafName);

            runThisTask(curTask, curTarget, typeToTAndT);
        }

        // then to middle
        for(String midName : middles.keySet()){

            Task curTask = middles.get(midName);
            Target curTarget = targets.get(midName);

            runThisTask(curTask, curTarget, typeToTAndT);
        }

        // then to root
        for(String rootName : middles.keySet()){

            Task curTask = roots.get(rootName);
            Target curTarget = targets.get(rootName);

            runThisTask(curTask, curTarget, typeToTAndT);
        }
    }

    private static String targetsThatOpened(){
        return new String();
    }

    private static void runThisTask(Task curT, Target curTarget, Map<String, Map<String, Task>> typeToTAndT){

        // here ui is going to have a function which gets and prints --> to ui and to file
        String status = new String(), targetName = new String(), generalTargetInfo = new String(), namesOfOpenedTargets = new String();


        if(curT.getCanRunTask()) {

            curT.runMe();

            // print start process on specific target
            targetName = curTarget.getName();

            // print free data on current data
            generalTargetInfo = curTarget.getGeneralInfo();

            // print finished current task (after time) and status Success | Warning | Failure
            status = curT.getTargetStatus();

            // if not failed --> get all his depends tasks and give them -1
            if(status != "Failure")
                updateDependsCounterFor(curTarget, typeToTAndT);

            // update depends on counters
            curT.getTimeToRunOnEachT();

            // print if targets that depend on him got open
            namesOfOpenedTargets = targetsThatOpened(curTarget, typeToTAndT);
        }
    }

    private static void updateDependsCounterFor(Target curTarget, Map<String, Map<String, Task>> typeToTAndT){

        for(Target curTDepend : curTarget.getDependsOn()){

            Task tToUpdate = typeToTAndT.get(curTDepend.getTargetType().toString()).get(curTDepend.getName());

            tToUpdate.setHowManyRequiredFor(tToUpdate.getHowManyRequiredFor() - 1);
        }
    }

    public static void run(List<Target> targets, Integer timeToRunOnEachT, Integer chancesOfSuccess, Integer chancesOfWarning){

        // start data needed
        // make map --> target to task

        // make map --> target type to --> map <target to task
    }

    private static String targetsThatOpened(Target curTarget, Map<String, Map<String, Task>> typeToTAndT){

        String targetResNames = new String();

        // go over all depends on list
        for(Target curTDepend : curTarget.getDependsOn()) {

            Task taskToCheck = typeToTAndT.get(curTDepend.getTargetType().toString()).get(curTDepend.getName());

            // if there task counter == 0
            if(taskToCheck.getCanRunTask())
                targetResNames += curTDepend.getName();

        }
        return targetResNames;
    }

    public static void getFinalReport(){

    }
}
