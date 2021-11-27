package fileHandler;

import Graph.*;
import errors.ErrorUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class HandlerSaveFile {

    public HandlerSaveFile(/*List<Target> targets, Map<String,Target> mNameToTarget*/Graph graph, String fullPath) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(fullPath /*+ ".bin"*/))){
            out.writeObject(graph);
            out.writeObject(TaskFile.gpupPath); // the directory path
            out.flush();
        }catch(IOException e){};
        }

    }


