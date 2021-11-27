// This class is responsible to print & interact with the ui

import errors.ErrorUtils;

import java.util.*;

public class Menu {

    private UiDataManager dM = new UiDataManager();
    private Boolean isThereGraph = false;

    public void start(){

        Scanner sc = new Scanner(System.in);
        String userInput = new String();

        System.out.println("\n" + this.welcomeMessage() + "\n");
        this.printMenu();

        userInput = sc.nextLine();

        while(userInput != "9"){      // In menu until presses 6 == exit

            switch(userInput) {

                case "1":
                    this.fileHandler(); break;
                case "2":
                    this.graphHandler(); break;
                case "3":
                    this.targetHandler(); break;
                case "4":
                    this.pathHandler(); break;
                case "5":
                    this.processHandler(); break;
                case "6":
                    this.findCircle(); break;
                case "7":
                    this.saveToFile(); break;
                case "8":
                    this.loadFile(); break;
                case "9":
                    this.exitProgram(); break;
                default: // Invalid input, not number\number isn't good
                    System.out.println("\r\n" + ErrorUtils.invalidInput("Please enter a number from 1-6.")+ "\r\n"); break;
            }
            this.printMenu();
            userInput = sc.nextLine();
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
        System.out.println("8) Load data from file. ");
        System.out.println("9) Exit the system.");

    }

    public void loadFile() {
        try{
            this.printBackToMenu();
            System.out.println("Please write the full path of the file: (example: C:\\Users\\guyyo\\IdeaProjects\\Gpup\\Engine\\src\\resources\\ex1-big.xml)");
            Scanner scan = new Scanner(System.in);
            String fullPath = scan.nextLine();
            if(fullPath.toLowerCase().equals("menu"))
                return;
            this.dM.loadFile(fullPath);
            this.isThereGraph = true;
        }catch (ErrorUtils e){e.getMessage();}
    }

    public void findCircle() {

        if(this.isThereGraph) {
            System.out.println("At any time you can press 'menu' to go back to the main menu.");
            System.out.println("Lets find circle!");
            System.out.println("Please enter name of target: ");
            Scanner scan = new Scanner(System.in);
            String targetName = scan.nextLine();
            if (targetName.toLowerCase().equals("menu"))
                return;
            try {
                String tPath = this.dM.findCircle(targetName);
                String[] allPath = tPath.split(",");
                System.out.println(allPath[0].replace(" ", "->") + targetName);
                // Arrays.stream(allPath).map(t -> t.replace(" ", "->")).forEach(s -> System.out.println(s + targetName)); if we want to print all the circle
            } catch (ErrorUtils e) {
                System.out.println(e.getMessage());
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
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

    private void fileHandler(){     // Starts the engine of the system if all good.

        Scanner scan = new Scanner(System.in);
        String fileName = new String();

        this.printBackToMenu();
        System.out.println("Please write the full path of the file: (example: C:\\Users\\guyyo\\IdeaProjects\\Gpup\\Engine\\src\\resources\\ex1-big.xml)");
        fileName = scan.nextLine();

        if(fileName.toLowerCase().equals("menu"))
            return;
        try {
            if (this.dM.checkFile(fileName)) // only check the file syntax
                this.isThereGraph = true;

            System.out.println("Graph given was uploaded successfully :)");
            System.out.println("You may start asking for data from the graph.\r\n ");

        }catch (ErrorUtils e){
            System.out.println(e.getMessage()+"\r\n");}
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
            this.printBackToMenu();
            System.out.println("Please enter a target name:");
            tName = sc.nextLine();          // ask user for the name of the target
            if(tName.toLowerCase().equals("menu"))
                return;
            try {
                //if tName not null
                List<String> tarInfo = this.dM.getInfoFromTarget(tName.toUpperCase());

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
                            System.out.println("General information about him: "+ tarInfo.get(i) + "\r\n"); break;
                    }
                }
            }
            catch (ErrorUtils e){
                System.out.println(e.getMessage()+"\r\n");
                this.targetHandler();
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private void processHandler(){

        String [] ints = new String[3];
        String userAnswer = new String();
        Integer timeToRun = 0 , chancesToSucceed = 0, chancesToBeAWarning = 0;

        if(this.isThereGraph){

            System.out.println("Lets start some processing!");

            userAnswer = this.askUserIfRandom();

            switch (userAnswer){

                case "menu": {
                    break;
                }
                case "userWantsToGiveValues": {
                    userAnswer = this.getValuesForRandomProcess();

                    if (userAnswer.toLowerCase().equals("menu"))
                        return;

                    ints = userAnswer.split(" ", 3);

                    timeToRun = Integer.valueOf(ints[0]);
                    chancesToSucceed = Integer.valueOf(ints[1]);
                    chancesToBeAWarning = Integer.valueOf(ints[2]);

                    try { this.startAndPrintProcess(true, timeToRun, chancesToSucceed, chancesToBeAWarning); }
                    catch (ErrorUtils e) { System.out.println(e.getMessage()); }
                    break;
                }
                case "userWantsRandom": {

                    try { this.startAndPrintProcess(false, -1, -1, -1);}
                    catch (ErrorUtils e) { System.out.println(e.getMessage()); }
                    break;
                }
            }
        }
        else
            System.out.println("\r\n" + ErrorUtils.noGraph() + "\r\n");
    }

    private String getValuesForRandomProcess() {

        Scanner scan = new Scanner(System.in);
        String userAnswer = new String();
        Boolean isValidInput = false;
        String [] ints = new String[3];
        ints[0]= ""; ints[1]= ""; ints[2]= "";


        while(!isValidInput) {

            this.printBackToMenu();

            // ask user for four inputs
            System.out.println("Ok, so you want to be in control, no problem.");
            System.out.println("Please enter the following numbers: Time to run on each target (in ms'), The chances of success (0-100) & the chance that the success will be a warning (0-100). ");
            System.out.println("Example format: 1500  55 66");

            userAnswer = scan.nextLine();

            if(userAnswer.toLowerCase().equals("menu"))
                break;

            ints = userAnswer.split(" ", 3);

            if(ints.length == 3){

                //check that each number is in the parameters wanted
                if (this.isNumeric(ints[0]) && this.isNumeric(ints[1]) && this.isNumeric(ints[2])) {

                    if (Integer.valueOf(ints[0]) >= 0) {

                        if (Integer.valueOf(ints[1]) >= 0 && Integer.valueOf(ints[1]) <= 100 && Integer.valueOf(ints[2]) >= 0 && Integer.valueOf(ints[2]) <= 100) {
                            isValidInput = true;
                        }
                    } else
                        System.out.println(ErrorUtils.invalidInput("Time given to run is a negative number"));
                } else
                    System.out.println(ErrorUtils.invalidInput("input given weren't numbers."));
            }
            else
                System.out.println(ErrorUtils.invalidInput());
        }

        return userAnswer;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void printBackToMenu(){ System.out.println("\r\n *** At any time you can press 'menu' to go back to the main menu. *** ");}

    private String askUserIfRandom() {

        Scanner scan = new Scanner(System.in);
        String userAnswer = new String();
        Boolean isValidInput = false;

        while(!isValidInput) {

            this.printBackToMenu();
            System.out.println("First things first... Do you want to give me numbers for the coming process?");
            System.out.println("If so, please press- 'Y'");
            System.out.println("If you want us to generate random numbers for you, press 'N'");

            userAnswer = scan.nextLine();

            if (userAnswer.toLowerCase().equals("y")) {
                userAnswer = "userWantsToGiveValues";
                isValidInput = true;
            }
            else if (userAnswer.toLowerCase().equals("n")) {
                userAnswer = "userWantsRandom";
                isValidInput = true;
            }
            else if (userAnswer.toLowerCase().equals("menu")) {
                userAnswer = "menu";
                isValidInput = true;
            }
            else{
                System.out.println(ErrorUtils.invalidInput("PLease Choose from the options above"));
            }
        }
        return userAnswer;
    }

    private void pathHandler(){

        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        String[] words = {"","",""};
        String pathName = null, src = null, dest = null, connection = null;
        String tPath;

        if(this.isThereGraph){

            // need to get from the user src, dest & connection
            this.printBackToMenu();
            System.out.println("Let's try to find the targets path you want!");
            System.out.println("PLease enter path in the following format: <target source>  <target destination>  <wanted relationship (depends On -> D /required For -> R)>");
            System.out.println("After that, press enter.");

            pathName = sc.nextLine();

            words = pathName.split(" ");
            if(words[0].toLowerCase().equals("menu"))
                return;

            // check the input is in the format way
            try {
                src = words[0]; dest = words[1]; connection = words[2];
            }
            catch (Exception e){
                System.out.println("Please enter three values.");
                this.pathHandler();
            }

            try {
                    tPath = this.dM.getPathFromTargets(src.toUpperCase(), dest.toUpperCase(), connection.toUpperCase());
                    String[] allPath = tPath.split(",");
                    if(allPath[0].length() > 2)
                    {
                        System.out.println("All the paths from " + src.toUpperCase() + " to " + dest.toUpperCase()  + ":");

                        if(connection.toLowerCase().equals("D"))
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

    private void startAndPrintProcess(Boolean isRandom, int timeToRun, int chancesToSucceed, int chancesToBeAWarning) throws ErrorUtils {

        Map<String,List<String>> targetNameToHisProcessData = new HashMap<>();
        Scanner sc= new Scanner(System.in);
        String userChoice = new String();
        boolean heWantsIncremental = false, heWantesMenu = false,  heWantsFromBeginning = false;
        Boolean heWantsRandom = isRandom;
        Boolean processIsFinished = false;
        ConsumerUI cUI = new ConsumerUI();
        String [] ints = new String[3];

        // first process:
        System.out.println("Starting the process..\r\n");
        if(heWantsRandom)
            targetNameToHisProcessData =  this.dM.startProcess(cUI,timeToRun, chancesToSucceed, chancesToBeAWarning, null);
        else
            targetNameToHisProcessData =  this.dM.startProcess(cUI ,null);
        System.out.println("\r\nCongrats! first process is done.");
        processIsFinished = this.isThisFinished(targetNameToHisProcessData);


        // how to keep going?
        userChoice = this.askUserHowToKeepGoing(processIsFinished);

        switch(userChoice){
            case "heWantsFromBeginning":
                heWantsFromBeginning = true; break;
            case "heWantsIncremental":
                heWantsIncremental = true; break;
            case "menu":
                break;
        }


        // we keep going as long as he wants
        while(!processIsFinished && (heWantsIncremental || heWantsFromBeginning) && !heWantesMenu){

            if(heWantsIncremental) {
                targetNameToHisProcessData = this.dM.startProcess(cUI, targetNameToHisProcessData);
            }
            else if(heWantsFromBeginning){

                userChoice = this.askUserIfRandom();

                switch (userChoice){
                    case "menu":
                        heWantesMenu = true;
                        break;
                    case "userWantsToGiveValues": {

                        userChoice = this.getValuesForRandomProcess();

                        if (userChoice.toLowerCase().equals("menu"))
                            return;

                        ints = userChoice.split(" ", 3);

                        timeToRun = Integer.valueOf(ints[0]);
                        chancesToSucceed = Integer.valueOf(ints[1]);
                        chancesToBeAWarning = Integer.valueOf(ints[2]);

                        try { this.startAndPrintProcess(true, timeToRun, chancesToSucceed, chancesToBeAWarning); }
                        catch (ErrorUtils e) { System.out.println(e.getMessage()); }
                        break;
                    }
                    case "userWantsRandom": {
                        try { this.startAndPrintProcess(false, -1, -1, -1);}
                        catch (ErrorUtils e) { System.out.println(e.getMessage()); }
                        break;
                    }
                }
            }

            processIsFinished = this.isThisFinished(targetNameToHisProcessData);

            userChoice = this.askUserHowToKeepGoing(processIsFinished);

            switch(userChoice){

                case "heWantsFromBeginning":
                    heWantsFromBeginning = true; break;

                case "heWantsIncremental":
                    heWantsIncremental = true; break;
                case "menu":
                    break;
            }
        }
        System.out.println("Well that's that!\r\n");
    }

    private Boolean isThisFinished(Map<String,List<String>> targetNameToHisProcessData){

        Boolean iFinished = true;

        if(!targetNameToHisProcessData.keySet().isEmpty()) {

            for (List<String> curTaskData : targetNameToHisProcessData.values()) {

                if (curTaskData != null) {
                    if (curTaskData.get(3) != "SUCCESS" && curTaskData.get(3) != "WARNING") {
                        iFinished = false;
                        break;
                    }
                }
            }
        }
        return iFinished;
    }

    private String askUserHowToKeepGoing(boolean processFinished){

        Scanner sc= new Scanner(System.in);
        String userChoice = new String();
        String[] words = new String[2];
        Boolean validInput = false;

        if(processFinished) {
            System.out.println("All the process finished successfully!");
            return "menu";
        }

        while(!validInput) {

            // ask the use how to keep going
            this.printBackToMenu();
            System.out.println("How do u want to keep going?");
            System.out.println("If you to run again (on the targets which didn't succeed), please type: 'Run Incremental'.");
            System.out.println("If you want to start the process from the beginning, please type: 'Run again'.");


            userChoice = sc.nextLine();

            // check user input
            words = userChoice.split(" ", 2);

            if (words.length == 2) {

                // options
                switch (userChoice.toLowerCase()) {

                    case "run incremental":
                        userChoice = "heWantsIncremental";
                        validInput = true; break;
                    case "run again":
                        userChoice = "heWantsFromBeginning";
                        validInput = true; break;
                    case "menu":
                        userChoice = "menu";
                        validInput = true; break;
                    default:
                        System.out.println(ErrorUtils.invalidInput());
                        break;
                }
            }
            else if(userChoice.toLowerCase().equals("menu"))
                return "menu";
            else
                System.out.println(ErrorUtils.invalidInput());
        }
        return userChoice;
    }
}
