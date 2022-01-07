package Graph.process;

import Flagger.Flagger;
import Graph.Target;
import errors.ErrorUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class DataSetupProcess {

    public Flagger flagger;
    public Integer timeToRun;
    public Consumer cUI;
    public Integer chancesToSucceed;
    public Integer chancesToBeAWarning;
    public List<Minion> minionsChoosenByUser;
    public List<Target> allGraphTargets;
    public Task oldTask;

    public DataSetupProcess builder(){ return this;}

    public DataSetupProcess allGraphTargets(List<Target> allGraphTargets){

        this.allGraphTargets = allGraphTargets;

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
}
