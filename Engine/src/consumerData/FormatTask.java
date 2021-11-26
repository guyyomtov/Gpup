package consumerData;

import sun.security.jca.GetInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class FormatTask {

    String makeFormat(List<String> currTargetData)
    {
        String targetName, status, generalInfo, iOpened;
        int tToSleep;
        tToSleep = Integer.valueOf(currTargetData.get(0));
        targetName = currTargetData.get(1);
        generalInfo = currTargetData.get(2);
        status = currTargetData.get(3);
        iOpened = currTargetData.get(4);
        Date date  = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String start= formatter.format(date);
        Instant s = Instant.now();
        try {
            Thread.sleep(tToSleep);
        }catch (InterruptedException e){}
        String end = formatter.format(new Date());
        Instant e = Instant.now();
        Duration timeElapsed = Duration.between(s, e);
        String time = String.format("%02d:%02d:%02d" ,timeElapsed.toHours(), timeElapsed.toMinutes(), timeElapsed.getSeconds());
        return
         "Start: " + start + "\n" + "End: " + end + "\n"
        + "Total time: " +  time + "\n"
        + "Target Name: " + targetName + "\n"
        + "General Info: " + generalInfo + "\n"
        + "Target status: " + status + "\n"
        +"It opened up these tasks: " + (iOpened == "" ? "None" : iOpened) +"\r\n"
        ;
    }
}
