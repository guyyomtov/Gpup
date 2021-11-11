package Graph;

import java.util.Set;

public class Middle extends Targets {
    private Set<Targets> dependsOn;
    private Set<Targets> requiredFor;
    public Middle(String name)
    {
        super(name);
    }

}
