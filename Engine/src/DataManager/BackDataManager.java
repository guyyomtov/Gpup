package DataManager;
import errors.ErrorUtils;

import java.io.File;
import java.util.List;
import Graph.Graph;
public class BackDataManager implements DataManager {

    private Graph graph;

    public boolean checkFile(File f)
    {
        return false;
    }
    public void setUpGraph(File f)
    {
        // graph = new Graph(f);
        graph.buildMe(f);
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
