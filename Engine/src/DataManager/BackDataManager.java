package DataManager;
import errors.ErrorUtils;
import fileHandler.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


import Graph.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private Graph graph = new Graph();
    private Map<String, Set<Target>> mTypeToTargets = new HashMap<String, Set<Target>>();
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "fileHandler";

    public boolean checkFile(String fileName) throws ErrorUtils {
        boolean fileSuccses = false;
        try {
                InputStream inputStream = new FileInputStream(new File("Engine/src/resources/"+fileName));
                //to check if the ended file is with xml
                GPUPDescriptor information = deserializeFrom(inputStream);
                if(information == null)
                    throw new ErrorUtils(ErrorUtils.invalidFile("file given is empty"));
                try{
                    this.graph.buildMe(information);
                    fileSuccses =  true;
                    this.mTypeToTargets = this.makeMap(this.graph.getAllTargets());
                }catch(ErrorUtils e){throw e;}

            } catch (JAXBException | FileNotFoundException e) {
                 throw new ErrorUtils(ErrorUtils.invalidFile("the given file doesnt exist"));
            }

        return fileSuccses;
    }

    private static GPUPDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GPUPDescriptor) u.unmarshal(in);
    }

//    private List<Target> testTList(){
//
//        Root        a = new Root("A","");
//        Middle      c =  new Middle("C","");
//        Leaf        b =  new Leaf("B","");
//        Middle      e = new Middle("E","");
//        Independent f = new Independent("F","");
//
//        a.setDependsOn(Arrays.asList(c,b));
//
//        c.setRequiredFor(Arrays.asList(a));
//        b.setRequiredFor(Arrays.asList(a));
//
//        c.setDependsOn(Arrays.asList(a));
//
//        e.setRequiredFor(Arrays.asList(c));
//
//        e.setDependsOn(Arrays.asList(a));
//
//        return Arrays.asList(a, c, b, e, f);
//    }

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
                dataOfT.add(1, target.getClass().getSimpleName());    // = location of the target.
                dataOfT.add(2, listOfRequired);                 // = list of dependants targets
                dataOfT.add(3, listOfDependent);                // = list of requires for targets.
                dataOfT.add(4, target.getGeneralInfo());        // = general information.

           }catch(ErrorUtils e){ throw e;}
        }
        return dataOfT;
    }

    private String makeToString(List<Target> targets)
    {
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
        if(connection.compareTo("dependsOn") == 0)
            return this.graph.getPathFromTargets(src, dest);
        else if(connection.compareTo("requiredFor") == 0)
            return this.graph.getPathFromTargets(dest, src);
        else
            throw new ErrorUtils(ErrorUtils.invalidInput("Please enter in the wanted relationship 'dependsOn'/ 'requiredFor'."));
    }
}