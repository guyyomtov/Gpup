package Graph.process;

import DataManager.consumerData.ConsumerTaskInfo;
import Graph.Target;
import fileHandler.TaskFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class MinionForCompilation extends Minion{

    protected String fullPathDestination;
    protected String fullPathSource;

    public MinionForCompilation(Target target, boolean imSimulation) {
        super(target, imSimulation);
    }

    public List<String> runMe(Map<String,Minion> allMinions, Consumer cUI)  {

        List<String> resData = new ArrayList<>();
        String openedParents = new String();
        ConsumerTaskInfo cTI = new ConsumerTaskInfo();
        cTI.openFile(this.target.getName());

        Instant start, end;
        String resSrcArg = new String();
        int res = 0;
        start = Instant.now();
        myStatus = "IN PROCESS";
        this.setStatus(myStatus);

        try {
            Thread.sleep(100);
        }catch (InterruptedException e) { }

        taskConsumer.accept("The target " + this.target.getName() + " about to run.");

        resSrcArg += this.fullPathSource + "/" + this.target.getGeneralInfo().replace('.', '/' ) + ".java";

        String[] c = {"javac", "-d", this.fullPathDestination, "-cp", this.fullPathDestination, resSrcArg};
        taskConsumer.accept("Full command: javac-d" + " " + this.fullPathDestination +  "-cp " +  this.fullPathDestination + resSrcArg );

        try {
            start = Instant.now();
            Process process = Runtime.getRuntime().exec(c);
            process.waitFor();
            end = Instant.now();
            taskConsumer.accept("The compiler finish the task on target " + targetName + " in: " + Duration.between(start, end).toMillis() + " milli second.");
            res = process.exitValue();
            if(res == 0) // if res == 0 == > process on curr target success.
                this.myStatus = "SUCCESS";
            else {
                this.myStatus = "FAILURE";
                String errorMessage = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())).readLine();

                taskConsumer.accept("The target is failed, output from javac: " + errorMessage);

                checkAndUpdateWhoImClosedTORunning(this, true);

            }
            this.status.setValue(myStatus);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(targetName + " is " + "failed" );
        }

         openedParents = iOpened(this.parentsNames, allMinions);
        return resData;

    }





}
