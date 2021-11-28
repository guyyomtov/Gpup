package consumerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessInfo {

    public static Map<String, List<String>> targetNameToHisProcessData = new HashMap<>();

    public static Map<String, List<String>> getTargetNameToHisProcessData() {
        return targetNameToHisProcessData;
    }

    public static void setTargetNameToHisProcessData(Map<String, List<String>> tmp) {
        targetNameToHisProcessData = tmp;
    }


}
