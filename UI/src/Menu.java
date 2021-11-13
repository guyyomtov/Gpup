// This class is responsible to print & interact with the ui

import errors.ErrorUtils;
import fileHandler.GPUPDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private UiDataManager dM = new UiDataManager();
    private Boolean isThereGraph = false;

    public void start(){

        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + this.welcomeMessage() + "\n");
        this.printMenu();

        userInput = sc.next().charAt(0);

        while(userInput != '6'){      // In menu until presses 6 == exit

            switch(userInput) {

                case '1':
                    this.fileHandler();
                    break;
                case '2':
                    this.graphHandler();
                    break;
                case '3':
                    this.targetHandler();
                    break;
                case '4':
                    this.pathHandler();
                    break;
                case '5':
                    this.processHandler();
                    break;
                case '6':
                    this.exitProgram();
                    break;
                default: // Invalid input, not number\number isn't good
                    System.out.println("\r\n" + ErrorUtils.invalidInput("Please enter a number from 1-6.")+ "\r\n");
            }
            this.printMenu();
            userInput = sc.next().charAt(0);
        }
    }

    private String welcomeMessage(){ return "Hello there, welcome to Nadavs & Guys implementation of G.P.U.P, enjoy.";}

    private void printMenu(){

        System.out.println("Main Menu, PLease enter a number from the options bellow (1-6)");
        System.out.println("1) Upload to the system a new XML file with a graph. ");
        System.out.println("2) Show generic data of the graph.");
        System.out.println("3) Show data of a specific given task.");
        System.out.println("4) Find path between two given targets.");
        System.out.println("5) Start the build process on the graph. ");
        System.out.println("6) Exit the system. ");
    }

    // TO DO
    private void fileHandler(){     // Starts the engine of the system if all good.

       //System.out.println("Please write the path of the file: (example:src/resources/ex1-big.xml)");
        System.out.println("Please write the name of the file: (example: ex1-big.xml)");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        try {
            if (this.dM.checkFile(fileName)) // only check the file syntax
                this.isThereGraph = true;
        }catch (ErrorUtils e){
            System.out.println(e.getMessage());}

    }
    private void graphHandler(){

        if(this.isThereGraph) {

            List<Integer> gData = this.dM.getInfoFromGraph();          // get all data from data manger

            for (int i = 0; i < gData.size(); i++) {

                switch (i) {

                    case 0:
                        System.out.println("The amount of targets in the current graph are: " + String.valueOf(gData.get(i)));
                        break;
                    case 1:
                        System.out.println("The amount of targets of type ");
                        System.out.println("'Leaf': " + String.valueOf(gData.get(i)));
                        break;
                    case 2:
                        System.out.println("'Middle': " + String.valueOf(gData.get(i)));
                        break;
                    case 3:
                        System.out.println("'Roots': " + String.valueOf(gData.get(i)));
                        break;
                    case 4:
                        System.out.println("'Independents': " + String.valueOf(gData.get(i)));
                        break;
                }
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void targetHandler(){

        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String tName = "";

        if(this.isThereGraph){

            tName = sc.nextLine();          // ask user for the name of the target

            try {
                //if tName not null
                List<String> tarInfo = this.dM.getInfoFromTarget(tName);

                for(int i = 0; i < tarInfo.size(); i++){

                    switch(i){
                        case 1:
                            System.out.println("Info of the wanted target: ");
                            System.out.println("Name: "+ tarInfo.get(i)); break;
                        case 2:
                            System.out.println("Target Role in the graph: " + tarInfo.get(i)); break;
                        case 3:
                            System.out.println("Names of targets which depend on him: "+ tarInfo.get(i)); break;
                        case 4:
                            System.out.println("Names of targets which he depends on: "+ tarInfo.get(i)); break;
                        case 5:
                            System.out.println("General information about him: "+ tarInfo.get(i)); break;
                    }
                }
            }
            catch (ErrorUtils e){
                System.out.println(e.getMessage());
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    // TO DO
    private void processHandler(){

        if(this.isThereGraph){

        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void pathHandler(){

        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String[] words;
        String pathName = null, src = null, dest = null, connection = null;                     // start to null? empty?
        List<String> tPath = new ArrayList<>();

        if(this.isThereGraph){

            // need to get from the user src, dest & connection
            System.out.println("Let's try to find the targets path you want!");
            System.out.println("PLease enter path in the following format: <target source>  <target destination>  <wanted relationship>");
            System.out.println("After that, press enter.");

            pathName = sc.nextLine();

            words = pathName.split(" ");

            // check the input is in the format way


            if(pathName != null && src != null && dest != null && connection != null) {

                try {
                    tPath = this.dM.getPathFromTargets(src, dest, connection);
                }
                catch (ErrorUtils e) {
                    System.out.println(e.getMessage());
                }
            }
            else
                System.out.println(ErrorUtils.invalidInput("Incorrcet format"));
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void exitProgram(){ System.exit(0);}
}
