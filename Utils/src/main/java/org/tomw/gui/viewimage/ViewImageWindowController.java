package org.tomw.gui.viewimage;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.mediautils.MediaFileUtils;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class ViewImageWindowController implements Initializable {

    private final static String className = (new Object() {
    }.getClass().getEnclosingClass()).getName();
    private final static Logger LOGGER = Logger.getLogger(className);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button closeButton;
    @FXML
    private Button rotateLeftButton;
    @FXML
    private Button rotateRightButton;
    @FXML
    private Button fitToDisplayButton;
    @FXML
    private Button saveButton;

    @FXML
    private ImageView imageView;

    private int imageViewDefaultWidth;
    private int imageViewDefaultHeight;

    private File file = null;
    private Image image = null;

    @FXML
    private Slider slider;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    public void downloadImageMenuAction() {
        LOGGER.info("downloadImageMenuAction");
    }

    @FXML
    public void closeAction() {
        LOGGER.info("closeAction");
        closeWindow();
    }

    @FXML
    public void rotateRightAction() {
        LOGGER.info("rotateRightAction");
    }

    @FXML
    public void rotateLeftAction() {
        LOGGER.info("rotateLeftAction");
    }

    @FXML
    public void saveButtonAction() {
        LOGGER.info("saveButtonAction");
    }

    @FXML
    public void sliderMovedAction() {
        LOGGER.info("slider=" + slider.getValue());
        scaleImageView(slider.getValue() / 100);
    }

    @FXML
    public void fitToDisplayButtonAction() {
        LOGGER.info("fitToDisplayButtonAction");
        fitImageToDisplay();
    }

    void setImage(Image image) {
        this.image = image;
    }

    void setFile(File file){
        this.file=file;
        if (this.file != null) {
            if (MediaFileUtils.isImage(this.file)) {
                setImage(new Image(this.file.toURI().toString()));
            } else {
                LOGGER.warn("File " + this.file + " is not an image");
            }
        } else {
            LOGGER.warn("File is null");
        }

    }

    void displayImage() {
        if (this.image != null) {
            showImage();
        } else {
            LOGGER.warn("Image is null");
        }
    }

    private void showImage() {
        if (this.image != null) {
            imageView.setImage(image);
        } else {
            LOGGER.warn("No image to display");
        }
    }

    /**
     * reset size of image view to size of the image
     */
    private void resetImageViewSize() {
        imageView.setFitHeight(imageView.getImage().getHeight());
        imageView.setFitWidth(imageView.getImage().getWidth());
    }

    private void fitImageToDisplay() {
        imageView.setFitHeight(scrollPane.getHeight());
        imageView.setFitWidth(scrollPane.getWidth());
    }

    private void scaleImageView(double scale) {
        imageView.setFitHeight(imageView.getImage().getHeight() * scale);
        imageView.setFitWidth(imageView.getImage().getWidth() * scale);
    }

    private void closeWindow() {
        LOGGER.info("close window");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
