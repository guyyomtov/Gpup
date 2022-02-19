package leftSideMenu;

import DashBoardAdmin.MainDashboardController2;
import InterrogatorComponent.InterrogatorController;
import graphInfoView.GraphInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import transferGraphData.AllGraphInfo;

import java.io.IOException;

public class LeftSideController {

    @FXML private Button dashboardButton;
    @FXML private Button graphInfoButton;
    @FXML private Button InterrogatorButton;
    @FXML private Button taskButton;
    private AllGraphInfo allGraphInfo;

    private BorderPane mainBorderPane;
    private Node centerDashboard;
    private Node leftSideDashboard;
    private MainDashboardController2 mainDashboardController;

    private GraphInfoController graphInfoController;
    private Node graphInfoView;
    private Node leftSideMenuForGraphView;

    private InterrogatorController interrogatorController;
    private Node interrogatorView;


    @FXML
    void InterrogatorButtonAction(ActionEvent event) {
        this.mainBorderPane.setCenter(this.interrogatorView);
    }

    @FXML
    void dashboardButtonAction(ActionEvent event) {
        this.mainBorderPane.setCenter(this.centerDashboard);
        this.mainBorderPane.setLeft(this.leftSideDashboard);
    }

    @FXML
    void graphInfoAction(ActionEvent event) {
        this.mainBorderPane.setCenter(this.graphInfoView);
    }

    @FXML
    void taskButtonAction(ActionEvent event) {

    }

    public void initComponents() {
        this.initGraphInfoComponent();
        this.initInterrogatorComponent();
    }

    public void initGraphInfoComponent(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphInfoView/graphInfoFxml.fxml"));
            this.graphInfoView = loader.load();
            this.graphInfoController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initInterrogatorComponent(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterrogatorComponent/InteragatorComponent.fxml"));
            this.interrogatorView = loader.load();
            this.interrogatorController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getMainBorderPane() {
        return mainBorderPane;
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
        this.centerDashboard = mainBorderPane.getCenter();
        this.leftSideDashboard =mainBorderPane.getLeft();

    }

    public void setAllGraphInfo(AllGraphInfo allGraphInfo) {
        this.allGraphInfo = allGraphInfo;
    }

    public AllGraphInfo getAllGraphInfo() {
        return allGraphInfo;
    }

    public void swapToGraphViewComponents() {
        try {
            this.graphInfoController.setAllGraphInfo(this.allGraphInfo);
            this.graphInfoController.initGraphInfo();
            this.interrogatorController.init(this.allGraphInfo);

            this.mainBorderPane.setCenter(this.graphInfoView);
            this.mainBorderPane.setLeft(this.leftSideMenuForGraphView);
        }catch (Exception e){
            System.out.println("init graph info failed.");
        }
    }

    public Node getLeftSideMenuForGraphView() {
        return leftSideMenuForGraphView;
    }

    public void setLeftSideMenuForGraphView(Node leftSideMenuForGraphView) {
        this.leftSideMenuForGraphView = leftSideMenuForGraphView;
    }
}
