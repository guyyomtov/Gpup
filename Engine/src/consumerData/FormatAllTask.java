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
        mStatusToNumber.put("Success", 0);
        mStatusToNumber.put("Warning", 0);
        mStatusToNumber.put("Failure ", 0);
        mStatusToNumber.put("Skipped", 0);
    }

    public static void updateCounter(String status){mStatusToNumber.put(status,mStatusToNumber.get(status) + 1);}

    public static void sendData(Consumer cUI)
    {
        Duration timeElapsed = Duration.between(start, end);
        String time = String.format("%02d:%02d:%02d" ,timeElapsed.toHours(), timeElapsed.toMinutes(), timeElapsed.getSeconds());
        cUI.accept("The result of the process: ");
        cUI.accept("Total Time: " + time);
        cUI.accept("Targets that ended with success: " + mStatusToNumber.get("Success"));
        cUI.accept("Targets that ended with warning: " + mStatusToNumber.get("Warning"));
        cUI.accept("Targets that ended with failure: " + mStatusToNumber.get("Failure"));
        cUI.accept("Targets that ended with skipped: " + mStatusToNumber.get("Skipped"));

    }


}
