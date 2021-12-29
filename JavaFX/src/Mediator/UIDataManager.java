package Mediator;


import errors.ErrorUtils;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

import DataManager.*;

public class UIDataManager implements DataManager {

    private BackDataManager bDM = new BackDataManager();

    public UIDataManager() {} // default ctor


    public boolean checkFile(String fileName) throws ErrorUtils {
        boolean result = false;
        if(!fileName.contains(".xml"))
            throw new ErrorUtils( ErrorUtils.invalidFile("the file given doesn't end with a '.xml'."));
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

    public String findCircle(String targetName) throws ErrorUtils {
        try{
            return this.bDM.findCircle(targetName);
        }catch (ErrorUtils e){throw e;}

    }

    void saveToFile(String fullPath) {

        this.bDM.saveToFile(fullPath);
    }

    public void startProcess(Consumer cUI, boolean isTimeRandom, boolean isIncremental, int timeToRun, int chancesToSucceed, int chancesToBeAWarning) throws ErrorUtils {

        this.bDM.startProcess(cUI, isTimeRandom, isIncremental, timeToRun, chancesToSucceed, chancesToBeAWarning);
    }

    public void loadFile(String fullPath) throws ErrorUtils {
        try {
            this.bDM.loadFile(fullPath);
        }catch (ErrorUtils e){throw e;}
    }

    public void whatIf(String a, String dependsOn) throws ErrorUtils {
        try{
            this.bDM.whatIf(a, dependsOn);
        } catch(ErrorUtils e){throw e;}
    }




}