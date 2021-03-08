package org.tomw.guiutils.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ErrorAlert {
    public boolean error(String message){
        return error("Error",null, message);
    }

    public boolean error(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }else{
            return false;
        }
    }
}
