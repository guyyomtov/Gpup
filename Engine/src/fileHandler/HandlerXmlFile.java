package fileHandler;

import GpupClassesEx3.GPUPDescriptor;
import GpupClassesEx3.GPUPTarget;
import GpupClassesEx3.GPUPTargetDependencies;
import Graph.Graph;
import Graph.Target;
import Graph.process.Task;
import errors.ErrorUtils;
import java.util.*;

public class HandlerXmlFile {

    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();
    private List<Target> targets = new ArrayList<Target>();
    private Map<String, Set<Target>> nameToSerialSet = new HashMap<String, Set<Target>>();
    private String graphName = new String();

    public void buildMe(GPUPDescriptor information) throws ErrorUtils {

        try {
            this.initGraphFromFile(information);
        }
        catch (ErrorUtils e) {throw e;}
    }

    public void initGraphFromFile(GPUPDescriptor information) throws ErrorUtils {

        int size = information.getGPUPTargets().getGPUPTarget().size();

        //GPUP PART TWO
        //Task.maxParallelism = information.getGPUPConfiguration().getGPUPMaxParallelism();

        this.targets = new ArrayList<Target>(size);

        try {

            this.targets = Target.CreateTargetsFromXmlInfo(information, size);

            this.mNameToTarget = Target.initNameToTargetFrom(this.targets);

            this.getFromFileDependencies(information.getGPUPTargets().getGPUPTarget());

            this.targets = Target.updateTypeOfTargets(this.targets);

            this.graphName = information.getGPUPConfiguration().getGPUPGraphName();

            this.checkCircleBetweenTwoTargets();

            //GPUP PART TWO
            //TaskFile.gpupPath = information.getGPUPConfiguration().getGPUPWorkingDirectory();

           this.nameToSerialSet = Graph.initSerialSetsFrom(information, this.targets);
        }
        catch (ErrorUtils e) {throw e;}
    }

    public void getFromFileDependencies(List<GPUPTarget> gpupTargets) throws ErrorUtils {

        for (int i = 0; i < targets.size(); i++) {

            try {

                setDependenciesList(gpupTargets.get(i).getGPUPTargetDependencies(), targets.get(i));

            } catch (ErrorUtils e) {
                throw e;
            }
        }
    }

    public void setDependenciesList(GPUPTargetDependencies gpupTargetDependencies, Target currTarget) throws ErrorUtils {

        if (gpupTargetDependencies == null) // its independent target
            return;
        List<GPUPTargetDependencies.GPUGDependency> lstOfGpupDependncies = gpupTargetDependencies.getGPUGDependency();
        int size = lstOfGpupDependncies.size();

        for (int i = 0; i < size; i++) {
            GPUPTargetDependencies.GPUGDependency gpupDependency = lstOfGpupDependncies.get(i);
            Target toAdd = this.mNameToTarget.get(gpupDependency.getValue());
            if(toAdd == null)
                throw new ErrorUtils("The target " + gpupDependency.getValue() + " can't include in dependencies list of any target because he doesn't exist.");
            if (gpupDependency.getType().compareTo("dependsOn") == 0) {
                currTarget.addTargetToDependsOnList(toAdd);
                toAdd.addTargetToRequiredForList(currTarget);

            } else /*if(!requiredFor.contains(gpupDependency.getValue()))*/ {
                currTarget.addTargetToRequiredForList(toAdd);
                toAdd.addTargetToDependsOnList(currTarget);
            }

        }


    }

    public void checkCircleBetweenTwoTargets() throws ErrorUtils {

        for (Target t : targets) {

            String type = t.getClass().getSimpleName();
            List<Target> dependsOn = t.getDependsOn();
            List<Target> requiredFor = t.getRequiredFor();

            try {

                checkCircleBetweenTwoTargetsHelper(t, dependsOn, true);

                checkCircleBetweenTwoTargetsHelper(t, requiredFor, false);
            }catch (ErrorUtils e){throw e;}

        }
    }

    public void checkCircleBetweenTwoTargetsHelper(Target currTarget, List<Target> dependenciesLst, boolean dependsOn) throws ErrorUtils {

        for(Target tR : dependenciesLst) {
            if(dependsOn)
            {
                List<Target> lst = tR.getDependsOn();
                if(lst.contains(currTarget))
                    throw new ErrorUtils(ErrorUtils.invalidFile("The target " + currTarget.getName() + " depends on the target " + tR.getName() + " and " +  tR.getName() + " depends on " + currTarget.getName()));

            }
            else // checking required for dependency
            {
                List<Target> lst = tR.getRequiredFor();
                if(lst.contains(currTarget))
                    throw new ErrorUtils(ErrorUtils.invalidFile("The target " + currTarget.getName() + " required for the target " + tR.getName() + " and " +  tR.getName() + " required for " + currTarget.getName()));

            }
        }

    }

    public List<Target> getListOfTargets(){return this.targets;}

    public Map<String, Target> getMap(){return this.mNameToTarget;}

    public Map<String, Set<Target>> getNameToSerialSet() { return this.nameToSerialSet;}

    public String getGraphName() {return graphName;}
}
