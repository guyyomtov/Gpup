package Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    private Set<Targets> targets;
    private boolean matrixOfDependency[][];

    public void buildMe(File f)
    {

    }

    public Set<Targets> getAllTargets(){ return this.targets; }
 /*   public Graph(Collection<Targets> info)
    {
        this.targets = new HashSet<Targets>(20);

    }*/

}
