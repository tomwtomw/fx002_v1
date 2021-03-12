package org.tomw.fxmlutils.buttons;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.tomw.fxmlutils.constants.Fonts;

public class CommonButton extends Button {
    private final static Logger LOGGER = Logger.getLogger(CommonButton.class.getName());

    public CommonButton(String text) {
        super(text);
        setFont(Font.font(Fonts.SYSTEM, FontWeight.BOLD, Fonts.F12));
    }

    public CommonButton(String text, FontWeight fontWeight, double fontSize) {
        super(text);
        setFont(Font.font(Fonts.SYSTEM, fontWeight, fontSize));
    }
}
