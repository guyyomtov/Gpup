package AnimationComponent;

import javafx.animation.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class AnimationController {

    @FXML private BorderPane boarderPane;
    @FXML private Button animationButton1;
    @FXML private Button animationButton2;
    @FXML private Button stopAnimationButton;
    @FXML private ChoiceBox<String> cuntButton;
    private Node square;
    private Node circle;
    private Text textAno1;
    private Text textCircle;
    private StackPane stack = new StackPane();

    public void init(){

        this.textAno1 = new Text("Target: None");
        this.textCircle = new Text("CLICK ME!");

        this.initAnimation1();
        this.initCuntButton();
        this.initCircle();

        this.boarderPane.setCenter(stack);

    }

    private void initCuntButton() {

        this.cuntButton.getItems().add("A");
        this.cuntButton.getItems().add("B");

        this.cuntButton.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    this.textAno1.setText("Target: " + this.cuntButton.getValue());
                }
        );
    }

    private void initCircle(){

        this.circle = new Circle(100, Color.RED);
        circle.setTranslateZ(150);
        circle.setOpacity(0.7);
        circle.setMouseTransparent(true);

        this.makeCircleMoveByMouse();

        this.makeCircleInvisible();

        stack.getChildren().addAll(this.circle, this.textCircle);
    }

    private void makeCircleInvisible() {

        this.circle.setVisible(false);
        this.textCircle.setVisible(false);
    }

    private void makeCircleMoveByMouse(){

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.50), circle);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                circle.setLayoutX(circle.getTranslateX() + circle.getLayoutX());
                circle.setLayoutY(circle.getTranslateY() + circle.getLayoutY());
                circle.setTranslateX(0);
                circle.setTranslateY(0);
            }
        });

        this.boarderPane.setOnMousePressed(e->{
            transition.setToX(e.getSceneX() - circle.getLayoutX());
            transition.setToY(e.getSceneY() - circle.getLayoutY());
            transition.playFromStart();
            this.textCircle.setVisible(false);
        });

    }


    private void initAnimation1() {

        try {
            square = new Rectangle(100, 100, Color.FORESTGREEN);
        } catch (Exception e) {
            square = new Rectangle(512, 512, Color.FORESTGREEN);
        }

        square.setTranslateZ(150);
        square.setOpacity(0.7);
        square.setMouseTransparent(true);

        // create a rotation transform starting at 0 degrees, rotating about pivot point 50, 50.
        final Rotate rotationTransform = new Rotate(0, 50, 50);
        square.getTransforms().add(rotationTransform);

        // rotate a square using timeline attached to the rotation transform's angle property.
        final Timeline rotationAnimation = new Timeline();
        rotationAnimation.getKeyFrames()
                .add(
                        new KeyFrame(
                                Duration.seconds(5),
                                new KeyValue(
                                        rotationTransform.angleProperty(),
                                        360
                                )
                        )
                );


        this.makeSquareInvisible();

        stack.getChildren().addAll(square, this.textAno1);
        rotationAnimation.setCycleCount(Animation.INDEFINITE);
        rotationAnimation.play();
    }

    @FXML
    void animationButtonAction1(ActionEvent event) {

        this.textAno1.setVisible(true);
        square.setVisible(true);

    }

    @FXML
    void animationButtonAction2(ActionEvent event) {

        this.circle.setVisible(true);
        this.textCircle.setVisible(true);
    }

    @FXML
    void stopAnimationButtonAction(ActionEvent event) {

        this.makeSquareInvisible();

        this.makeCircleInvisible();
    }

    private void makeSquareInvisible() {

        this.textAno1.setVisible(false);
        this.square.setVisible(false);
    }
}
