package Graph.process;

import transferGraphData.TaskData;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<String, TaskData> nameToTask;

    public TaskManager(){this.nameToTask = new HashMap<>();}

    public synchronized void addTask(TaskData taskData){
        this.nameToTask.put(taskData.getTaskName(), taskData);
    }

    public synchronized void removeTask(TaskData taskData){ this.nameToTask.remove(taskData.getTaskName());}

    public boolean taskExist(TaskData taskData){

        return this.nameToTask.containsKey(taskData.getTaskName());

    }

    public Map<String, TaskData> getNameToTask() {
        return nameToTask;
    }

    public boolean taskExist(String taskName) {

        return this.nameToTask.containsKey(taskName);

    }



    }
