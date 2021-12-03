package consumerData;

import fileHandler.TaskFile;

import java.util.List;
import java.util.function.Consumer;

    public class ConsumerTaskInfo {

        public ConsumerTaskInfo(String targetName)
        {
            TaskFile.openFile(targetName);
        }

        public void getInfo(Consumer cUI, String info)
        {
            cUI.accept(info);
            TaskFile.writeToFile(info + '\n');
        }


    }

