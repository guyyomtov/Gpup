package Graph.process;

import Graph.Target;
import DataManager.consumerData.FormatAllTask;
import DataManager.consumerData.ProcessInfo;
import errors.ErrorUtils;
import fileHandler.TaskFile;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Simulation extends Task implements Serializable, Runnable {

    public Simulation(DataSetupProcess dSp) throws ErrorUtils {

        super(dSp);

        this.setName();

        TaskFile taskFile = new TaskFile();
        taskFile.makeTaskDir(this.taskName);
    }

    @Override
    public void run(){

        //setup data part 1
        Map<String, Target> namesToTargetsMap = Target.initNameToTargetFrom(this.targets);
        Map<String, Minion> namesToMinions     = Minion.startMinionMapFrom(this.minions);
        Map<String, Map<String, Minion>>    typeOfTargetToTargetNameToHisTask   = this.startMinionMapByTargetType(namesToMinions, namesToTargetsMap);

        // setup data part 2
        Map<String,Minion> independents = typeOfTargetToTargetNameToHisTask.get("Independent");
        Map<String,Minion> leaves = typeOfTargetToTargetNameToHisTask.get("Leaf");
        Map<String,Minion> middles = typeOfTargetToTargetNameToHisTask.get("Middle");
        Map<String,Minion> roots = typeOfTargetToTargetNameToHisTask.get("Root");

        this.cUI = new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        };

        FormatAllTask.restartMap();
        FormatAllTask.start = Instant.now();

        // start process

        // go over all targets:

        // 1) start with independents
//        this.runTheseTargetsByType(independents,namesToMinions);

        // ----------- needed to be fixed !!!
        this.makeQueue();
       this.runMinions();

        // move to leaves
//        this.runTheseTargetsByType(leaves,namesToMinions);

        // then to middle
  //      this.runTheseTargetsByType(middles,namesToMinions);

        // then to root
    //    this.runTheseTargetsByType(roots,namesToMinions);

        System.out.println("process finished");
        FormatAllTask.end = Instant.now();
        FormatAllTask.sendData(cUI);


        FormatAllTask.sendData(cUI, this.targetNameToSummeryProcess);
        ProcessInfo.setOldTask(this);
    }

    private Map<String, Map<String,Minion>> startMinionMapByTargetType(Map<String, Minion> namesToTasks, Map<String, Target> namesToTarget) {

        String curTargetType = new String();
        Map<String, Map<String,Minion>> resM = new HashMap<>();

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

    private void runTheseTargetsByType(Map<String,Minion> curMinions, Map<String, Minion> namesToMinions){

        Boolean curMinionsFinished = false;

        if(!curMinions.keySet().isEmpty()) {

        //    while (!curMinionsFinished) {

                this.runTheseTasks(new ArrayList<>(curMinions.values()), namesToMinions); // go over all cur tasks and get the needed data back

                curMinionsFinished = checkIfWeFinished(new ArrayList<>(curMinions.values())); // go over all cur tasks and check if finished (with get
          //  }
        }
    }


    private void runTheseTasks(List<Minion> minions, Map<String, Minion> namesToMinions){

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,
        List<String> curTaskData = new ArrayList<>();
        List<Minion> kids = new ArrayList<Minion>();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(Minion curM : minions){

            if(this.iAllReadyRan(curM))
                curTaskData = this.getOldDataFrom(curM);
            else{

                kids = findMyKids(curM, namesToMinions);
                executorService.execute(curM); //curTaskData = curM.tryToRunMe(kids, namesToMinions, this.cUI);
                if(!curTaskData.isEmpty()) {
                    this.targetNameToSummeryProcess.put(curM.getName(), curTaskData);
                    FormatAllTask.updateCounter(curTaskData.get(3));// the status
                }
            }
        }
    }

    private void checkIfToAddMyParents(Minion curM, Map<String, Minion> namesToMinions) {

        if(curM.ISucceeded())
        {
            List<Minion> parents = Minion.getMinionsByName(curM.getParentsNames(), namesToMinions);

            for(Minion curD : parents) {
                if(curD.getCanIRun())
                    this.waitingList.add(curD);
            }
        }
    }

    private Boolean checkIfWeFinished(List<Minion> minions){

        Boolean finished = true;

        for(Minion curM : minions){

            if(!curM.imFinished()) {
                finished = false;
                break;
            }
        }
        return  finished;
    }

    private List<String> getOldDataFrom(Minion curM){

        List<String> resData = new ArrayList<>();

        resData.add(0, "0");
        resData.add(1, curM.getName());
        resData.add(2, curM.getMyTargetGenaralInfo());
        resData.add(3, curM.getMyStatus());
        resData.add(4, "");

        return resData;
    }

    private static List<Minion> findMyKids(Minion curT, Map<String, Minion> namesToTasks){

        List<String> kidsNames = curT.getMyKidsNames();
        List<Minion> kids = new ArrayList<Minion>();

        for(String curKidName : kidsNames)
            kids.add(namesToTasks.get(curKidName));

        return kids;
    }

    private Boolean iAllReadyRan(Minion curM){

        Boolean iRan = false;

        if(curM.getMyStatus().equals("WARNING") || curM.getMyStatus().equals("SUCCESS") || curM.getMyStatus().equals("SKIPPED"))
            iRan = true;

        return iRan;
    }
    
    public void setName(){this.taskName = "simulation";}


    public static Integer threadCounter = 0;
    public void runMinions(){
       /* we have first queue
       ==> therdspools
       ==> run on Q and dont stop until all threads are back.
                threadPolls.excutre(minion)
                        ==> in minion update Q if my run open relevant minion add minion
        */

        Integer numOfThreads = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        //first case
        Minion minion = waitingList.poll();
        executorService.execute(minion);
        threadCounter++;

        // go out of while when: no thread exist & queue is empty
        while(threadCounter != 0 || !waitingList.isEmpty()) {

            minion = waitingList.poll();

            if(minion != null) {

                executorService.execute(minion);

                threadCounter++;
            }
            else{
                try {
                    Thread.sleep(300);
                }catch (InterruptedException e) {
                   // e.printStackTrace();
                }
            }
        }
        int x = 5;
    }

    public List<Minion> getMinionsWhoArntInWaitingList(List<Minion> minions){

        List<Minion> res = new ArrayList<>();

        for(Minion curM : minions){

            if(!waitingList.contains(curM))
                res.add(curM);
        }
        return res;
    }
}
