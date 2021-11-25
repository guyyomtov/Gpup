package DataManager;

import Graph.Target;

import consumerData.ConsumerTaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProcessUtil {

    public static Map<String,List<String>> run(Consumer cUI, List<Target> targets, Map<String, Task> oldNamesToTasks){

        Map<String, Target>             namesToTargetsMap                   = startTargetMap(targets);
        Map<String, Task>               namesToTasks                        = oldNamesToTasks.isEmpty() == true? startTaskMap(targets) : oldNamesToTasks;
        Map<String, Map<String, Task>>  typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);

        // start
        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
    }

    public static Map<String,List<String>> run(Consumer cUI, List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning ){

        //setup data
        Map<String, Target>             namesToTargetsMap                   = startTargetMap(targets);
        Map<String, Task>               namesToTasks                        = startTaskMap(targets, timeToRun, chancesToSucceed, chancesToBeAWarning);
        Map<String, Map<String, Task>>  typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);

        // start
        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
    }


    private static Map<String, Task> startTaskMap(List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning){

        Map<String, Task> resM = new HashMap<String, Task>();

        for(Target curTarget : targets)
            resM.put(curTarget.getName(), new Task(curTarget, timeToRun, chancesToSucceed, chancesToBeAWarning));

        return resM;
    }

    private static Map<String, Task> startTaskMap(List<Target> targets){

        Map<String, Task> resM = new HashMap<String, Task>();

        for(Target curTarget : targets)
            resM.put(curTarget.getName(), new Task(curTarget));

        return resM;
    }

    private static Map<String, Target> startTargetMap(List<Target> targets) {

        Map<String, Target> resM = new HashMap<>();

        for(Target curT : targets)
            resM.put(curT.getName(), curT);

        return resM;
    }

    private static Map<String, Map<String,Task>> startTaskMapByTargetType(Map<String, Task> namesToTasks, Map<String, Target> namesToTarget) {

        String curTargetType = new String();
        Map<String, Map<String,Task>> resM = new HashMap<>();

        resM.put("Independent", new HashMap<>());
        resM.put("Leaf", new HashMap<>());
        resM.put("Middle", new HashMap<>());
        resM.put("Root", new HashMap<>());

        for(String curTaskName : namesToTasks.keySet()){

            curTargetType = namesToTarget.get(curTaskName).getTargetType().toString();

            resM.get(curTargetType).put(curTaskName, namesToTasks.get(curTaskName));
        }
        return resM;
    }

    private static  Map<String,List<String>> startProcess(Consumer cUI, Map<String, Map<String,Task>> typeOfTargetToTargetNameToHisTask, Map<String, Target> targets, Map<String, Task> namesToTasks) {

        // setup data
        Map<String,Task> independents = typeOfTargetToTargetNameToHisTask.get("Independent");
        Map<String,Task> leaves = typeOfTargetToTargetNameToHisTask.get("Leaf");
        Map<String,Task> middles = typeOfTargetToTargetNameToHisTask.get("Middle");
        Map<String,Task> roots = typeOfTargetToTargetNameToHisTask.get("Root");
        Boolean curTasksFinished = false;
        Map<String,List<String>> targetNameToTaskData = new HashMap<>();
        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released
        Map<String,List<String>> curTasksData = new HashMap<>();


        // start process

        // go over all targets:

        // 1) start with independents
        if(!independents.keySet().isEmpty()) {

            while (!curTasksFinished) {

                curTasksData = runTheseTasks(cUI, new ArrayList<>(independents.values()), namesToTasks); // go over all cur tasks and get the needed data back

                targetNameToTaskData.putAll(curTasksData);

                curTasksFinished = checkIfWeFinished(new ArrayList<>(independents.values())); // go over all cur tasks and check if finished (with get
            }
            curTasksFinished = false;
        }
        // move to leaves
        if(!leaves.keySet().isEmpty()) {

            while (!curTasksFinished) {

                curTasksData = runTheseTasks(cUI, new ArrayList<>(leaves.values()), namesToTasks); // go over all cur tasks and get the needed data back

                targetNameToTaskData.putAll(curTasksData);

                curTasksFinished = checkIfWeFinished(new ArrayList<>(leaves.values())); // go over all cur tasks and check if finished (with get
            }
            curTasksFinished = false;
        }

        // then to middle
        if(!middles.keySet().isEmpty()) {

            while (!curTasksFinished) {

                curTasksData = runTheseTasks(cUI, new ArrayList<>(middles.values()), namesToTasks); // go over all cur tasks and get the needed data back

                targetNameToTaskData.putAll(curTasksData);

                curTasksFinished = checkIfWeFinished(new ArrayList<>(middles.values())); // go over all cur tasks and check if finished (with get
            }
            curTasksFinished = false;
        }

        // then to root
        if(!roots.keySet().isEmpty()) {

            while (!curTasksFinished) {

                curTasksData = runTheseTasks(cUI, new ArrayList<>(roots.values()), namesToTasks); // go over all cur tasks and get the needed data back

                targetNameToTaskData.putAll(curTasksData);

                curTasksFinished = checkIfWeFinished(new ArrayList<>(roots.values())); // go over all cur tasks and check if finished (with get
            }
        }

        return targetNameToTaskData;
    }

    private static Boolean checkIfWeFinished(List<Task> tasks){

        Boolean finished = true;

        for(Task curT : tasks){

            if(!curT.imFinished()) {
                finished = false;
                break;
            }
        }
        return  finished;
    }

    private static Map<String,List<String>> runTheseTasks(Consumer cUI, List<Task> tasks, Map<String, Task> namesToTasks){

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,
        List<String> curTaskData = new ArrayList<>();
        List<Task> kids = new ArrayList<Task>();
        Map<String,List<String>> resData = new HashMap<>();
        ConsumerTaskInfo cE = new ConsumerTaskInfo();

        for(Task curT : tasks)
            resData.put(curT.getMyName(), new ArrayList<>());


        for(Task curT : tasks){

            if(iAllReadyRan(curT))
                curTaskData = getOldDataFrom(curT);
            else{

                kids = findMyKids(curT, namesToTasks);

                curTaskData = curT.tryToRunMe(kids, namesToTasks);
                if(!curTaskData.isEmpty())
                     cE.getData(curTaskData, cUI);
            }

            if(!curTaskData.isEmpty()){
                resData.get(curT.getMyName()).addAll(curTaskData);
         //      if(!iAllReadyRan(curT))
                //    cE.getData(curTaskData, cUI);
            }
        }
        return resData;
    }

    private static Boolean iAllReadyRan(Task curT){

        Boolean iRan = false;

        if(curT.getMyStatus().equals("WARNING") || curT.getMyStatus().equals("SUCCESS"))
            iRan = true;

        return iRan;
    }

    private static List<String> getOldDataFrom(Task curT){

        List<String> resData = new ArrayList<>();

        resData.add(0, "0");
        resData.add(1, curT.getMyName());
        resData.add(2, curT.getMyTargetGenaralInfo());
        resData.add(3, curT.getMyStatus());
        resData.add(4, "");

        return resData;
    }

    private static List<Task> findMyKids(Task curT, Map<String, Task> namesToTasks){

        List<String> kidsNames = curT.getMyKidsNames();
        List<Task> kids = new ArrayList<Task>();

        for(String curKidName : kidsNames)
            kids.add(namesToTasks.get(curKidName));

        return kids;
    }
}
