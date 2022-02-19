package transferGraphData;


import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

public class GraphInfo {
    private String name;
    private String byWhoUpload;
    private Integer totalTargets;
    private Integer totalIndependents;
    private Integer totalLeaf;
    private Integer totalMiddles;
    private Integer totalRoots;
    private String taskInfo;

    public GraphInfo(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalTargets() {
        return totalTargets;
    }

    public void setTotalTargets(Integer totalTargets) {
        this.totalTargets = totalTargets;
    }

    public Integer getTotalIndependents() {
        return totalIndependents;
    }

    public void setTotalIndependents(Integer totalIndependents) {
        this.totalIndependents = totalIndependents;
    }

    public Integer getTotalLeaf() {
        return totalLeaf;
    }

    public void setTotalLeaf(Integer totalLeaf) {
        this.totalLeaf = totalLeaf;
    }

    public Integer getTotalMiddles() {
        return totalMiddles;
    }

    public void setTotalMiddles(Integer totalMiddles) {
        this.totalMiddles = totalMiddles;
    }

    public Integer getTotalRoots() {
        return totalRoots;
    }

    public void setTotalRoots(Integer totalRoots) {
        this.totalRoots = totalRoots;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getByWhoUpload() {
        return byWhoUpload;
    }

    public void setByWhoUpload(String byWhoUpload) {
        this.byWhoUpload = byWhoUpload;
    }

    public RadioButton getRadioButton() {
        return new RadioButton();
    }

    public void setRadioButton(RadioButton radioButton) {
        radioButton = radioButton;
    }

}
