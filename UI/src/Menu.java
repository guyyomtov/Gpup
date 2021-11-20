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
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("3) Show data of a specific given target.");
        System.out.println("4) Find path between two given targets.");
        System.out.println("5) Start the build process on the graph. ");
        System.out.println("6) Exit the system. ");

    }

    // TO DO
    private void fileHandler(){     // Starts the engine of the system if all good.

       //System.out.println("Please write the path of the file: (example:src/resources/ex1-big.xml)");
        System.out.println("At any time you can press 'menu' to go back to the main menu.");
        System.out.println("Please write the name of the file: (example: ex1-big.xml)");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        if(fileName.toLowerCase().equals("menu"))
            return;
        try {
            if (this.dM.checkFile(fileName)) // only check the file syntax
                this.isThereGraph = true;

            System.out.println("Graph given was uploaded successfully :)");
            System.out.println("You may start asking for data from the graph.\r\n ");

        }catch (ErrorUtils e){
            System.out.println(e.getMessage());}

    }
    private void graphHandler(){

        if(this.isThereGraph) {

            List<Integer> gData = this.dM.getInfoFromGraph();          // get all data from data manger

            for (int i = 0; i < gData.size(); i++) {

                switch (i) {

                    case 0:
                        System.out.println("The amount of targets in the current graph are: " + String.valueOf(gData.get(i))); break;
                    case 1:
                        System.out.println("The amount of targets of type ");
                        System.out.println("'Leaf': " + String.valueOf(gData.get(i))); break;
                    case 2:
                        System.out.println("'Middle': " + String.valueOf(gData.get(i))); break;
                    case 3:
                        System.out.println("'Roots': " + String.valueOf(gData.get(i))); break;
                    case 4:
                        System.out.println("'Independents': " + String.valueOf(gData.get(i)) + "\r\n"); break;
                }
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void targetHandler(){
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String tName;

        if(this.isThereGraph){
            System.out.println("At any time you can press 'menu' to go back to the main menu.");
            System.out.println("Please enter a target name:");
            tName = sc.nextLine();          // ask user for the name of the target
            if(tName.toLowerCase().equals("menu"))
                return;
            try {
                //if tName not null
                List<String> tarInfo = this.dM.getInfoFromTarget(tName);

                for(int i = 0; i < tarInfo.size(); i++){

                    switch(i){
                        case 0:
                            System.out.println("Info of the wanted target: ");
                            System.out.println("Name: "+ tarInfo.get(i)); break;
                        case 1:
                            System.out.println("Role in the graph: " + tarInfo.get(i)); break;
                        case 2:
                            System.out.println("Names of targets which depends on him: "+ tarInfo.get(i)); break;
                        case 3:
                            System.out.println("Names of targets which he is required for : "+ tarInfo.get(i)); break;
                        case 4:
                            System.out.println("General information about him: "+ tarInfo.get(i)); break;
                    }
                }
            }
            catch (ErrorUtils e){
                System.out.println(e.getMessage());
                this.targetHandler();
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    // TO DO
    private void processHandler(){

        if(this.isThereGraph){
            Scanner sc= new Scanner(System.in);
            System.out.println("At any time you can press 'menu' to go back to the main menu.");
            System.out.println("Lets start the process!");
            System.out.println("If you want to chose the percent of success and the time for running om each task press '1', else press '2' and it will be done randomly.");
            String userChoice = sc.nextLine();
            if(userChoice.toLowerCase().equals("menu"))
                return;
            else {


                // check and get data from the user
                try{
                    this.startAndPrintProcess();
                }
                catch (ErrorUtils e){
                    System.out.println(e.getMessage());
                }catch(InterruptedException e){}
            }


        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void pathHandler(){

        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String[] words = {"","",""};
        String pathName = null, src = null, dest = null, connection = null;                     // start to null? empty?
        String tPath;

        if(this.isThereGraph){

            // need to get from the user src, dest & connection
            System.out.println("At any time you can press 'menu' to go back to the main menu.");
            System.out.println("Let's try to find the targets path you want!");
            System.out.println("PLease enter path in the following format: <target source>  <target destination>  <wanted relationship (depends On -> D /required For -> R)>");
            System.out.println("After that, press enter.");

            pathName = sc.nextLine();

            words = pathName.split(" ");
            if(words[0].toLowerCase().equals("menu"))
                return;
            // check the input is in the format way
            try {
                src = words[0];
                dest = words[1];
                connection = words[2];
            }catch (Exception e){
                System.out.println("Please enter three values.");
                this.pathHandler();
            }

            try {
                    tPath = this.dM.getPathFromTargets(src, dest, connection);
                    String[] allPath = tPath.split(",");
                    if(allPath[0].length() > 2)
                    {
                        System.out.println("All the paths from " + src + " to " + dest  + ":");
                        if(connection.equals("D"))
                            Arrays.stream(allPath).map(t -> t.replace(" ", "->")).forEach(s -> System.out.println(s.substring(0, s.length() - 2 )));
                        else
                        {
                            for(String s: allPath) {
                               StringBuilder output = new StringBuilder(s).reverse();
                               System.out.println(output.toString().replace(" ", "->").substring(2));
                            }
                        }
                    }
                    else
                        System.out.println("There is no path between " + src + " to " + dest + ".");
                   // System.out.println(tPath);
            }
            catch (ErrorUtils e) {
                System.out.println(e.getMessage());
                this.pathHandler();
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void exitProgram(){ System.exit(0);}

    private void startAndPrintProcess() throws ErrorUtils, InterruptedException {

        Map<String,List<String>> targetNameToHisProcessData =  this.dM.startProcess();
        List<String> curPData = new ArrayList<>();
        Integer tToSleep;
        String targetName;
        String status;

        // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released,

        // go over each target
        for(String tName : targetNameToHisProcessData.keySet()) {

            curPData = targetNameToHisProcessData.get(tName);
            tToSleep = Integer.valueOf(curPData.get(0));
            status = curPData.get(3);
            targetName = curPData.get(1);
            System.out.println("Target Name: " + targetName);
            if(status.equals("55"))
            System.out.println("Loading.." + tToSleep + "ms");
            while(tToSleep != 0)
            {
                System.out.println(tToSleep + "ms");
                try{
                    Thread.sleep(1000);
                }catch(Error InterruptedException){}
                tToSleep-=1000;
            }
            System.out.println("Process completed the status of this target is: " + status);

            //print his name
            // change from string to time --> sleep
            //wait the time
            // print the other data
        }
    }
}
