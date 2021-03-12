/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.IOException;
import java.util.List;
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
public class CheckImageSelector {

    private final static Logger LOGGER = Logger.getLogger(CheckImageSelector.class.getName());

    public CheckImageSelector() {

    }

    public CheckImage  selectCheckImage() {
        String pageFxml = CzekiRegistry.CHECK_IMAGE_SELECTOR_FXML;
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(pageFxml));

            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Browse Images");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CheckImageSelectorController controller = loader.getController();
            controller.initCheckImageTable();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
            return controller.getSelectedCheckImage();
            
        } catch (IOException ex) {
            LOGGER.error("Failed to load page " + pageFxml, ex);
        }
        return null;
    }

    public CheckImage selectCheckImage(List<CheckImage> checkImageCandidates) {
        String pageFxml = CzekiRegistry.CHECK_IMAGE_SELECTOR_FXML;
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(pageFxml));

            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Browse Images");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CheckImageSelectorController controller = loader.getController();
            controller.initCheckImageTable(checkImageCandidates);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
            return controller.getSelectedCheckImage();
            
        } catch (IOException ex) {
            LOGGER.error("Failed to load page " + pageFxml, ex);
        }
        return null;
    }
}
