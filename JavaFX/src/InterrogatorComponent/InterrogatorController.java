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
        import javafx.scene.input.MouseEvent;
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
    private Paint textColor = Color.BLACK;
    private FindCircleComponent.FindCircleController fCController;
    private FindPathController fPController;
    private WhatIfController wIController;
    private Parent findCircleView;
    private Parent findPathView;
    private Parent whatIfView;
    private String pathName = new String();
    private FXMLLoader loader;


    public void init(BackDataManager other) throws IOException {

        this.bDM = other;

        this.initSkinsButton();

        this.initCircleComponenet();
        this.initPathComponent();
        this.WhatIfComponent();
    }


    private FXMLLoader getCurrentFXMLoader(String curPath){

        return new FXMLLoader(getClass().getResource(curPath));
    }

    private void WhatIfComponent() throws IOException {

        String curP = "../WhatIfComponent/WhatIfComponent.fxml";

        this.loader = getCurrentFXMLoader(curP);
        this.whatIfView = loader.load();
        this.wIController = loader.getController();

        this.wIController.init(this.bDM);
    }

    private void initPathComponent() throws IOException {

        String curP = "../FindPathComponent/FindPathComponent.fxml";
        this.loader = getCurrentFXMLoader(curP);
        this.findPathView = loader.load();
        this.fPController = loader.getController();

        this.fPController.init(this.bDM);
    }

    private void initCircleComponenet() throws IOException {

        String curP = "../FindCircleComponent/FindCircleComponent.fxml";
        this.loader = getCurrentFXMLoader(curP);

        this.findCircleView = loader.load();

        this.fCController = loader.getController();

        this.fCController.initFindCircleController(this.bDM);
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
            this.textColor = Color.BLACK;
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

        this.setKidComponentButtonSkins();
    }

    private void setKidComponentButtonSkins() {

        this.fCController.setSkins(this.wantedColor, this.textColor);
        this.fPController.setSkins(this.wantedColor, this.textColor);
        this.wIController.setSkins(this.wantedColor, this.textColor);
    }

    @FXML
    void changeSkins(ActionEvent event) {

        this.changeButtonColorTo(this.skinsChoiceBox.getValue());
    }

    @FXML
    void circleButtonAction(ActionEvent event) throws IOException {

        this.mainBoarderPain.setCenter(findCircleView);
    }

    @FXML
    void pathButtonAction(ActionEvent event) throws IOException {

        this.mainBoarderPain.setCenter(findPathView);
    }

    @FXML
    void whatIfButtonAction(ActionEvent event) throws IOException {

        this.mainBoarderPain.setCenter(whatIfView);
    }

    @FXML
    void whatIfOnMOuseEntered(MouseEvent event) {

        this.whatIfButton.setTextFill(Color.BLACK);
        this.whatIfButton.setStyle(null);
    }

    @FXML
    void whatIfOnMOuseExist(MouseEvent event) {

        this.whatIfButton.setStyle(this.wantedColor);
        this.whatIfButton.setTextFill(this.textColor);
    }

    @FXML
    void pathOnMOuseEntered(MouseEvent event) {

        this.pathButton.setTextFill(Color.BLACK);
        this.pathButton.setStyle(null);
    }

    @FXML
    void pathOnMOuseExist(MouseEvent event) {

        this.pathButton.setStyle(this.wantedColor);
        this.pathButton.setTextFill(this.textColor);
    }

    @FXML
    void circleOnMOuseEntered(MouseEvent event) {

        this.circleButton.setTextFill(Color.BLACK);
        this.circleButton.setStyle(null);
    }

    @FXML
    void circleOnMOuseExist(MouseEvent event) {

        this.circleButton.setStyle(this.wantedColor);
        this.circleButton.setTextFill(this.textColor);
    }

    @FXML
    void changeSkinfOnMouseEntered(MouseEvent event) {

        this.changeSkins.setTextFill(Color.BLACK);
        this.changeSkins.setStyle(null);
    }

    @FXML
    void changeSkinfOnMouseExist(MouseEvent event) {

        this.changeSkins.setTextFill(this.textColor);
        this.changeSkins.setStyle(this.wantedColor);
    }
}


