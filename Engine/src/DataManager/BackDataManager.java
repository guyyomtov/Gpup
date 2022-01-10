package DataManager;

import GpupClassesEx2.GPUPDescriptor;
import Graph.process.Compilation;
import Graph.process.DataSetupProcess;
import Graph.process.Simulation;
import errors.ErrorUtils;
import fileHandler.HandlerLoadFile;
import fileHandler.HandlerSaveFile;
import fileHandler.HandlerXmlFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


import Graph.*;
import fileHandler.GraphizHHandler;
import taskView.TaskController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private Graph graph;
    private Map<String, Set<Target>> mTypeToTargets = new HashMap<String, Set<Target>>();
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GpupClassesEx2";

    private TaskController taskController;

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

                    this.graph = new Graph(handlerFile.getListOfTargets(),handlerFile.getMap(), handlerFile.getNameToSerialSet());

                    this.graph.updateTotalDependenciesAndSerialSets();

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

        // Choose process
        if(dSP.flagger.processIsSimulation){

            Simulation simulation = new Simulation(dSP);

            this.taskController.bindTaskToUIComponents(simulation);

            new Thread(simulation).start();
        }
        else if(dSP.flagger.processIsCompilation){

            if(dSP.compilationProcessHasNeededData()){
                Compilation compilation = new Compilation(dSP);

                this.taskController.bindTaskToUIComponents(compilation);

                new Thread(compilation).start();
            }
        }

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

    public Set<List<Target>> whatIf(String name, String connection) throws ErrorUtils {
        try {
            return this.graph.whatIf(name, connection);
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
}