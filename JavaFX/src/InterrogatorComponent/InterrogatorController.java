package InterrogatorComponent;

        import DataManager.BackDataManager;
        import FindPathComponent.FindPathController;
        import WhatIfComponent.WhatIfController;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.BorderPane;

        import java.io.IOException;

public class InterrogatorController {

    @FXML
    private Button circleButton;

    @FXML
    private Button pathButton;

    @FXML
    private Button whatIfButton;

    @FXML
    private AnchorPane mainSplitPane;

    @FXML
    private BorderPane mainBoarderPain;

    private BackDataManager bDM;


    public void init(BackDataManager other){
        this.bDM = other;
    }

    @FXML
    void circleButtonAction(ActionEvent event) throws IOException {

        // TODO
         String circlePathName = "../FindCircleComponent/FindCircleComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent findCircleView = loader.load();
        FindCircleComponent.FindCircleController fCController = loader.getController();
        fCController.initFindCircleController(this.bDM);

        this.mainBoarderPain.setCenter(findCircleView);
    }

    @FXML
    void pathButtonAction(ActionEvent event) throws IOException {

        //TODO
        String circlePathName = "../FindPathComponent/FindPathComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent findPathView = loader.load();
        FindPathController fPController = loader.getController();
        fPController.init(this.bDM);

        this.mainBoarderPain.setCenter(findPathView);
    }

    @FXML
    void whatIfButtonAction(ActionEvent event) throws IOException {

        // TODO
        String circlePathName = "../WhatIfComponent/WhatIfComponent.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(circlePathName));
        Parent whatIfView = loader.load();
        WhatIfController wIController = loader.getController();
        wIController.init(this.bDM);

        this.mainBoarderPain.setCenter(whatIfView);
    }
}

