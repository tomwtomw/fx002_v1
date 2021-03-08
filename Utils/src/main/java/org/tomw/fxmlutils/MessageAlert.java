package org.tomw.fxmlutils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Optional;

public class MessageAlert {
    private final static Logger LOGGER = Logger.getLogger(MessageAlert.class.getName());
    public void notify(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information:");
        alert.setHeaderText("INFO");
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();

    }
}
