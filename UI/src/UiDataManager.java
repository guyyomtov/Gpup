import errors.ErrorUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import DataManager.*;
import fileHandler.GPUPDescriptor;

import javax.xml.bind.JAXBException;

public class UiDataManager implements DataManager {

    private BackDataManager bDM = new BackDataManager();

    public UiDataManager() {} // default ctor

    public Map<String,List<String>> startProcess() throws ErrorUtils {return this.bDM.startProcess();}

    public boolean checkFile(String fileName) throws ErrorUtils
    {
        boolean result = false;
        if(!fileName.contains(".xml"))
            throw new ErrorUtils( ErrorUtils.invalidFile("the file given doesn't end with a '.xml'."));
        else if(fileName.contains(" "))
            throw new ErrorUtils( ErrorUtils.invalidFile("the file given has a space in it's name."));
        try{
            result = bDM.checkFile(fileName);
        }catch(ErrorUtils e){throw e;}
        return result;
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
        try{
            return this.bDM.getInfoFromTarget(nameOfTarget);
        }catch (ErrorUtils e){throw e;}

    }

    //to do find target
    @Override
    public String getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {
        try{
            return this.bDM.getPathFromTargets(src, dest, connection);
        }catch(ErrorUtils e){throw e;}
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