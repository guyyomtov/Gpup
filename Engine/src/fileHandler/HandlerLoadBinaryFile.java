package fileHandler;

import Graph.Graph;
import errors.ErrorUtils;

import javax.imageio.IIOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HandlerLoadBinaryFile {

    public HandlerLoadBinaryFile(Graph graph, String fullPath)
    {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fullPath));
            // we know that we read array list of Persons
            graph = (Graph) in.readObject();
            TaskFile.gpupPath = (String)in.readObject();
        }catch(Exception e){}
    }

}