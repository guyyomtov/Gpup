package WhatIfComponent;

import AnimationComponent.SkinsUtils;
import DashBoardAdmin.MainDashboardController;
import DataManager.BackDataManager;
import errors.ErrorUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import transferGraphData.AllGraphInfo;
import transferGraphData.TargetInfo;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;

import static util.Constants.GSON_INSTANCE;

public class WhatIfController {

    @FXML private ChoiceBox<String> targetListButton;
    @FXML private ChoiceBox<String> dependenciesType;
    @FXML private Button findButton;
    private List<Button> buttons = new ArrayList<>();
    @FXML private TableColumn<String, String> resTable;
    @FXML private TableView<String> tableView;
    private BackDataManager bDM;
    private AllGraphInfo allGraphInfo;
    private List<TargetInfo> targets;
    private Set<List<TargetInfo>> resTargets = new HashSet<>();
    private String backroundColor = new String();
    private Paint textColor;
    private List<String> resPaths = new ArrayList<>();


    public void init(AllGraphInfo allGraphInfo){

        // remove extra unneeded column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.allGraphInfo = allGraphInfo;
        this.targets = allGraphInfo.getTargetInfoList();

        this.buttons = Arrays.asList(this.findButton);

        this.initTargetListButton();
        this.initDependenciesTypeButton();
    }

    @FXML
    void findButtonAction(ActionEvent event) throws ErrorUtils {

        this.initFindButtonNeededData();

        tableView.setDisable(false);

    }

    private void convertToStringPath() {


        this.resPaths.clear();
        for(List<TargetInfo> targets : this.resTargets ) {
            String path = new String();
            if(dependenciesType.getValue().equals("Depends On")) {
                for (int i = 0; i < targets.size(); i++) {
                    path += targets.get(i).getName();
                    if (i != targets.size() - 1)
                        path += "-->";
                }
            }

            else{ // print it as required for should be
                for (int i = targets.size() - 1; i >=0 ; i--) {
                    path += targets.get(i).getName();
                    if (i != 0)
                        path += "-->";
                }
            }
            this.resPaths.add(path);
        }

    }

    private void initResTable() {

        ObservableList<String> dataL = FXCollections.observableArrayList(this.resPaths);

        this.resTable.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        tableView.setItems(dataL);
    }



    private void initFindButtonNeededData() throws ErrorUtils {

        if(dependenciesType.getValue() != null && !dependenciesType.getValue().isEmpty() && targetListButton.getValue() != null && !targetListButton.getValue().isEmpty()) {
            String srcV = targetListButton.getValue();
            String relationshipType = dependenciesType.getValue() == "Depends On" ? "D" : "R";
            String finalUrl = HttpUrl
                    .parse(Constants.WHAT_IF_REQUEST)
                    .newBuilder()
                    .addQueryParameter("src", srcV)
                    .addQueryParameter("relationship",relationshipType)
                    .addQueryParameter("graphname", MainDashboardController.currGraphName)
                    .build()
                    .toString();

            // make a request
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
                        TargetInfo[][] res = GSON_INSTANCE.fromJson(jsonString, TargetInfo[][].class);
                        convertResToCollection(res);
                        convertToStringPath();
                        Platform.runLater(()-> initResTable() );
                        System.out.println("we succeeded " + response.code());
                    }
                }
            });

        }
        else{
            ErrorUtils.makeJavaFXCutomAlert("Please choose source ,destination and relationship.");
        }
            //this.resTargets = this.bDM.whatIf(targetListButton.getValue(), relationshipType);

    }

    private void convertResToCollection(TargetInfo[][] res) {
        this.resTargets.clear();
        for(TargetInfo[] arr : res){
            this.resTargets.add(Arrays.asList(arr));
        }
    }


    private void initDependenciesTypeButton() {

        List<String> relations = Arrays.asList("Depends On", "Required For");

        ObservableList<String> data = FXCollections.observableArrayList(relations);

        dependenciesType.setItems(data);
    }

    private void initTargetListButton() {

        Set<String> tNames = new HashSet<>();
        this.targets.stream().forEach((targetInfo) -> tNames.add(targetInfo.getName()));

        ObservableList<String> data = FXCollections.observableArrayList(tNames);

        targetListButton.setItems(data);
    }

    public void setSkins(String wantedColor, Paint textColor) {

        this.backroundColor = wantedColor;
        this.textColor = textColor;

        this.dependenciesType.setStyle(wantedColor);
        this.targetListButton.setStyle(wantedColor);
        this.findButton.setStyle(wantedColor);
        this.findButton.setTextFill(textColor);
    }


    @FXML
    void findOnMOuseEntered(MouseEvent event) {

      //  this.findButton.setTextFill(Color.BLACK);
      //  this.findButton.setStyle(null);
    }

    @FXML
    void findOnMOuseExist(MouseEvent event) {

      //  this.findButton.setStyle(this.backroundColor);
      //  this.findButton.setTextFill(this.textColor);
    }

    @FXML
    void relOnMOuseEntered(MouseEvent event) {

       // this.dependenciesType.setStyle(null);
    }

    @FXML
    void relOnMOuseExist(MouseEvent event) {

       //  this.dependenciesType.setStyle(this.backroundColor);
    }

    @FXML
    void tOnMOuseEntered(MouseEvent event) {

//        this.targetListButton.setStyle(null);
    }
    @FXML
    void tOnMOuseExist(MouseEvent event) {

      //  this.targetListButton.setStyle(this.backroundColor);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        SkinsUtils.changeButtonColorTo(wantedColors, this.buttons);
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
