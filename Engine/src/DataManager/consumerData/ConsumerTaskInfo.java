package DataManager.consumerData;

import fileHandler.TaskFile;
import javafx.application.Platform;

import java.util.function.Consumer;

    public class ConsumerTaskInfo {

        private TaskFile taskFile = new TaskFile();

        public ConsumerTaskInfo(String targetName)
        {
            taskFile.openFile(targetName);
        }


        public void getInfo(String info)
        {
            System.out.println(info);
            taskFile.writeToFile(info + '\n');
        }


        public void closeFile() {
            this.taskFile.closeFile();
        }
    }

