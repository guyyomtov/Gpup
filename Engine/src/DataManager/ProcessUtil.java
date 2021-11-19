package DataManager;

import Graph.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessUtil {

    public static Map<String,List<String>> run(List<Target> targets){

        //setup data
        Map<String, Target> nameToTargetMap = startTargetMap(targets);
        Map<String, Map<String, Task>> typeToTAndT = startData(nameToTargetMap); // add info

        // start
        return startProcess(typeToTAndT, nameToTargetMap);
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

    private static  Map<String,List<String>> startProcess(Map<String, Map<String,Task>> typeToTAndT, Map<String, Target> targets) {

        Map<String,Task> independents = typeToTAndT.get("Independent");
        Map<String,Task> leaves = typeToTAndT.get("Leaf");
        Map<String,Task> middles = typeToTAndT.get("Middle");
        Map<String,Task> roots = typeToTAndT.get("Root");

        Map<String,List<String>> targetNameToTaskData = new HashMap<>();

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,
        List<String> curTaskData = new ArrayList<>();

        // to do --> don't give data on task that didn't work
        // need to go over middle twice

        // go over all targets
        // start with independents
        for(String indName : independents.keySet()){

            targetNameToTaskData.put(indName, new ArrayList<>());

            Task curTask = independents.get(indName);
            Target curTarget = targets.get(indName);

            curTaskData = runThisTask(curTask, curTarget, typeToTAndT);
        }

        // move to leaves
        for(String leafName : leaves.keySet()){

            targetNameToTaskData.put(leafName, new ArrayList<>());

            Task curTask = leaves.get(leafName);
            Target curTarget = targets.get(leafName);

            curTaskData = runThisTask(curTask, curTarget, typeToTAndT);
        }

        // then to middle
        for(String midName : middles.keySet()){

            targetNameToTaskData.put(midName, new ArrayList<>());

            Task curTask = middles.get(midName);
            Target curTarget = targets.get(midName);

            curTaskData = runThisTask(curTask, curTarget, typeToTAndT);
        }

        // then to root
        for(String rootName : middles.keySet()){

            targetNameToTaskData.put(rootName, new ArrayList<>());

            Task curTask = roots.get(rootName);
            Target curTarget = targets.get(rootName);

            curTaskData = runThisTask(curTask, curTarget, typeToTAndT);
        }
        return targetNameToTaskData;
    }

    private static String targetsThatOpened(){
        return new String();
    }

    private static List<String> runThisTask(Task curT, Target curTarget, Map<String, Map<String, Task>> typeToTAndT){

        String status = new String(), targetName = new String(), generalTargetInfo = new String(), namesOfOpenedTargets = new String();
        List<String> resData = new ArrayList<>();

        if(curT.getCanRunTask()) {

            curT.runMe();

            resData.add(String.valueOf(curT.getTimeToRunOnEachT()));

            targetName = curTarget.getName();
            resData.add(targetName);

            generalTargetInfo = curTarget.getGeneralInfo();
            resData.add(generalTargetInfo);

            status = curT.getTargetStatus();
            resData.add(status);


            if(status != "Failure")
                updateDependsCounterFor(curTarget, typeToTAndT);

            namesOfOpenedTargets = targetsThatOpened(curTarget, typeToTAndT);
            resData.add(namesOfOpenedTargets);
        }
        return resData;
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
