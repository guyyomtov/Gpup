package Login;

import DashBoard.WorkerDashBoardController;
import Utils.Constants;
import Utils.ErrorHandling.ErrorHandling;
import Utils.http.HttpClientUtil;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static Utils.Constants.DASHBOARD_FXML;

/* This class is responsible for the login logic & the actual login of the worker to the app*/

public class LoginPageController {


    @FXML private ScrollPane welcomeView;
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;
    @FXML private Spinner<Integer> threadSpinnerButton = new Spinner<Integer>();;
    private Stage primaryStage;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();


    @FXML
    public void initialize() {

        this.initSpinButtonValues();

        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    private void initSpinButtonValues() {

        // Value factory.
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);

        this.threadSpinnerButton.setValueFactory(valueFactory);
    }

    @FXML
    void loginButtonClicked(ActionEvent event) throws ErrorUtils {

        String userName = userNameTextField.getText();

        //Check that username was given
        if (userName.isEmpty()) {

            ErrorHandling.showUserErrorAlert("User name is empty.");
            return;
        }

        //create url for servlet
        String finalUrl = this.makeUrl(Constants.LOGIN_PAGE, "username", userName + " Worker");

        // make request to server & move to dashboard
        this.makeRequestToLoginToDashBoard(finalUrl);
    }

    private void makeRequestToLoginToDashBoard(String finalUrl) throws ErrorUtils {

        if(finalUrl == null || finalUrl.isEmpty())
            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            switchToDashBoard();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void switchToDashBoard() throws IOException {

        // This takes us to worker dashboard
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DASHBOARD_FXML));
        Parent load = loader.load();
        WorkerDashBoardController dashBoardController= (WorkerDashBoardController) loader.getController();
        dashBoardController.setPrimaryStage(this.primaryStage);
        Scene scene = new Scene(load, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
    }

    private String makeUrl(String urlPath, String queryParameter1Name, String queryParameter1Value) throws ErrorUtils {

        if(urlPath == null || urlPath.isEmpty()) {

            ErrorHandling.showUserErrorAlert("Invalid Url");

            throw new ErrorUtils(ErrorUtils.NEEDED_DATA_IS_NULL);
        }

        String finalUrl = HttpUrl
                .parse(urlPath)
                .newBuilder()
                .addQueryParameter(queryParameter1Name, queryParameter1Value)
                .build()
                .toString();

        return finalUrl;
    }

    @FXML
    void quitButtonClicked(ActionEvent event) {

    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {

    }

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage;}
}
