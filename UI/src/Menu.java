// This class is responsible to print & interact with the ui

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Menu {

    // private File f =  new File(); // needes to be a file?
    private UiDataManager dM = new UiDataManager();
    private Boolean isThereGraph = false;

    // TO CHECK
    public void start(){

        Integer userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + this.welcomeMessage() + "\n");
        this.printMenu();

        userInput = sc.nextInt();

        while(userInput != 6){      // In menu until presses 6 == exit

            switch(userInput) {

                case 1:
                    this.fileHandler();
                    break;
                case 2:
                    this.graphHandler();
                    break;
                case 3:
                    this.targetHandler();
                    break;
                case 4:
                    this.pathHandler();
                    break;
                case 5:
                    this.processHandler();
                    break;
                case 6:
                    this.exitProgram();
                    break;
                default: // Invalid inpt, not number\number isn't good
                    ErrorUtils.invalidInput("Please enter a number from 1-6.");
            }
            this.printMenu();
            userInput = sc.nextInt();
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

        File f = new File();

        // ask for file

        // get file from user

        try {
            this.dM = new UiDataManager(f);

            this.isThereGraph = true;
        }
        catch(ErrorUtils e){            // invalid file
            System.out.println(e);
        }
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
            System.out.println(ErrorUtils.noGraph());
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
                            System.out.println("Name: "+ tarInfo.get(i));
                            break;
                        case 2:
                            System.out.println("Target Role in the graph: " + tarInfo.get(i));
                            break;
                        case 3:
                            System.out.println("Names of targets which depend on him: "+ tarInfo.get(i));
                            break;
                        case 4:
                            System.out.println("Names of targets which he depends on: "+ tarInfo.get(i));
                            break;
                        case 5:
                            System.out.println("General information about him: "+ tarInfo.get(i));
                            break;
                    }
                }
            }
            catch (ErrorUtils e){
                System.out.println(e);
            }
        }
        else
            System.out.println(ErrorUtils.noGraph());
    }

    // TO DO
    private void processHandler(){

        if(this.isThereGraph){

        }
        else
            System.out.println(ErrorUtils.noGraph());
    }

    private void pathHandler(){

        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String[] words;
        String pathName = null, src = null, dest = null, connection = null;                     // start to null? empty?

        if(this.isThereGraph){

            // need to get from the user src, dest & connection
            System.out.println("Let's try to find the targets path you want!");
            System.out.println("PLease enter path in the following format: <target source>  <target destination>  <wanted relationship>");

            pathName = sc.nextLine();
            try{

                words = pathName.split(" ");

                // check the input is in the format way

                //if so
                if(pathName != null && src != null && dest != null && connection != null)
                    List<String> tPath = this.dM.getPathFromTargets();
                else
                    System.out.println(ErrorUtils.invalidInput("Incorrcet format"));
            }
            catch(ErrorUtils e){
                System.out.println(e);
            }
        }
        else
            System.out.println(ErrorUtils.noGraph());
    }

    private void exitProgram(){

    }
}
