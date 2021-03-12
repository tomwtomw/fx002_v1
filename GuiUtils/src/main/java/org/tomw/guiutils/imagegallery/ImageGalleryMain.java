package org.tomw.guiutils.imagegallery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.fileutils.TomwFileUtils;
import ort.tomw.guiutils.entities.imagefile.*;

import java.io.File;
import java.io.IOException;

public class ImageGalleryMain extends Application {
    private final static Logger LOGGER = Logger.getLogger(ImageGalleryMain.class.getName());

    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageGalleryRegistry.primaryStage = primaryStage;

        initRootLayout();

        initImagegallery();
    }

    private void initImagegallery() {
        File daoDirectory = new File(TomwFileUtils.getApplicationDirectory(),"imageGallerydaoDirectory");
        File daoFile = new File(daoDirectory,"daoFile.json");
        ImageFileDao dao = new ImageFileDaoJsonImpl(daoFile,daoDirectory);

        ImageFileList imageFileList = new ImageFileListImpl(dao,dao.getAll());

        ImageGallery imageGallery = new ImageGallery();
        imageGallery.setImageFileList(imageFileList);
        imageGallery.init();
        ImageGalleryRegistry.mainPageController.getBorderPane().setCenter(imageGallery.getController().getAnchroPane());
    }

    public static void main(String[] args) {
        LOGGER.info(ImageGalleryRegistry.APPLICATION_NAME+" starts...");

        launch(args);
    }
    private void initRootLayout() {
        BorderPane rootLayout;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ImageGalleryRegistry.class.getResource(ImageGalleryRegistry.MAIN_PAGE_FXML));
            rootLayout =  loader.load();

            ImageGalleryRegistry.rootLayout = rootLayout;

            ImageGalleryRegistry.mainPageController = loader.getController();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            ImageGalleryRegistry.primaryStage.setScene(scene);

            ImageGalleryRegistry.primaryStage.show();
        } catch (IOException e) {
            LOGGER.error("Error occured when initializing root layout ", e);
        }
    }
}
