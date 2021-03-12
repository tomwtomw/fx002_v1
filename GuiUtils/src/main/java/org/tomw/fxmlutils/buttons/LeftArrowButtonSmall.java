package org.tomw.fxmlutils.buttons;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LeftArrowButtonSmall extends Button {

    public static final String ICON_FILE_NAME="/LeftArrow_small.jpg";

    public LeftArrowButtonSmall(){
        Image image = new Image(getClass().getResourceAsStream(ICON_FILE_NAME));

        setGraphic(new ImageView(image));
    }
}
