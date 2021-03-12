/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 *
 * @author tomw
 */
public class AccountCrud {
    
    private final static Logger LOGGER = Logger.getLogger(AccountCrud.class.getName());
    
    
    public Account selectAccount(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.ACCOUNT_CRUD_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Accounts");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AccountCrudController controller = loader.getController();
            controller.initAccountTable();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if(controller.isOkClicked()){
                return controller.getSelectedAccount();
            }else{
                return null;
            }
           
        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
            return null;
        }
    }
    
}
