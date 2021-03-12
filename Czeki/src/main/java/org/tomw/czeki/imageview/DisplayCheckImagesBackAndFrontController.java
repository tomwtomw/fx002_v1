/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class DisplayCheckImagesBackAndFrontController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(DisplayCheckImagesBackAndFrontController.class.getName());

    @FXML
    private ImageView imageViewFront;
    @FXML
    private ImageView imageViewBack;

    @FXML
    private Button closeButton;

    private CheckImage checkImageFront;
    private CheckImage checkImageBack;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setImages(CheckImage checkImageFront, CheckImage checkImageBack) {
        this.checkImageFront = checkImageFront;
        this.checkImageBack = checkImageBack;
    }

    public void displayImages() {
        if (this.checkImageFront != null || this.checkImageBack != null) {
            showFrontImage();
            showBackImage();
        }
    }

    private void showFrontImage() {
        if (this.checkImageFront != null) {
            LOGGER.info("display: " + this.checkImageFront.toString());
            Image image = new Image(checkImageFront.getFile().toURI().toString());
            imageViewFront.setImage(image);
        } else {
            LOGGER.warn("No front image to display");
        }
    }

    private void showBackImage() {
        if (this.checkImageBack != null) {
            LOGGER.info("display: " + this.checkImageBack.toString());
            Image image = new Image(checkImageBack.getFile().toURI().toString());
            imageViewBack.setImage(image);
        } else {
            LOGGER.warn("No back image to display");
        }
    }

    @FXML
    private void downloadFrontImageAction() {
        LOGGER.info("downloadFrontImageAction");
    }

    @FXML
    private void downloadBackImageAction() {
        LOGGER.info("downloadBackImageAction");
    }

    @FXML
    private void downloadFrontAndBackImageAction() {
        LOGGER.info("downloadFrontAndBackImageAction");
    }

    @FXML
    private void closeButtonAction() {
        LOGGER.info("closeButtonAction");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
