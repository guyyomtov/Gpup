package Graph.process;


import errors.ErrorUtils;

import java.io.Serializable;

public class Compilation extends Task implements Serializable{

    private String fullPathSource;
    private String fullPathDestination;
    public Compilation(DataSetupProcess dSp) throws ErrorUtils {
        super(dSp); //
        this.fullPathSource = dSp.fullPathSource;
        this.fullPathDestination = dSp.fullPathDestination;
    }


    @Override
    public void setName() {

    }

    @Override
    public void accept(String s) {

    }
}
