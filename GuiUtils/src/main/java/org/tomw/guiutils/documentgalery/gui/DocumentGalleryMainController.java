package org.tomw.guiutils.documentgalery.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;
import org.tomw.guiutils.documentgalery.core.DocumentGaleryRegistry;
import org.tomw.imagedao.ImageDaoException;

import java.net.URL;
import java.util.ResourceBundle;

public class DocumentGalleryMainController implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(DocumentGalleryMainController.class.getName());

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
    public void exitButtonAction(){
        try {
            DocumentGaleryRegistry.dao.commit();
        } catch (ImageDaoException e) {
            LOGGER.error("Failed to commit dao "+DocumentGaleryRegistry.dao,e);
        }
        LOGGER.info("Exit...");
        Platform.exit();
    }

}
