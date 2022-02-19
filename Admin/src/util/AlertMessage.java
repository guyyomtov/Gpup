package util;

import javafx.scene.control.Alert;

public class AlertMessage {

    public static void showUserSuccessAlert(String message) {

        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION);
        alertUser.setHeaderText(message);
        alertUser.showAndWait();
    }
}
