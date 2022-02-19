package FindCircleComponent;

import AnimationComponent.SkinsUtils;
import DashBoardAdmin.MainDashboardController2;
import DataManager.BackDataManager;
import Graph.Target;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.AllGraphInfo;
import transferGraphData.TargetInfo;
import util.AlertMessage;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static util.Constants.GSON_INSTANCE;

public class FindCircleController {

    @FXML private ChoiceBox<String> targetList;
    @FXML private Button findCircleButton;
    private List<Button> myButtons = new ArrayList<>();
    @FXML private TextArea resultPrintArea;
    private BackDataManager bDM;
    private AllGraphInfo allGraphInfo;
    private List<TargetInfo> targets;
    private Target curTarget;
    private String backroundColor = new String();
    private Paint textColor;


    public void initFindCircleController(AllGraphInfo allGraphInfo){

        this.allGraphInfo = allGraphInfo;

        this.myButtons = Arrays.asList(this.findCircleButton);

        this.initTargetListButton(this.allGraphInfo.getTargetInfoList());
    }

    @FXML
    void findCircleAction(ActionEvent event) throws ErrorUtils {

        String tName = targetList.getValue();
        resultPrintArea.setDisable(false);
        if(tName != null) {
            String finalUrl = HttpUrl
                    .parse(Constants.FIND_CIRCLE)
                    .newBuilder()
                    .addQueryParameter("src", tName)
                    .addQueryParameter("graphname", MainDashboardController2.currGraphName)
                    .build()
                    .toString();

            // make a request
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            //  errorMessageProperty.set("Something went wrong: " + e.getMessage())
                            System.out.println("server failed")
                    );
                    Platform.runLater(() ->
                            ErrorUtils.makeJavaFXCutomAlert("We failed, server problem")
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();

                        Platform.runLater(() ->
                                ErrorUtils.makeJavaFXCutomAlert(responseBody)
                        );
                        System.out.println("we failed " + response.code());

                    } else { // the code is 200
                        String jsonString = response.body().string();
                        String res = GSON_INSTANCE.fromJson(jsonString, String.class);
                        if(res.equals(""))
                            Platform.runLater(()-> AlertMessage.showUserSuccessAlert("The target " + tName +  " is not in circle."));
                        else
                            Platform.runLater(()-> makePrettyTheResultAndPrint(res));
                        System.out.println("we succeeded " + response.code());
                    }
                }
            });

        }
        else{
            ErrorUtils.makeJavaFXCutomAlert("Please choose source ,destination and relationship.");
        }
    }

    private void makePrettyTheResultAndPrint(String res) {
            // make string pretty
            // take comma away in the end
            res = res.replaceAll(",", "");
            // trim string
            res.trim();

            // put arrows and
            res = res.replaceAll(" ", " --> ");

            // the last target name in the end
            res += this.curTarget.getName();

            resultPrintArea.setText(res);

    }


    public void initTargetListButton(List<TargetInfo> targets){

        this.targets = targets;

        Set<String> tNames = new HashSet<>();
        targets.stream().forEach((target) -> tNames.add(target.getName()));
        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        targetList.setItems(data);
    }

    public void setSkins(String wantedColor, Paint textColor) {

        this.backroundColor = wantedColor;
        this.textColor = textColor;

        this.findCircleButton.setStyle(wantedColor);
        this.findCircleButton.setTextFill(textColor);
        this.targetList.setStyle(wantedColor);
    }

    @FXML
    void findCircleButtonMouseEnteredAction(MouseEvent event) {

     //   this.findCircleButton.setStyle(null);
      //  this.findCircleButton.setTextFill(Color.BLACK);
    }

    @FXML
    void findCircleButtonMouseExitedAction(MouseEvent event) {

      //  this.findCircleButton.setStyle(this.backroundColor);
        //this.findCircleButton.setTextFill(this.textColor);
    }

    @FXML
    void tOnMOuseEntered(MouseEvent event) {

       // this.targetList.setStyle(null);
    }

    @FXML
    void tOnMOuseExist(MouseEvent event) {

      //  this.targetList.setStyle(this.backroundColor);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        // set my buttons skins
        SkinsUtils.changeButtonColorTo(wantedColors, this.myButtons);

        // set my kids buttons skins
    }

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        //set buttons colors
        this.setButtonsColors(enumWantedColor);

        // set my label skins

        //set background colors
        this.setBackRoundColors(enumWantedColor);

        // set my kids skins
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {

    }
}
