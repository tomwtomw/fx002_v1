package org.tomw.guiutils.documentgalery.core;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.tomw.guiutils.documentgalery.gui.DocumentGalleryMainController;
import ort.tomw.guiutils.entities.imagefile.ImageFileDao;

public class DocumentGaleryRegistry {
    public static final String APPLICATION_NAME="DocumentGalleryMain";
    public static final String MAIN_PAGE_FXML = "/DocumentGalleryMain.fxml";

    public static Stage primaryStage;
    public static BorderPane rootLayout;
    public static DocumentGalleryMainController mainPageController;

    public static ImageFileDao dao;
    public static ImageFileListService service;
}
