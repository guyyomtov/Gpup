package Main;

import Login.LoginPageController;
import Utils.ErrorHandling.ErrorHandling;
import errors.ErrorUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static Utils.Constants.Login_PAGE_FXML_RESOURCE_LOCATION;

public class WorkerMain extends Application {


    private LoginPageController loginPageController;


    public static void main(String[] args){ launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {

        // set stage
        this.setStage(primaryStage);

        // make login page url
        URL loginPageUrl = getClass().getResource(Login_PAGE_FXML_RESOURCE_LOCATION);

        // open login page
        this.openLoginPage(loginPageUrl, primaryStage);
    }

    private void setStage(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Worker App Client");
    }

    private void openLoginPage(URL loginPageUrl, Stage primaryStage) throws ErrorUtils {

        if(loginPageUrl == null || primaryStage == null) {

            ErrorHandling.showUserErrorAlert("Login Url or JavaFX stage in null.");

            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            Parent root = fxmlLoader.load();
            this.loginPageController = fxmlLoader.getController();
            this.loginPageController.setPrimaryStage(primaryStage);
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
