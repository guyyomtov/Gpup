package main;


import DashBoardAdmin.*;
import fileHandler.TaskFile;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import uploadGraphPane.UploadPaneController;
import util.Constants;
import util.http.HttpClientUtil;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainAdminController {

    @FXML private ScrollPane welcomeView;
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;
    private Stage primaryStage;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
//        HttpClientUtil.setCookieManagerLoggingFacility(line ->
//                Platform.runLater(() ->
//                        updateHttpStatusLine(line)));
    }
    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName + " Admin")
                .build()
                .toString();



        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
                System.out.println("we failed server problem");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set(responseBody)
                    );
                    System.out.println("we failed " + response.code());
                } else {
                    Platform.runLater(() -> {
                       // switchToUploadFileView(); --> takes us to gpup part 2 home page
                        switchToDashBoard();
                        openDirectory();
                        //switchToDashBoard(); --> shouldn't be here
                    });
                    System.out.println("we success " + response.code());
                }
            }

        });
    }

    // open "c:\gpup-working-dir" directory in the beginning of the application
    private void openDirectory() {

        TaskFile taskFile = new TaskFile();

    }


    @FXML
    void quitButtonClicked(ActionEvent event) {

    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {

    }

    public void userNameKeyTyped(javafx.scene.input.KeyEvent keyEvent) {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    // shouldn't be here
    public void switchToDashBoard() {

        try {

            //this.testUploadFile();

            // This takes us to Admin dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoardAdmin/mainDashboard2Fxml.fxml"));
            Parent load = loader.load();
            MainDashboardController dashBoardController = loader.getController();
            dashBoardController.setPrimaryStage(this.primaryStage);
            dashBoardController.setActive();
            dashBoardController.setUserName(userNameTextField.getText());
            dashBoardController.initComponents();
            Scene scene = new Scene(load, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void switchToUploadFileView(){
        try {

            // This takes us to gpup part 2 home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uploadGraphPane/uploadFileView.fxml")); // todo to move path to constants!
            Parent load = loader.load();
            UploadPaneController uploadFileViewController = (UploadPaneController) loader.getController();
            Scene scene = new Scene(load, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
        }catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
