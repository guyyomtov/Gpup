package Graph;

import java.util.List;

public class Independent extends Target {

    public Independent(String name, String generalInfo)
    {
        super(name, generalInfo);
    }

    public List<Target> getDependsOn() {return null;}

    public List<Target> getRequiredFor() {return null;}
}
