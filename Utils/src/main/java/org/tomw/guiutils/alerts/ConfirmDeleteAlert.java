package org.tomw.guiutils.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmDeleteAlert {

    public boolean deleteConfirmed(String objectName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Should I delete the following object?");
        alert.setHeaderText(objectName);
        alert.setContentText("Please confim Y/N");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }else{
            return false;
        }
    }
}
