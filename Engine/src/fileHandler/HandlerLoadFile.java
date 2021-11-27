package fileHandler;

import Graph.*;
import consumerData.ProcessInfo;
import errors.ErrorUtils;

import javax.imageio.IIOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerLoadFile {

    public static Graph loadFile(String fullPath) throws ErrorUtils {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(fullPath/* + ".bin"*/))) {
            Graph graph = (Graph) in.readObject();
            TaskFile.gpupPath = (String) in.readObject();
            ProcessInfo.setTargetNameToHisProcessData((Map<String, List<String>>) in.readObject());
            return graph;
        } catch (Exception e) {
            throw new ErrorUtils(ErrorUtils.invalidFile("The File doesn't exist."));
        }
    }

}