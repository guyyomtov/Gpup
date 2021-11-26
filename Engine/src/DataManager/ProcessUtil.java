package DataManager;

import Graph.Target;

import consumerData.ConsumerTaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProcessUtil {

    public static Map<String,List<String>> run(Consumer cUI, List<Target> targets, Map<String, Simulation> oldNamesToTasks){

        Map<String, Target>             namesToTargetsMap                   = startTargetMap(targets);
        Map<String, Simulation>               namesToTasks                        = oldNamesToTasks.isEmpty() == true? startTaskMap(targets) : oldNamesToTasks;
        Map<String, Map<String, Simulation>>  typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);

        // start
        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
    }

    public static Map<String,List<String>> run(Consumer cUI, List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning ){

        //setup data
        Map<String, Target>             namesToTargetsMap                   = startTargetMap(targets);
        Map<String, Simulation>               namesToTasks                        = startTaskMap(targets, timeToRun, chancesToSucceed, chancesToBeAWarning);
        Map<String, Map<String, Simulation>>  typeOfTargetToTargetNameToHisTask   = startTaskMapByTargetType(namesToTasks, namesToTargetsMap);

        // start
        return startProcess(cUI, typeOfTargetToTargetNameToHisTask, namesToTargetsMap, namesToTasks);
    }


    private static Map<String, Simulation> startTaskMap(List<Target> targets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning){

        Map<String, Simulation> resM = new HashMap<String, Simulation>();

        for(Target curTarget : targets)
            resM.put(curTarget.getName(), new Simulation(curTarget, timeToRun, chancesToSucceed, chancesToBeAWarning));

        return resM;
    }

    private static Map<String, Simulation> startTaskMap(List<Target> targets){

        Map<String, Simulation> resM = new HashMap<String, Simulation>();

        for(Target curTarget : targets)
            resM.put(curTarget.getName(), new Simulation(curTarget));

        return resM;
    }

    private static Map<String, Target> startTargetMap(List<Target> targets) {

        Map<String, Target> resM = new HashMap<>();

        for(Target curT : targets)
            resM.put(curT.getName(), curT);

        return resM;
    }

    private static Map<String, Map<String,Simulation>> startTaskMapByTargetType(Map<String, Simulation> namesToTasks, Map<String, Target> namesToTarget) {

        String curTargetType = new String();
        Map<String, Map<String,Simulation>> resM = new HashMap<>();

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

    private static  Map<String,List<String>> startProcess(Consumer cUI, Map<String, Map<String,Simulation>> typeOfTargetToTargetNameToHisTask, Map<String, Target> targets, Map<String, Simulation> namesToTasks) {

        // setup data
        Map<String,Simulation> independents = typeOfTargetToTargetNameToHisTask.get("Independent");
        Map<String,Simulation> leaves = typeOfTargetToTargetNameToHisTask.get("Leaf");
        Map<String,Simulation> middles = typeOfTargetToTargetNameToHisTask.get("Middle");
        Map<String,Simulation> roots = typeOfTargetToTargetNameToHisTask.get("Root");
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

    private static Boolean checkIfWeFinished(List<Simulation> tasks){

        Boolean finished = true;

        for(Simulation curT : tasks){

            if(!curT.imFinished()) {
                finished = false;
                break;
            }
        }
        return  finished;
    }

    private static Map<String,List<String>> runTheseTasks(Consumer cUI, List<Simulation> tasks, Map<String, Simulation> namesToTasks){

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,
        List<String> curTaskData = new ArrayList<>();
        List<Simulation> kids = new ArrayList<Simulation>();
        Map<String,List<String>> resData = new HashMap<>();
        ConsumerTaskInfo cE = new ConsumerTaskInfo();

        for(Simulation curT : tasks)
            resData.put(curT.getMyName(), new ArrayList<>());


        for(Simulation curT : tasks){

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

    private static Boolean iAllReadyRan(Simulation curT){

        Boolean iRan = false;

        if(curT.getMyStatus().equals("WARNING") || curT.getMyStatus().equals("SUCCESS"))
            iRan = true;

        return iRan;
    }

    private static List<String> getOldDataFrom(Simulation curT){

        List<String> resData = new ArrayList<>();

        resData.add(0, "0");
        resData.add(1, curT.getMyName());
        resData.add(2, curT.getMyTargetGenaralInfo());
        resData.add(3, curT.getMyStatus());
        resData.add(4, "");

        return resData;
    }

    private static List<Simulation> findMyKids(Simulation curT, Map<String, Simulation> namesToTasks){

        List<String> kidsNames = curT.getMyKidsNames();
        List<Simulation> kids = new ArrayList<Simulation>();

        for(String curKidName : kidsNames)
            kids.add(namesToTasks.get(curKidName));

        return kids;
    }
}
