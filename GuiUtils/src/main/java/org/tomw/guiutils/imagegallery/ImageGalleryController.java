package org.tomw.guiutils.imagegallery;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import ort.tomw.guiutils.entities.imagefile.ImageFileList;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImageGalleryController implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(ImageGalleryMainController.class.getName());

    private ImageFileList imageFileList;

    @FXML
    private AnchorPane anchroPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Button uploadButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button rotateLeftButton;
    @FXML
    private Button rotateRightButton;
    @FXML
    private Button rightButton;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;


    public AnchorPane getAnchroPane() {
        return anchroPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Imitialize controller");
    }

    @FXML
    private void uploadButtonAction() {
        LOGGER.info("uploadButtonAction");
        FileChooser imageFileChooser = new FileChooser();
        List<File> selectedFiles = imageFileChooser.showOpenMultipleDialog(ImageGalleryRegistry.primaryStage);
        if(selectedFiles!=null){
            for(File file : selectedFiles){
                LOGGER.info(file);
                imageFileList.upload(file);
            }
            displayCurrent();
        }
    }

    @FXML
    private void leftButtonAction() {
        LOGGER.info("leftButtonAction");
        if (imageFileList != null) {
            imageFileList.moveLeft();
            displayCurrent();
        }
    }

    @FXML
    private void viewButtonAction() {
        LOGGER.info("viewButtonAction");
    }

    @FXML
    private void downloadButtonAction() {
        LOGGER.info("downloadButtonAction");
    }

    @FXML
    private void deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
    }

    @FXML
    private void rotateLeftButtonAction() {
        LOGGER.info("rotateLeftButtonAction");
    }

    @FXML
    private void rotateRightButtonAction() {
        LOGGER.info("rotateRightButtonAction");
    }

    @FXML
    private void rightButtonAction() {
        LOGGER.info("rightButtonAction");
        if (imageFileList != null) {
            imageFileList.moveRight();
            displayCurrent();
        }
    }

    @FXML
    private void okButtonAction() {
        LOGGER.info("okButtonAction");
        imageFileList.commit();
    }

    @FXML
    private void cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
    }

    public void setImageFileList(ImageFileList imageFileList) {
        this.imageFileList = imageFileList;
        displayCurrent();
    }

    public void displayCurrent() {
        enableArrows();
        this.imageView.setImage(
                new Image(imageFileList.getCurrent().getFile().toURI().toString()));
    }

    public void enableArrows(){
        if(imageFileList.canMoveLeft()){
            leftButton.setDisable(false);
        }else{
            leftButton.setDisable(true);
        }
        if(imageFileList.canMoveRight()){
            rightButton.setDisable(false);
        }else{
            rightButton.setDisable(true);
        }
    }
}
