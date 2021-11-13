package DataManager;
import errors.ErrorUtils;
import fileHandler.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


import Graph.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private Graph graph = new Graph();
    private Map<String, Set<Target>> mTypeToTargets = new HashMap<String, Set<Target>>();
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "fileHandler";

    public boolean checkFile(String fileName) throws ErrorUtils {
        try {
                InputStream inputStream = new FileInputStream(new File("Engine/src/resources/"+fileName));
                //to check if the ended file is with xml
                GPUPDescriptor information = deserializeFrom(inputStream);
                if(information == null)
                    throw new ErrorUtils(ErrorUtils.invalidFile("file given is empty"));
                try{
                    this.graph.buildMe(information);
                }catch(ErrorUtils e){throw e;}

            } catch (JAXBException | FileNotFoundException e) {
                 throw new ErrorUtils(ErrorUtils.invalidFile("the given file doesnt exist"));
            }


        return false;
    }
    private static GPUPDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GPUPDescriptor) u.unmarshal(in);
    }
//    public boolean setUpGraph(GPUPDescriptor information ) throws ErrorUtils {
//        // graph = new Graph(f);
//        if(this.graph.buildMe(information))
//            return true;
//        else {
//            throw new ErrorUtils(ErrorUtils.invalidFile());
//        }
//
//    }
    private Map<String, Set<Target>> makeMap(List<Target> targets){

        String tType;
        Set<Target> leaf, independent, middle, root = new HashSet<Target>();

        Map<String, Set<Target>> tmp = new HashMap<>();
        tmp.put("Independent", new HashSet<Target>() );
        tmp.put("Leaf", new HashSet<Target>());
        tmp.put("Middle", new HashSet<Target>());
        tmp.put("Root", new HashSet<Target>());

        for(Target curT : targets){

            tType = (curT.getClass()).getSimpleName();

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

//            try {
                Target target = this.graph.getThisTarget(nameOfTarget);

                String listOfDependent = "", listOfRequired = "";

                if(target.getClass().getSimpleName() == "Leaf"){
                    Leaf leaf = (Leaf)target;
                    listOfRequired = this.makeToString(leaf.getRequired());
                }
                else if(target.getClass().getSimpleName() == "Middle"){

                    Middle middle = (Middle) target;
                    listOfRequired = this.makeToString(middle.getRequired());
                    listOfDependent = this.makeToString(middle.getDependencies());
                }
                else if(target.getClass().getSimpleName() == "Root"){

                    Root root = (Root) target;
                    listOfDependent = this.makeToString(root.getDependencies());
                }

                dataOfT.add(0, target.getName());               // = target name;
                dataOfT.add(1, target.getClass().getSimpleName());    // = location of the target.
                dataOfT.add(2, listOfRequired);                 // = list of dependants targets
                dataOfT.add(3, listOfDependent);                // = list of requires for targets.
                dataOfT.add(4, target.getGeneralInfo());        // = general information.

//            }catch(ErrorUtils e){ throw e;}
        }
        return dataOfT;
    }

    private String makeToString(List<Target> targets){
        return " ";

    }

    @Override
    public List<String> getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {
        return null;
    }
}