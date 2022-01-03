package InterrogatorComponent;

        import DataManager.BackDataManager;
        import FindPathComponent.FindPathController;
        import WhatIfComponent.WhatIfController;
        import javafx.beans.value.ObservableValue;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.paint.Color;
        import javafx.scene.paint.Paint;

        import java.io.IOException;

public class InterrogatorController {

    @FXML private Button circleButton;
    @FXML private Button pathButton;
    @FXML private Button whatIfButton;
    @FXML private AnchorPane mainSplitPane;
    @FXML private BorderPane mainBoarderPain;
    @FXML private ChoiceBox<String> skinsChoiceBox;
    @FXML private Button changeSkins;
    private BackDataManager bDM;
    private String wantedColor = new String();
    private Paint textColor;


    public void init(BackDataManager other){

        this.bDM = other;

        this.initSkinsButton();
    }

    private void initSkinsButton() {

        this.skinsChoiceBox.getItems().add("RED");
        this.skinsChoiceBox.getItems().add("BLUE");
        this.skinsChoiceBox.getItems().add("DEFAULT");


        this.skinsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    this.changeSkins.setDisable(false);
                }
        );

    }

    private void changeButtonColorTo(String value) {

        String text = "color: white;";
        if(value == "RED") {
            wantedColor = "-fx-background-color: red; ";
            this.textColor = Color.WHITE;
        }
        else if(value == "BLUE") {
            wantedColor = "-fx-background-color: blue; ";
            this.textColor = Color.YELLOW;
        }
        else {
            wantedColor = null;
        }

        this.changeButtonSkins(this.wantedColor, this.textColor);


    }

    private void changeButtonSkins(String wantedColor, Paint textColor) {

        this.changeSkins.setTextFill(textColor);
        this.circleButton.setTextFill(textColor);
        this.pathButton.setTextFill(textColor);
        this.whatIfButton.setTextFill(textColor);

        this.changeSkins.setStyle(wantedColor);
        this.circleButton.setStyle(wantedColor);
        this.pathButton.setStyle(wantedColor);
        this.whatIfButton.setStyle(wantedColor);
    }

    @FXML
    void changeSkins(ActionEvent event) {

        this.changeButtonColorTo(this.skinsChoiceBox.getValue());
    }

    @FXML
    void circleButtonAction(ActionEvent event) throws IOException {

         String circlePathName = "../FindCircleComponent/FindCircleComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent findCircleView = loader.load();
        FindCircleComponent.FindCircleController fCController = loader.getController();

        fCController.initFindCircleController(this.bDM);
        fCController.setSkins(this.wantedColor, this.textColor);

        this.mainBoarderPain.setCenter(findCircleView);
    }

    @FXML
    void pathButtonAction(ActionEvent event) throws IOException {

        String circlePathName = "../FindPathComponent/FindPathComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent findPathView = loader.load();
        FindPathController fPController = loader.getController();

        fPController.init(this.bDM);
        fPController.setSkins(this.wantedColor, this.textColor);

        this.mainBoarderPain.setCenter(findPathView);
    }

    @FXML
    void whatIfButtonAction(ActionEvent event) throws IOException {

        String circlePathName = "../WhatIfComponent/WhatIfComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent whatIfView = loader.load();
        WhatIfController wIController = loader.getController();

        wIController.init(this.bDM);
        wIController.setSkins(this.wantedColor, this.textColor);

        this.mainBoarderPain.setCenter(whatIfView);
    }
}


