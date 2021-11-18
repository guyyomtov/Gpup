package Graph;

import java.util.List;
import java.util.Set;

public class Leaf extends Target {

    private List<Target> requiredFor;
    public Leaf(String name,String generalInfo)
    {
        super(name,generalInfo);
    }
    public void setRequiredFor(List<Target> requiredFor) {
        this.requiredFor = requiredFor;
    }
    public List<Target> getRequiredFor() {
        return requiredFor;
    }
    public List<Target> getDependsOn() {return null;}
}
