package Graph;

import java.util.Set;

public class Middle extends Target {
    private Set<Target> dependsOn;
    private Set<Target> requiredFor;
    public Middle(String name)
    {
        super(name);
    }

}
