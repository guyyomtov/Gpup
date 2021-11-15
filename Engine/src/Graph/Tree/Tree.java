package Graph.Tree;

import Graph.Leaf;
import Graph.Root;
import Graph.Target;
import errors.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tree {

    // No. of vertices in graph
    private int v;

    // adjacency list
    private ArrayList<Integer>[] adjList;

    private Map<String,Integer> letToNum = new HashMap<String,Integer>();
    private Map<Integer, String> numToLet = new HashMap<Integer, String>();
    private String resPaths = new String();


    // "Constructor"
    public void startMe(int vertices, List<Target> targets) throws ErrorUtils {

        // initialise vertex count
        this.v = vertices;

        // initialise adjacency list
        initAdjList();

        for(Target curT : targets) {

            if(curT.getClass().getSimpleName() == "Middle" || curT.getClass().getSimpleName() == "Root") {
                Root tmp = (Root)curT;

                for(Target curDep : tmp.getDependencies()) {
                    try {
                        this.addEdge(tmp.getName(), curDep.getName());
                    }
                    catch(ErrorUtils e){ throw  e; }
                }
            }
        }
    }

    private void startMaps() {

        int i = 0;

        for(char ch = 'a'; ch <= 'z'; ch++, i++) {

            this.letToNum.put(String.valueOf(ch), i);
            this.numToLet.put(i, String.valueOf(ch));
        }
    }

    @SuppressWarnings("unchecked")
    private void initAdjList() {
        adjList = new ArrayList[v];

        for (int i = 0; i < v; i++)
            adjList[i] = new ArrayList<>();
    }

    public void addEdge(String c1, String c2) throws ErrorUtils {

        int u, v;

        if(this.letToNum.containsKey(c1) && this.letToNum.containsKey(c2)) {
            u = this.letToNum.get(c1);
            v = this.letToNum.get(c2);
        }
        else
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target names don't match the 'abc' letters"));

        // Add v to u's list.
        adjList[u].add(v);
    }


    public String findAllPaths(String c1, String c2) throws ErrorUtils{

        int s, d;
        String resPaths =  new String();

        if(this.letToNum.containsKey(c1) && this.letToNum.containsKey(c2)) {
            s = this.letToNum.get(c1);
            d = this.letToNum.get(c2);
        }
        else
            throw new ErrorUtils(ErrorUtils.invalidTarget("The target names don't match the 'abc' letters"));

        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<>();

        // add source to path[]
        pathList.add(s);

        this.resPaths = "";
        // Call recursive utility
        getAllPathsUtil(s, d, isVisited, pathList, resPaths);

        return resPaths;
    }


    private void getAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList, String resPaths) {

        if (u.equals(d)) {
            this.resPaths = this.resPaths + this.curPath(localPathList) + ",";

            // if match found then no need to traverse more till depth
            return;
        }

        // Mark the current node
        isVisited[u] = true;

        // Recur for all the vertices
        // adjacent to current vertex
        for (Integer i : adjList[u]) {
            if (!isVisited[i]) {
                // store current node
                // in path[]
                localPathList.add(i);
                getAllPathsUtil(i, d, isVisited, localPathList, resPaths);

                // remove current node
                // in path[]
                localPathList.remove(i);
            }
        }
        // Mark the current node
        isVisited[u] = false;
    }

    private String curPath(List<Integer> localPathList){

        String res = new String();

        for(Integer curT : localPathList)
            res += this.numToLet.get(curT);

        return res;
    }
}

