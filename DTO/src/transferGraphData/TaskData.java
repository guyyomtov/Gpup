package transferGraphData;

import javafx.beans.property.BooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class TaskData {

    private String taskName;
    private List<TargetInfo> targetInfoList = new ArrayList<>();
    private Integer pricePerTarget;
    private String whatKindOfTask;
    private Boolean fromScratch;
    private String graphName;
    //todo maybe we dont need those members
    private BooleanProperty pauseProperty;
    private BooleanProperty stopProperty;


    //args for simulation
    private Boolean isRandom;
    private Integer maxTimePerTarget;
    private Integer chancesToSuccess;
    private Integer chancesToWarning;

    //args for compilation
    private String fullPathSource;
    private String fullPathDestination;



    public TaskData(){}
    public TaskData(String taskName, Integer pricePerTarget, List<TargetInfo> targetInfoList, String whatKindOfTask, String graphName) {
        this.taskName = taskName;
        this.pricePerTarget = pricePerTarget;
        this.targetInfoList = targetInfoList;
        this.whatKindOfTask = whatKindOfTask;
        this.graphName = graphName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPricePerTarget() {
        return pricePerTarget;
    }

    public void setPricePerTarget(Integer pricePerTarget) {
        this.pricePerTarget = pricePerTarget;
    }

    public List<TargetInfo> getTargetInfoList() {
        return targetInfoList;
    }

    public void setTargetInfoList(List<TargetInfo> targetInfoList) {
        this.targetInfoList = targetInfoList;
    }

    public String getWhatKindOfTask() {
        return whatKindOfTask;
    }

    public void setWhatKindOfTask(String whatKindOfTask) {
        this.whatKindOfTask = whatKindOfTask;
    }


    public Boolean fromScratchProperty() {
        return fromScratch;
    }

    public void setFromScratch(boolean fromScratch) {
        this.fromScratch = fromScratch;
    }

    public Integer getMaxTimePerTarget() {
        return maxTimePerTarget;
    }

    public void setMaxTimePerTarget(Integer maxTimePerTarget) {
        this.maxTimePerTarget = maxTimePerTarget;
    }

    public Integer getChancesToSuccess() {
        return chancesToSuccess;
    }

    public void setChancesToSuccess(Integer chancesToSuccess) {
        this.chancesToSuccess = chancesToSuccess;
    }

    public Integer getChancesToWarning() {
        return chancesToWarning;
    }

    public void setChancesToWarning(Integer chancesToWarning) {
        this.chancesToWarning = chancesToWarning;
    }

    public String getFullPathSource() {
        return fullPathSource;
    }

    public void setFullPathSource(String fullPathSource) {
        this.fullPathSource = fullPathSource;
    }

    public String getFullPathDestination() {
        return fullPathDestination;
    }

    public void setFullPathDestination(String fullPathDestination) {
        this.fullPathDestination = fullPathDestination;
    }

    public boolean isStopProperty() {
        return stopProperty.get();
    }

    public BooleanProperty stopPropertyProperty() {
        return stopProperty;
    }

    public void setStopProperty(boolean stopProperty) {
        this.stopProperty.set(stopProperty);
    }

    public Boolean getRandom() {
        return isRandom;
    }

    public void setRandom(Boolean random) {
        isRandom = random;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }
}
