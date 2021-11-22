package DataManager;

import errors.ErrorUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogFile {

    public static String gpupPath;

    public static String currPath;

    public static FileWriter writer;

    public static BufferedWriter currBuffer;

    public static void makeDirectory(String taskName)
    {
        File theDir = new File(gpupPath);
        if(!theDir.exists())
        {
            try{
                theDir.mkdir();
            }catch(Exception e){}
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.mm.yyyy");
        Date date = new Date();
        String currDate = simpleDateFormat.format(date);
        File taskDirectory = new File(gpupPath + "\\" + taskName + "-" + currDate);
        if(!taskDirectory.exists()) try{taskDirectory.mkdir();} catch(Exception e){}
        currPath = gpupPath + "\\" + taskName + "-" + currDate;
    }
    // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,

    public static void setNameInFile(String targetName) throws IOException {

        writer = new FileWriter(currPath + "\\" + targetName +".log");
        currBuffer = new BufferedWriter(writer);
        currBuffer.write("Target name: " + targetName + "\n");
        currBuffer.close();
    }

    public static void setGeneralInfo(String generalInfo)  {
        try {
            currBuffer = new BufferedWriter(writer);
            currBuffer.write("General Information: " + generalInfo);

            currBuffer.close();
        }catch (IOException e){}
    }

    public static void setStatus(String status) {
        try {
            currBuffer = new BufferedWriter(writer);
            currBuffer.write("Target status: " + status);
            currBuffer.close();
        } catch (IOException e) {
        }
    }

    public static void setTargetsDependsAndGotReleased(String targetsName) {
        try {
            currBuffer = new BufferedWriter(writer);
            currBuffer.write("Targets that depends and got released: " + targetsName);
            currBuffer.close();
        } catch (IOException e) {
        }
    }
}
