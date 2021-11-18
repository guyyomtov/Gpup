package Graph;

import java.util.List;

public class Independent extends Target {

    public Independent(String name)
    {
        super(name);
    }

    public List<Target> getDependsOn() {return null;}

    public List<Target> getRequireFor() {return null;}
}
