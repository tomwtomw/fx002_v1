/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.counterparties;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.Czeki;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.mergecounterparties.MergeCounterPartiesController;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.CounterpartiesNameComparator;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class CounterpartiesOverviewController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(CounterpartiesOverviewController.class.getName());

    @FXML
    private TableView<CounterParty> counterpartiesTable;
    @FXML
    private TableColumn<CounterParty, String> counterpartyShortNameColumn;
    @FXML
    private TableColumn<CounterParty, String> counterpartyFullNameColumn;
    @FXML
    private TableColumn<CounterParty, String> counterpartyCommentColumn;

    private final ObservableList<CounterParty> counterpartiesData = FXCollections.observableArrayList();

    private CounterParty displayedCounterParty = null;

    @FXML
    private Label shortNameLabel;
    @FXML
    private TextField shortNameField;
    @FXML
    private Label shortNameErrorLabel;

    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label commentLabel;
    @FXML
    private TextField commentField;
    @FXML
    private Label commentErrorLabel;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    private boolean okButtonClicked = false;

    public CounterParty getDisplayedCounterParty() {
        return displayedCounterParty;
    }

    private void resetDisplayedCounterParty() {
        this.displayedCounterParty = null;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        counterpartyShortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyFullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyCommentColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        // TODO
        counterpartyShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty());
        counterpartyFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        counterpartyCommentColumn.setCellValueFactory(cellData -> cellData.getValue().getCommentProperty());

        counterpartiesTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends CounterParty> observable, CounterParty oldValue, CounterParty newValue) -> {
                    showCounterPartyDetails(newValue);
                });

        resetErrorMessages();
        this.okButtonClicked = false;
    }

    @FXML
    private void newCounterPartyButton() {
        LOGGER.info("newCounterPartyButton");
        resetDisplayedCounterParty();
        resetSelectedCounterPartyDisplay();
    }

    @FXML
    private void deleteCounterPartyButton() {
        LOGGER.info("deleteCounterPartyButton - not implemented yet");
        //TODO implement this button
    }

    @FXML
    private void clearCounterPartyButton() {
        LOGGER.info("clearCounterPartyButton");
        resetDisplayedCounterParty();
        resetSelectedCounterPartyDisplay();
    }

    @FXML
    private void acceptCounterPartyButton() {
        LOGGER.info("acceptCounterPartyButton");
        this.unpackCounterpartyDisplay();
        this.insertOrUpdateDisplayedCounterParty();
        CzekiRegistry.currentAccount.getDao().add(displayedCounterParty);
        refreshCounterPartiesTable();
    }

    @FXML
    private void mergeCounterPartiesButton() {
        LOGGER.info("mergeCounterPartiesButton");
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.MERGE_COUNTERPARTIES_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Merge Counterparties");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MergeCounterPartiesController controller = loader.getController();
            //controller.setDialogStage(dialogStage);
            controller.initCounterPartiesTables();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (controller.isAtLeastOneMergeExecuted()) {
                reloadCounterPartiesTable();
            }

            refreshCounterPartiesTable();

        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
        }
    }

    @FXML
    private void okButton() {
        LOGGER.info("okButton=" + okButton);
        this.okButtonClicked = true;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButton() {
        LOGGER.info("cancelButton");
        this.okButtonClicked = false;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void initCounterPartiesTable() {
        LOGGER.info("We are in initCounterPartiesTable");
        if (counterpartiesTable == null) {
            LOGGER.error("counterpartiesTable is null");
        }
        LOGGER.info("number of cp=" + this.counterpartiesData.size());

        reloadCounterPartiesTable();
    }

    /**
     * fill counterparties table with data from dao
     */
    private void reloadCounterPartiesTable() {
        initCounterPartiesData(CzekiRegistry.currentAccount.getDao().getCounterParties().values());
        counterpartiesTable.setItems(counterpartiesData);
    }

    private void showCounterPartyDetails(CounterParty counterParty) {
        LOGGER.info("CounterParty details: " + counterParty.toJsonString());
        this.diplayCounterParty(counterParty);
    }

    public void diplayCounterParty(CounterParty counterParty) {
        this.displayedCounterParty = counterParty;
        if (this.displayedCounterParty != null) {

            this.shortNameField.setText(displayedCounterParty.getShortName());
            this.nameField.setText(displayedCounterParty.getName());
            this.commentField.setText(displayedCounterParty.getComment());
        } else {
            resetSelectedCounterPartyDisplay();
        }

    }

    public boolean isOkClicked() {
        return this.okButtonClicked;
    }

    private void resetErrorMessages() {
        shortNameErrorLabel.setText(CzekiRegistry.BLANK);
        nameErrorLabel.setText(CzekiRegistry.BLANK);
        commentErrorLabel.setText(CzekiRegistry.BLANK);
    }

    private void initCounterPartiesData(Collection<CounterParty> values) {
        List<CounterParty>listOfCounterparties = new ArrayList<>(values);
        Collections.sort(listOfCounterparties, new CounterpartiesNameComparator());
        counterpartiesData.clear();
        for (CounterParty cp : listOfCounterparties) {
            counterpartiesData.add(cp);
        }
    }

    private void resetSelectedCounterPartyDisplay() {
        this.shortNameField.setText(CzekiRegistry.BLANK);
        this.nameField.setText(CzekiRegistry.BLANK);
        this.commentField.setText(CzekiRegistry.BLANK);
        resetErrorMessages();
    }

    private void unpackCounterpartyDisplay() {
        if (this.displayedCounterParty == null) {
            this.displayedCounterParty = new CounterParty();
        }
        displayedCounterParty.setName(this.nameField.getText());
        displayedCounterParty.setShortNameProperty(this.shortNameField.getText());
        displayedCounterParty.setComment(this.commentField.getText());
    }

    private void insertOrUpdateDisplayedCounterParty() {
        if (!this.counterPartyIsDisplayed(this.displayedCounterParty)) {
            this.counterpartiesData.add(this.displayedCounterParty);
        }
    }

    private boolean counterPartyIsDisplayed(CounterParty cp) {
        String id = cp.getId();
        for (CounterParty cpIter : this.counterpartiesData) {
            if (id.equals(cpIter.getId())) {
                return true;
            }
        }
        return false;
    }

    private void refreshCounterPartiesTable() {
        counterpartiesTable.getColumns().get(0).setVisible(false);
        counterpartiesTable.getColumns().get(0).setVisible(true);
    }

}
