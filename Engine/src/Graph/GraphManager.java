package Graph;

import DataManager.BackDataManager;
import errors.ErrorUtils;
import transferGraphData.GraphInfo;

import java.util.*;

/* This class is responsible to handle the graphs' data.
* Save all uploaded graphs in the system.
* Check that graphs aren't uploaded more than once.
* */
public class GraphManager {


    private final Map<String, BackDataManager> nameToBackDataManager;


    public GraphManager() {
        this.nameToBackDataManager = new HashMap<String, BackDataManager>();
    }

    public synchronized void addGraph(BackDataManager bdm) {

        this.nameToBackDataManager.put(bdm.getGraph().getGraphName(), bdm);
    }

    public synchronized void removeGraph(Graph graph) {

        this.nameToBackDataManager.remove(graph.getGraphName());
    }

    public synchronized Map<String, BackDataManager> getGraphs() {

        return this.nameToBackDataManager;
    }

    public boolean graphExists(Graph graph) {

        return this.nameToBackDataManager.containsKey(graph.getGraphName());
    }

    public boolean graphExists(String graphName) {

        return this.nameToBackDataManager.containsKey(graphName);
    }

    public synchronized List<GraphInfo> makeListOfGraphInfo() {
        List<GraphInfo> graphInfoList = new ArrayList<>();
        for(BackDataManager bdm : nameToBackDataManager.values()){
            try {
                GraphInfo graphInfo = this.createGraphInfo(bdm);

                graphInfoList.add(graphInfo);
            }catch(Exception e){
                System.out.println("something went wrong");
            }
        }
        return graphInfoList;
    }

    public GraphInfo createGraphInfo(BackDataManager bdm){
        GraphInfo graphInfo = new GraphInfo();
        graphInfo.setName(bdm.getGraph().getGraphName());
        graphInfo.setByWhoUpload(bdm.getUserNameThatUploadTheCurGraph());
        graphInfo.setTotalTargets(bdm.getNumOfTargets());
        graphInfo.setTotalIndependents(bdm.getNumOfIndependents());
        graphInfo.setTotalLeaf(bdm.getNumOfLeafs());
        graphInfo.setTotalMiddles(bdm.getNumOfMiddle());
        graphInfo.setTotalRoots(bdm.getNumOfRoots());
        graphInfo.setTaskInfo(bdm.getTasksAndPricingAndConvertToString());
        return graphInfo;
    }

    public BackDataManager getBDM(String graphNameFromParameter) throws ErrorUtils {

        if(graphExists(graphNameFromParameter))
            return nameToBackDataManager.get(graphNameFromParameter);
        else
            throw new ErrorUtils("there is not graph " + graphNameFromParameter);
    }

}
