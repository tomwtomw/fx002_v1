/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.cpsummary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.Czeki;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;

/**
 *
 * @author tomw
 */
public class CounterPartySummaryWindow {

    private final static Logger LOGGER = Logger.getLogger(CounterPartySummaryWindow.class.getName());

    private ObservableList<Transaction> listOfTransactions;
    
    private Map<CounterParty,CounterPartySummary> counterPartiesSummaries = new HashMap<>();

    public boolean display() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.COUNTERPARTY_SUMMARY_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("CounterPartiesSummary");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CounterPartySummaryController controller = loader.getController();

            controller.setTransactions(listOfTransactions);
            
            controller.initCounterPartiesTable();
            //controller.diplayCounterParty(null);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
//            if (controller.getDisplayedCounterParty() == null) {
//                LOGGER.info("CP selected is null");
//            } else {
//                LOGGER.info("CP selected=" + controller.getDisplayedCounterParty().toJsonString());
//            }

            return controller.isOkClicked();
        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
            return false;
        }
    }
    
    public void setTransactions(ObservableList<Transaction> transactions) {
        listOfTransactions = transactions;
    }
}
