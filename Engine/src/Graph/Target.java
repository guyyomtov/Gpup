package Graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Target implements Serializable {
    public enum Type implements Serializable {
        INDEPENDENT{public String toString(){return "Independent";}}

        , ROOT{public String toString(){return "Root";}},

        MIDDLE{public String toString(){return "Middle";}}

        , LEAF{public String toString(){return "Leaf";}};

    }

    private String name;

    private Integer countOfDependency;

    private String generalInfo;

    private Type targetType;

    private List<Target> dependsOn = new ArrayList<Target>();

    private List<Target> requiredFor = new ArrayList<Target>();;

    public void setDependsOn(List<Target> dependsOn)
    {
        this.dependsOn = dependsOn;
    }

    public void setRequiredFor(List<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public List<Target> getDependsOn() {return this.dependsOn;}

    public List<Target> getRequiredFor() {return this.requiredFor;}

    public void addTargetToDependsOnList(Target toAdd) {
        if(!this.dependsOn.contains(toAdd))
            dependsOn.add(toAdd);
    }

    public void addTargetToRequiredForList(Target toAdd) {
        if(!this.requiredFor.contains(toAdd))
            this.requiredFor.add(toAdd);
    }

    public Target(String name, String generalInfo)
    {
        this.name = name;
        this.countOfDependency =0;
        this.generalInfo = generalInfo;
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

//    public void saveToFile(DataOutputStream dataOut)
//    {
//
//
//    }
//
//    public void saveToFileDependencies(DataOutputStream dataOut)
//    {
//        try {
//            dataOut.writeInt(this.dependsOn.size());
//            for(Target t: this.dependsOn)
//                dataOut.writeUTF(t.getName());
//            dataOut.writeInt(this.requiredFor.size());
//            for(Target t : this.requiredFor)
//                dataOut.writeUTF(t.getName());
//        }catch (Exception e){System.out.println("somthing went wrong with saving file --> its here only to check");}
//    }


}