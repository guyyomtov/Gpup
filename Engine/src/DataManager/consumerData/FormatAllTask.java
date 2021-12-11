package DataManager.consumerData;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FormatAllTask {

    public static Map<String, Integer>  mStatusToNumber = new HashMap<>();
    public static Instant start;
    public static Instant end;

    public static void restartMap()
    {
        mStatusToNumber.put("SUCCESS", 0);
        mStatusToNumber.put("WARNING", 0);
        mStatusToNumber.put("FAILURE", 0);
        mStatusToNumber.put("SKIPPED", 0);
    }

    public static void updateCounter(String status){
        Integer toAdd = mStatusToNumber.get(status);
    mStatusToNumber.put(status, toAdd + 1);
    }

    public static void sendData(Consumer cUI)
    {
        Duration timeElapsed = Duration.between(start, end);
        String time = String.format("%02d:%02d:%02d" ,timeElapsed.toHours(), timeElapsed.toMinutes(), timeElapsed.getSeconds());
        cUI.accept("The result of the process: ");
        cUI.accept("Total Time: " + time);
        cUI.accept("Targets that ended with 'SUCCESS': " + mStatusToNumber.get("SUCCESS"));
        cUI.accept("Targets that ended with 'WARNING': " + mStatusToNumber.get("WARNING"));
        cUI.accept("Targets that ended with 'FAILURE': " + mStatusToNumber.get("FAILURE"));
        cUI.accept("Targets that ended with 'SKIPPED': " + mStatusToNumber.get("SKIPPED"));
        cUI.accept("--------------------------------------");

    }

    public static void sendData(Consumer cUI, Map<String, List<String>> nameToData)
    {
        for(List<String> data : nameToData.values())
        {
            Integer timeThatRun = Integer.parseInt(data.get(0));
            String name = data.get(1);
            String status = data.get(3);
            cUI.accept("Target's name: " + name);
            if(status != "SKIPPED")
                cUI.accept("The total time that run: 00:00:0" + timeThatRun/1000);
            cUI.accept("Target's status: " + status);
            cUI.accept("---------------------------------");

        }
    }


}
