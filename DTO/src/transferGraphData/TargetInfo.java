package transferGraphData;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class TargetInfo {

    private String name;
    private String type;
    private Integer totalDependsOn;
    private Integer totalRequiredFor;
    private String information;
    private Object checkBox;
    private String status;
   // private List<TargetInfo> dependsOn = new ArrayList<>();
    //private List<TargetInfo> requiredFor = new ArrayList<>();

    public TargetInfo(){};

    public TargetInfo(TargetInfo targetInfo){
        this.name = targetInfo.getName();
        this.type = targetInfo.getType();
        this.totalDependsOn = targetInfo.getTotalDependsOn();
        this.totalRequiredFor = targetInfo.getTotalRequiredFor();
        this.information = targetInfo.getInformation();
        this.status = "not initialized"; // todo handle with it in back end.
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalDependsOn() {
        return totalDependsOn;
    }

    public void setTotalDependsOn(Integer totalDependsOn) {
        this.totalDependsOn = totalDependsOn;
    }

    public Integer getTotalRequiredFor() {
        return totalRequiredFor;
    }

    public void setTotalRequiredFor(Integer totalRequiredFor) {
        this.totalRequiredFor = totalRequiredFor;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public CheckBox getCheckBox() {
        if(checkBox == null)
            this.checkBox = new CheckBox();
        return (CheckBox) this.checkBox;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
