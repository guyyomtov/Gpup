package Graph;

public class Targets {
    protected String name;
    protected Integer countOfDependency;
    public Targets(String name)
    {
        this.name = name;
        this.countOfDependency =0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}