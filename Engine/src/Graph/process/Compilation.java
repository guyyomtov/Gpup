package Graph.process;


import errors.ErrorUtils;
import fileHandler.TaskFile;

import java.io.Serializable;

public class Compilation extends Task implements Serializable{

    private String fullPathSource;
    private String fullPathDestination;
    public Compilation(DataSetupProcess dSp) throws ErrorUtils {

        super(dSp); //
        this.fullPathSource = dSp.fullPathSource;

        this.fullPathDestination = dSp.fullPathDestination;
        //todo should be in the client
//        TaskFile taskFile = new TaskFile();
//        taskFile.makeTaskDir("Compilation");
    }


    @Override
    public void setName() {

    }

}
