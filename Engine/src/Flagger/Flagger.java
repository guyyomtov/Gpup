package Flagger;

import errors.ErrorUtils;

public class Flagger {


    public Boolean processFromScratch;
    public Boolean processIncremental;
    public Boolean processFromRandomTargets;
    public Boolean timeIsRandomInProcess;
    public Boolean chancesIsRandomInProcess;
    public Boolean processIsSimulation;
    public Boolean processIsCompilation;
    public boolean thereIsSerialSets = false;


    public Flagger builder(){return this;}

    public Flagger processFromScratch(Boolean b) throws ErrorUtils {

        this.processFromScratch = b;

        // can't be true together
        if(this.processIncremental != null && this.processFromRandomTargets !=null
                && (this.processIncremental == b || this.processFromRandomTargets == b))
            throw new ErrorUtils(ErrorUtils.invalidProcess());

        this.processIncremental = !b;
        this.processFromRandomTargets = !b;

        return this;
    }

    public Flagger processIncremental(Boolean b) throws ErrorUtils {

        this.processIncremental = b;

        this.processFromScratch = !b;
        this.processFromRandomTargets = !b;

        return this;
    }

    public Flagger thereIsSerialSets(Boolean thereIsSerialSets){

        this.thereIsSerialSets = thereIsSerialSets;

        return this;
    }

    public Flagger processFromRandomTargets(Boolean b) throws ErrorUtils {

        this.processFromRandomTargets = b;

        // can't be true together
        if(this.processFromScratch != null && this.processIncremental !=null
                && (this.processFromScratch == b || this.processIncremental == b))
            throw new ErrorUtils(ErrorUtils.invalidProcess());

        this.processFromScratch = !b;
        this.processIncremental = !b;

        return this;
    }

    public Flagger timeIsRandomInProcess(Boolean b){

        this.timeIsRandomInProcess = b;

        return this;
    }

    public Flagger processIsCompilation(Boolean processIsCompilation) throws ErrorUtils {

        if(this.processIsSimulation != null && this.processIsSimulation == processIsCompilation){
            throw new ErrorUtils(ErrorUtils.TYPE_OF_PROCESS);
        }

        this.processIsCompilation = processIsCompilation;
        this.processIsSimulation = !processIsCompilation;

        return this;
    }

    public Flagger processIsSimulation(Boolean processIsSimulation) throws ErrorUtils {

        if(this.processIsCompilation != null && this.processIsCompilation == processIsSimulation){
            throw new ErrorUtils(ErrorUtils.TYPE_OF_PROCESS);
        }

        this.processIsSimulation = processIsSimulation;
        this.processIsCompilation = !processIsSimulation;

        return this;
    }

    public Flagger chancesIsRandomInProcess(Boolean chancesIsRandomInProcess){

        this.chancesIsRandomInProcess = chancesIsRandomInProcess;

        return this;
    }
}
