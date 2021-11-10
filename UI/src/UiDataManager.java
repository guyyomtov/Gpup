import E.Engine;
import Graph.Graph;
import Graph.Targets;

import java.beans.Expression;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UiDataManager implements DataManager {

    private Engine engine;



    public UiDataManager() {
    } // default ctor

    public UiDataManager(File f) throws ErrorUtils {
        if (this.checkFile(f)) {
            this.setUpEngine(f);
        }
    else{
            throw new ErrorUtils(ErrorUtils.invalidFile());
        }

    }

    public boolean checkFile(File f) {

        return false;
    }

    public void setUpEngine(File f) {

        this.engine = new Engine(f); // ctor of Engine with file
    }

    public List<Integer> getInfoFromGraph() {
        List<Integer> infoList = new ArrayList<>();
        infoList.add(this.getNumOfTargets());
        infoList.add(this.getNumOfLeafs());
        infoList.add(this.getNumOfMiddle());
        infoList.add(this.getNumOfRoots());
        infoList.add(this.getNumOfIndependents());
        return infoList;
    }

    @Override
    public int getNumOfIndependents() {
        // return this.engine.getNumOfIndependents();
        return 0;
    }

    @Override
    public int getNumOfRoots() {
        //return this.engine.getNumOfRoots();
        return 0;
    }

    @Override
    public int getNumOfMiddle() {
        //return this.engine.getNumOfMiddle();
        return 0;
    }

    @Override
    public int getNumOfLeafs() {
        //return this.engine.getNumOfLeafs();
        return 0;
    }

    @Override
    public int getNumOfTargets() {
        //return this.engine.getNumOfTargets();
        return 0;
    }

    @Override
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils {
        if (this.engine.findTarget(nameOfTarget)) // to do findTarget
            return this.engine.getInfoFromTarget(nameOfTargets);
        else {
            throw new ErrorUtils(ErrorUtils.invalidFile());// the target doesn't exist.
        }
    }

    //to do find target
    @Override
    public List<String> getPathFromTargets(String src, String dest, String connection) {

        if (this.engine.findTarget(src) && this.engine.findTarget(dest)) // only if find both of the Targets
        {
            return this.engine.getPathFromTargets(src, dest, connection);
        } else if (this.engine.findTarget(src)) {
            throw (new ErrorUtils())
            {
                e.invalidTarget("The Target " + dest + " doesn't exist");
            }

        } else if (this.engine.findTarget(dest)) {
            throw (ErrorUtils e)
            {
                e.invalidTarget("The Target " + src + " doesn't exist");
            }
        } else {
            throw (ErrorUtils e)
            {
                e.invalidTarget("The Targets " + src + dest + " doesn't exist");

            }
        }
    }

    public void swapFiles(File newFile) {

        if (this.checkFile(newFile))
            this.engine = new Engine(newFile);
        else {
            throw new ErrorUtils()  /*(ErrorUtils e){
                e.invalidFile();
            }*/
        }
    }
//to check with aviad.
    public void setUpTask(String taskName) {
        if (checkValidTask(taskName))
            this.startProcess(taskName);
        else {
            throw (ErrorUtils e){
                e.invalidTask();
            }

        }

    }

    public void startProcess(String taskName) {
        //engine.startTask();// or whatever it will be
    }

    public boolean checkValidTask(String taskName) {
        return false;
    }
}
