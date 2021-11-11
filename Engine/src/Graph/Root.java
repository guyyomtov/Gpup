package Graph;

import java.util.Set;

public class Root extends Targets{
    private Set<Targets> dependsOn;
    public Root(String name)
    {
        super(name);
    }

}
