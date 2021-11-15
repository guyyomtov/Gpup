package Graph;
import errors.ErrorUtils;
import fileHandler.*;

import java.util.*;

public class Graph {
    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;
    private Tree tree;
    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();

    public void buildMe(GPUPDescriptor information ) throws ErrorUtils {
        try{
            intilizeTargetsAndCheckDuplication(information);
        }catch(ErrorUtils e){throw e;}

    }

    public void intilizeTargetsAndCheckDuplication(GPUPDescriptor information) throws ErrorUtils {
        int size = information.getGPUPTargets().getGPUPTarget().size();
        this.targets = new ArrayList<Target>(size);
        for(int i = 0;i<size;i++)
        {
            Target tmpTarget = this.setUpTarget(information, i);
           if(targetExist(tmpTarget.getName()))
                throw new ErrorUtils(ErrorUtils.invalidFile()); // to send more messege
            this.targets.add(tmpTarget);
        }
        this.initializeMap();
        try{
            this.getFromFileDependencies(information.getGPUPTargets().getGPUPTarget());
            this.checkCyrcltBetweenTwoTargets();
        }catch(ErrorUtils e){throw e;}

    }

    public void initializeMap()
    {
        for(Target currT: this.targets)
            this.mNameToTarget.put(currT.getName(), currT);

    }

    public Target setUpTarget(GPUPDescriptor information, int index)
    {
        GPUPTarget currTarget = information.getGPUPTargets().getGPUPTarget().get(index); // get name
        String name = currTarget.getName();

        if(currTarget.getGPUPTargetDependencies() == null) // the target is independent
            return new Independent(name);
        else {
            int size = currTarget.getGPUPTargetDependencies().getGPUGDependency().size();
            Boolean middle = false, root = false, leaf = false;
            for(int i = 0;i<size;i++)
            {
                GPUPTargetDependencies.GPUGDependency gpupDependency = currTarget.getGPUPTargetDependencies().getGPUGDependency().get(i);
                if(gpupDependency.getType().compareTo("dependsOn") == 0 )
                    root = true;
                else if(gpupDependency.getType().compareTo("requiredFor") == 0 )
                    leaf = true;
            }
            if(root&&leaf)
                return new Middle(name);
            else if(root)
                return new Root(name);
            else
                return new Leaf(name);
        }
    }
    public boolean targetExist(String currTargetName)
    {
        boolean result = false;
        for(int i=0;i<targets.size();i++)
        {
            if(targets.get(i).getName().compareTo(currTargetName) == 0)
                return true;
        }
        return result;
    }
    public void getFromFileDependencies(List<GPUPTarget> gpupTargets) throws ErrorUtils
    {
        for(int i = 0; i<targets.size();i++)
        {
            try {
                setDepenciesList(gpupTargets.get(i).getGPUPTargetDependencies(), targets.get(i));
            }catch(ErrorUtils e){throw e;}
        }
    }

    public void setDepenciesList(GPUPTargetDependencies gpupTargetDependencies, Target currTarget) throws ErrorUtils{
        List<Target> depensOnList = new ArrayList<Target>();
        List<Target> requiredFor = new ArrayList<Target>();
        if(gpupTargetDependencies == null) // its independent target
            return;
        List<GPUPTargetDependencies.GPUGDependency> lstOfGpupDependncies = gpupTargetDependencies.getGPUGDependency();
        int size = lstOfGpupDependncies.size();

        for(int i = 0;i<size;i++)
        {
            GPUPTargetDependencies.GPUGDependency gpupDependency = lstOfGpupDependncies.get(i);
            if(gpupDependency.getType().compareTo("dependsOn") == 0 && !depensOnList.contains(gpupDependency.getValue()))
                depensOnList.add(this.mNameToTarget.get(gpupDependency.getValue()));

            else if(!requiredFor.contains(gpupDependency.getValue()))
                requiredFor.add(this.mNameToTarget.get(gpupDependency.getValue()));
            else
                throw new ErrorUtils(ErrorUtils.invalidFile("The target" + gpupDependency.getValue() + "has two dependcies on the target" + currTarget.getName()));
        }
        if(!depensOnList.isEmpty() && !requiredFor.isEmpty())
        {
            if(currTarget.getClass().getSimpleName().compareTo("Middle") == 0) {
                ((Middle) currTarget).setDependsOn(depensOnList);
                ((Middle) currTarget).setRequiredFor(requiredFor);
            }
        }
        else if(!depensOnList.isEmpty())
        {
            if(currTarget.getClass().getSimpleName().compareTo("Root") == 0)
                ((Root) currTarget).setDependsOn(depensOnList);
        }
        else if(!requiredFor.isEmpty())
            ((Leaf) currTarget).setRequiredFor(requiredFor);

    }

    public Target getThisTarget(String nameOfTarget) throws ErrorUtils {
//        try{
//            Target currTarget = this.mNameToTarget.get(nameOfTarget);
//        }finally{ throw new ErrorUtils(ErrorUtils.invalidTarget());}
        Target currTarget = this.mNameToTarget.get(nameOfTarget);
        if(currTarget == null)
            throw new ErrorUtils(ErrorUtils.invalidTarget());
        return currTarget;

    }

    public List<Target> getAllTargets() {return this.targets;}

    public void checkCyrcltBetweenTwoTargets() throws ErrorUtils
    {
        for(int i = 0;i<targets.size() - 1;i++)
            for(int j = i+1;j<targets.size();j++){
                String targetName1 = targets[i].getName();
                String targetName2 = targets[j].getName();
                String path1 = Tree.findAllPath(targetName1, targetName2);
                String path2 = Tree.findAllPath(targetName2, targetName1);
                if((targetName1+targetName2).contains.(path1) && (targetName2+targetName1).contains.(path2))
                    throw new ErrorUtils(ErrorUtils.invalidFile("the target" + targetName1 "depends on the target " + targetName2 + "and" +targetName2 + "depends on" + targetName1));

            }

       // Map<String, List<Target>> mNameToList = new HashMap<>();


    }


//    public Set<Targets> getAllTargets(){ return this.targets; }
//    public Graph(Collection<Targets> info)
//    {
//        this.targets = new HashSet<Targets>(20);
//
//    }

}
