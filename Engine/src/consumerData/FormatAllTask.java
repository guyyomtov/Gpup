package consumerData;

import Graph.Target;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
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

    }


}