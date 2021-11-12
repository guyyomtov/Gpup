package DataManager;
import errors.ErrorUtils;

import java.io.File;
import java.util.*;

import Graph.Graph;
import Graph.Targets;

public class BackDataManager implements DataManager {

    private Graph graph;
    // private Set<Targets> targets = new HashSet<>();
    Map<String, Set<Target> mTypeToTargets = new HashMap<>();

    public boolean checkFile(File f)
    {
        return false;
    }

    public void setUpGraph(File f) throws ErrorUtils {
        // graph = new Graph(f);
        try {
            this.graph.buildMe(f);
        }
        catch(ErrorUtils e){ throw e; }

        this.mTypeToTargets = this.makeMap(this.graph.getAllTargets());
    }

    private Map<String, Set<Targets>> makeMap(Set<Target> targets){

        Set<Target> leaf, independent, middle, root = new HashSet<Target>();
        Map<String, Set<Target>> tmp = new HashMap<>();

        tmp.put("Independent", new HashSet<Target>() );
        tmp.put("Leaf", new HashSet<Target>());
        tmp.put("Middle", new HashSet<Target>());
        tmp.put("Root", new HashSet<Target>());


        return tmp;
    }

    @Override
    public int getNumOfIndependents() {


        return 0;
    }

    @Override
    public int getNumOfRoots() {
        return 0;
    }

    @Override
    public int getNumOfMiddle() {
        return 0;
    }

    @Override
    public int getNumOfLeafs() {
        return 0;
    }

    @Override
    public int getNumOfTargets() {
        return 0;
    }

    @Override
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils {
        return null;
    }

    @Override
    public List<String> getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {
        return null;
    }
}
