package DashBoardAdmin;

import DataManager.BackDataManager;
import Graph.Graph;
import Graph.GraphManager;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class DashBoardButtonColum {


    private BackDataManager bDM = new BackDataManager();
    public static Stage primaryStage;
    private Graph curGraph;
    private GraphManager graphManager = new GraphManager();
    @FXML private Button uploadAGraphButton;
    @FXML private Button makeANewAssignmentButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();


    @FXML
    void makeANewAssignmentAction(ActionEvent event) {

    }

    @FXML
    void uploadAGraphAction(ActionEvent event) {

        //Let User Choose a file from his machine
        String absolutePath = this.letUserChooseAFile();

        //make url
        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_FILE)
                .newBuilder()
                .addQueryParameter("absolutePath", absolutePath)
                .build()
                .toString();

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );

                System.out.println("We failed, server problem");
                showUserErrorAlert("We failed, server problem");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );

                    System.out.println("we failed " + response.code());
                    showUserErrorAlert(responseBody);
                } else {
                    Platform.runLater(() -> {

                        showUserSuccsesAlert();
                    });
                    System.out.println("we succeeded " + response.code());
                }
            }
        });

    }

    private String letUserChooseAFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    private static void showUserErrorAlert(String errorType) {

        Alert alertUser = new Alert(Alert.AlertType.ERROR);
        alertUser.setHeaderText(errorType);
        alertUser.setContentText(ErrorUtils.invalidFile());
        alertUser.showAndWait();
    }


    private static void showUserSuccsesAlert() {

        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION);
        alertUser.setHeaderText("Graph uploaded successfully.");
        alertUser.showAndWait();
    }
}
