package Graph;
import GpupClassesEx2.GPUPDescriptor;
import errors.ErrorUtils;

import java.io.*;
import java.util.*;

public class Graph implements Serializable {




    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;
    private PathFinder pathFinder = new PathFinder();


    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();

    private Map<String, Set<Target>> mSerialSets = new HashMap<>();


    public Graph(List<Target> targets, Map<String, Target> mNameToTarget, Map<String, Set<Target>> mSerialSets) throws ErrorUtils {
        this.targets = targets;
        this.mNameToTarget = mNameToTarget;
        this.mSerialSets = mSerialSets;
        try {
            this.pathFinder.startMe(this.targets.size(), this.targets);
        }catch(ErrorUtils e){throw e;}
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

    //todo to give attention to the connection
    public  Set<List<Target>> whatIf(String targetName, String connection) throws ErrorUtils{

        List<String> allDependencies = new ArrayList<>();
        Map<String, Boolean> isVisited = new HashMap<>();
        isVisited = this.makeIsVisitedMap();
        if(!targetExist(targetName))
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + targetName + "doesn't exist"));

        else {
            findDependencies(mNameToTarget.get(targetName), mNameToTarget.get(targetName),  allDependencies, isVisited);
        }

        return this.createListOfDependencies(allDependencies);
    }

    private void findDependencies(Target currTarget, Target target,  List<String> allDependencies,Map<String, Boolean> isVisited ) throws ErrorUtils {

        List<Target> directDependencies = target.getDependsOn();
        try {
            if (target.getTargetType().equals(Target.Type.LEAF)) {
                isVisited.put(target.getName(), true);
                String path = this.pathFinder.findAllPaths(currTarget.getName(), target.getName());
                allDependencies.add(path);
            }
            else {
                isVisited.put(target.getName(), true);
                for (Target currT : directDependencies) {
                    if(!isVisited.get(currT.getName()))
                        findDependencies(currTarget, currT, allDependencies, isVisited);
                }
            }
        }catch (ErrorUtils e){throw e;}


    }
    //todo
    private Set<List<Target>> createListOfDependencies(List<String> allDependencies) {

        Set<List<Target>> setOfDependencies = new HashSet<>();
        for(String str : allDependencies)
        {
            String[] split = str.split(",");
            for(String string : split)
            {
                string = string.replace(" ", ",");
                try {
                    //todo
                    List<Target> path = new ArrayList<Target>(Target.getTargetsByName(string, this.targets));
                    setOfDependencies.add(path);
                }catch(ErrorUtils e){}
//                for(String ch : s)
//                {
//                    path.add(this.mNameToTarget.get(ch));
//                }

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
        Map <String, Boolean> isVisited = makeIsVisitedMap();
        for(Target target : this.targets)
        {
            target.countTotalDependsOn(isVisited);
            target.countTotalRequiredFor(isVisited);
            target.countIncludedSerialSets(this.mSerialSets);

        }

    }

    private Map<String, Boolean> makeIsVisitedMap() {
        Map <String, Boolean> isVisited = new HashMap<>();
        for(Target target: targets)
            isVisited.put(target.getName(), false);
        return isVisited;
    }

}
