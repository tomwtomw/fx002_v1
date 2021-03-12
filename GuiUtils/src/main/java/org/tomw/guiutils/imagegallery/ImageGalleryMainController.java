package org.tomw.guiutils.imagegallery;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageGalleryMainController implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(ImageGalleryMainController.class.getName());

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button exitButton;

    public BorderPane getBorderPane() {
        return borderPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO implement
       LOGGER.info("Imitialize controller");
    }

    @FXML
    private void exitButtonAction(){
        LOGGER.info("Exit...");
        Platform.exit();
    }
}
