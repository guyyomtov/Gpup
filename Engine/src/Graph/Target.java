package Graph;

public class Target {
    protected String name;
    protected Integer countOfDependency;
    public Target(String name)
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