package Graph;
import Graph.Tree.Tree;
import errors.ErrorUtils;

import fileHandler.HandlerSaveFile;

import java.io.*;
import java.util.*;

public class Graph implements Serializable {




    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;
    private Tree tree = new Tree();


    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();

    public Graph(List<Target> targets, Map<String, Target> mNameToTarget) throws ErrorUtils {
        this.targets = targets;
        this.mNameToTarget = mNameToTarget;
        try {
            this.tree.startMe(this.targets.size(), this.targets);
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

    public boolean targetExist(Target currTarget)
    {
        if(targets.contains(currTarget))
            return true;
        return false;
    }

    public boolean targetExist(String targetName)
    {
        if(this.mNameToTarget.get(targetName) == null)
            return false;
        return true;
    }

    public String getPathFromTargets(String src, String dest) throws ErrorUtils
    {
        try{
            return this.tree.findAllPaths(src, dest);
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
                    return this.tree.findAllPaths(currTarget.getName(), res.get(0));
                }
                else throw new ErrorUtils("The target " + targetName + " doesn't in circle.");
            }
            else throw new ErrorUtils("The target " + targetName + " doesn't in circle.");
        }
        else
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target " + targetName + " doesn't exist"));

    }

    public void findCircleHelper(Target currTarget,Target dest ,List<String> res,Map<String, Boolean> isVisited)
    {
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

//    public void saveToFile(String fullPath)
//    {
//        HandlerSaveFile saveToFile = new HandlerSaveFile(this, fullPath);
//    }
//
//    public void loadDataFromFile(String fullPath) throws ErrorUtils {
//        try {
//            HandlerLoadBinaryFile loadFile = new HandlerLoadBinaryFile(this, fullPath);
//        }catch(ErrorUtils e){throw e;}
//    }

   // public void saveToFile(ObjectOutputStream)



}
