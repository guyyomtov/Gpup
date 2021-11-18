package Graph;

import java.util.List;
import java.util.Set;

public class Root extends Target {

    private List<Target> dependsOn;

    public Root(String name)
    {
        super(name);
    }

    public void setDependsOn(List<Target> dependsOn)
    {
        this.dependsOn = dependsOn;
    }

    public List<Target> getDependsOn() {return dependsOn;}

    public List<Target> getRequireFor() {return null;}

}
