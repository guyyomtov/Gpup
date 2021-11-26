// This class is responsible to print & interact with the ui

import errors.ErrorUtils;

import java.util.*;

public class Menu {

    private UiDataManager dM = new UiDataManager();
    private Boolean isThereGraph = false;

    public void start(){

        char userInput;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n" + this.welcomeMessage() + "\n");
        this.printMenu();

        userInput = sc.next().charAt(0);

        while(userInput != '8'){      // In menu until presses 6 == exit

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
                    this.findCircle();
                    break;
                case '7':
                    this.saveToFile();
                    break;
                case '8':
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
        System.out.println("6) Find circle. ");
        System.out.println("7) Save data to file. ");
        System.out.println("8) Exit the system.");

    }

    public void findCircle()
    {
        System.out.println("At any time you can press 'menu' to go back to the main menu.");
        System.out.println("Lets find circle!");
        System.out.println("Please enter name of target: ");
        Scanner scan = new Scanner(System.in);
        String targetName = scan.nextLine();
        if(targetName.toLowerCase().equals("menu"))
            return;
        try {
            String tPath = this.dM.findCircle(targetName);
            String[] allPath = tPath.split(",");
            System.out.println(allPath[0].replace(" ", "->") + targetName);
           // Arrays.stream(allPath).map(t -> t.replace(" ", "->")).forEach(s -> System.out.println(s + targetName)); if we want to print all the circle
        }catch (ErrorUtils e){
            System.out.println(e.getMessage());}
    }

    public void saveToFile() {
        System.out.println("At any time you can press 'menu' to go back to the main menu.");
        System.out.println("Please enter full path for saving Gpup to a file:");
        Scanner scan = new Scanner(System.in);
        String fullPath = scan.nextLine();
        if(fullPath.toLowerCase().equals("menu"))
            return;
        else
            this.dM.saveToFile(fullPath);

    }

    // TO DO
    private void fileHandler(){     // Starts the engine of the system if all good.

        System.out.println("At any time you can press 'menu' to go back to the main menu.");
        System.out.println("Please write the full path of the file: (example: C:\\Users\\guyyo\\IdeaProjects\\Gpup\\Engine\\src\\resources\\ex1-big.xml)");
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

        Boolean heWantsRandom = true;
        String userInput = new String();
        Integer timeToRun = 0 , chancesToSucceed = 0, chancesToBeAWarning = 0;

        if(this.isThereGraph){
            Scanner sc= new Scanner(System.in);
            System.out.println("At any time you can press 'menu' to go back to the main menu.");
            System.out.println("Lets start the process!");

            heWantsRandom = this.askUserIfRandom();

            if(!heWantsRandom) {

                // get input numbers from user
            }
            String userChoice = sc.nextLine();
            if(userChoice.toLowerCase().equals("menu"))
                return;
            else {

                // check and get data from the user
                try{
                    this.startAndPrintProcess(heWantsRandom, timeToRun, chancesToSucceed, chancesToBeAWarning);
                }
                catch (ErrorUtils e){
                    System.out.println(e.getMessage());
                }catch(InterruptedException e){}
            }


        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private Boolean askUserIfRandom() {
        Boolean res = false;
        System.out.println("");

        return res;
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

    private void startAndPrintProcess(Boolean isRandom, int timeToRun, int chancesToSucceed, int chancesToBeAWarning) throws ErrorUtils, InterruptedException {

        Map<String,List<String>> targetNameToHisProcessData = new HashMap<>();
        Boolean heWantsAgain = true;
        Boolean heWantsRandom = isRandom;
        Integer counter = 0;
        ConsumerUI cUI = new ConsumerUI();
        // first process:
        if(heWantsRandom)
            targetNameToHisProcessData =  this.dM.startProcess(cUI,timeToRun, chancesToSucceed, chancesToBeAWarning, null);
        else
            targetNameToHisProcessData =  this.dM.startProcess(cUI ,null);

       // this.printThisProcess(targetNameToHisProcessData);
        System.out.println("\r\nCongrats! first process is done.");


        while(!targetNameToHisProcessData.isEmpty() && heWantsAgain == true){

            // first time == counter == 0
            if(counter != 0) {

                // run some kind of process
//                if (heWantsRandom)
//                    targetNameToHisProcessData = this.dM.startProcess(timeToRun, chancesToSucceed, chancesToBeAWarning, targetNameToHisProcessData);
//                else
                    targetNameToHisProcessData = this.dM.startProcess(cUI ,targetNameToHisProcessData);

                // print it
              //  this.printThisProcess(targetNameToHisProcessData);
            }

            // check if he wants again
            heWantsAgain = this.askUserToProcessAgain();
            if(heWantsAgain) {

                heWantsRandom = this.askUserIfRandom();

                if(heWantsRandom) {
                    // get input from user for default values
                }
            }
            counter++;
        }
        System.out.println("Well that's that!");
    }

    private Boolean askUserToProcessAgain(){

        Scanner sc= new Scanner(System.in);
        Boolean heWantsAgain = false;

        System.out.println("If you'd like the process to run again (on the targets which didn't succeed), please press - 'Y'. If not press 'N'.");

        String userChoice = sc.nextLine();

        Map<String, List<String>> targetNameToHisProcessData = new HashMap<>();

        ConsumerUI cUI = new ConsumerUI();

        int timeToRun = 0;
        int chancesToSucceed = 0;
        int chancesToBeAWarning = 0;

        if(userChoice.toLowerCase().equals("y"))
            heWantsAgain = true;
        else if(userChoice.toLowerCase().equals("n"))
            heWantsAgain = false;
        else if(userChoice.toLowerCase().equals("menu"))
            this.printMenu();
        else
        {
            try {
                targetNameToHisProcessData = this.dM.startProcess(cUI, timeToRun, chancesToSucceed, chancesToBeAWarning, targetNameToHisProcessData);
            }catch (ErrorUtils e){
                System.out.println(e.getMessage());
            }
        }


        List<String> curPData = new ArrayList<>();
        Integer tToSleep;
        String targetName, status, generalInfo, iOpened;


        System.out.println("Starting the process: ");

        // go over each target
        return heWantsAgain;

    }

    private void printThisProcess(Map<String,List<String>> targetNameToHisProcessData) throws InterruptedException {

        List<String> curPData = new ArrayList<>();
        Integer tToSleep;
        String targetName, status, generalInfo, iOpened;


        System.out.println("Starting the process: ");

        // go over each target
        for(String tName : targetNameToHisProcessData.keySet()) {

            // get data
            // Date: [0]->sleep time, [1]->Target name, [2]->Target general info, [3]-> Target status in process, [4]-> Targets that depends and got released

            curPData = targetNameToHisProcessData.get(tName);

            tToSleep = Integer.valueOf(curPData.get(0));
            targetName = curPData.get(1);
            generalInfo = curPData.get(2);
            status = curPData.get(3);
            iOpened = curPData.get(4);

            // print it
            System.out.println("Target Name: " + targetName);
            System.out.println("General Info: " + (generalInfo == "" ? "None" : generalInfo));

            System.out.println("processing current target..");
            Thread.sleep(tToSleep);

            System.out.println("Process completed: ");
            System.out.println("target status: " + status);
            System.out.println("It opened up these tasks: " + (iOpened == " " ? "None" : iOpened) +"\r\n");
        }

    }
}
