package DataManager;

import errors.ErrorUtils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogFile {

    private FileHandler fh;
    private Logger logger = Logger.getLogger("MyLog");

    public void setFh(String targetName, String generalTargetInfo, String status, String getTimeToRunOnEachTm, String namesOfOpenedTargets ) throws IOException {
        try{
            fh = new FileHandler("Engine/src/fileHandler" + targetName +".log" /*"C:/Users/guyyo/IdeaProjects/Gpup/taskNAme"*/);
            logger.addHandler(fh);
            logger.info("Target name: " + targetName);
            logger.info("Target status: " + status);
            logger.info("Target name: " + targetName);
            logger.info("Target name: " + targetName);

        }catch (Exception e){}
    }

}
