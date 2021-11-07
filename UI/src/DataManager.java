import java.util.List;

public interface DataManager {

    public int getNumOfIndependents();

    public int getNumOfRoots() ;

    public int getNumOfMiddle();

    public int getNumOfLeafs();

    public int getNumOfTargets();
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils;
    /*
    [0] = target name;
    [1] = location of the target.
    [2] = list of dependants targets
    [3] = list of requires for targets.
    [4] = general information.
    */
    public List<String> getPathFromTargets(String src, String dest, String connection); // connection meaning depends on required for

}
