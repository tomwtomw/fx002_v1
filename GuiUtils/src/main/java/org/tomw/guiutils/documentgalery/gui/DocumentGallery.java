package org.tomw.guiutils.documentgalery.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.guiutils.documentgalery.core.DocumentGaleryRegistry;
import org.tomw.guiutils.documentgalery.core.ImageFileListService;
import ort.tomw.guiutils.entities.imagefile.ImageFile;
import ort.tomw.guiutils.entities.imagefile.ImageFileDao;
import ort.tomw.guiutils.entities.imagefile.ImageFileList;

import java.io.IOException;

public class DocumentGallery {
    private final static Logger LOGGER = Logger.getLogger(DocumentGallery.class.getName());
    private static final String FXML_PAGE = "/DocumentGallery.fxml";

    private AnchorPane page;
    private FXMLLoader loader = null;
    private DocumentGalleryController controller = null;
    private ImageFileListService service;

    public FXMLLoader getLoader() {
        return loader;
    }

    public DocumentGalleryController getController() {
        return controller;
    }

    public ImageFileListService getService() {
        return service;
    }

    public void setService(ImageFileListService service) {
        this.service = service;
    }

    void init() {
        try {
            if (loader == null) {
                LOGGER.info("loader is null, load page");
                loader = new FXMLLoader();

                LOGGER.info("Create " + FXML_PAGE + " page for the first time");
                LOGGER.info("Load page " + FXML_PAGE);
                loader.setLocation(DocumentGallery.class.getResource(FXML_PAGE));
                page = loader.load();
                LOGGER.info("page=" + page);

                controller = loader.getController();
                //controller.setImageFileList(imageFileList);
                //controller.initTable();

            } else {
                LOGGER.info("loader is " + loader);
            }
        } catch (IOException e) {
            LOGGER.error("Error occured while loading " + FXML_PAGE + " page", e);
        }
    }


    public boolean display() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DocumentGallery.class.getResource(FXML_PAGE));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Documents");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(DocumentGaleryRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DocumentGalleryController controller = loader.getController();
            controller.setService(service);
            controller.initTable();

            //controller.setDialogStage(dialogStage);
            //controller.initCounterPartiesTable();

            //controller.diplayCounterParty(null);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if (controller.getObservableListOfDocuments() == null) {
                LOGGER.info("Document selected is null");
            } else {
                LOGGER.info("Number of documents selected=" + controller.getObservableListOfDocuments().size());
            }

            return controller.isOkClicked();
        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
            return false;
        }
    }
}
