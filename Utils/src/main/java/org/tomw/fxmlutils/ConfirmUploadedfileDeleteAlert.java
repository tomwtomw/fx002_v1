package org.tomw.fxmlutils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Optional;

/**
 * Ask for confirmation if the uploaded file source should be deleted
 */
public class ConfirmUploadedfileDeleteAlert {
    private final static Logger LOGGER = Logger.getLogger(ConfirmUploadedfileDeleteAlert.class.getName());
    public void askForConfirmation(File file) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Uploaded");
        alert.setHeaderText("Please confirm");
        alert.setContentText("Should I delete the source file "+file.getAbsolutePath());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            LOGGER.info("User asked to delete source file "+file.getAbsolutePath());
            file.delete();
        }
    }
}
