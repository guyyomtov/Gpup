package Graph.process;

import transferGraphData.TaskData;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<String, TaskData> nameToTaskData; // -> dto class

    private Map<String, Task> nameToTask; // -> original task class

    public TaskManager(){
        this.nameToTaskData = new HashMap<>();
        this.nameToTask = new HashMap<>();
    }
//methods for taskData
    public synchronized void addTaskData(TaskData taskData){
        this.nameToTaskData.put(taskData.getTaskName(), taskData);
    }

    public synchronized void removeTaskData(TaskData taskData){ this.nameToTaskData.remove(taskData.getTaskName());}

    public boolean taskDataExist(TaskData taskData){

        return this.nameToTaskData.containsKey(taskData.getTaskName());

    }

    public Map<String, TaskData> getNameToTaskData() {
        return nameToTaskData;
    }

    public boolean taskDataExist(String taskName) {

        return this.nameToTaskData.containsKey(taskName);

    }

    // methods for task
    public synchronized void addTask(Task task){
        this.nameToTask.put(task.getTaskName(), task);
    }

    public synchronized void removeTask(Task task){ this.nameToTask.remove(task.getTaskName());}

    public boolean taskExist(Task task){
        return this.nameToTask.containsKey(task.getTaskName());
    }

    public Map<String, Task> getNameToTask() {
        return nameToTask;
    }

    public boolean taskExist(String taskName) {
        return this.nameToTask.containsKey(taskName);
    }


}
