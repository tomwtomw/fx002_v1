package org.tomw.guiutils.imagegallery;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import ort.tomw.guiutils.entities.imagefile.ImageFileDao;
import ort.tomw.guiutils.entities.imagefile.ImageFileList;

import java.io.IOException;

public class ImageGallery {
    private final static Logger LOGGER = Logger.getLogger(ImageGallery.class.getName());
    private static final String FXML_PAGE = "/ImageGallery.fxml";

    private AnchorPane page;
    private FXMLLoader loader = null;
    private ImageGalleryController controller = null;

    private ImageFileList imageFileList;

    public void setImageFileList(ImageFileList imageFileList) {
        this.imageFileList = imageFileList;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public ImageGalleryController getController() {
        return controller;
    }

    void init() {
        try {
            if (loader == null) {
                LOGGER.info("loader is null, load page");
                loader = new FXMLLoader();

                    LOGGER.info("Create " + FXML_PAGE + " page for the first time");
                    LOGGER.info("Load page " + FXML_PAGE);
                    loader.setLocation(ImageGallery.class.getResource(FXML_PAGE));
                    page = loader.load();
                    LOGGER.info("page=" + page);

                    controller = loader.getController();
                    controller.setImageFileList(imageFileList);
                    //controller.initTable();

            } else {
                LOGGER.info("loader is " + loader);
            }
        } catch (IOException e) {
            LOGGER.error("Error occured while loading " + FXML_PAGE + " page", e);
        }
    }
}
