package consumerData;

import fileHandler.TaskFile;

import java.util.List;
import java.util.function.Consumer;

    public class ConsumerTaskInfo {

        public void getData(List<String> lst, Consumer cUI)
        {
            String taskFormat = new FormatTask().makeFormat(lst);
            transferToUI(taskFormat, cUI);
            transferToFile(taskFormat, lst.get(1));
        }

        public void transferToUI(String taskFormat,Consumer cUI ){
            cUI.accept(taskFormat);}

        public void transferToFile(String taskFormat, String targetName) {
            try{
            TaskFile.openAndWriteToFile(taskFormat, targetName);
            }catch (Exception e){}
        }

    }

