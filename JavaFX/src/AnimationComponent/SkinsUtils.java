package AnimationComponent;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class SkinsUtils {

    public static enum Colors{

        RED{
            @Override
            public String toString() {return "RED";}
        },
        BLUE{
            @Override
            public String toString() {return "BLUE";}
        },
        DEFAULT{
            @Override
            public String toString() {return "DEFAULT";}
        }
    }


    public static void initSkinsButton(ChoiceBox<String> skinsChoiceBox, Button changeSB) {

        skinsChoiceBox.getItems().add("RED");
        skinsChoiceBox.getItems().add("BLUE");
        skinsChoiceBox.getItems().add("DEFAULT");

        skinsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    changeSB.setDisable(false);
                }
        );
    }


    public static void changeButtonColorTo(Colors wantedColor, List<Button> buttons) {

        String buttonColor = wantedColor.toString();
        Paint textColor = Color.BLACK;

        switch (wantedColor){

            case RED:
                buttonColor = "-fx-background-color: red; ";
                textColor = Color.WHITE;
                break;
            case BLUE:
                buttonColor = "-fx-background-color: blue; ";
                textColor = Color.YELLOW;
                break;
            case DEFAULT:
                buttonColor = null;
                textColor = Color.BLACK;
                break;
        }
        changeButtonSkins(buttonColor, textColor, buttons);
    }


    private static List<Button> changeButtonSkins(String wantedColor, Paint textColor, List<Button> buttons) {

        for(Button curB : buttons){

            curB.setStyle(wantedColor);
            curB.setTextFill(textColor);
        }
        return buttons;
    }

    public static SkinsUtils.Colors makeColorsEnum(String s) {

        switch(s){

            case "RED":
                return SkinsUtils.Colors.RED;
            case "BLUE":
                return SkinsUtils.Colors.BLUE;
            default:
                return SkinsUtils.Colors.DEFAULT;
        }
    };

}
