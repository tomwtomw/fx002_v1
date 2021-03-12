/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.utils.LocalDateUtils;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class ImageStoreBrowserController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(ImageStoreBrowserController.class.getName());

    @FXML
    Button closeButton;

    @FXML
    Button deleteButton;

    @FXML
    ImageView imageView;

    @FXML
    TableView<PictureImage> pictureImageView;
    @FXML
    private TableColumn<PictureImage, String> nameColumn;
    @FXML
    private TableColumn<PictureImage, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<PictureImage, String> checkNumberColumn;
    @FXML
    private TableColumn<PictureImage, LocalDateTime> frontBackColumn;

    private final File imageStoreDirectory = CzekiRegistry.currentAccount.getImageDirectory();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        dateColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getModificationTimeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());        

        pictureImageView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends PictureImage> observable, PictureImage oldValue, PictureImage newValue) -> {
                    showPictureImageDetails(newValue);
                });
    }

    private void showPictureImageDetails(PictureImage pictureImage) {
        LOGGER.info("PictureImage details: " + pictureImage.toString());
        Image image = new Image(pictureImage.getFile().toURI().toString());
        imageView.setImage(image);
    }

    public void initPictureImagesTable() {
        LOGGER.info("We are in initPictureImagesTable");
        if (pictureImageView == null) {
            LOGGER.error("pictureImageView is null");
        }
        pictureImageView.setItems(getListOfPictureImages());
    }

    @FXML
    public void closeButtonAction() {
        LOGGER.info("closeButtonAction");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
    }

    private List<File> getImagesInStorage() {
        List<File> result = new ArrayList<>();
        for (File file : imageStoreDirectory.listFiles()) {
            result.add(file);
        }
        return result;
    }

    private ObservableList<PictureImage> getListOfPictureImages() {
        System.out.println("imageDirectory=" + CzekiRegistry.currentAccount.getImageDirectoryString());
        ObservableList<PictureImage> pictureImagesObservableList = FXCollections.observableArrayList();
        for (File file : CzekiRegistry.currentAccount.getImageDirectory().listFiles()) {
            System.out.println("File=" + file + " " + file.lastModified() + " " + LocalDateUtils.getDateTimeFromTimestamp(file.lastModified()));
            pictureImagesObservableList.add(new PictureImage(file));
        }
        return pictureImagesObservableList;
    }
}
