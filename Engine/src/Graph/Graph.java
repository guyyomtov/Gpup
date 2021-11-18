package Graph;
import Graph.Tree.Tree;
import errors.ErrorUtils;
import fileHandler.*;

import java.util.*;

public class Graph {
    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;
    private Tree tree = new Tree();
    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();
    private Map<String, Leaf> mNameToLeaf = new HashMap<String, Leaf>();
    private Map<String, Root> mNameToRoot= new HashMap<String, Root>();
    private Map<String, Middle> mNameToMiddle= new HashMap<String, Middle>();
    private Map<String, Independent> mNameToIndependent= new HashMap<String, Independent>();

    public void buildMe(GPUPDescriptor information) throws ErrorUtils {
        try {
            intilizeGraphFromFile(information);
        } catch (ErrorUtils e) {
            throw e;
        }

    }

    public void intilizeGraphFromFile(GPUPDescriptor information) throws ErrorUtils {
        int size = information.getGPUPTargets().getGPUPTarget().size();
        this.targets = new ArrayList<Target>(size);
        for (int i = 0; i < size; i++) {
            Target tmpTarget = this.setUpTarget(information, i);
            if (targetExist(tmpTarget.getName()))
                throw new ErrorUtils(ErrorUtils.invalidFile("The target " + tmpTarget.getName() + " was given twice.")); // to send more messege
            this.targets.add(tmpTarget);
        }

        try {

            this.initializeMap();

            this.getFromFileDependencies(information.getGPUPTargets().getGPUPTarget());

            this.tree.startMe(this.targets.size(), this.targets);

            this.checkCircleBetweenTwoTargets();

        } catch (ErrorUtils e) {throw e;}

    }

    private List<Target> testTList() {

        Root a = new Root("A","");
        Middle c = new Middle("C","");
        Leaf b = new Leaf("B","");
        Middle e = new Middle("E","");
        Independent f = new Independent("F","");

        a.setDependsOn(Arrays.asList(c, b));

        c.setRequiredFor(Arrays.asList(a));
        b.setRequiredFor(Arrays.asList(a));

        c.setDependsOn(Arrays.asList(a));

        e.setRequiredFor(Arrays.asList(c));

        e.setDependsOn(Arrays.asList(a));

        return Arrays.asList(a, c, b, e, f);
    }

    public void initializeMap() {
        for (Target currT : this.targets)
            this.mNameToTarget.put(currT.getName(), currT);

    }

    public Target setUpTarget(GPUPDescriptor information, int index) {
        GPUPTarget currTarget = information.getGPUPTargets().getGPUPTarget().get(index); // get name
        String name = currTarget.getName();
        String generalInfo = currTarget.getGPUPUserData();

        if (currTarget.getGPUPTargetDependencies() == null) // the target is independent
            return new Independent(name,generalInfo);
        else {
            int size = currTarget.getGPUPTargetDependencies().getGPUGDependency().size();
            Boolean middle = false, root = false, leaf = false;
            for (int i = 0; i < size; i++) {
                GPUPTargetDependencies.GPUGDependency gpupDependency = currTarget.getGPUPTargetDependencies().getGPUGDependency().get(i);
                if (gpupDependency.getType().compareTo("dependsOn") == 0)
                    root = true;
                else if (gpupDependency.getType().compareTo("requiredFor") == 0)
                    leaf = true;
            }
            if (root && leaf)
                return new Middle(name,generalInfo);
            else if (root)
                return new Root(name,generalInfo);
            else
                return new Leaf(name,generalInfo);
        }
    }

    public boolean targetExist(String currTargetName) {
        boolean result = false;
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i).getName().compareTo(currTargetName) == 0)
                return true;
        }
        return result;
    }

    public void getFromFileDependencies(List<GPUPTarget> gpupTargets) throws ErrorUtils {
        for (int i = 0; i < targets.size(); i++) {
            try {
                setDepenciesList(gpupTargets.get(i).getGPUPTargetDependencies(), targets.get(i));
            } catch (ErrorUtils e) {
                throw e;
            }
        }
    }

    public void setDepenciesList(GPUPTargetDependencies gpupTargetDependencies, Target currTarget) throws ErrorUtils {
        List<Target> depensOnList = new ArrayList<Target>();
        List<Target> requiredFor = new ArrayList<Target>();
        if (gpupTargetDependencies == null) // its independent target
            return;
        List<GPUPTargetDependencies.GPUGDependency> lstOfGpupDependncies = gpupTargetDependencies.getGPUGDependency();
        int size = lstOfGpupDependncies.size();

        for (int i = 0; i < size; i++) {
            GPUPTargetDependencies.GPUGDependency gpupDependency = lstOfGpupDependncies.get(i);
            if (gpupDependency.getType().compareTo("dependsOn") == 0 /*&& !depensOnList.contains(gpupDependency.getValue())*/)
                depensOnList.add(this.mNameToTarget.get(gpupDependency.getValue()));

            else /*if(!requiredFor.contains(gpupDependency.getValue()))*/
                requiredFor.add(this.mNameToTarget.get(gpupDependency.getValue()));
            /*else
            throw new ErrorUtils(ErrorUtils.invalidFile("The target" + gpupDependency.getValue() + "has two dependcies on the target" + currTarget.getName()));*/
        }
        if (!depensOnList.isEmpty() && !requiredFor.isEmpty()) {
            if (currTarget.getClass().getSimpleName().compareTo("Middle") == 0) {
                ((Middle) currTarget).setDependsOn(depensOnList);
                ((Middle) currTarget).setRequiredFor(requiredFor);
            }
        } else if (!depensOnList.isEmpty()) {
            if (currTarget.getClass().getSimpleName().compareTo("Root") == 0)
                ((Root) currTarget).setDependsOn(depensOnList);
        } else if (!requiredFor.isEmpty())
            ((Leaf) currTarget).setRequiredFor(requiredFor);

    }

    public Target getThisTarget(String nameOfTarget) throws ErrorUtils {
//        try{
//            Target currTarget = this.mNameToTarget.get(nameOfTarget);
//        }finally{ throw new ErrorUtils(ErrorUtils.invalidTarget());}
        Target currTarget = this.mNameToTarget.get(nameOfTarget);
        if (currTarget == null)
            throw new ErrorUtils(ErrorUtils.invalidTarget());
        return currTarget;

    }

    public List<Target> getAllTargets() {
        return this.targets;
    }

    public void checkCircleBetweenTwoTargets() throws ErrorUtils {

        for (Target t : targets) {
            String type = t.getClass().getSimpleName();
            List<Target> dependsOn = t.getDependsOn();
            List<Target> requiredFor = t.getRequiredFor();
            try {
                if (dependsOn != null)
                    checkCircleBetweenTwoTargetsHelper(t, dependsOn, true);
                if (requiredFor != null)
                    checkCircleBetweenTwoTargetsHelper(t, requiredFor, false);
            }catch (ErrorUtils e){throw e;}

    }
}

    public void checkCircleBetweenTwoTargetsHelper(Target currTarget, List<Target> dependenciesLst, boolean dependsOn) throws ErrorUtils {

        for(Target tR : dependenciesLst) {
            if(dependsOn)
            {
                List<Target> lst = tR.getDependsOn();
                if(lst != null)
                    if(lst.contains(currTarget))
                        throw new ErrorUtils(ErrorUtils.invalidFile("The target " + currTarget.getName() + " depends on the target " + tR.getName() + " and " +  tR.getName() + " depends on " + currTarget.getName()));

            }
            else // checking required for dependency
            {
                List<Target> lst = tR.getRequiredFor();
                if(lst != null)
                    if(lst.contains(currTarget))
                        throw new ErrorUtils(ErrorUtils.invalidFile("The target " + currTarget.getName() + " required for the target " + tR.getName() + " and " +  tR.getName() + " required for " + currTarget.getName()));

            }
        }

    }









//        for(int i = 0;i<targets.size() - 1;i++)
//            for(int j = i+1;j<targets.size();j++){
//                String targetName1 = targets.get(i).getName();
//                String targetName2 = targets.get(j).getName();
//                try {
//                    String path1 = tree.findAllPaths(targetName1, targetName2);
//                    String path2 = tree.findAllPaths(targetName2, targetName1);
//                    if(!path1.isEmpty() && !path2.isEmpty())
//                        if((targetName1+targetName2).contains(path1) && (targetName2+targetName1).contains(path2))
//                            throw new ErrorUtils(ErrorUtils.invalidFile("the target " + targetName1 + " depends on the target " + targetName2 + " and " +targetName2 + " depends on " + targetName1));
//                }catch(ErrorUtils e){throw e;}
//
//            }

        // Map<String, List<Target>> mNameToList = new HashMap<>();



    public String getPathFromTargets(String src, String dest) throws ErrorUtils
    {
        try{
            return this.tree.findAllPaths(src, dest);
        }catch (ErrorUtils e){throw e;}

    }


//    public Set<Targets> getAllTargets(){ return this.targets; }
//    public Graph(Collection<Targets> info)
//    {
//        this.targets = new HashSet<Targets>(20);
//
//    }

}
