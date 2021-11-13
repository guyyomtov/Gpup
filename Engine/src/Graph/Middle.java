package Graph;

import java.util.List;
import java.util.Set;

public class Middle extends Target {

    private List<Target> dependsOn;
    private List<Target> requiredFor;
    public Middle(String name)
    {
        super(name);
    }

    public void setDependsOn(List<Target> dependsOn)
    {
        this.dependsOn = dependsOn;
    }

    public void setRequiredFor(List<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public List<Target> getDependencies() {
        return dependsOn;
    }

    public List<Target> getRequired() {
        return requiredFor;
    }
}
