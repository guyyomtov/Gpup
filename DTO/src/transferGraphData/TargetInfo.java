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
   // private List<TargetInfo> dependsOn = new ArrayList<>();
    //private List<TargetInfo> requiredFor = new ArrayList<>();

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
        return new CheckBox();
    }

//    public List<TargetInfo> getDependsOn() {
//        return dependsOn;
//    }
//
//    public void setDependsOn(List<TargetInfo> dependsOn) {
//        this.dependsOn = dependsOn;
//    }
//
//    public List<TargetInfo> getRequiredFor() {
//        return requiredFor;
//    }
//
//    public void setRequiredFor(List<TargetInfo> requiredFor) {
//        this.requiredFor = requiredFor;
//    }
}
