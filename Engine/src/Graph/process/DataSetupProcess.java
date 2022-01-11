package Graph.process;

import DataManager.BackDataManager;
import Flagger.Flagger;
import Graph.Target;
import errors.ErrorUtils;
import javafx.beans.property.BooleanProperty;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class DataSetupProcess {

    public Flagger flagger;
    public Integer timeToRun;
    public Integer amountOfThreads;
    public Consumer cUI;
    public Integer chancesToSucceed;
    public Integer chancesToBeAWarning;
    public List<Minion> minionsChoosenByUser;
    public List<Target> allGraphTargets;
    public Task oldTask;
    public String dTargetFolderPath;
    public String cpTargetFolderPath;
    public String pathToJavaFile;
    public Map<String, Set<Target>> serialSets;
    public BackDataManager bDM;
    public BooleanProperty pauseTask;
    public String lastProcessTextArea;
    public String fullPathSource = new String();
    public String fullPathDestination = new String();

    public DataSetupProcess builder(){ return this;}

    public DataSetupProcess allGraphTargets(List<Target> allGraphTargets){

        this.allGraphTargets = allGraphTargets;

        return this;
    }

    public DataSetupProcess demoFlagger(Flagger flagger) throws ErrorUtils {

        this.flagger = flagger;

        return this;
    }

    public DataSetupProcess flagger(Flagger flagger) throws ErrorUtils {

        this.flagger = flagger;

        if(flagger != null && flagger.chancesIsRandomInProcess)
            this.startChancesByRandom();

        return this;
    }

    public DataSetupProcess timeToRun(Integer timeToRun){

        this.timeToRun = timeToRun;

        return this;
    }

    public DataSetupProcess setLastProcessTextArea(String lastProcessTextArea) {

        this.lastProcessTextArea = lastProcessTextArea;

        return this;
    }

    public DataSetupProcess pauseTaskProperty(BooleanProperty pause){

        this.pauseTask = pause;

        return this;
    }

    public DataSetupProcess serialSets(Map<String, Set<Target>> serialSets){

        this.serialSets = serialSets;

        return this;
    }

    public DataSetupProcess amountOfThreads(Integer amountOfThreads) throws ErrorUtils {

        if(amountOfThreads <0)
            throw new ErrorUtils(ErrorUtils.INVALID_INPUT + "amount of threads");

        this.amountOfThreads = amountOfThreads;

        return this;
    }

    public DataSetupProcess chancesToSucceed(Integer chancesToSucceed){

        this.chancesToSucceed = chancesToSucceed;

        return this;
    }

    public DataSetupProcess oldTask(Task oldTask){

        this.oldTask = oldTask;

        return this;
    }

    public DataSetupProcess chancesToBeAWarning(Integer chancesToBeAWarning){

        this.chancesToBeAWarning = chancesToBeAWarning;

        return this;
    }

    public DataSetupProcess minionsChoosenByUser(List<Minion> minionsChoosenByUser){

        this.minionsChoosenByUser = minionsChoosenByUser;

        return this;
    }

    public DataSetupProcess startChancesByRandom() throws ErrorUtils {

        if(flagger == null ||
                flagger.chancesIsRandomInProcess == null ||
                !flagger.chancesIsRandomInProcess){
            throw new ErrorUtils(ErrorUtils.PROCESS_RANDOM);
        }

        Random rand = new Random();
        int upperBound = 101;

        this.chancesToSucceed =rand.nextInt(upperBound);
        this.chancesToBeAWarning = rand.nextInt(upperBound);

        return this;
    }

    public DataSetupProcess pathToJavaFile(String path) throws ErrorUtils {

        //check valid data
        if(this.flaggerHasNeededInformationForCompilation()) {

            path = path.trim();

            String checkValid = path.substring(path.length() - 5);

            if(!checkValid.equals(".java"))
                throw new ErrorUtils(ErrorUtils.INVALID_INPUT);

            //get path
            this.pathToJavaFile = path;
        }
        return this;
    }

    public DataSetupProcess dTargetFolderPath(String path) throws ErrorUtils {

        // Check valid data
        if(this.flaggerHasNeededInformationForCompilation()) {

            path = path.trim();

            if (!this.libaryExsistsInProject(path))
                throw new ErrorUtils(ErrorUtils.LIBRARY_DOSENT_EXSIST_IN_PROJECT);

            //get path
            this.dTargetFolderPath = path;
        }
        return this;
    }

    private Boolean flaggerHasNeededInformationForCompilation() throws ErrorUtils {

        if(flagger == null || flagger.processIsCompilation == false){
            throw new ErrorUtils(ErrorUtils.MISSING_COMPILATION_NEEDED_DATA);
        }
        return true;
    }

    public DataSetupProcess cpTargetFolderPath(String path) throws ErrorUtils {

        if(this.flaggerHasNeededInformationForCompilation()) {

            path = path.trim();

            this.cpTargetFolderPath = path;
        }
            return this;
    }


    private boolean libaryExsistsInProject(String path) {

        Boolean foundLibrary;

        File tempFile = new File(path);

        foundLibrary = tempFile.exists();

        return foundLibrary;
    }

    public boolean compilationProcessHasNeededData(){

        // one of the three needed inputs is missing
        // user gave thread count
        if(this.fullPathSource == null || this.fullPathDestination == null ||
                this.fullPathSource.isEmpty() || this.fullPathDestination.isEmpty()) {
            return false;
        }

        return true;
    }

    public DataSetupProcess setFullPathSource(String fullPathSource) {
        this.fullPathSource = fullPathSource;
        return this;
    }

    public DataSetupProcess setFullPathDestination(String fullPathDestination) {
        this.fullPathDestination = fullPathDestination;
        return this;
    }
}
