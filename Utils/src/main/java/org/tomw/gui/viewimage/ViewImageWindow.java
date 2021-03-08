package org.tomw.gui.viewimage;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.mediautils.MediaFileUtils;

/**
 * @author tomw
 */
public class ViewImageWindow {

    private final static String className = (new Object() {
    }.getClass().getEnclosingClass()).getName();
    private final static Logger LOGGER = Logger.getLogger(className);

    private static final String VIEW_IMAGE_WINDOW_FXML = "/ViewImageWindow.fxml";

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void display(File file) {
        if (file != null) {
            if (MediaFileUtils.isImage(file)) {
                String pageFxml = VIEW_IMAGE_WINDOW_FXML;
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(ViewImageWindow.class.getResource(pageFxml));
                    LOGGER.info("location=" + loader.getLocation());

                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle(file.toString());
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(primaryStage);
                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    ViewImageWindowController controller = loader.getController();
                    controller.setFile(file);
                    controller.displayImage();

                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();
                } catch (IOException ex) {
                    LOGGER.error("Failed to load page " + pageFxml, ex);
                }
            }
        }
    }

    public void display(Image image) {
        String pageFxml = VIEW_IMAGE_WINDOW_FXML;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewImageWindow.class.getResource(pageFxml));
            LOGGER.info("location=" + loader.getLocation());

            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            //TODO set alternative way of passing title to window
            //dialogStage.setTitle(file.toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ViewImageWindowController controller = loader.getController();
            controller.setImage(image);
            controller.displayImage();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Failed to load page " + pageFxml, ex);
        }


    }
}
