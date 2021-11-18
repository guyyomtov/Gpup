package Graph;

import java.util.List;
import java.util.Set;

public class Middle extends Target {

    public Middle(String name,String generalInfo)
    {
        super(name,generalInfo);
    }
    private List<Target> dependsOn;
    private List<Target> requiredFor;

    public void setDependsOn(List<Target> dependsOn)
    {
        this.dependsOn = dependsOn;
    }

    public void setRequiredFor(List<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }

    public List<Target> getDependsOn() {return this.dependsOn;}

    public List<Target> getRequiredFor() {return this.requiredFor;}

}
