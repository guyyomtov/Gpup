package DashBoardAdmin;

import DashBoardAdmin.GraphsInfoTableComponent.GraphInfoTableController;
import DashBoardAdmin.TaskInfoTableComponent.TaskInfoTableController;
import DashBoardAdmin.onlineAdminsComponent.OnlineAdminsController;
import InterrogatorComponent.InterrogatorController;
import api.HttpStatusUpdate;
import errors.ErrorUtils;
import graphInfoView.GraphInfoController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import leftSideMenu.LeftSideController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import taskView.TaskController;
import transferGraphData.AllGraphInfo;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static util.Constants.GSON_INSTANCE;

public class MainDashboardController2 implements Closeable, HttpStatusUpdate {


    private Stage primaryStage;
    @FXML public Button interrogatorButton;
    @FXML private BorderPane MainBorderPane;
    @FXML private Button uploadFileButton;
    @FXML private Button newAssignmentButton;
    @FXML private MenuItem dashboardButton;
    @FXML private Parent graphInfoTable;
    @FXML private GraphInfoTableController graphInfoTableController;
    @FXML private Parent taskInfoTable;
    @FXML private TaskInfoTableController taskInfoTableController;
    @FXML private Parent onlineAdmin;
    @FXML private OnlineAdminsController onlineAdminController;

    public static String currGraphName;

    private String userName;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    private LeftSideController leftSideController;
    private Parent leftSideMenuForGraphView;

    private Parent leftSideForDashBoard;
    private Parent centerDashBoard;

    private GraphInfoController graphInfoController;
    private Parent graphInfoView;

    private InterrogatorController interrogatorController;
    private Parent interrogatorView;

    private TaskController taskController;
    private Parent taskView;

    @FXML void uploadFileAction(ActionEvent event) {
        //Let User Choose a file from his machine
        String absolutePath = this.letUserChooseAFile();

        //make url
        String finalUrl = HttpUrl
                .parse(Constants.UPLOAD_FILE)
                .newBuilder()
                .addQueryParameter("absolutePath", absolutePath)
                .addQueryParameter("username", userName)
                .build()
                .toString();

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())

                );
                Platform.runLater(() ->
                        showUserErrorAlert("We failed, server problem")
                );
                System.out.println("We failed, server problem");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );

                    Platform.runLater(() ->
                            showUserErrorAlert(responseBody)
                    );

                    System.out.println("we failed " + response.code());

                } else {
                    Platform.runLater(() -> {
                        showUserSuccsesAlert();
                    });
                    System.out.println("we succeeded " + response.code());
                }
            }
        });
    }

    @FXML
    void blueSkinAction(ActionEvent event) {

    }

    @FXML
    void defualtSkinAction(ActionEvent event) {

    }

    @FXML
    void newAssignmentButtonAction(ActionEvent event) {

        this.MainBorderPane.setCenter(this.taskView);

    }

    @FXML
    void redSkinAction(ActionEvent event) {

    }

    public void setActive() {
        this.onlineAdminController.setHttpStatusUpdate(this);
        this.onlineAdminController.startListRefresher();

        this.graphInfoTableController.setHttpStatusUpdate(this);
        graphInfoTableController.startGraphInfoRefresher();

        this.graphInfoTableController.initTable();
    }

    private String letUserChooseAFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    public static void showUserErrorAlert(String errorType) {

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

    @Override
    public void updateHttpLine(String line) {

    }

    @Override
    public void close() throws IOException {
        this.onlineAdminController.close(); // todo to looking for more thing like that to implement them
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void dashboardButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void interrogatorAction(ActionEvent actionEvent) {
        //make url
        currGraphName = this.getGraphName(); // todo there is a comment in this function fuck the toggle!
        String finalUrl = HttpUrl
                .parse(Constants.GRAPHS_VIEW)
                .newBuilder()
                .addQueryParameter("graphname", currGraphName)
                .build()
                .toString();

        // make a request
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())

                );
                Platform.runLater(() ->
                        showUserErrorAlert("We failed, server problem")
               );
                System.out.println("We failed, server problem");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );

                    Platform.runLater(() ->
                            showUserErrorAlert("Please chose graph!")
                    );

                    System.out.println( response.code());

                } else {
                    String jsonArrayOfAllGraphInfo = response.body().string();
                    AllGraphInfo allGraphInfo = GSON_INSTANCE.fromJson(jsonArrayOfAllGraphInfo, AllGraphInfo.class);
                    Platform.runLater(() -> {
                        updateGraphInfoToComponents(allGraphInfo);
                    });

                }
            }
        });


    }

    private String getGraphName() {
        try {
            String graphName = this.graphInfoTableController.getNameOfTheSelectedGraph();
            return graphName;
        }catch (ErrorUtils e){ErrorUtils.makeJavaFXCutomAlert(e.getMessage());}
        return "";
    }

    private void updateGraphInfoToComponents(AllGraphInfo allGraphInfo) {
        this.leftSideController.setAllGraphInfo(allGraphInfo);
        this.leftSideController.swapToGraphViewComponents();

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void initComponents() {

        this.initLeftSide();
        this.initNewAssignment();
    }

    private void initNewAssignment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskView/taskViewFxml.fxml"));
            this.taskView = loader.load();
            this.taskController = loader.getController();
//            this.taskController.setbDM(this.bDM);
//            this.taskController.initTaskView();   todo to change everything there to allGraphInfo object!
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void initLeftSide() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/leftSideMenu/leftSideMenuFxml.fxml"));
            this.leftSideMenuForGraphView = loader.load();
            this.leftSideController = loader.getController();
            this.leftSideController.initComponents();
            this.leftSideController.setMainBorderPane(this.MainBorderPane);
            this.leftSideController.setLeftSideMenuForGraphView(this.leftSideMenuForGraphView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraphInfoTableController getGraphInfoTableController(){return this.graphInfoTableController;}
}
