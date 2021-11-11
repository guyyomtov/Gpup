package Graph;

import java.util.Set;

public class Leaf extends Targets{
    private Set<Targets> requiredFor;
    public Leaf(String name)
    {
        super(name);
    }

}
