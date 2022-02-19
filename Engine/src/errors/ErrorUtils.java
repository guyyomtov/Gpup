package errors;

import javafx.scene.control.Alert;
import jdk.nashorn.internal.codegen.ApplySpecialization;

public class ErrorUtils extends Exception {
    private String ms;

    public ErrorUtils(String s) {
        ms = s;
        // super(s);
    }

    public static final String LIBRARY_DOSENT_EXSIST_IN_PROJECT = "Library doesn't exist in project.";
    public static final String MISSING_COMPILATION_NEEDED_DATA = "Missing compilation needed data.";
    public static final String BUTTON_IS_NULL = "Button given is null.";
    public static final String NEEDED_DATA_IS_NULL = "Basic needed data for process is null";
    public static final String TYPE_OF_PROCESS = "Process can't be of two types";
    public static final String PROCESS_RANDOM = "Appropriate flag wasn't started or was the oppisite from wanted";
    public static final String INVALID_INPUT = "The input given is invalid.";
    public static final String CHOOSE_GRAPH = "Please choose graph!";

    public static String invalidProcess() {
        return "Two types of diffrent process can't work together";
    }

    public static String noGraph() {
        return "No graph exists yet in the system, please upload one.";
    }

    public static String invalidInput() {
        return "The input given is invalid.";
    }

    public static String invalidInput(String err) {
        return "The input given is invalid (" + err + ")";
    }

    public static String invalidFile() {
        return "The file given is invalid, please try to upload a new one.";
    }

    public static String invalidFile(String currMs) {
        return "The file given is invalid, please try to upload a new one " + "(" + currMs + ")";
    }

    public static String invalidXMLFile(String curMsg) {
        return "The XML file given is invalid, please try to upload a new one " + "(" + curMsg + ")";
    }

    public static String invalidTarget() {
        return "No such target exists in the graph.";
    }

    public static String invalidTarget(String err) {
        return "No such target exists in the graph." + "(" + err + ")";
    }

    public static String noPathFound() {
        return "No path wanted was found between the two given targets.";
    }

    public static String noPathWithLogic() {
        return "A path was found between the two wanted targets, but with a different logic connection.";
    }

    public static String invalidTask() {
        return "The task given was invalid, please try again.";
    }

    @Override
    public String getMessage() {
        return this.ms;
    }

    public static void makeJavaFXCutomAlert(String error) {

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("ERROR");
        errorAlert.setContentText(error);
        errorAlert.showAndWait();
    }

    public static void showUserErrorAlertJavaFX(String errorType) {

        Alert alertUser = new Alert(Alert.AlertType.ERROR);
        alertUser.setHeaderText(errorType);
        alertUser.setContentText(ErrorUtils.invalidFile());
        alertUser.showAndWait();
    }
}
