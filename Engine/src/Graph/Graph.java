package Graph;
import errors.ErrorUtils;
import fileHandler.*;

import java.util.HashSet;
import java.util.Set;

public class Graph {
    private Set<Target> targets;
    private boolean matrixOfDependency[][];

    public boolean buildMe(GPUPDescriptor information )
    {
        try{
            intilizeTargetsAndCheckDuplication(information);
        }catch(ErrorUtils e){throw e;}


        return false;
    }

    public void intilizeTargetsAndCheckDuplication(GPUPDescriptor information)
    {
        int size = information.getGPUPTargets().getGPUPTarget().size();
        targets = new HashSet<Target>(size);
        for(int i = 0;i<size;i++)
        {
            Target tmpTarget = setUpTarget(information, i);

        }
    }
    public Target setUpTarget(GPUPDescriptor information, int index)
    {
        GPUPTarget tmp = information.getGPUPTargets().getGPUPTarget().get(index);
        String name = tmp.getName();
        if(tmp.getGPUPTargetDependencies().getGPUGDependency())
    }

    public Set<Targets> getAllTargets(){ return this.targets; }
 /*   public Graph(Collection<Targets> info)
    {
        this.targets = new HashSet<Targets>(20);

    }*/

}
