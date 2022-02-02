package AnimationComponent;

import errors.ErrorUtils;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
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

    public static void changeLabelsTextColorTo(Colors wantedColor, List<Label> buttons) throws ErrorUtils {

        Paint textColor = Color.BLACK;

        switch (wantedColor){

            case RED:
                textColor = Color.WHITE;
                break;
            case BLUE:
                textColor = Color.YELLOW;
                break;
            case DEFAULT:
                textColor = Color.BLACK;
                break;
        }
        changeLabelSkins(textColor, buttons);
    }

    private static void changeLabelSkins(Paint textColor, List<Label> buttons) throws ErrorUtils {

        for(Label curB : buttons){

            if(curB == null)
                throw new ErrorUtils(ErrorUtils.BUTTON_IS_NULL);
            curB.setTextFill(textColor);
        }
    }


    public static void changeButtonColorTo(Colors wantedColor, List<Button> buttons) throws ErrorUtils {

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

    public static void changeCheckBoxButtonTextColorTo(Colors wantedColor, List<CheckBox> buttons) throws ErrorUtils {

        String buttonColor = wantedColor.toString();
        Paint textColor = Color.BLACK;

        switch (wantedColor){

            case RED:
                textColor = Color.WHITE;
                break;
            case BLUE:
                textColor = Color.YELLOW;
                break;
            case DEFAULT:
                textColor = Color.BLACK;
                break;
        }
        changeCheckBoxButtonSkins(buttonColor, textColor, buttons);
    }

    private static List<CheckBox> changeCheckBoxButtonSkins(String wantedColor, Paint textColor, List<CheckBox> buttons) throws ErrorUtils {

        for(CheckBox curB : buttons){

            if(curB == null)
                throw new ErrorUtils(ErrorUtils.BUTTON_IS_NULL);

            curB.setTextFill(textColor);
        }
        return buttons;
    }

    public static void changeRadioButtonTextColorTo(Colors wantedColor, List<RadioButton> buttons) throws ErrorUtils {

        String buttonColor = wantedColor.toString();
        Paint textColor = Color.BLACK;

        switch (wantedColor){

            case RED:
                textColor = Color.WHITE;
                break;
            case BLUE:
                textColor = Color.YELLOW;
                break;
            case DEFAULT:
                textColor = Color.BLACK;
                break;
        }
        changeRadioButtonSkins(buttonColor, textColor, buttons);
    }

    private static List<RadioButton> changeRadioButtonSkins(String wantedColor, Paint textColor, List<RadioButton> buttons) throws ErrorUtils {

        for(RadioButton curB : buttons){

            if(curB == null)
                throw new ErrorUtils(ErrorUtils.BUTTON_IS_NULL);

            curB.setTextFill(textColor);
        }
        return buttons;
    }


    private static List<Button> changeButtonSkins(String wantedColor, Paint textColor, List<Button> buttons) throws ErrorUtils {

        for(Button curB : buttons){

            if(curB == null)
                throw new ErrorUtils(ErrorUtils.BUTTON_IS_NULL);

            curB.setStyle(wantedColor);
            curB.setTextFill(textColor);
        }
        return buttons;
    }

    public static Colors makeColorsEnum(String s) {

        switch(s){

            case "RED":
                return Colors.RED;
            case "BLUE":
                return Colors.BLUE;
            default:
                return Colors.DEFAULT;
        }
    };

}
