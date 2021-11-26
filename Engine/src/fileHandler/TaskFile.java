package fileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskFile {

    public static String gpupPath;

    private static String currPath;

    private boolean openTaskDir = false;

    public TaskFile(){

        File theDir = new File(gpupPath);
        if(!theDir.exists())
        {
            try{
                theDir.mkdir();
            }catch(Exception e){}
        }

    }

    public void makeTaskDir(String taskName)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy HH.mm.ss");
        Date date = new Date();
        String currDate = simpleDateFormat.format(date);
        File taskDirectory = new File(gpupPath + "\\" + taskName + "-" + currDate);
        if (!taskDirectory.exists())
            taskDirectory.mkdir();

        currPath = gpupPath + "\\" + taskName + "-" + currDate;
    }
    public static void openAndWriteToFile(String taskFormat, String targetName) throws IOException
    {
        try {
            FileWriter writer = new FileWriter(currPath + "\\" + targetName + ".log");
            BufferedWriter currBuffer = new BufferedWriter(writer);
            currBuffer.write(taskFormat);
            currBuffer.close();
        }catch (IOException e){}

    }

    public boolean isOpenTaskDir() {
        return openTaskDir;
    }

    public void setOpenTaskDir(boolean openTaskDir) {
        this.openTaskDir = openTaskDir;
    }

}
