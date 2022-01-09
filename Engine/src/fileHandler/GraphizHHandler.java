package fileHandler;

import Graph.Graph;
import Graph.Target;

import java.io.*;
import java.util.List;
import java.util.Set;

public class GraphizHHandler {


    static public void makeGraphizPNGFrom(String savePhotoPath, Graph curGraph) throws IOException {

        List<Target> targets = curGraph.getAllTargets();

        makeGraphizPNGFrom(savePhotoPath, targets);
    }

    static public void makeGraphizPNGFrom(String savePhotoPath, List<Target> targets) throws IOException {

        String res  = new String();

        res = "digraph G {\n\n";

        // get list of roots
        List<Target> roots = Target.getTargetByType(targets, Target.Type.ROOT);

        /// for each call rec function
        for(Target curRoot : roots)
            res = makeDotFormatString(curRoot, res);

        List<Target> independents = Target.getTargetByType(targets, Target.Type.INDEPENDENT);

        //get independents & add them to text
        for(Target curIndepend : independents)
            res += "\n"+ curIndepend.getName() + ";";

        res += "\n}";

        makeDotAndPNGFile(savePhotoPath, res);
    }

    private static void makeDotAndPNGFile(String wantedUserPath, String res) throws IOException {

        // make text file
        //String userHomeFolder = System.getProperty("user.home"); --> to save
        File textFile = new File(wantedUserPath, "curGraph.viz");
        BufferedWriter out = new BufferedWriter(new FileWriter(textFile));
        out.write(res);
        out.close();
        textFile.mkdir();

        // make image from text file
        FileOutputStream file = new FileOutputStream(textFile);
        PrintStream Output = new PrintStream(file);
        Output.print(res);
        Output.close();
        File f = new File(textFile.getAbsolutePath());
        String arg1 = f.getAbsolutePath();
        String arg2 = arg1 + ".png";
        String[] c = {"dot", "-Tpng", arg1, "-o", arg2};
        Process p = Runtime.getRuntime().exec(c);
    }

    static private String makeDotFormatString(Target curT, String res){

        String curTName = curT.getName();

        // if i have no kids
        if(curT.getDependsOn().isEmpty())
            return res;

        //get & add kids to string
        Set<String> kidNames = Target.getTargetNamesFrom(curT.getDependsOn());
        for(String curKidName : kidNames){

            if(res.contains(curTName+" -> "+curKidName+";\n"))
                continue;

            res += curTName+" -> "+curKidName+";\n";
        }

        // do the same to each kid
        for(Target curKid : curT.getDependsOn())
            res = makeDotFormatString(curKid, res);

        return res;
    }
}
