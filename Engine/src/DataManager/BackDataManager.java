package DataManager;
import errors.ErrorUtils;
import fileHandler.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;


import Graph.Graph;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class BackDataManager implements DataManager {

    private Graph graph;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "resources";

    public boolean checkFile() throws ErrorUtils {

        System.out.println("Please write the path of the file: (example:src/resources/ex1-big.xml)");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.next();
        InputStream inputStream = null;
            if(fileName.contains(".xml")) // to check more things.
            {
                try {
                    inputStream = new FileInputStream(new File("src/resources/" + fileName));
                    //to check if the ended file is with xml
                    GPUPDescriptor information = deserializeFrom(inputStream);
                    if(setUpGraph(information))
                        return true;
                    else
                        throw new ErrorUtils(ErrorUtils.invalidFile());

                } catch (JAXBException | FileNotFoundException e) {
                    e.printStackTrace(); // maybe to throw?
                }
            }
            else{
                throw new ErrorUtils(ErrorUtils.invalidFile());
            }

        return false;
    }
    private static GPUPDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GPUPDescriptor) u.unmarshal(in);
    }
    public boolean setUpGraph(GPUPDescriptor information ) throws ErrorUtils {
        // graph = new Graph(f);
    if(this.graph.buildMe(information))
        return true;
    else {
        throw new ErrorUtils(ErrorUtils.invalidFile());
     }

    }

    @Override
    public int getNumOfIndependents() {
        return 0;
    }

    @Override
    public int getNumOfRoots() {
        return 0;
    }

    @Override
    public int getNumOfMiddle() {
        return 0;
    }

    @Override
    public int getNumOfLeafs() {
        return 0;
    }

    @Override
    public int getNumOfTargets() {
        return 0;
    }

    @Override
    public List<String> getInfoFromTarget(String nameOfTarget) throws ErrorUtils {
        return null;
    }

    @Override
    public List<String> getPathFromTargets(String src, String dest, String connection) throws ErrorUtils {
        return null;
    }
}
