package Utils.ErrorHandling;

import errors.ErrorUtils;
import javafx.scene.control.Alert;

public class ErrorHandling {

    public static void showUserErrorAlert(String errorType) {

        Alert alertUser = new Alert(Alert.AlertType.ERROR);
        alertUser.setHeaderText(errorType);
        alertUser.setContentText(ErrorUtils.invalidFile());
        alertUser.showAndWait();
    }
}
