package Graph.process;


import errors.ErrorUtils;

import java.io.Serializable;

public class Compilation extends Task implements Serializable, Runnable{

    public Compilation(DataSetupProcess dSp) throws ErrorUtils {
        super(dSp);
    }

    @Override
    public void run() {

    }

    @Override
    public void setName() {

    }
}
