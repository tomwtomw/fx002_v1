/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.mergecounterparties;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.entities.CounterParty;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class MergeCounterPartiesController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(MergeCounterPartiesController.class.getName());

    @FXML
    private TableView<CounterParty> counterpartiesTableLeft;
    @FXML
    private TableColumn<CounterParty, String> counterpartyShortNameColumnLeft;
    @FXML
    private TableColumn<CounterParty, String> counterpartyFullNameColumnLeft;
    @FXML
    private TableColumn<CounterParty, String> counterpartyCommentColumnLeft;

    @FXML
    private Label shortNameLabelLeft;
    @FXML
    private Label fullNameLabelLeft;
    @FXML
    private Label commentLabelLeft;

    private final ObservableList<CounterParty> counterpartiesDataLeft = FXCollections.observableArrayList();
    private CounterParty displayedCounterPartyLeft = null;

    @FXML
    private TableView<CounterParty> counterpartiesTableRight;
    @FXML
    private TableColumn<CounterParty, String> counterpartyShortNameColumnRight;
    @FXML
    private TableColumn<CounterParty, String> counterpartyFullNameColumnRight;
    @FXML
    private TableColumn<CounterParty, String> counterpartyCommentColumnRight;

    @FXML
    private Label shortNameLabelRight;
    @FXML
    private Label fullNameLabelRight;
    @FXML
    private Label commentLabelRight;

    private final ObservableList<CounterParty> counterpartiesDataRight = FXCollections.observableArrayList();
    private CounterParty displayedCounterPartyRight = null;

    private CounterParty mergedCounterParty = null;
    @FXML
    private TextField shortNameFieldMerged;
    @FXML
    private TextField fullNameFieldMerged;
    @FXML
    private TextField commentFieldMerged;

    @FXML
    private Button cancelButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button clearButton;

    private boolean acceptButtonClicked = false;

    @FXML
    private Button exitWindowButton;

    @FXML
    private Button shortNameLeftButton;
    @FXML
    private Button fullNameLeftButton;
    @FXML
    private Button commentLeftButton;

    @FXML
    private Button shortNameRightButton;
    @FXML
    private Button fullNameRightButton;
    @FXML
    private Button commentRightButton;

    private boolean atLeastOneMergeExecuted = false;

    /**
     * getters and setters
     */
    /**
     * return whether accept button has been clicked
     *
     * @return
     */
    public boolean isAcceptButtonClicked() {
        return acceptButtonClicked;
    }

    /**
     * tell if at lease one merge operation was performed
     *
     * @return
     */
    public boolean isAtLeastOneMergeExecuted() {
        return atLeastOneMergeExecuted;
    }

    /**
     * get merged counterParty
     *
     * @return
     */
    public CounterParty getMergedCounterParty() {
        return mergedCounterParty;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumnAlignement();

        counterpartyShortNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty());
        counterpartyFullNameColumnLeft.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        counterpartyCommentColumnLeft.setCellValueFactory(cellData -> cellData.getValue().getCommentProperty());

        counterpartiesTableLeft.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends CounterParty> observable, CounterParty oldValue, CounterParty newValue) -> {
                    showCounterPartyDetailsLeft(newValue);
                });

        counterpartyShortNameColumnRight.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty());
        counterpartyFullNameColumnRight.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        counterpartyCommentColumnRight.setCellValueFactory(cellData -> cellData.getValue().getCommentProperty());

        counterpartiesTableRight.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends CounterParty> observable, CounterParty oldValue, CounterParty newValue) -> {
                    showCounterPartyDetailsRight(newValue);
                });

        resetCounterpartyLeft();
        resetCounterpartyRight();

        atLeastOneMergeExecuted = false;
    }

    private void showCounterPartyDetailsLeft(CounterParty counterParty) {
        LOGGER.info("Left CounterParty details : " + counterParty.toJsonString());
        this.diplayCounterPartyLeft(counterParty);
    }

    public void diplayCounterPartyLeft(CounterParty counterParty) {
        this.displayedCounterPartyLeft = counterParty;
        if (this.displayedCounterPartyLeft != null) {
            shortNameLabelLeft.setText(displayedCounterPartyLeft.getShortName());
            fullNameLabelLeft.setText(displayedCounterPartyLeft.getName());
            commentLabelLeft.setText(displayedCounterPartyLeft.getComment());
        } else {
            resetCounterpartyLeft();
        }

    }

    private void showCounterPartyDetailsRight(CounterParty counterParty) {
        LOGGER.info("Right CounterParty details : " + counterParty.toJsonString());
        this.diplayCounterPartyRight(counterParty);
    }

    public void diplayCounterPartyRight(CounterParty counterParty) {
        this.displayedCounterPartyRight = counterParty;
        if (this.displayedCounterPartyRight != null) {
            shortNameLabelRight.setText(displayedCounterPartyRight.getShortName());
            fullNameLabelRight.setText(displayedCounterPartyRight.getName());
            commentLabelRight.setText(displayedCounterPartyRight.getComment());
        } else {
            resetCounterpartyRight();
        }

    }

    /**
     * initialize alignement of columns
     */
    private void initializeColumnAlignement() {
        counterpartyShortNameColumnLeft.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyFullNameColumnLeft.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyCommentColumnLeft.setStyle("-fx-alignment: CENTER-LEFT;");

        counterpartyShortNameColumnRight.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyFullNameColumnRight.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyCommentColumnRight.setStyle("-fx-alignment: CENTER-LEFT;");
    }

    @FXML
    private void cancelButtonPressed() {
        LOGGER.info("cancelButtonPressed");
        this.acceptButtonClicked = false;
        this.mergedCounterParty = null;
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void acceptButtonPressed() {
        LOGGER.info("acceptButtonPressed");
        if (mergedFieldsAreValidatedOk()) {
            mergedCounterParty = constructMergedCounterParty();
            if (confirmMergeMessage(displayedCounterPartyLeft, displayedCounterPartyRight, mergedCounterParty)) {
                mergeCounterParties(displayedCounterPartyLeft, displayedCounterPartyRight, mergedCounterParty);
                // remove the old counterparties from displayed lists
                removeCounterPartyLeft(displayedCounterPartyLeft);
                removeCounterPartyRight(displayedCounterPartyRight);
                displayedCounterPartyLeft = mergedCounterParty;
                displayedCounterPartyRight = mergedCounterParty;
                counterpartiesDataLeft.add(mergedCounterParty);
                counterpartiesDataRight.add(mergedCounterParty);

                atLeastOneMergeExecuted = true;
            }
        } else {
            displayMergeNotApprovedMessage();
        }
        this.acceptButtonClicked = true;
    }

    @FXML
    private void clearButtonPressed() {
        LOGGER.info("clearButtonPressed");
        shortNameFieldMerged.setText(CzekiRegistry.BLANK);
        fullNameFieldMerged.setText(CzekiRegistry.BLANK);
        commentFieldMerged.setText(CzekiRegistry.BLANK);
        mergedCounterParty = null;
    }

    @FXML
    private void closeWindowButtonPressed() {
        LOGGER.info("closeWindowButtonPressed");
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void shortNameLeftButtonPressed() {
        LOGGER.info("shortNameLeftButtonPressed");
        shortNameFieldMerged.setText(shortNameLabelLeft.getText());
    }

    @FXML
    private void fullNameLeftButtonPressed() {
        LOGGER.info("fullNameLeftButtonPressed");
        fullNameFieldMerged.setText(fullNameLabelLeft.getText());
    }

    @FXML
    private void commentLeftButtonPressed() {
        LOGGER.info("commentLeftButtonPressed");
        commentFieldMerged.setText(commentLabelLeft.getText());
    }

    @FXML
    private void shortNameRightButtonPressed() {
        LOGGER.info("shortNameRightButtonPressed");
        shortNameFieldMerged.setText(shortNameLabelRight.getText());
    }

    @FXML
    private void fullNameRightButtonPressed() {
        LOGGER.info("fullNameRightButtonPressed");
        fullNameFieldMerged.setText(fullNameLabelRight.getText());
    }

    @FXML
    private void commentRightButtonPressed() {
        LOGGER.info("commentRightButtonPressed");
        commentFieldMerged.setText(commentLabelRight.getText());
    }

    /**
     * reset both left and right counterparty tables
     */
    private void resetCounterpartyTables() {
        resetCounterpartyLeft();
        resetCounterpartyRight();
    }

    /**
     * reset left counterparty display
     */
    private void resetCounterpartyLeft() {
        shortNameLabelLeft.setText(CzekiRegistry.BLANK);
        fullNameLabelLeft.setText(CzekiRegistry.BLANK);
        commentLabelLeft.setText(CzekiRegistry.BLANK);
    }

    /**
     * reset right counterparty fields
     */
    private void resetCounterpartyRight() {
        shortNameLabelRight.setText(CzekiRegistry.BLANK);
        fullNameLabelRight.setText(CzekiRegistry.BLANK);
        commentLabelRight.setText(CzekiRegistry.BLANK);
    }

    /**
     * reset merged counterparty fields
     */
    private void resetMergedCounterpartyFields() {
        shortNameFieldMerged.setText(CzekiRegistry.BLANK);
        fullNameFieldMerged.setText(CzekiRegistry.BLANK);
        commentFieldMerged.setText(CzekiRegistry.BLANK);
    }

    /**
     * init counterparties tables
     */
    public void initCounterPartiesTables() {
        initCounterPartiesTableLeft();
        initCounterPartiesTableRight();
    }

    void initCounterPartiesTableLeft() {
        LOGGER.info("We are in initCounterPartiesTableLeft");
        if (counterpartiesTableLeft == null) {
            LOGGER.error("counterpartiesTableLeft is null");
        }
        LOGGER.info("number of cp=" + this.counterpartiesDataLeft.size());
        initCounterPartiesDataLeft(CzekiRegistry.currentAccount.getDao().getCounterParties().values());
        counterpartiesTableLeft.setItems(counterpartiesDataLeft);
    }

    void initCounterPartiesTableRight() {
        LOGGER.info("We are in initCounterPartiesTableRight");
        if (counterpartiesTableRight == null) {
            LOGGER.error("counterpartiesTableRight is null");
        }
        LOGGER.info("number of cp=" + this.counterpartiesDataRight.size());
        initCounterPartiesDataRight(CzekiRegistry.currentAccount.getDao().getCounterParties().values());
        counterpartiesTableRight.setItems(counterpartiesDataRight);
    }

    private void initCounterPartiesDataLeft(Collection<CounterParty> values) {
        counterpartiesDataLeft.clear();
        for (CounterParty cp : values) {
            counterpartiesDataLeft.add(cp);
        }
    }

    private void initCounterPartiesDataRight(Collection<CounterParty> values) {
        counterpartiesDataRight.clear();
        for (CounterParty cp : values) {
            counterpartiesDataRight.add(cp);
        }
    }

    /**
     * verify if the fields in the merged counterparty are validated OK
     *
     * @return
     */
    private boolean mergedFieldsAreValidatedOk() {
        boolean result = true;
        if (!shortNameValidatedOk()) {
            result = false;
        }
        if (!fullNameValidatedOk()) {
            result = false;
        }
        if (!commentValidatedOk()) {
            result = false;
        }
        return result;
    }

    /**
     * validate if the short name fields in merged counterparty is acceptable
     *
     * @return
     */
    private boolean shortNameValidatedOk() {
        return !CzekiRegistry.BLANK.equals(shortNameFieldMerged.getText());
    }

    /**
     * validate if the full name in merged counterparty is acceptable
     *
     * @return
     */
    private boolean fullNameValidatedOk() {
        return !CzekiRegistry.BLANK.equals(fullNameFieldMerged.getText());
    }

    /**
     * verify if comment field is OK
     *
     * @return
     */
    private boolean commentValidatedOk() {
        return true;
    }

    /**
     * construct merged counterparty based on fields in GUI
     *
     * @return
     */
    private CounterParty constructMergedCounterParty() {
        CounterParty cp = new CounterParty();
        cp.setShortNameProperty(shortNameFieldMerged.getText());
        cp.setName(fullNameFieldMerged.getText());
        cp.setComment(commentFieldMerged.getText());
        return cp;
    }

    /**
     * display confirmation dialog, to check if one should proceed with merge
     *
     * @param displayedCounterPartyLeft
     * @param displayedCounterPartyRight
     * @param mergedCounterParty
     * @return
     */
    private boolean confirmMergeMessage(CounterParty displayedCounterPartyLeft,
            CounterParty displayedCounterPartyRight,
            CounterParty mergedCounterParty) {

        String alertTitle = "Confirm merge";
        String alertHeader = "Confirm merge";
        String alertContent = "Alert\n" + displayedCounterPartyLeft.getShortName() + "\n";
        alertContent = alertContent + "will be merged with \n";
        alertContent = alertContent + displayedCounterPartyRight.getShortName();
        alertContent = alertContent + "\nand result will stored as\n";
        alertContent = alertContent + mergedCounterParty.getShortName();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * merge the counterparties
     *
     * @param displayedCounterPartyLeft
     * @param displayedCounterPartyRight
     * @param mergedCounterParty
     */
    private void mergeCounterParties(
            CounterParty displayedCounterPartyLeft,
            CounterParty displayedCounterPartyRight,
            CounterParty mergedCounterParty) {
        CzekiRegistry.currentAccount.getDao().mergeCounterparties(
                displayedCounterPartyLeft,
                displayedCounterPartyRight,
                mergedCounterParty);
    }

    /**
     * remove counterparty from left list
     *
     * @param cp
     */
    private void removeCounterPartyLeft(CounterParty cp) {
        removeCounterPartyFromObservableList(counterpartiesDataLeft, cp);
    }

    /**
     * remove counterparty from right list
     *
     * @param cp
     */
    private void removeCounterPartyRight(CounterParty cp) {
        removeCounterPartyFromObservableList(counterpartiesDataRight, cp);
    }

    /**
     * remove counterparty from observable list
     *
     * @param counterpartiesList
     * @param cp
     */
    private void removeCounterPartyFromObservableList(
            ObservableList<CounterParty> counterpartiesList,
            CounterParty cp) {
        Iterator iter = counterpartiesList.iterator();
        while (iter.hasNext()) {
            CounterParty currentCp = (CounterParty) iter.next();
            if (currentCp.equals(cp)) {
                iter.remove();
            }
        }
    }

    private void displayMergeNotApprovedMessage() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Merge Error");
        alert.setHeaderText("Merge Operation Failed");
        alert.setContentText("At least one property in the merged counterparty is not correct");

        alert.showAndWait();
    }
}
