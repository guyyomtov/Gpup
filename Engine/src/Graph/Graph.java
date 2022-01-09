package Graph;
import GpupClassesEx2.GPUPDescriptor;
import errors.ErrorUtils;
import fileHandler.schemaXmlFile.GraphizHHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class Graph implements Serializable {

    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;
    private PathFinder pathFinder = new PathFinder();
    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();
    private Map<String, Set<Target>> mSerialSets = new HashMap<>();


    public Graph(List<Target> targets, Map<String, Target> mNameToTarget, Map<String, Set<Target>> mSerialSets) throws ErrorUtils, IOException {

        this.targets = targets;
        this.mNameToTarget = mNameToTarget;
        this.mSerialSets = mSerialSets;
        try {
            this.pathFinder.startMe(this.targets.size(), this.targets);
        }catch(ErrorUtils e){throw e;}

//        GraphizHHandler.makeGraphizPNGFrom(null, this);
    }

    public List<Target> getTargets() {return targets;}

    public Map<String, Target> getmNameToTarget() {return mNameToTarget;}

    public Target getThisTarget(String nameOfTarget) throws ErrorUtils {
        Target currTarget = this.mNameToTarget.get(nameOfTarget);
        if (currTarget == null)
            throw new ErrorUtils(ErrorUtils.invalidTarget());
        return currTarget;

    }

    public List<Target> getAllTargets() {
        return this.targets;
    }

    public boolean targetExist(Target currTarget) {
        if(targets.contains(currTarget))
            return true;
        return false;
    }

    public boolean targetExist(String targetName) {
        if(this.mNameToTarget.get(targetName) == null)
            return false;
        return true;
    }

    public String getPathFromTargets(String src, String dest) throws ErrorUtils {
        try{
            return this.pathFinder.findAllPaths(src, dest);
        }catch (ErrorUtils e){throw e;}

    }

    public String findCircle(String targetName) throws ErrorUtils {
        if(targetExist(targetName))
        {
            Target currTarget = mNameToTarget.get(targetName);
            if(currTarget.getTargetType().toString().equals("Middle"))
            {
                List<String> res = new ArrayList<>();
                Map<String, Boolean> isVisited = new HashMap<>();
                for(Target t : targets)
                    isVisited.put(t.getName(), false);
                findCircleHelper(currTarget,currTarget,res, isVisited);
                if(!res.isEmpty())
                {
                    return this.pathFinder.findAllPaths(currTarget.getName(), res.get(0));
                }
                else throw new ErrorUtils("The target " + targetName + " doesn't in circle.");
            }
            else throw new ErrorUtils("The target " + targetName + " isn't in a circle.");
        }
        else
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + targetName + " doesn't exist"));

    }

    public void findCircleHelper(Target currTarget,Target dest ,List<String> res,Map<String, Boolean> isVisited) {
        if(currTarget.getTargetType().toString().equals("Leaf"))
            return;
        else if(currTarget.getDependsOn().contains(dest)) {
            res.add(currTarget.getName());
            return;
        }
        else if(isVisited.get(currTarget.getName()))
            return;
        else
        {
            isVisited.put(currTarget.getName(), true);
            List<Target> dependsOnLst = currTarget.getDependsOn();
            for(Target t: dependsOnLst) {
                findCircleHelper(t, dest, res, isVisited);
            }

        }

    }

    public static Map<String, Set<Target>> initSerialSetsFrom(GPUPDescriptor information, List<Target> targets) throws ErrorUtils  {

        List<String> serialSetNames = new ArrayList<>();
        Map<String, Set<Target>> serialNameToTargets = new HashMap<>();
        String curSerialName = new String(), curTargetsName = new String();
        Set<Target> curSerialTargets = new HashSet<>();

        // get to serial sets
        GPUPDescriptor.GPUPSerialSets gpupTmpMap = information.getGPUPSerialSets();
        List<GPUPDescriptor.GPUPSerialSets.GPUPSerialSet> tmp = gpupTmpMap.getGPUPSerialSet();

        // add each serial set to res map
        for(GPUPDescriptor.GPUPSerialSets.GPUPSerialSet cur : tmp){

            curSerialName = cur.getName();
            serialSetNames.add(curSerialName);

            curTargetsName = cur.getTargets();

            // get serial targets from all targets & checks that serial targets exist
            curSerialTargets = Target.getTargetsByName(curTargetsName, targets);

            serialNameToTargets.put(curSerialName, curSerialTargets);
        }

        if(serialNameRepeatsItself(serialSetNames))
            throw new ErrorUtils(ErrorUtils.invalidXMLFile("Different serial sets with  the same name found."));

        return serialNameToTargets;
    }

    public static boolean serialNameRepeatsItself(List<String> serialSetNames) {

        boolean foundDouble = false;
        int occurCounter = 0;

        for(String curName : serialSetNames){

           for(String c : serialSetNames){

               if(c == curName)
                   occurCounter++;
           }

           if(occurCounter > 1) {
               foundDouble = true;
               break;
           }

            occurCounter = 0;
        }
        return foundDouble;
    }

    public Set<List<Target>> whatIf(String targetName, String connection) throws ErrorUtils{

        List<String> allDependencies = new ArrayList<>();
        Map<String, Boolean> isVisited = new HashMap<>();
        isVisited = makeIsVisitedMap();
        if(!targetExist(targetName))
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + targetName + "doesn't exist"));

        else {
            findDependencies(mNameToTarget.get(targetName), mNameToTarget.get(targetName),  allDependencies, isVisited, connection);
        }

        return this.createListOfDependencies(allDependencies);
    }

    private void findDependencies(Target currTarget, Target target,  List<String> allDependencies,Map<String, Boolean> isVisited, String connection ) throws ErrorUtils {

        List<Target> directDependencies;
        try {
            if ((target.getTargetType().equals(Target.Type.LEAF) && connection.equals("D") )|| (target.getTargetType().equals(Target.Type.ROOT) && connection.equals("R")) && currTarget != target) {
                isVisited.put(target.getName(), true);
                String path;
                if(connection.equals("D"))
                    path = this.pathFinder.findAllPaths(currTarget.getName(), target.getName());
                else
                    path = this.pathFinder.findAllPaths(target.getName(), currTarget.getName());

                allDependencies.add(path);
            }
            else {
                isVisited.put(target.getName(), true);
                if(connection.equals("D"))
                    directDependencies = target.getDependsOn();
                else
                    directDependencies = target.getRequiredFor();

                    for (Target currT : directDependencies) {
                        if (!isVisited.get(currT.getName()))
                            findDependencies(currTarget, currT, allDependencies, isVisited, connection);
                    }

                }

        }catch (ErrorUtils e){throw e;}


    }

    private Set<List<Target>> createListOfDependencies(List<String> allDependencies) {

        Set<List<Target>> setOfDependencies = new HashSet<>();
        for(String str : allDependencies)
        {
            String[] split = str.split(",");
            for(String string : split)
            {
                string = string.replace(" ", "");
                   List<Target> path = new ArrayList<Target>();

                   String[] targetNames = string.split("");
                   for(String targetName : targetNames) {
                       Target target = this.mNameToTarget.get(targetName);
                       if(target != null)
                           path.add(target);
                   }
                    setOfDependencies.add(path);
            }
        }
        return setOfDependencies;
    }

    public Map<String, Set<Target>> getmSerialSets() {
        return mSerialSets;
    }

    public void setmSerialSets(Map<String, Set<Target>> mSerialSets) {
        this.mSerialSets = mSerialSets;
    }


    public void updateTotalDependenciesAndSerialSets() {
        Map <String, Boolean> isVisited;
        Set<List<Target>> allPath;
        int totalDependsON, totalRequiredFor;
        try {
            for (Target target : this.targets) {
                allPath = this.whatIf(target.getName(), "D");
                totalDependsON = countTotalDependencies(target ,allPath);
                allPath = this.whatIf(target.getName(), "R");;
                totalRequiredFor =  countTotalDependencies(target,allPath);
                target.countIncludedSerialSets(this.mSerialSets);
                target.setTotalDependsOn(totalDependsON);
                target.setTotalRequiredFor(totalRequiredFor);
            }
        }catch(ErrorUtils e){}
    }

    public int countTotalDependencies(Target currTarget,Set<List<Target>> allPath){
        int res = 0;
        Map<String, Boolean> isVisited = this.makeIsVisitedMap();
        isVisited.put(currTarget.getName(), true);
        for(List<Target> path : allPath){
         //   path.stream().filter(target -> isVisited.get(target.getName()) == false).forEach((target -> isVisited.put(target.getName(), true));
            for(Target target : path){
                if(!isVisited.get(target.getName())){
                    isVisited.put(target.getName(), true);
                    ++res;
                }
            }
        }
        return res;
    }


    private Map<String, Boolean> makeIsVisitedMap() {
        Map <String, Boolean> isVisited = new HashMap<>();
        for(Target target: targets)
            isVisited.put(target.getName(), false);
        return isVisited;
    }


}
