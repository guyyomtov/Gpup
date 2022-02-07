package Graph;

import java.util.*;

/* This class is responsible to handle the graphs' data.
* Save all uploaded graphs in the system.
* Check that graphs aren't uploaded more than once.
* */
public class GraphManager {


    private final Map<String, Graph> nameToGraph;


    public GraphManager() {
        this.nameToGraph = new HashMap<String, Graph>();
    }

    public synchronized void addGraph(Graph graph) {

        this.nameToGraph.put(graph.getGraphName(), graph);
    }

    public synchronized void removeGraph(Graph graph) {

        this.nameToGraph.remove(graph.getGraphName());
    }

    public synchronized Map<String, Graph> getGraphs() {

        return this.nameToGraph;
    }

    public boolean graphExists(Graph graph) {

        return this.nameToGraph.containsKey(graph.getGraphName());
    }
}
