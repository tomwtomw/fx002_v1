/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.czekiimagedisplay;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class CzekiImageDisplayController implements Initializable {
    
    private final static Logger LOGGER = Logger.getLogger(CzekiImageDisplayController.class.getName());
    
    private Stage dialogStage;
    
    @FXML
    private Label topLabel;
    
    @FXML
    private Button okButton;
    
    private boolean okButtonPressed;
    
    @FXML
    ImageView imageView;
    
    private Image image;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setText(String inputText) {
        topLabel.setText(inputText);
    }

    public void setImage(File imageFile) {
        image = new Image(imageFile.toString());
        imageView.setImage(image);
    }
    
    public void setImage(String imageFileName) {
        image = new Image(imageFileName);
        imageView.setImage(image);
    }
    
    public void setImage(Image inputImage) {
        image = inputImage;
        imageView.setImage(image);
    }
    
    public boolean okButtonHasBeenPressed(){
        return okButtonPressed;
    }
    
    @FXML
    public void handleOKButton(){
        LOGGER.info("handleOKButton");
        okButtonPressed=true;        
        dialogStage.close();
    }
    
}
