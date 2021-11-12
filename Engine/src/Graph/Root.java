package Graph;

import java.util.Set;

public class Root extends Target {
    private Set<Target> dependsOn;
    public Root(String name)
    {
        super(name);
    }

}
