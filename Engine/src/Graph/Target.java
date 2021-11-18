package Graph;

import fileHandler.GPUPTargetDependencies;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Target {
    private String name;

    private Integer countOfDependency;

    private String generalInfo = "";
    public Target(String name)
    {
        this.name = name;
        this.countOfDependency =0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGeneralInfo() {
        return generalInfo;
    }
    public Integer getCountOfDependency() {
        return countOfDependency;
    }

    public void setCountOfDependency(Integer countOfDependency) {
        this.countOfDependency = countOfDependency;
    }

    public abstract List<Target> getDependsOn();

    public abstract List<Target> getRequireFor();




}