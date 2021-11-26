package fileHandler;

import Graph.Graph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HandlerSaveFile {

    public HandlerSaveFile(Graph graph, String fullPath)
    {
        try{
            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(fullPath + ".bin"));
            out.writeObject(this);
            out.writeObject(TaskFile.gpupPath); // the directory path
            out.flush();
        }catch(IOException e){}

    }

}
