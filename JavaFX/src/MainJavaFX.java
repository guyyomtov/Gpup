
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uploadFileView.MainController;

public class MainJavaFX extends Application {

    MainController mainController = new MainController();
    FXMLLoader loader = new FXMLLoader();



    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GPUP");
        loader = new FXMLLoader(getClass().getResource("uploadFileView/uploadFileView.fxml"));
        Parent load = loader.load();
        mainController = (MainController)loader.getController();
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        MainController.primaryStage = primaryStage;
        primaryStage.show();
    }



    public static void main(String[] args) {

        Flagger curFlagger = new Flagger().builder()
                        .processFromScratch(true)
                                .timeIsRandomInProcess(false);

        launch(args);
    }
}
