package fileHandler;

import Graph.*;
import DataManager.consumerData.ProcessInfo;

import java.io.*;

public class HandlerSaveFile {

    public HandlerSaveFile(Graph graph, String fullPath) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(fullPath))){
            out.writeObject(graph);
            out.writeObject(TaskFile.gpupPath); // the directory path
            out.writeObject(ProcessInfo.getOldTask());
            out.flush();
        }catch(IOException e){};
    }

    }


