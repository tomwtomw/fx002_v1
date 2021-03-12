package org.tomw.fxmlutils.buttons;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class CloseWindowButton extends Button {
    private final static Logger LOGGER = Logger.getLogger(CloseWindowButton.class.getName());

    public CloseWindowButton(String text){
        super(text);
        setFont(Font.font("System", FontWeight.BOLD,12.));

        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                LOGGER.info("Close window");
                Stage stage = (Stage) getScene().getWindow();
                stage.close();
            }
        });
    }

    public CloseWindowButton(){
        this("Close Window");
    }

    public void anchor(){
        AnchorPane.setTopAnchor(this, 1.0);
        AnchorPane.setLeftAnchor(this, 1.0);
        AnchorPane.setRightAnchor(this, 1.0);
        AnchorPane.setBottomAnchor(this, 1.0);

    }


}
