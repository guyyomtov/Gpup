package DataManager;

import errors.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogFile {

    public static Logger logger = Logger.getLogger("MyLog");

    public static String gpupPath;

    public static void makeDirectory(String taskName)
    {
        File theDir = new File(gpupPath);
        if(!theDir.exists())
        {
            try{
                theDir.mkdir();
            }catch(Exception e){}
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy HH.MM.SS");
        Date date = new Date();
        String currDate = simpleDateFormat.format(date);
        File taskDirectory = new File(gpupPath + taskName +"-" + currDate);
        if(!taskDirectory.exists()) try{taskDirectory.mkdir();} catch(Exception e){}
    }
    // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,

    public static void setNameInFile(String targetName) throws IOException {

        FileHandler fh = new FileHandler("test/" + targetName +".log");
        logger.addHandler(fh);
        logger.info("Target name: " + targetName);
    }

    public static void setGeneralInfo(String generalInfo)  {logger.info("General Information: " + generalInfo );}

    public static void setStatus(String status)  {logger.info("Target status: " + status );}

    public static void setTargetsDependsAndGotReleased(String targetsName)  {logger.info("Targets that depends and got released: " + targetsName);}

}
