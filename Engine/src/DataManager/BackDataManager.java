package DataManager;
import errors.ErrorUtils;
import fileHandler.HandlerLoadFile;
import fileHandler.HandlerSaveFile;
import fileHandler.HandlerXmlFile;
import fileHandler.TaskFile;
import schemaXmlFile.*;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


import Graph.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private Graph graph;
    private Map<String, Set<Target>> mTypeToTargets = new HashMap<String, Set<Target>>();
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "schemaXmlFile";

    public boolean checkFile(String fileName) throws ErrorUtils {
        boolean fileSuccess = false;
        try {
                InputStream inputStream = new FileInputStream(new File(fileName));
                //to check if the ended file is with xml
                GPUPDescriptor information = deserializeFrom(inputStream);
                if(information == null)
                    throw new ErrorUtils(ErrorUtils.invalidFile("file given is empty"));
                try{
                   // Graph tmpGraph = new Graph();
                    HandlerXmlFile handlerFile = new HandlerXmlFile();
                    handlerFile.buildMe(information);
                    fileSuccess =  true;
                    this.graph = new Graph(handlerFile.getListOfTargets(),handlerFile.getMap());
                    this.mTypeToTargets = this.makeMap(this.graph.getAllTargets());
                }catch(ErrorUtils e){throw e;}

            } catch (JAXBException | FileNotFoundException e) {
                 throw new ErrorUtils(ErrorUtils.invalidFile("the given file doesnt exist"));
            }

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

    public Map<String,List<String>> startProcess(Consumer cUI, Map<String,List<String>> oldProcessData) throws ErrorUtils{


        Map<String, Simulation> oldNamesToTasks = new HashMap<String, Simulation>();

        if(oldProcessData != null)
            oldNamesToTasks = this.makeTaskMapFrom(oldProcessData, this.graph.getAllTargets());

        if(this.graph.getAllTargets().isEmpty())
            throw new ErrorUtils(ErrorUtils.noGraph());
        TaskFile taskFile = new TaskFile();
        taskFile.makeTaskDir("simulation");
        return ProcessUtil.run(cUI, this.graph.getAllTargets(), oldNamesToTasks);
    }

    public Map<String,List<String>> startProcess(Consumer cUI, int timeToRun, int chancesToSucceed, int chancesToBeAWarning, Map<String,List<String>> oldProcessData) throws ErrorUtils{


        Map<String, Simulation> oldNamesToTasks = new HashMap<String, Simulation>();

        if(oldProcessData != null)
            oldNamesToTasks = this.makeTaskMapFrom(oldProcessData, this.graph.getAllTargets(), timeToRun, chancesToSucceed, chancesToBeAWarning);

        if(this.graph.getAllTargets().isEmpty())
            throw new ErrorUtils(ErrorUtils.noGraph());
        TaskFile taskFile = new TaskFile();
        taskFile.makeTaskDir("simulation");


        return ProcessUtil.run(cUI, this.graph.getAllTargets(), oldNamesToTasks, timeToRun, chancesToSucceed, chancesToBeAWarning);
    }

    private Map<String, String> generateMapTaskNameToStatus(Map<String,List<String>> targetNameToHisProcessData){

        Map<String, String> taskNameToStatus = new HashMap<>();
        List<String> curTaskData = new ArrayList<>();
        String curStatus = new String();

        for(String curTaskName : targetNameToHisProcessData.keySet()) {

            curTaskData = targetNameToHisProcessData.get(curTaskName);

            curStatus = curTaskData.get(3);

            taskNameToStatus.put(curTaskName, curStatus);
        }

        return taskNameToStatus;
    }

    public String findCircle(String name) throws ErrorUtils {
        try{
            return this.graph.findCircle(name);
        }catch (ErrorUtils e){throw e;}
    }

    private Map<String, Simulation> makeTaskMapFrom(Map<String,List<String>> oldProcessData, List<Target> allTargets, int timeToRun, int chancesToSucceed, int chancesToBeAWarning){

        Map<String, Simulation> oldNamesToTasks = new HashMap<String, Simulation>();
        List<String> curTaskData = new ArrayList<>();
        String curTaskStatus = new String();

        // if first time running
        if(oldProcessData.isEmpty()){

            for (Target curTarget : allTargets)
                oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget, timeToRun, chancesToSucceed, chancesToBeAWarning));

        }
        else { // ran all ready

            for (Target curTarget : allTargets) {

                // need this target to process
                if (oldProcessData.containsKey(curTarget.getName())) {

                    curTaskData = oldProcessData.get(curTarget.getName());

                    curTaskStatus = curTaskData.get(3);
                    oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget,curTaskStatus, timeToRun, chancesToSucceed, chancesToBeAWarning));
                }
                else // don't process this target
                    oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget, "SUCCESS"));
            }
        }

        return oldNamesToTasks;
    }


    private Map<String, Simulation> makeTaskMapFrom(Map<String,List<String>> oldProcessData, List<Target> allTargets){

        Map<String, Simulation> oldNamesToTasks = new HashMap<String, Simulation>();
        List<String> curTaskData = new ArrayList<>();
        String curTaskStatus = new String();

        // if first time running
        if(oldProcessData.isEmpty()){

            for (Target curTarget : allTargets)
                oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget));

        }
        else { // ran all ready

            for (Target curTarget : allTargets) {

                // need this target to process
                if (oldProcessData.containsKey(curTarget.getName())) {

                    curTaskData = oldProcessData.get(curTarget.getName());

                    curTaskStatus = curTaskData.get(3);
                    oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget, curTaskStatus));
                }
                else // don't process this target
                    oldNamesToTasks.put(curTarget.getName(), new Simulation(curTarget, "SUCCESS"));
            }
        }

        return oldNamesToTasks;
    }

    private List<Target> generateTargetListToProcess(List<Target> allTargets, Map<String,List<String>> targetNameToHisProcessData){

        // first time to run process
        if(targetNameToHisProcessData == null || targetNameToHisProcessData.isEmpty())
            return allTargets;

        List<Target> processOnlyThese = new ArrayList<Target>();
        String curTaskStatus = new String();
        Map<String, String> taskNameToStatus = generateMapTaskNameToStatus(targetNameToHisProcessData);

        // go over all targets
        for(Target curTarget : allTargets){

            curTaskStatus = taskNameToStatus.get(curTarget.getName());

            // only if it's a fitting task (didn't succeed), put it in the list.
            if(curTaskStatus == "SKIPPED" || curTaskStatus == "FAILURE")
                processOnlyThese.add(curTarget);
        }

        return processOnlyThese;
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


}