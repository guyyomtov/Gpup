package errors;

public class ErrorUtils extends Exception {
    private String ms;
    public ErrorUtils(String s)
    {
        ms = s;
       // super(s);
    }

    public static String noGraph(){ return "No graph doesn't exist yet in the system, please upload one.";}

    public static String invalidInput(){ return "The input given is invalid."; }

    public static String invalidInput(String err){ return "The input given is invalid (" + err + ")"; }

    public static String invalidFile(){ return "The file given is invalid, please try to upload a new one.";}

    public static String invalidTarget(){ return "No such target exists in the graph."; }

    public static String noPathFound(){ return "No path wanted was found between the two given targets.";}

    public static String noPathWithLogic(){ return "A path was found between the two wanted targets, but with a different logic connection.";}

    public static String invalidTask(){ return "The task given was invalid, please try again."; }

    @Override
    public String getMessage() { return this.ms; }
}
