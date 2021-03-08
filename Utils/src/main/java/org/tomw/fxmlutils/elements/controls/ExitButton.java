package org.tomw.fxmlutils.elements.controls;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ExitButton extends Button {

    public ExitButton(){
        super("Exit");
        setFont(Font.font("System", FontWeight.BOLD,12.));
        AnchorPane.setTopAnchor(this, 1.0);
        AnchorPane.setLeftAnchor(this, 1.0);
        AnchorPane.setRightAnchor(this, 1.0);
        AnchorPane.setBottomAnchor(this, 1.0);

        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
