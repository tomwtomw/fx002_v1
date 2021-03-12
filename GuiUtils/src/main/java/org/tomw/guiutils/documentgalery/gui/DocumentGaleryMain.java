package org.tomw.guiutils.documentgalery.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.guiutils.documentgalery.core.DocumentGaleryRegistry;
import org.tomw.guiutils.documentgalery.core.ImageFileListService;
import org.tomw.guiutils.documentgalery.core.ImageFileListServiceImpl;
import ort.tomw.guiutils.entities.imagefile.ImageFile;
import ort.tomw.guiutils.entities.imagefile.ImageFileDao;
import ort.tomw.guiutils.entities.imagefile.ImageFileDaoJsonImpl;

import java.io.File;
import java.io.IOException;

public class DocumentGaleryMain extends Application {
    private final static Logger LOGGER = Logger.getLogger(DocumentGaleryMain.class.getName());

    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        DocumentGaleryRegistry.primaryStage = primaryStage;

        initRootLayout();

        initDocumentGallery();
    }

    private void initDocumentGallery() {
        File daoDirectory = new File(TomwFileUtils.getApplicationDirectory(),"documentGallerydaoDirectory");
        File daoFile = new File(daoDirectory,"daoFile.json");
        ImageFileDao dao = new ImageFileDaoJsonImpl(daoFile,daoDirectory);

        DocumentGaleryRegistry.dao = dao;

        DocumentGallery documentGallery = new DocumentGallery();
        ObservableList<ImageFile> imageFiles = FXCollections.observableArrayList();
        imageFiles.addAll(dao.getAll());
        ImageFileListService service = new ImageFileListServiceImpl(dao,imageFiles);
        DocumentGaleryRegistry.service = service;

        documentGallery.setService(service);

        documentGallery.display();
    }

    public static void main(String[] args) {
        LOGGER.info(DocumentGaleryRegistry.APPLICATION_NAME+" starts...");

        launch(args);
    }
    private void initRootLayout() {
        BorderPane rootLayout;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DocumentGaleryRegistry.class.getResource(DocumentGaleryRegistry.MAIN_PAGE_FXML));
            rootLayout =  loader.load();

            DocumentGaleryRegistry.rootLayout = rootLayout;

            DocumentGaleryRegistry.mainPageController = loader.getController();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            DocumentGaleryRegistry.primaryStage.setScene(scene);

            DocumentGaleryRegistry.primaryStage.show();
        } catch (IOException e) {
            LOGGER.error("Error occured when initializing root layout ", e);
        }
    }
}
