package Graph.process;


import errors.ErrorUtils;

import java.io.Serializable;

public class Compilation extends Task implements Serializable{

    public Compilation(DataSetupProcess dSp) throws ErrorUtils {
        super(dSp);
    }

    @Override
    public void run() {

    }

    @Override
    public void setName() {

    }


    @Override
    protected Object call() throws Exception {
        return null;
    }

    @Override
    public void accept(String s) {

    }
}
