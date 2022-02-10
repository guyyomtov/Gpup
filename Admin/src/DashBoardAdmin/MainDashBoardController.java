package DashBoardAdmin;

import DashBoardAdmin.onlineAdminsComponent.OnlineAdminsController;
import api.HttpStatusUpdate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;

public class MainDashBoardController implements Closeable, HttpStatusUpdate {

    private Stage primaryStage;
    @FXML Parent onlineAdminsComponents;
    @FXML private OnlineAdminsController onlineAdminsComponentsController;
    @FXML private DashBoardButtonColum dashBoardButtonColumController;

    private void setMainPanelTo() {

        try {
            // This takes us to Admin dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoardAdmin/MainDashBoard.fxml"));
            Parent load = loader.load();
            MainDashBoardController dashBoardController = (MainDashBoardController) loader.getController();
            Scene scene = new Scene(load, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void switchToDashBoard() {

      //  setMainPanelTo();
        this.setActive();
    }

    public void setActive() {
        this.onlineAdminsComponentsController.setHttpStatusUpdate(this);
        this.onlineAdminsComponentsController.startListRefresher();
    }

    @Override
    public void updateHttpLine(String line) {

    }

    @Override
    public void close() throws IOException {

        this.onlineAdminsComponentsController.close();
    }
}
