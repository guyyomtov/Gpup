public class Flagger {


    public Boolean processFromScratch;
    public Boolean processIncremental;
    public Boolean processFromRandomTargets;
    public Boolean timeIsRandomInProcess;


    public Flagger builder(){return this;}

    public Flagger processFromScratch(Boolean b){
        this.processFromScratch = b;
        return this;
    }

    public Flagger processIncremental(Boolean b){

        this.processIncremental = b;
        return this;
    }


    public Flagger processFromRandomTargets(Boolean b){

        this.processFromRandomTargets = b;
        return this;
    }

    public Flagger timeIsRandomInProcess(Boolean b){

        this.timeIsRandomInProcess = b;
        return this;
    }
}
