import errors.ErrorUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import DataManager.*;
import fileHandler.GPUPDescriptor;

import javax.xml.bind.JAXBException;

public class UiDataManager implements DataManager {

    private BackDataManager bDM = new BackDataManager();



    public UiDataManager() {} // default ctor

    public UiDataManager(File f) throws ErrorUtils {
//        if (this.bDM.checkFile(f)) {
//           // this.setUpEngine(f);
//        }
//    else{
//            throw new ErrorUtils(ErrorUtils.invalidFile());
//        }

    }
    public boolean checkFile()
    {
        boolean result = false;
        try{
            result = bDM.checkFile();
        }catch(ErrorUtils e){e.getMessage();}
        return result;
    }
    public void setUpGraph(File f) throws ErrorUtils
    {
        //     bDM.setUpGraph(f);
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
        return this.bDM.getNumOfIndependents();
    }

    @Override
    public int getNumOfRoots() {
        return this.bDM.getNumOfRoots();
    }

    @Override
    public int getNumOfMiddle() {
        return this.bDM.getNumOfMiddle();
    }

    @Override
    public int getNumOfLeafs() {
        return this.bDM.getNumOfLeafs();
    }

    @Override
    public int getNumOfTargets() {
        return this.bDM.getNumOfTargets();
    }

    @Override
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils {
//        if (this.bDM.findTarget(nameOfTarget)) // to do findTarget
//            return this.bDM.getInfoFromTarget(nameOfTarget);
//        else {
//            throw new ErrorUtils(ErrorUtils.invalidFile());// the target doesn't exist.
//        }
        List<String> res = Arrays.asList("Nadav", "Guy");
        return res;
    }

    //to do find target
    @Override
    public List<String> getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {
//
//        if (this.bDM.findTarget(src) && this.bDM.findTarget(dest)) {
//            return this.bDM.getPathFromTargets(src, dest, connection);
//        }
//        else if (this.bDM.findTarget(src)) {
//            throw new ErrorUtils(ErrorUtils.invalidTarget() + "/n The Target " + dest + " doesn't exist" );
//        }
//         else if (this.bDM.findTarget(dest)) {
//            throw new ErrorUtils(ErrorUtils.invalidTarget() + "The Target " + src + " doesn't exist");
//        }
//         else {
//            throw new ErrorUtils(ErrorUtils.invalidTarget() +"The Targets " + src + dest + " doesn't exist");
//        }
        List<String> res = Arrays.asList("A", "V");
        return res;
    }

    public void swapFiles(File newFile) throws ErrorUtils {
//
//        if (this.checkFile(newFile))
//            this.bDM = new BackDataManager(newFile);
//        else {
//            throw new ErrorUtils(ErrorUtils.invalidFile());
//        }
    }

    //to check with aviad.
    public void setUpTask(String taskName) throws ErrorUtils {
//        if (checkValidTask(taskName))
//            this.startProcess(taskName);
//        else {
//            throw new ErrorUtils(ErrorUtils.invalidTask());
//        }

    }

    public void startProcess(String taskName) {
        //engine.startTask();// or whatever it will be
    }

    public boolean checkValidTask(String taskName) {
        return false;
    }
}