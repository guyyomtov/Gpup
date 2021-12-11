package fileHandler;

import Graph.Target;
import errors.ErrorUtils;
import schemaXmlFile.GPUPDescriptor;
import schemaXmlFile.GPUPTarget;
import schemaXmlFile.GPUPTargetDependencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerXmlFile {

    private Map<String, Target> mNameToTarget = new HashMap<String, Target>();
    private List<Target> targets = new ArrayList<Target>();
    public boolean isGood = true;

    public void buildMe(GPUPDescriptor information) throws ErrorUtils {
        try {
            initGraphFromFile(information);
        } catch (ErrorUtils e) {
            throw e;
        }

    }

    public void initGraphFromFile(GPUPDescriptor information) throws ErrorUtils {
        int size = information.getGPUPTargets().getGPUPTarget().size();
        this.targets = new ArrayList<Target>(size);
        for (int i = 0; i < size; i++) {
            Target tmpTarget = this.setUpTarget(information, i);
            if (targetExist(tmpTarget))
                throw new ErrorUtils(ErrorUtils.invalidFile("The target " + tmpTarget.getName() + " was given twice.")); // to send more messege
            this.targets.add(tmpTarget);
        }

        try {

            this.initializeMap();

            this.getFromFileDependencies(information.getGPUPTargets().getGPUPTarget());

            this.updateTypeOfTargets();

          //  this.tree.startMe(this.targets.size(), this.targets);

            this.checkCircleBetweenTwoTargets();

            TaskFile.gpupPath = information.getGPUPConfiguration().getGPUPWorkingDirectory();

        } catch (ErrorUtils e) {throw e;}

    }

    public void updateTypeOfTargets() { for(Target tr : targets) tr.setTargetType(); }

    public void initializeMap() {
        for (Target currT : this.targets)
            this.mNameToTarget.put(currT.getName(), currT);

    }

    public Target setUpTarget(GPUPDescriptor information, int index) {

        GPUPTarget currTarget = information.getGPUPTargets().getGPUPTarget().get(index); // get name
        String name = currTarget.getName();
        String generalInfo;
        if(currTarget.getGPUPUserData() == null)
            generalInfo = "Nothing";
        else
            generalInfo = currTarget.getGPUPUserData();
        return new Target(name, generalInfo);
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

    public List<Target> getListOfTargets(){return this.targets;}

    public Map<String, Target> getMap(){return this.mNameToTarget;}





}
