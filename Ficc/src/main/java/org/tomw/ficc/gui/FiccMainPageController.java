package org.tomw.ficc.gui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.ficc.core.FiccCounterParty;
import org.tomw.ficc.core.FiccIngestor;
import org.tomw.ficc.core.FiccTransaction;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by tomw on 7/29/2017.
 */
public class FiccMainPageController implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(FiccMainPageController.class.getName());

    public static final String VERSION="1.00";

    private static final String BLANK="";

    @FXML
    private TableView<FiccTransaction> transactionsTable;
    @FXML
    private TableColumn<FiccTransaction, LocalDate> dateColumn;
    @FXML
    private TableColumn<FiccTransaction, String> transactionColumn;
    @FXML
    private TableColumn<FiccTransaction, Number> amountColumn;
    @FXML
    private TableColumn<FiccTransaction, String> counterpartyNameColumn;
    @FXML
    private TableColumn<FiccTransaction, String> commentColumn;

    private ObservableList<FiccTransaction> listOfCurrentlyDisplayedTransactions =  FXCollections.observableArrayList();

    @FXML
    private Label dateValueLabel;
    @FXML
    private Label transactionValueLabel;
    @FXML
    private Label amountValueLabel;
    @FXML
    private Label counterPartyNameValueLabel;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private TextField groupsTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button editGroupsButton;
    @FXML
    private Button counterPartyDetailsButton;

    @FXML
    private Label numberOfTransactionsLabel;
    @FXML
    private Label sumOfTransactionsLabel;

    private FiccTransaction currentTransaction = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        counterpartyNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().datePropertyProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountPropertyProperty());
        transactionColumn.setCellValueFactory(cellData -> cellData.getValue().transactionPropertyProperty());
        counterpartyNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCounterparty().namePropertyProperty());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentPropertyProperty());

        transactionsTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends FiccTransaction> observable, FiccTransaction oldValue, FiccTransaction newValue) -> {
                    boolean executeIt = confirmWhetherSaveOldData();
                    if (executeIt) {
                        showTransactionDetails(newValue);
                    }
                });

        redisplay();

    }

    private void redisplay() {
        resetSummary();
        displaySummary();
    }

    private void displaySummary() {
        this.numberOfTransactionsLabel.setText(countNumberOfTransactions());
        this.sumOfTransactionsLabel.setText(countFormattedSumOfTransactions());
    }

    private String countFormattedSumOfTransactions() {
        double sum=0.0;
        if(listOfCurrentlyDisplayedTransactions!=null) {
            for (FiccTransaction t : listOfCurrentlyDisplayedTransactions) {
                sum = sum + t.getAmount();
            }
        }
        return TomwStringUtils.formatMoney(sum);
    }

    private String countNumberOfTransactions() {
        if(listOfCurrentlyDisplayedTransactions!=null) {
            return String.format("%d", listOfCurrentlyDisplayedTransactions.size());
        }else{
            return "0";
        }
    }


    private void resetSummary() {
        this.numberOfTransactionsLabel.setText("0");
        this.sumOfTransactionsLabel.setText("0.00");

    }

    private void showTransactionDetails(FiccTransaction transaction) {
        LOGGER.info("Transaction details: " + transaction);
        if (transaction != null) {
            this.diplayTransaction(transaction);
        }
    }

    private void diplayTransaction(FiccTransaction transaction) {
        this.currentTransaction = transaction;
        clearCurrentTransactionForm();
        fillTransactionDetailsForm(currentTransaction);
    }

    private void fillTransactionDetailsForm(FiccTransaction currentTransaction) {
        dateValueLabel.setText(currentTransaction.datePropertyProperty().get().toString());
        transactionValueLabel.setText(currentTransaction.getTransaction());
        amountValueLabel.setText(currentTransaction.amountPropertyProperty().get()+"");
        counterPartyNameValueLabel.setText(currentTransaction.getCounterparty().getName());
        commentsTextArea.setText(currentTransaction.getComment());

    }

    private void clearCurrentTransactionForm() {
        dateValueLabel.setText(BLANK);
        transactionValueLabel.setText(BLANK);
        amountValueLabel.setText(BLANK);
        counterPartyNameValueLabel.setText(BLANK);
        commentsTextArea.setText(BLANK);
    }

    private void resetCurrentTransaction(){
        currentTransaction=null;
        clearCurrentTransactionForm();
    }

    private boolean confirmWhetherSaveOldData() {
        //TODO implement properly
        return true;
    }

    public void initTable() {
        LOGGER.info("We are in initTable");
        if (transactionsTable == null) {
            LOGGER.error("Table is null");
        }
        transactionsTable.setItems(listOfCurrentlyDisplayedTransactions);
        refreshDisplay();
    }

    public void refreshDisplay(){
        displayAllTransactions();
        resetSummary();
        displaySummary();
    }

    private void displayAllTransactionsForCounterParty(FiccCounterParty cp) {
        listOfCurrentlyDisplayedTransactions.clear();
        listOfCurrentlyDisplayedTransactions.addAll(FiccRegistry.dao.getAllFiccTransactionsForCounterPartyAsObservableList(cp));
        Collections.sort(listOfCurrentlyDisplayedTransactions, FiccTransaction.FiccTransactionByDataComparator);
    }

    private void displayAllTransactions() {
        listOfCurrentlyDisplayedTransactions.clear();
        listOfCurrentlyDisplayedTransactions.addAll(FiccRegistry.dao.getAllFiccTransactionsAsObservableList());
        Collections.sort(listOfCurrentlyDisplayedTransactions, FiccTransaction.FiccTransactionByDataComparator);
    }

    @FXML
    private void menuEditIngestCsvFilesAction(ActionEvent event) {
        LOGGER.warn("menuEditIngestCsvFilesAction");
        List<File> list = FiccCsvFileChooser.selectCsvFiles();
        FiccIngestor ingestor = new FiccIngestor(FiccRegistry.dao);
        ingestor.ingestFromMultipleCsvFiles(list);
        refreshDisplay();
    }

    @FXML
    private void menuFileNewAction(ActionEvent event) {
        LOGGER.warn("menuExitAction not implemented yet");
    }

    @FXML
    private void menuFileOpenAction(ActionEvent event) {
        LOGGER.warn("menuFileOpenAction not implemented yet");
    }

    @FXML
    private void menuFileSaveAction(ActionEvent event) {
        LOGGER.info("menuFileSaveAction");
        saveTransactions();
    }

    @FXML
    private void menuFileSaveAsAction(ActionEvent event) {
        LOGGER.warn("menuFileSaveAsAction not implemented yet");
    }

    @FXML
    private void menuFileExitAction(ActionEvent event) {
        exit();
    }

    @FXML
    private void saveTransactionButtonAction(ActionEvent event){
        LOGGER.info("saveTransactionButtonAction");
        updateCurrentTransaction();
        saveTransactions();
    }

    @FXML
    private void deleteTransactionButtonAction(ActionEvent event){
        LOGGER.info("deleteTransactionButtonAction");
        int selectedIndex = transactionsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String idToRemove = transactionsTable.getItems().get(selectedIndex).getId();
            LOGGER.info("delete"+FiccRegistry.dao.getFiccTransaction(idToRemove));
            FiccRegistry.dao.deleteFiccTransaction(idToRemove);
            resetCurrentTransaction();
            refreshDisplay();
        }else{
            LOGGER.info("nothing selected");
        }
    }

    @FXML
    private void allTransactionsButtonAction(ActionEvent event){
        LOGGER.info("allTransactionsButtonAction");
        displayAllTransactions();
        resetSummary();
        displaySummary();
    }

    @FXML
    private void currentCounterPartyButtonAction(ActionEvent event){
        LOGGER.info("currentCounterPartyButtonAction");
        displayAllTransactionsForCounterParty(this.currentTransaction.getCounterparty());
        resetSummary();
        displaySummary();
    }

    @FXML
    private void customSelectionButtonAction(ActionEvent event){
        LOGGER.info("customSelectionButtonAction");
        displayCustomSelection();
        resetSummary();
        displaySummary();
    }

    private boolean displayCustomSelection() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FiccMainGui.class.getResource(FiccRegistry.SELECT_TRANSACTIONS_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select Transactions");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(FiccRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SelectTransactionsController controller = loader.getController();

            controller.initCounterPartiesTable();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if (controller.isOkClicked()) {
                //TODO implement
//                this.listOfTransactions = controller.getSelectedTransactions();
//                displaySelectedTransactions(controller.getSelectedTransactions());
            }

            return controller.isOkClicked();
        } catch (Exception e) {
            LOGGER.error("Error occured while building query", e);
            return false;
        }
    }

    private void saveTransactions() {
        try {
            FiccRegistry.dao.commit();
            LOGGER.info("Saved data to "+FiccRegistry.dao.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to save data to "+FiccRegistry.dao.toString(),e);
            //TODO add display message here
        }
    }

    private void updateCurrentTransaction() {
        this.currentTransaction.setComment(commentsTextArea.getText());
    }


    @FXML
    private void editGroupsButtonAction(ActionEvent event){
        LOGGER.error("Not implemented yet...");
    }
    @FXML
    private void counterPartyDetailsButtonAction(ActionEvent event){
        LOGGER.error("Not implemented yet...");
    }

    private void exit(){
        FiccRegistry.saveConfig();
        FiccRegistry.saveContext();
        try {
            FiccRegistry.dao.commit();
        } catch (IOException e) {
            LOGGER.error("Error occured while saving data",e);
            //TODO add display message here
        }
        LOGGER.info("Going to exit. Bye...");
        Platform.exit();
    }
}
