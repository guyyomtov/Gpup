package DataManager;

import errors.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogFile {

    private FileHandler fh;
    private Logger logger = Logger.getLogger("MyLog");
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


    public void setFh(String targetName, String generalTargetInfo, String status, String getTimeToRunOnEachTm, String namesOfOpenedTargets ) throws IOException {
        try{
            fh = new FileHandler("test/" + targetName +".log");
            logger.addHandler(fh);
            logger.info("Target name: " + targetName);
            logger.info("Target status: " + status);
            logger.info("Time to run on task: " + getTimeToRunOnEachTm);
            logger.info("Names of opened targets " + namesOfOpenedTargets);

        }catch (Exception e){}
    }


}
