package fileHandler;

import Graph.process.Simulation;
import Graph.*;
import DataManager.consumerData.ProcessInfo;
import errors.ErrorUtils;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class HandlerLoadFile {

    public static Graph loadFile(String fullPath) throws ErrorUtils {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(fullPath/* + ".bin"*/))) {
            Graph graph = (Graph) in.readObject();
            TaskFile.gpupPath = (String) in.readObject();
            Simulation sim = (Simulation) in.readObject();
            ProcessInfo.setOldTask(sim);

            return graph;

        } catch (Exception e) {
            throw new ErrorUtils(ErrorUtils.invalidFile("The File doesn't exist."));
        }
    }

}