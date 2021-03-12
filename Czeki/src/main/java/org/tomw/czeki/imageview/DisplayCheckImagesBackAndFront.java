/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.Czeki;
import org.tomw.czeki.CzekiRegistry;

/**
 *
 * @author tomw
 */
public class DisplayCheckImagesBackAndFront {

    private final static Logger LOGGER = Logger.getLogger(DisplayCheckImagesBackAndFront.class.getName());
    private Stage stage;

    public void display(CheckImage checkImageFront, CheckImage checkImageBack) {
        if (checkImageFront != null || checkImageBack != null) {
            String pageFxml = CzekiRegistry.DISPLAY_IMAGE_BACK_AND_FRONT_FXML;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Czeki.class.getResource(pageFxml));
                System.out.println("location=" + loader.getLocation());

                AnchorPane page = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Check Image");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(CzekiRegistry.primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                DisplayCheckImagesBackAndFrontController controller = loader.getController();
                controller.setImages(checkImageFront, checkImageBack);
                controller.displayImages();

                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
            } catch (IOException ex) {
                LOGGER.error("Failed to load page " + pageFxml, ex);
            }
        }
    }

}
