package InterrogatorComponent;

        import AnimationComponent.SkinsUtils;
        import DataManager.BackDataManager;
        import FindPathComponent.FindPathController;
        import WhatIfComponent.WhatIfController;
        import errors.ErrorUtils;
        import javafx.beans.value.ObservableValue;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.ScrollPane;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.Background;
        import javafx.scene.layout.BackgroundFill;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.paint.Color;
        import javafx.scene.paint.Paint;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class InterrogatorController {

    @FXML private Button circleButton;
    @FXML private ScrollPane scrollPane;
    @FXML private Button pathButton;
    @FXML private Button whatIfButton;
    private List<Button> buttons = new ArrayList<>();
    @FXML private AnchorPane mainSplitPane;
    @FXML private BorderPane mainBoarderPain;
    @FXML private ChoiceBox<String> skinsChoiceBox;
    private BackDataManager bDM;
    private String wantedColor = new String();
    private Paint textColor = Color.BLACK;
    private Parent findCircleView;
    private Parent findPathView;
    private Parent whatIfView;
    private String pathName = new String();
    private FXMLLoader loader;
    private FindCircleComponent.FindCircleController fCController;
    private FindPathController fPController;
    private WhatIfController wIController;


    public void init(BackDataManager other) throws IOException {

        this.bDM = other;

       // this.initSkinsButton();

        this.initCircleComponenet();
        this.initPathComponent();
        this.WhatIfComponent();

       this.buttons = Arrays.asList(this.circleButton, this.pathButton, this.whatIfButton);
    }

    public void setButtonsColors(SkinsUtils.Colors wantedColors) throws ErrorUtils {

        // set my buttons skins
        SkinsUtils.changeButtonColorTo(wantedColors, this.buttons);
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

    public void setSkins(SkinsUtils.Colors enumWantedColor) throws ErrorUtils {

        //set buttons colors
        this.setButtonsColors(enumWantedColor);

        // set my label skins

        //set background colors
        this.setBackRoundColors(enumWantedColor);

        // set my kids skins
        this.fCController.setSkins(enumWantedColor);
        this.fPController.setSkins(enumWantedColor);
        this.wIController.setSkins(enumWantedColor);
    }

    private void setBackRoundColors(SkinsUtils.Colors enumWantedColor) {

        //set background colors
        if(enumWantedColor != SkinsUtils.Colors.DEFAULT) {
            this.mainBoarderPain.setBackground(new Background(new BackgroundFill(Color.gray(0.2), null, null)));
            this.scrollPane.setBackground(new Background(new BackgroundFill(Color.gray(0.2), null, null)));
        }
        else {
            this.mainBoarderPain.setBackground(new Background(new BackgroundFill(null, null, null)));
        }
    }
}


