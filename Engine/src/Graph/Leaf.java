package Graph;

import java.util.Set;

public class Leaf extends Target {
    private Set<Target> requiredFor;
    public Leaf(String name)
    {
        super(name);
    }

}
