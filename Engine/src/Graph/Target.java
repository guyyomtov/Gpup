package Graph;

import GpupClassesEx3.GPUPDescriptor;
import GpupClassesEx3.GPUPTarget;
import errors.ErrorUtils;
import javafx.scene.control.CheckBox;
//import fileHandler.schemaXmlFile.GPUPDescriptor;
//import fileHandler.schemaXmlFile.GPUPTarget;

import java.io.Serializable;
import java.util.*;

public class Target implements Serializable {

    public enum Type implements Serializable {
        INDEPENDENT {
            public String toString() {
                return "Independent";
            }
        }, ROOT {
            public String toString() {
                return "Root";
            }
        },

        MIDDLE {
            public String toString() {
                return "Middle";
            }
        }, LEAF {
            public String toString() {
                return "Leaf";
            }
        };
    }

    private String name;
    private Integer countOfDependency;
    private String generalInfo;
    private Type targetType;
    private List<Target> dependsOn = new ArrayList<Target>();
    private List<Target> requiredFor = new ArrayList<Target>();
    ;
    private Integer totalDependsOn = 0;
    private Integer totalRequiredFor = 0;
    private Integer totalSerialSets = 0;
    private CheckBox remark = new CheckBox();

    public Integer getTotalSerialSets() {
        return totalSerialSets;
    }

    public Integer getTotalRequiredFor() {
        return totalRequiredFor;
    }

    public Integer getTotalDependsOn() {
        return totalDependsOn;
    }

    public void setDependsOn(List<Target> dependsOn) {
        this.dependsOn = dependsOn;
    }

    public void setRequiredFor(List<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public List<Target> getDependsOn() {
        return this.dependsOn;
    }

    public List<Target> getRequiredFor() {
        return this.requiredFor;
    }

    public void addTargetToDependsOnList(Target toAdd) {
        if (!this.dependsOn.contains(toAdd))
            dependsOn.add(toAdd);
    }

    public void addTargetToRequiredForList(Target toAdd) {
        if (!this.requiredFor.contains(toAdd))
            this.requiredFor.add(toAdd);
    }

    public Target(String name, String generalInfo) {
        this.name = name;
        this.countOfDependency = 0;
        this.generalInfo = generalInfo;
    }

    public static List<Target> getTargetByType(List<Target> targets, Type wantedType) {

        List<Target> res = new ArrayList<Target>();

        for (Target curT : targets) {

            if (curT.getTargetType() == wantedType)
                res.add(curT);
        }

        return res;
    }

    public static Map<Target, Boolean> initiHaveBeenHereMap(List<Target> targets) {

    Map<Target, Boolean> iHaveBeenHereMap = new HashMap<Target, Boolean>();

        for(Target curT :targets) {

            iHaveBeenHereMap.put(curT, false);
        }

        return iHaveBeenHereMap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneralInfo() {
        return generalInfo;
    }

    public Integer getCountOfDependency() {
        return countOfDependency;
    }

    public void setCountOfDependency(Integer countOfDependency) {
        this.countOfDependency = countOfDependency;
    }

    public Type getTargetType() {
        return targetType;
    }

    public void setTargetType() {

        if(this.dependsOn.isEmpty() && this.requiredFor.isEmpty())
            this.targetType = Type.INDEPENDENT;
        else if(!this.dependsOn.isEmpty() && !this.requiredFor.isEmpty())
            this.targetType = Type.MIDDLE;
        else if(!this.dependsOn.isEmpty())
            this.targetType = Type.ROOT;
        else
            this.targetType = Type.LEAF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return Objects.equals(name, target.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    static public Map<String, Target> initNameToTargetFrom(List<Target> targets) {

        Map<String, Target> resM = new HashMap<>();

        for(Target curT : targets)
            resM.put(curT.getName(), curT);

        return resM;
    }

    static public Target makeMe(GPUPDescriptor information, int index) {

        GPUPTarget currTarget = information.getGPUPTargets().getGPUPTarget().get(index); // get name
        String name = currTarget.getName();
        String generalInfo;

        if(currTarget.getGPUPUserData() == null)
            generalInfo = "Nothing";
        else
            generalInfo = currTarget.getGPUPUserData();

        return new Target(name, generalInfo);
    }

    static public  List<Target> updateTypeOfTargets(List<Target> targets) {

        for(Target tr : targets)
            tr.setTargetType();

        return targets;
    }

    static public List<Target> CreateTargetsFromXmlInfo(GPUPDescriptor information, int size) throws ErrorUtils {

        List<Target> targets = new ArrayList<Target>(size);

        for (int i = 0; i < size; i++) {

            Target tmpTarget = Target.makeMe(information, i);

            if (iExsistAllReady(tmpTarget, targets))
                throw new ErrorUtils(ErrorUtils.invalidFile("The target " + tmpTarget.getName() + " was given twice.")); // to send more messege

            targets.add(tmpTarget);
        }
        return targets;
    }

    static public boolean iExsistAllReady(Target currTarget,List<Target> targets){

        if(targets.contains(currTarget))
            return true;

        return false;
    }

    public static Set<Target> getTargetsByName(String curTargetsName, List<Target> targets) throws ErrorUtils {

        Set<Target> res = new HashSet<Target>();
        Set<String> allTargetNames = new HashSet<>();
        List<String> serialTargetNames = new ArrayList<>();

        // get all targets names & serial set names
        allTargetNames = Target.getTargetNamesFrom(targets);
        serialTargetNames = Arrays.asList(curTargetsName.split(","));

        for(String curTName : serialTargetNames){

            if(allTargetNames.contains(curTName)) {

                Target curT = Target.getTargetByName(curTName, targets);

                res.add(curT);
            }
            else
                throw new ErrorUtils(ErrorUtils.invalidXMLFile("Target in serial set doesn't exist."));
        }
        return res;
    }

    public static Set<String> getTargetNamesFrom(List<Target> targets) {

        Set<String> res = new HashSet<>();

        for(Target curT : targets)
            res.add(curT.getName().toString());

        return res;
    }

    public static Target getTargetByName(String curTName, List<Target> targets) throws ErrorUtils {

        Map<String, Target> namesToTargets = Target.initNameToTargetFrom(targets);

        if(!namesToTargets.containsKey(curTName))
            throw new ErrorUtils(ErrorUtils.invalidTarget("Target doesn't exist in the given list."));
        else
            return namesToTargets.get(curTName);
    }

    public void countIncludedSerialSets(Map<String, Set<Target>> mSerialSets) {

        for(Set<Target> targets : mSerialSets.values()) {
            if(targets.contains(this))
                ++this.totalSerialSets;
        }

    }


    public CheckBox getRemark() {return remark;}

    public void setTotalRequiredFor(Integer totalRequiredFor) {
        this.totalRequiredFor = totalRequiredFor;
    }

    public void setTotalDependsOn(Integer totalDependsOn) {
        this.totalDependsOn = totalDependsOn;
    }


}