package transferGraphData;

import java.util.ArrayList;
import java.util.List;

public class AllGraphInfo {
    private GraphInfo graphInfo;
    private List<TargetInfo> targetInfoList = new ArrayList<>();

    public AllGraphInfo(GraphInfo graphInfo, List<TargetInfo> targetInfoList){
        this.graphInfo = graphInfo;
        this.targetInfoList = targetInfoList;
    }
    public GraphInfo getGraphInfo() {
        return graphInfo;
    }

    public void setGraphInfo(GraphInfo graphInfo) {
        this.graphInfo = graphInfo;
    }

    public List<TargetInfo> getTargetInfoList() {
        return targetInfoList;
    }

    public void setTargetInfoList(List<TargetInfo> targetInfoList) {
        this.targetInfoList = targetInfoList;
    }
}
