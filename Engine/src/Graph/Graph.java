package Graph;
import errors.ErrorUtils;
import fileHandler.*;

import java.util.*;

public class Graph {
    private List<Target> targets;
    //private boolean matrixOfDependency[][];
    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();

    public boolean buildMe(GPUPDescriptor information ) throws ErrorUtils {
        try{
            intilizeTargetsAndCheckDuplication(information);
        }catch(ErrorUtils e){throw e;}

        return false;
    }

    public void intilizeTargetsAndCheckDuplication(GPUPDescriptor information) throws ErrorUtils {
        int size = information.getGPUPTargets().getGPUPTarget().size();
        targets = new ArrayList<Target>(size);
        for(int i = 0;i<size;i++)
        {
            Target tmpTarget = setUpTarget(information, i);

           if(targetExist(tmpTarget.getName()))
                throw new ErrorUtils(ErrorUtils.invalidFile()); // to send more messege

            targets.add(tmpTarget);
        }
        initializeMap();
        try{
            getFromFileDependencies(information.getGPUPTargets().getGPUPTarget());

        }catch(ErrorUtils e){throw e;}
    }
    public void initializeMap()
    {
        for(Target currT: this.targets)
        {
            this.mNameToTarget.put(currT.getName(), currT);
        }
    }

    public Target setUpTarget(GPUPDescriptor information, int index)
    {
        GPUPTarget currTarget = information.getGPUPTargets().getGPUPTarget().get(index); // get name
        String name = currTarget.getName();

        if(currTarget.getGPUPTargetDependencies().getGPUGDependency().isEmpty())
            return new Independent(name);
        else {
            int size = currTarget.getGPUPTargetDependencies().getGPUGDependency().size();
            Boolean middle = false, root = false;

            for(int i = 0;i<size;i++)
            {
                if(currTarget.getGPUPTargetDependencies().getGPUGDependency().get(i).getType().compareTo("dependsOn") == 0)
                    root = true;
                else if(currTarget.getGPUPTargetDependencies().getGPUGDependency().get(i).getType().compareTo("requiredFor") == 0 && root)
                    middle = true;
            }

            if(middle)
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
    public void getFromFileDependencies(List<GPUPTarget> gpupTargets)
    {
        for(int i = 0; i<targets.size();i++)
        {
            setDepenciesList(gpupTargets.get(i).getGPUPTargetDependencies(), targets.get(i));
        }
    }

    public void setDepenciesList(GPUPTargetDependencies gpupTargetDependencies, Target currTarget) {
        int size = gpupTargetDependencies.getGPUGDependency().size();
        List<Target> depensOnList = new ArrayList<Target>();
        List<Target> requiredFor = new ArrayList<Target>();
        for(int i = 0;i<size;i++)
        {

            if(gpupTargetDependencies.getGPUGDependency().get(i).getType().compareTo("dependsOn") == 0)
                depensOnList.add(this.mNameToTarget.get(gpupTargetDependencies.getGPUGDependency().get(i).getValue()));
            else
                requiredFor.add(this.mNameToTarget.get(gpupTargetDependencies.getGPUGDependency().get(i).getValue()));
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
            {
                ((Root) currTarget).setDependsOn(depensOnList);
            }
        }
        else if(!requiredFor.isEmpty())
        {
            ((Leaf) currTarget).setRequiredFor(requiredFor);
        }

    }


//    public Set<Targets> getAllTargets(){ return this.targets; }
//    public Graph(Collection<Targets> info)
//    {
//        this.targets = new HashSet<Targets>(20);
//
//    }

}
