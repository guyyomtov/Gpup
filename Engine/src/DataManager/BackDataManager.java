package DataManager;

import Flagger.Flagger;
import GpupClassesEx3.GPUPDescriptor;
import Graph.process.*;
import errors.ErrorUtils;
import fileHandler.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


import Graph.*;
import taskView.TaskController;
import transferGraphData.ExecuteTarget;
import transferGraphData.TargetInfo;
import transferGraphData.TaskData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private String userNameThatUploadTheCurGraph;
    private Graph graph;
    private Map<String, Set<Target>> mTypeToTargets = new HashMap<String, Set<Target>>();
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GpupClassesEx3";
    private List<TargetInfo> targetInfoList = new ArrayList<>();

    private TaskController taskController;

    private Task gpupTask;

    // for console application
    public boolean checkFile(String fileName) throws ErrorUtils {

        boolean fileSuccess = false;

        try {
                InputStream inputStream = new FileInputStream(new File(fileName));
                //to check if the ended file is with xml
                GPUPDescriptor information = deserializeFrom(inputStream);

                if(information == null)
                    throw new ErrorUtils(ErrorUtils.invalidFile("file given is empty"));
                try{

                    HandlerXmlFile handlerFile = new HandlerXmlFile();

                    handlerFile.buildMe(information);

                    fileSuccess =  true;

                    this.graph = new Graph(handlerFile.getListOfTargets(),
                                            handlerFile.getMap(),
                                            handlerFile.getNameToSerialSet());

                    this.graph.updateTotalDependenciesAndSerialSets();

                    this.graph.setGraphName(handlerFile.getGraphName());

                    this.graph.setTasksAndPricing(handlerFile.getTasksAndPricing());

                    this.mTypeToTargets = this.makeMap(this.graph.getAllTargets());

                }catch(ErrorUtils e){throw e;} catch (IOException e) {
                    e.printStackTrace();
                }

        } catch (JAXBException | FileNotFoundException e) {throw new ErrorUtils(ErrorUtils.invalidFile("the given file doesnt exist"));}

        return fileSuccess;
    }

    private static GPUPDescriptor deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();

        return (GPUPDescriptor) u.unmarshal(in);
    }

    private Map<String, Set<Target>> makeMap(List<Target> targets){

        String tType;

        Map<String, Set<Target>> tmp = new HashMap<>();
        tmp.put("Independent", new HashSet<Target>() );
        tmp.put("Leaf", new HashSet<Target>());
        tmp.put("Middle", new HashSet<Target>());
        tmp.put("Root", new HashSet<Target>());

        for(Target curT : targets){

            tType = curT.getTargetType().toString();

            tmp.get(tType).add(curT);
        }
        return tmp;
    }

    @Override
    public int getNumOfIndependents() {
        return this.graph.isGood == true ? (this.mTypeToTargets.get("Independent")).size() : -1;
    }

    @Override
    public int getNumOfRoots() {
        return this.graph.isGood == true ? (this.mTypeToTargets.get("Root")).size() : -1;
    }

    @Override
    public int getNumOfMiddle() {
        return this.graph.isGood == true ? (this.mTypeToTargets.get("Middle")).size() : -1;
    }

    @Override
    public int getNumOfLeafs() {
        return this.graph.isGood == true ? (this.mTypeToTargets.get("Leaf")).size() : -1;
    }

    @Override
    public int getNumOfTargets() {
        return this.graph.isGood == true ? (this.graph.getAllTargets()).size() : -1;
    }

    @Override
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils {

        List<String> dataOfT = new ArrayList<>(5);

        if(this.graph.isGood == false)
            throw new ErrorUtils(ErrorUtils.noGraph());
        else{

           try {
                Target target = this.graph.getThisTarget(nameOfTarget);
                List<Target> dependsOn = target.getDependsOn();
                List<Target> requiredFor = target.getRequiredFor();
                String listOfDependent  = makeToString(dependsOn);
                String listOfRequired = makeToString(requiredFor);


                dataOfT.add(0, target.getName());               // = target name;
                dataOfT.add(1, target.getTargetType().toString());    // = location of the target.
                dataOfT.add(2, listOfRequired);                 // = list of dependants targets
                dataOfT.add(3, listOfDependent);                // = list of requires for targets.
                dataOfT.add(4, target.getGeneralInfo());        // = general information.

           }catch(ErrorUtils e){ throw e;}
        }
        return dataOfT;
    }

    private String makeToString(List<Target> targets) {
        if(targets.size() == 0)
            return "Nobody";
        else
            return targets.stream().map(t->t.getName()).collect(Collectors.joining(","));
    }

    @Override
    public String getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {

        if(!this.graph.targetExist(src))
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + src + " doesn't exist in the graph."));
        if(!this.graph.targetExist(dest))
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + dest + " doesn't exist in the graph."));
        if(connection.compareTo("D") == 0)
            return this.graph.getPathFromTargets(src, dest);
        else if(connection.compareTo("R") == 0)
            return this.graph.getPathFromTargets(dest, src);
        else
            throw new ErrorUtils(ErrorUtils.invalidInput("Please enter in the wanted relationship 'depends On' -> D/ 'required For' -> R."));
    }

    public void startProcess(DataSetupProcess dSP) throws ErrorUtils {

        //give more needed data
        dSP.allGraphTargets(this.graph.getAllTargets());
        dSP.serialSets(this.graph.getmSerialSets());
        dSP.bDM = this;

//        // Choose process
//        if(dSP.flagger.processIsSimulation){
//
//            gpupTask = new Simulation(dSP);
//
//            this.taskController.bindTaskToUIComponents(gpupTask);
//
//            new Thread(gpupTask).start();
//        }
//        else if(dSP.flagger.processIsCompilation){
//
//            if(dSP.compilationProcessHasNeededData()){
//                gpupTask = new Compilation(dSP);
//
//                this.taskController.bindTaskToUIComponents(gpupTask);
//
//                new Thread(gpupTask).start();
//            }
//            else{
//                throw new ErrorUtils(ErrorUtils.MISSING_COMPILATION_NEEDED_DATA);
//            }
//        }

    }

    public void resume(){
        this.gpupTask.resumeProcess();
    }

    public String findCircle(String name) throws ErrorUtils {
        try{
            return this.graph.findCircle(name);
        }catch (ErrorUtils e){throw e;}
    }

    public void saveToFile(String fullPath){

        HandlerSaveFile saveToFile = new HandlerSaveFile(this.graph, fullPath);
    }

    public void loadFile(String fullPath) throws ErrorUtils {
        try {
            this.graph = HandlerLoadFile.loadFile(fullPath);
            this.mTypeToTargets = this.makeMap(this.graph.getAllTargets());
        }catch (ErrorUtils e){throw e;}
    }

    public Set<List<TargetInfo>> whatIf(String name, String connection) throws ErrorUtils {
        try {
            Set<List<Target>> whatIfRes = this.graph.whatIf(name, connection);
            Set<List<TargetInfo>> whatIfResAfterConvert = new HashSet<>();
            for(List<Target> targets : whatIfRes){
                List<TargetInfo> targetsInfo = new ArrayList<>();
                this.createListOfTargetInfo(targets, targetsInfo);
                whatIfResAfterConvert.add(targetsInfo);
            }
            return whatIfResAfterConvert;
        }catch(ErrorUtils e){throw e;}
    }

    public List<Target> getAllTargets() { return this.graph.getAllTargets(); }

    public Map<String, Set<Target>> getSerialSets() {
        return this.graph.getmSerialSets();
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void makeGraphizImage(String wantedSavingPath) throws IOException {

        GraphizHHandler.makeGraphizPNGFrom(wantedSavingPath, this.graph);
    }
    //in this method we demo process and update the status of the minions.
    public void demoSimulationProcess(DataSetupProcess dSP){

        dSP.allGraphTargets(this.graph.getAllTargets());
        dSP.serialSets(this.graph.getmSerialSets());
        dSP.bDM = this;
        Simulation demoTask = new Simulation(dSP, true);

    }

    public Graph getGraph(){return this.graph;}

    public void stopProcess() {
        this.gpupTask.cancelTask();
    }

    public void setUserNameThatUploadTheCurGraph(String userNameThatUploadTheCurGraph) {
        this.userNameThatUploadTheCurGraph = userNameThatUploadTheCurGraph;
    }

    public String getUserNameThatUploadTheCurGraph() {
        return userNameThatUploadTheCurGraph;
    }

    public List<TargetInfo> convertTargetsToDtoClass() {
        List<Target> targets = this.graph.getAllTargets();
        this.createListOfTargetInfo(targets, this.targetInfoList);
        Map<String, TargetInfo> nameToTargetInfo = this.makeMapToTargetInfo();
        //this.addDependencies(targets, nameToTargetInfo, "dependsOn");
        //this.addDependencies(targets, nameToTargetInfo, "requiredFor");
        return this.targetInfoList;
    }

    private Map<String, TargetInfo> makeMapToTargetInfo() {
        Map<String, TargetInfo> nameToTargetInfo = new HashMap<>();
        for(TargetInfo targetInfo : this.targetInfoList){
            nameToTargetInfo.put(targetInfo.getName(), targetInfo);
        }
        return nameToTargetInfo;
    }

    // not in used maybe in the future when I put List of Target info in target info the json throw exception of stack overflow..
    private void addDependencies(List<Target> targets, Map<String, TargetInfo> nameToTargetInfo, String dependenciesType) {
            List<TargetInfo> dependenciesForTargetInfo = new ArrayList<>();
            List<Target> dependenciesForTarget = new ArrayList<Target>();
            Map<String, Target> nameToTarget = this.graph.getmNameToTarget();
            for (TargetInfo targetInfo : targetInfoList) {
                Target currTarget = nameToTarget.get(targetInfo.getName());
                if (dependenciesType.equals("dependsOn")) {
                   // dependenciesForTargetInfo = targetInfo.getDependsOn();
                    dependenciesForTarget = currTarget.getDependsOn();
                } else { // its requiredFor
                   // dependenciesForTargetInfo = targetInfo.getRequiredFor();
                    dependenciesForTarget = currTarget.getRequiredFor();
                }
                for (Target target : dependenciesForTarget) {
                    dependenciesForTargetInfo.add(nameToTargetInfo.get(target.getName()));
                }
            }

    }

    private void createListOfTargetInfo(List<Target> targets, List<TargetInfo> targetInfoList) {
        for(Target target : targets){
            TargetInfo targetInfo = new TargetInfo();
            targetInfo.setName(target.getName());
            targetInfo.setType(target.getTargetType().toString());
            targetInfo.setTotalDependsOn(target.getDependsOn().size());
            targetInfo.setTotalRequiredFor(target.getRequiredFor().size());
            targetInfo.setInformation(target.getGeneralInfo());
            targetInfoList.add(targetInfo);
        }
    }

    public List<TargetInfo> getTargetInfoList(){return this.targetInfoList; }

    public List<String> getTasksAndPricing(){
        return this.graph.getTasksAndPricing();
    }

    public String getTasksAndPricingAndConvertToString() {
        String res = new String("");
        List<String> taskAndPricing = this.graph.getTasksAndPricing();
        for(String currTaskAndPrice : taskAndPricing){
            res += currTaskAndPrice + "\n";
        }
        return res;
    }

    public Task makeNewTask(TaskData taskData) throws ErrorUtils {

        DataSetupProcess dSP = this.createNewDataSetUpProcess(taskData);
        Task newTask;
        try {
            if (taskData.getWhatKindOfTask().equals("simulation")) {
                newTask = new Simulation(dSP);
            }

            else {//its compilation
                newTask = new Compilation(dSP);
            }
            return newTask;
        }catch (ErrorUtils e){throw e;}
    }

    private DataSetupProcess createNewDataSetUpProcess(TaskData taskData) {
        DataSetupProcess dSP = new DataSetupProcess();
        Flagger flagger = createNewFlagger(taskData);
        dSP.flagger = flagger;
        dSP.bDM = this;
        dSP.allGraphTargets = this.graph.getAllTargets();
        dSP.minionsChoosenByUser = createMinionsForProcess(taskData);
        if(!taskData.getFromScratch())// incremental
            this.updateStatusMinionLikeLastProcess(taskData, dSP.minionsChoosenByUser);
        dSP.chancesToSucceed = taskData.getChancesToSuccess();
        dSP.chancesToBeAWarning = taskData.getChancesToWarning();
        dSP.timeToRun = taskData.getMaxTimePerTarget();
        dSP.taskName = taskData.getTaskName();
        dSP.fullPathDestination = taskData.getFullPathDestination();
        dSP.fullPathSource = taskData.getFullPathSource();
        return dSP;
    }

    private void updateStatusMinionLikeLastProcess(TaskData taskData, List<Minion> minionsChoosenByUser) {
        Map<String, Minion> nameToMinion = Minion.startMinionMapFrom(minionsChoosenByUser);
        List<ExecuteTarget> lastExecuteTargets = taskData.getLastExecuteTargetsList();
        for(ExecuteTarget executeTarget : lastExecuteTargets){
            Minion currentMinion = nameToMinion.get(executeTarget.getTargetName());
            String status = executeTarget.getStatus();
            currentMinion.setMyStatus(status);
            currentMinion.setStatus(status);
        }
    }

    private List<Minion> createMinionsForProcess(TaskData taskData) {
        List<Minion> minions = new ArrayList<>();
        List<TargetInfo> targetInfoList = taskData.getTargetInfoList();
        Map<String, Target> nameToTarget = this.graph.getmNameToTarget();
        for(TargetInfo targetInfo : targetInfoList){
            // -> make minion for simulation.
            if(taskData.getWhatKindOfTask().equals("simulation"))
            {
                minions.add(new Minion(nameToTarget.get(targetInfo.getName()), taskData.getMaxTimePerTarget(), taskData.getChancesToSuccess(), taskData.getChancesToWarning(), true));
            }
            else{ // -> make minion for compilation.

                minions.add(new Minion(nameToTarget.get(targetInfo.getName()), taskData.getMaxTimePerTarget(), taskData.getChancesToSuccess(), taskData.getChancesToWarning(), false, taskData.getFullPathDestination(), taskData.getFullPathSource()));
            }
        }
        return minions;
    }

    private Flagger createNewFlagger(TaskData taskData) {
        Flagger flagger = new Flagger();
        flagger.processIsSimulation = taskData.getWhatKindOfTask() == "simulation" ? true : false;
        flagger.processFromScratch = taskData.getFromScratch();
        flagger.processIncremental = !taskData.getFromScratch();
        flagger.chancesIsRandomInProcess = taskData.getRandom();
        return flagger;
    }

    // after the admin press start.
    public List<ExecuteTarget> transferFromMinionToExecuteTarget(Task newTask, TaskData taskData) {
        List<ExecuteTarget> executeTargetList = new ArrayList<>();
        Map<String, Minion> nameToMinion = Minion.startMinionMapFrom(newTask.getMinionsThatUserChose());
        List<TargetInfo> targetInfoList = taskData.getTargetInfoList();
        Map<String, ExecuteTarget> nameToExecuteTarget = this.makeMapNameToExecuteTargets(taskData.getExecuteTargetList());
        for(TargetInfo targetInfo : targetInfoList){

            Minion minion = nameToMinion.get(targetInfo.getName());
            ExecuteTarget lastExecuteTarget = nameToExecuteTarget.get(targetInfo.getName());
            ExecuteTarget executeTarget = new ExecuteTarget(taskData, targetInfo, minion);
            executeTarget.setLogs(lastExecuteTarget.getLogs());
            executeTarget.setISkippedBecause(lastExecuteTarget.getISkippedBecause());
            executeTarget.setWorkerThatDoneMe(lastExecuteTarget.getWorkerThatDoneMe());
            executeTargetList.add(executeTarget);

        }
        return executeTargetList;
    }

    private Map<String, ExecuteTarget> makeMapNameToExecuteTargets(List<ExecuteTarget> executeTargetList) {
        Map<String, ExecuteTarget> nameToExecuteTarget = new HashMap<>();
        for(ExecuteTarget executeTarget : executeTargetList){
            nameToExecuteTarget.put(executeTarget.getTargetName(), executeTarget);
        }
        return nameToExecuteTarget;
    }

    // the 'execute target' will be with the status not initialized.
    public List<ExecuteTarget> makeExecuteTargetsBeforeAdminPressStart(TaskData taskData) {
        List<ExecuteTarget> executeTargetList = new ArrayList<>();
        List<TargetInfo> targetInfoList = taskData.getTargetInfoList();
        for(TargetInfo targetInfo : targetInfoList){
            ExecuteTarget executeTarget = new ExecuteTarget(targetInfo);
            executeTargetList.add(executeTarget);
        }
        return executeTargetList;
    }

    public List<ExecuteTarget> makeExecuteTargetsToSend(Task currentTask, TaskData taskData, Integer amountOfThreads) {
        // to get from the queue the minions that can run
        //List<ExecuteTarget> executeTargetsToSendToProcess
        //to change there status to in process
        List<ExecuteTarget> executeTargetsToSend = new ArrayList<>();
        List<Minion> minionsToSend = this.getMinionsFromQueue(currentTask, amountOfThreads);
        //to convert them execute and the change in task view the execute target
        if(!minionsToSend.isEmpty()) {
            List<ExecuteTarget> executeTargetList = this.transferFromMinionToExecuteTarget(currentTask, taskData);
            executeTargetsToSend = this.findExecuteTargetsToSend(executeTargetList, currentTask);
            taskData.setExecuteTargetList(executeTargetList);
        }
        synchronized (this){this.checkIfTaskIsDone(taskData);}
        return executeTargetsToSend;
    }

    private void checkIfTaskIsDone(TaskData taskData) {
        //if all the status of the execute targets is not in process or waiting so the tasks is done
        List<ExecuteTarget> executeTargetList = taskData.getExecuteTargetList();
        boolean processIsDone = true;
        for(ExecuteTarget executeTarget : executeTargetList){
            String status = executeTarget.getStatus();
            if(status.equals("IN PROCESS") || status.equals("WAITING"))
                processIsDone = false;
        }
        if(processIsDone && !taskData.getStatus().equals(TaskData.Status.DONE)) {
            taskData.setStatus(TaskData.Status.DONE);
            this.openFiles(taskData);
        }
    }

    private void openFiles(TaskData taskData) {
        try {
            TaskFile taskFile = new TaskFile();
            // open directory to the task
            taskFile.makeTaskDir(taskData.getTaskName() + " " + taskData.getWhatKindOfTask());
            List<ExecuteTarget> executeTargetList = taskData.getExecuteTargetList();
            for (ExecuteTarget executeTarget : executeTargetList) {
                if(!executeTarget.getStatus().equals("SKIPPED")) {
                    //for each target open new log file
                    taskFile.openFile(executeTarget.getTargetName());
                    taskFile.writeToFile(executeTarget.getLogs());
                    taskFile.closeFile();
                }
            }
        }catch (Exception e){}
    }

    private List<ExecuteTarget> findExecuteTargetsToSend(List<ExecuteTarget> executeTargetList, Task currentTask) {
        List<ExecuteTarget> executeTargetsToSend = new ArrayList<>();
        for(ExecuteTarget executeTarget : executeTargetList){
            boolean IWasInProcess = currentTask.IWasInProcess(executeTarget.getTargetName());
            if(executeTarget.getStatus().equals("IN PROCESS") && !IWasInProcess) {
                executeTargetsToSend.add(executeTarget);
                currentTask.getNameToWasInProcess().put(executeTarget.getTargetName(), true);
            }
        }
        return executeTargetsToSend;
    }

    private void changeStatusToInProcess(List<Minion> minionsToSend) {
        minionsToSend.forEach((minion -> minion.setStatus("IN PROCESS")));
    }

    private List<Minion> getMinionsFromQueue(Task task, Integer amountOfThreads) {
        Queue<Minion> waitingList = task.getWaitingList();
        List<Minion> minionsToSend = new ArrayList<>();
        synchronized (this) {
            while (!waitingList.isEmpty() && amountOfThreads > minionsToSend.size()) {
                Minion minion = waitingList.poll();
                minion.setStatus("IN PROCESS");
                // because I send him to run and I don't want it will run again, he can't run twice!
                minion.setCanIRun(false);
                minion.setiAmFinished(true);
                minionsToSend.add(minion);
            }
        }
        return minionsToSend;

    }

    public void updateExecuteTargetStatus(Task currentTask, TaskData taskData, ExecuteTarget executeTarget) {
        // get the current minion
        Map<String, Minion> nameToChosenMinion = currentTask.getNamesToMinionsThatUserChose();
        Minion finishedMinion = nameToChosenMinion.get(executeTarget.getTargetName());
        //update his status and who is open accordingly to the status
        finishedMinion.setStatus(executeTarget.getStatus());
        finishedMinion.setMyStatus(executeTarget.getStatus());
        finishedMinion.setiAmFinished(true);
        finishedMinion.setCanIRun(false);

        this.checkStatusAndUpdateMinions(finishedMinion, currentTask, taskData);
        //if there is new minion so insert them to the queue

    }


    private void checkStatusAndUpdateMinions(Minion finishedMinion, Task currentTask, TaskData taskData) {
        String status = finishedMinion.getStatus();
        //if the target done with success
        if(status.equals("SUCCESS") || status.equals("WARNING")){
            //check what minion he open
            finishedMinion.iOpened(finishedMinion.getParentsNames(), finishedMinion.getAllNamesToMinions());
            //update in task the minionsToAdd to the queue
           synchronized (this){currentTask.updateQueue();}
        }
        else{ // status is failure
            finishedMinion.checkAndUpdateWhoImClosedTORunning(finishedMinion, true);
            this.whoClosed(finishedMinion, taskData.getExecuteTargetList());
        }
        // check what minions are changer and update the executeTargetsListdd
        List<ExecuteTarget> executeTargetList = this.transferFromMinionToExecuteTarget(currentTask, taskData);
        taskData.setExecuteTargetList(executeTargetList);
    }

    private void whoClosed(Minion finishedMinion, List<ExecuteTarget> executeTargetList) {
        Set<String> parentsName = new HashSet<>(finishedMinion.getParentsNames());
        for(ExecuteTarget executeTarget : executeTargetList){
            if(parentsName.contains(executeTarget.getTargetName()))
                executeTarget.addWhoClosedMe(finishedMinion.getName());
        }
    }


}