
import Graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import uploadFileView.MainController;

import java.util.Arrays;
import java.util.List;

public class MainJavaFX extends Application {

    MainController mainController = new MainController();
    FXMLLoader loader = new FXMLLoader();



    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GPUP");

        loader = new FXMLLoader(getClass().getResource("/uploadFileView/uploadFileView.fxml"));
        Parent load = loader.load();
        mainController = (MainController)loader.getController();
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        MainController.primaryStage = primaryStage;
        primaryStage.show();
    }



    public static void main(String[] args) {

       launch(args);

    }
}
