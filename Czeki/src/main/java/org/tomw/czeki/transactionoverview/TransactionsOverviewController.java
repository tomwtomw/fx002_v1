/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.transactionoverview;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.counterparties.CounterpartiesOverviewController;
import org.tomw.czeki.Czeki;
import org.tomw.czeki.czekiimagedisplay.CzekiImageDisplayController;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.czeki.FileSelectors;
import org.tomw.czeki.YesNoCancelWindow;
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.czeki.imageview.CheckImageSelector;
import org.tomw.czeki.imageview.CzekImageUtils;
import org.tomw.czeki.imageview.DisplayCheckImagesBackAndFront;
import org.tomw.czeki.selecttransactions.SelectTransactionsController;
import org.tomw.utils.LocalDateConverters;
import org.tomw.utils.TomwStringUtils;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class TransactionsOverviewController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(TransactionsOverviewController.class.getName());

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML
    private TableColumn<Transaction, Number> amountColumn;
    @FXML
    private TableColumn<Transaction, String> checkNumberColumn;
    @FXML
    private TableColumn<Transaction, String> transactionClearedColumn;
    @FXML
    private TableColumn<Transaction, String> counterpartyNameColumn;
    @FXML
    private TableColumn<Transaction, String> commentColumn;
    @FXML
    private TableColumn<Transaction, String> sumClearedColumn;
    @FXML
    private TableColumn<Transaction, String> sumAllColumn;

    private String transactionId = null;
    private Transaction displayedTransaction = null;
    // list of displayed transactions
    private ObservableList<Transaction> listOfTransactions = null;

    public ObservableList<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    @FXML
    private Label dateLabel;
    @FXML
    private Label dateErrorLabel;

    @FXML
    private Label amountLabel;
    @FXML
    private Label amountErrorLabel;

    @FXML
    private Label checkNumberLabel;
    @FXML
    private Label checkNumberErrorLabel;
    @FXML
    private Button checkNumberButton;


    @FXML
    private Label counterpartyNameLabel;
    @FXML
    private Label counterpartyNameErrorLabel;

    @FXML
    private Label commentLabel;
    @FXML
    private Label commentErrorLabel;

    @FXML
    private Label memoLabel;
    @FXML
    private Label memoErrorLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField amountField;

    @FXML
    private TextField checkNumberField;

    private MostRecentCheckNumberHandler checkNumberHandler = new MostRecentCheckNumberHandler();

    @FXML
    private TextField memoField;

    @FXML
    private TextField commentField;

    @FXML
    private Label counterpartyDisplay;
    private String displayedCounterpartyId = CzekiRegistry.BLANK;

    @FXML
    private Button autoSelectFrontPageButton;
    @FXML
    private Button selectFrontPageButton;
    @FXML
    private Button viewFrontPageButton;
    @FXML
    private Button deleteFrontPageButton;
    @FXML
    private Button autoSelectBackPageButton;
    @FXML
    private Button selectBackPageButton;
    @FXML
    private Button viewBackPageButton;
    @FXML
    private Button deleteBackPageButton;

    @FXML
    private ImageView checkImageFrontView;
    @FXML
    private ImageView checkImageBackView;

    @FXML
    private CheckBox clearedPropertyCheckBox;

    @FXML
    private Button newButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button saveButton;
    private boolean formNotSaved = false;

    @FXML
    private Button selectCounterpartyButton;

    @FXML
    private Label clearedTransactions;
    @FXML
    private Label clearedTransactionsNumber;
    @FXML
    private Label nonClearedTransactions;
    @FXML
    private Label nonClearedTransactionsNumber;
    @FXML
    private Label totalTransactions;
    @FXML
    private Label totalTransactionsNumber;

    @FXML
    private Button selectTransactionsButton;
    @FXML
    private Button allTransactionsButton;
    @FXML
    private Button transactionsForCurrentCounterpartyButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        checkNumberColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        counterpartyNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        transactionClearedColumn.setStyle("-fx-alignment: CENTER;");

        sumClearedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        sumAllColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getTransactionDateProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().getTransactionAmountProperty());
        checkNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckNumberProperty());
        counterpartyNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCounterParty().getShortNameProperty());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().getCommentProperty());
        transactionClearedColumn.setCellValueFactory(cellData -> cellData.getValue().getClearedProperty());

        sumClearedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumClearedTransactions())));
        sumAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumAllTransactions())));

        transactionTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Transaction> observable, Transaction oldValue, Transaction newValue) -> {
                    boolean executeIt = confirmWhetherSaveOldData();
                    if (executeIt) {
                        showTransactionDetails(newValue);
                    }
                });

        datePicker.setConverter(LocalDateConverters.yyyyMMdd);
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
        });

        // Custom rendering of the table cell.
//        counterpartyNameColumn.setCellFactory(column -> {
//            return new TableCell<Transaction, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    System.out.println("item="+item);
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        // Format date.
//                        setText(item);
//                        System.out.println("getTableRow()="+getTableRow());
//                        Transaction transaction = (Transaction) getTableRow().getItem();
//                        System.out.println("Transaction = "+transaction);
//						// Style all dates in March with a different color.
//                        //if (item.contains("a")) {
//                        if (!transaction.isCleared()) {
//                            setTextFill(Color.BROWN);
//                            //setStyle("-fx-background-color: yellow");
//                        } else {
//                            setTextFill(Color.BLACK);
//                            setStyle("");
//                        }
//                    }
//                }
//            };
//        });
        redisplay();
    }

    private void redisplay() {
        resetErrorMessages();
        disableTransactionControlsExeptNew();
        resetSummary();
        displaySummary();
        displayMostRecentCheckNumber();
    }

    private void displayMostRecentCheckNumber() {
        checkNumberHandler.setLastCheckNumber(CzekiRegistry.currentAccount.getDao().getMostRecentCheckNumber());
        checkNumberButton.setText(checkNumberHandler.getLastCheckNumber());
    }

    private void showTransactionDetails(Transaction transaction) {
        LOGGER.info("Transaction details: " + transaction);
        if (transaction != null) {
            this.diplayTransaction(transaction);
        }
    }

    public void diplayTransaction(Transaction transaction) {

        resetErrorMessages();

        enableTransactionControlsExeptNew();

        this.transactionId = transaction.getId();
        this.displayedTransaction = transaction;

        fillFormFromTransaction(transaction);

        if (transaction.getCounterParty() != null) {
            this.counterpartyDisplay.setText(transaction.getCounterParty().getName());
            this.displayedCounterpartyId = transaction.getCounterParty().getId();
        } else {
            this.counterpartyDisplay.setText("");
            this.displayedCounterpartyId = CzekiRegistry.BLANK;
        }

        if (transaction.hasImageFront()) {
            checkImageFrontView.setImage(new Image(transaction.getImageFront().getFile().toURI().toString()));
        } else {
            checkImageFrontView.setImage(null);
        }
        if (transaction.hasImageBack()) {
            checkImageBackView.setImage(new Image(transaction.getImageBack().getFile().toURI().toString()));
        } else {
            checkImageBackView.setImage(null);
        }
        clearedPropertyCheckBox.setSelected(transaction.isCleared());

        displaySummary();
        formNotSaved = false;
        toggleColorOfSaveButton();
    }

    private void resetDisplay() {
        this.transactionId = null;
        this.displayedTransaction = null;

        this.datePicker.setValue(LocalDate.now());
        this.amountField.setText(CzekiRegistry.BLANK);
        this.checkNumberField.setText(CzekiRegistry.BLANK);
        this.memoField.setText(CzekiRegistry.BLANK);
        this.commentField.setText(CzekiRegistry.BLANK);
        this.counterpartyDisplay.setText(CzekiRegistry.BLANK);
        this.clearedPropertyCheckBox.setSelected(false);

        this.displayedCounterpartyId = CzekiRegistry.BLANK;

        this.checkImageBackView.setImage(null);
        this.checkImageFrontView.setImage(null);

        this.resetErrorMessages();

        this.resetSummary();
    }

    public void initTransactionsTable() {
        LOGGER.info("We are in initTransactionsTable");
        if (transactionTable == null) {
            LOGGER.error("transactionTable is null");
        }
        if (CzekiRegistry.currentAccount != null) {
            displayAllTransactions();
            calculateSums();
            displayMostRecentCheckNumber();
        }
    }

    /**
     * Display all transactions, take them from dao
     */
    public void displayAllTransactions() {
        listOfTransactions = CzekiRegistry.currentAccount.getDao().getTransactionsData();
        Collections.sort(listOfTransactions, Transaction.TransactionByDateComparator);
        transactionTable.setItems(listOfTransactions);
    }

    public void clearTransactionTable() {
        LOGGER.info("clearTransactionTable");
        if (transactionTable == null) {
            LOGGER.error("transactionTable is null");
        }
        transactionTable.getItems().clear();
    }

    @FXML
    public void clearTransactionsInfo() {
        resetDisplay();
        clearTransactionTable();
    }

    @FXML
    private void buttonNewTransactionAction() {
        LOGGER.info("buttonNewTransactionAction");

        boolean executeIt = confirmWhetherSaveOldData();
        if (executeIt) {
            this.displayedTransaction = new Transaction(LocalDate.now());
            this.diplayTransaction(displayedTransaction);
            formEdited();
        }
    }

    private boolean confirmWhetherSaveOldData() {
        if (formNotSaved) {
            YesNoCancelWindow yesNoCancelWindow = new YesNoCancelWindow(
                    "Curent transaction not saved",
                    "Current transaction has not been saved",
                    "Should I save it now?");
            String buttonPressed = yesNoCancelWindow.display();
            if (YesNoCancelWindow.CANCEL.equals(buttonPressed)) {
                return false;
            }
            if (YesNoCancelWindow.SAVE.equals(buttonPressed)) {
                saveCurrentTransaction();
            }
        }
        return true;
    }

    @FXML
    private void buttonSaveTransactionAction() {
        LOGGER.info("buttonSaveTransactionAction");
        saveCurrentTransaction();
    }

    private void saveCurrentTransaction() {
        this.acceptCurrentTransaction();
        refreshTable();
        displaySummary();
        formNotSaved = false;
        toggleColorOfSaveButton();
    }

    @FXML
    private void buttonDeleteTransactionAction() {
        LOGGER.info("buttonDeleteTransactionAction");

        int selectedIndex = transactionTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String idToRemove = transactionTable.getItems().get(selectedIndex).getId();
            LOGGER.info("Selected " + CzekiRegistry.currentAccount.getDao().getTransaction(idToRemove));
            LOGGER.info("transactionTable.size() 1=" + transactionTable.getItems().size());
            CzekiRegistry.currentAccount.getDao().deleteTransaction(idToRemove);
            LOGGER.info("transactionTable.size() 2=" + transactionTable.getItems().size());
            //transactionTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(CzekiRegistry.primaryStage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Transaction Selected");
            alert.setContentText("Please select a record in the table.");

            alert.showAndWait();
        }
        displaySummary();
    }

    @FXML
    private void selectTransactionsButtonAction() {
        LOGGER.info("selectTransactionsButtonAction");
        displaySelectTransactionsWindow();
    }

    @FXML
    private void transactionsForCurrentCounterpartyButtonAction() {
        LOGGER.info("transactionsForCurrentCounterpartyButtonAction");
        if (displayedTransaction != null) {
            if (displayedTransaction.getCounterParty() != null) {
                displayTransactionsForCounterParty(displayedTransaction.getCounterParty());
            }
        }
    }

    @FXML
    private void allTransactionsButtonAction() {
        LOGGER.info("allTransactionsButtonAction");
        displayAllTransactions();
        displaySummary();
    }

    @FXML
    private void buttonClearTransactionAction() {
        LOGGER.info("buttonClearTransactionAction - not implemented");
        //TODO implement this
    }

    @FXML
    public void checkNumberButtonAction(){
        LOGGER.info("Last Check number="+checkNumberHandler.getLastCheckNumber());
        checkNumberField.setText(checkNumberHandler.getLastCheckNumber());
        LOGGER.info("New Check number="+checkNumberHandler.getLastCheckNumber());
        checkNumberButton.setText(checkNumberHandler.getLastCheckNumber());
        formEdited();
    }

    @FXML
    private void buttonSelectCounterParty() {
        CounterParty oldCp = displayedTransaction.getCounterParty();
        LOGGER.info("buttonSelectCounterParty");
        displayCounterPartyOverviewWindow();
        if (!(oldCp == null && displayedTransaction.getCounterParty() == null)) {
            if (oldCp == null && displayedTransaction.getCounterParty() != null) {
                formEdited();
            } else {
                if (!oldCp.equals(displayedTransaction.getCounterParty())) {
                    formEdited();
                }
            }
        }
    }

    @FXML
    private void displayImageButtonHandler() {
        LOGGER.info("displayImageButtonHandler");
        File selectedImageFile = FileSelectors.selectImageFileToOpen();
        try {
            LOGGER.info("file=" + selectedImageFile.getCanonicalPath());
        } catch (IOException ex) {
            LOGGER.error("error when displaying canonical path ", ex);
        }
        displayCzekImageWindow("Hello World", selectedImageFile.toURI().toString());
    }

    @FXML
    public void autoSelectFrontPageButtonAction() {
        LOGGER.info("autoSelectFrontPageButtonAction");
        LOGGER.info("CzekiRegistry current dao=" + CzekiRegistry.currentAccount.getDao().toString());
        if (displayedTransaction != null) {
            String checkNumber = displayedTransaction.getCheckNumber();
            LOGGER.info("Check number=" + checkNumber);
            if (checkNumber != null && !"".equals(checkNumber.trim())) {
                List<CheckImage> checkImageCandidates
                        = CzekiRegistry.currentAccount.getDao().getByCheckNumberAndSide(
                        checkNumber, CzekImageUtils.FRONT);
                if (checkImageCandidates.isEmpty()) {
                    LOGGER.info(
                            "No check images found for check no " + checkNumber
                                    + " side " + CzekImageUtils.FRONT);
                } else {
                    CheckImage imageSelected;
                    if (checkImageCandidates.size() == 1) {
                        imageSelected = checkImageCandidates.get(0);
                    } else {
                        CheckImageSelector checkImageSelector = new CheckImageSelector();
                        imageSelected = checkImageSelector.selectCheckImage(checkImageCandidates);
                    }
                    if (imageSelected != null) {
                        displayedTransaction.setImageFront(imageSelected);
                        showTransactionDetails(displayedTransaction);
                    }
                }
            }
        } else {
            LOGGER.warn("Selected transaction is null");
        }
    }

    @FXML
    public void autoSelectBackPageButtonAction() {
        LOGGER.info("autoSelectBackPageButtonAction");
        if (displayedTransaction != null) {
            String checkNumber = displayedTransaction.getCheckNumber();
            if (checkNumber != null && !"".equals(checkNumber.trim())) {
                List<CheckImage> checkImageCandidates
                        = CzekiRegistry.currentAccount.getDao().getByCheckNumberAndSide(
                        checkNumber, CzekImageUtils.BACK);
                if (checkImageCandidates.isEmpty()) {
                    LOGGER.info(
                            "No check images found for check no " + checkNumber
                                    + " side " + CzekImageUtils.BACK);
                } else {
                    CheckImage imageSelected;
                    if (checkImageCandidates.size() == 1) {
                        imageSelected = checkImageCandidates.get(0);
                    } else {
                        CheckImageSelector checkImageSelector = new CheckImageSelector();
                        imageSelected = checkImageSelector.selectCheckImage(checkImageCandidates);
                    }
                    if (imageSelected != null) {
                        displayedTransaction.setImageBack(imageSelected);
                        showTransactionDetails(displayedTransaction);
                    }
                }
            }
        } else {
            LOGGER.warn("Selected transaction is null");
        }
    }

    @FXML
    public void selectFrontPageButtonAction() {
        LOGGER.info("selectFrontPageButtonAction");
        CheckImageSelector checkImageSelector = new CheckImageSelector();
        CheckImage selectedCheckImage = checkImageSelector.selectCheckImage();
        LOGGER.info("selectedCheckImage=" + selectedCheckImage);
        displayedTransaction.setImageFront(selectedCheckImage);
        showTransactionDetails(displayedTransaction);
    }

    @FXML
    public void viewFrontPageButtonAction() {
        LOGGER.info("viewFrontPageButtonAction");
        displayFrontAndBackPage();
    }

    @FXML
    public void deleteFrontPageButtonAction() {
        LOGGER.info("deleteFrontPageButtonAction");
        displayedTransaction.setImageFront(null);
        showTransactionDetails(displayedTransaction);
    }

    @FXML
    public void selectBackPageButtonAction() {
        LOGGER.info("selectBackPageButtonAction");
        CheckImageSelector checkImageSelector = new CheckImageSelector();
        CheckImage selectedCheckImage = checkImageSelector.selectCheckImage();
        LOGGER.info("selectedCheckImage=" + selectedCheckImage);
        displayedTransaction.setImageBack(selectedCheckImage);
        showTransactionDetails(displayedTransaction);
    }

    @FXML
    public void viewBackPageButtonAction() {
        LOGGER.info("viewBackPageButtonAction");
        displayFrontAndBackPage();
    }

    @FXML
    public void deleteBackPageButtonAction() {
        LOGGER.info("deleteBackPageButtonAction");
        displayedTransaction.setImageBack(null);
        showTransactionDetails(displayedTransaction);
    }

    @FXML
    public void formEdited() {
        LOGGER.info("Form edited");
        formNotSaved = true;
        toggleColorOfSaveButton();
    }

    private void fillFormFromTransaction(Transaction transaction) {
        this.datePicker.setValue(transaction.getTransactionDate());
        this.amountField.setText(transaction.getTransactionAmount().toString());
        this.checkNumberField.setText(transaction.getCheckNumber());
        this.memoField.setText(transaction.getMemo());
        this.commentField.setText(transaction.getComment());
    }

    private void formFields2Transaction(Transaction transaction) {
        transaction.setTransactionDate(this.datePicker.getValue());
        transaction.setTransactionAmount(Double.parseDouble(this.amountField.getText()));
        transaction.setCheckNumber(this.checkNumberField.getText());
        transaction.setMemo(this.memoField.getText());
        transaction.setComment(this.commentField.getText());
        transaction.setCleared(clearedPropertyCheckBox.isSelected());
    }

    private void acceptCurrentTransaction() {
        if (this.dataEnteredIsOk()) {
            resetErrorMessages();
            formFields2Transaction(this.displayedTransaction);
            String counterpartyId = this.displayedCounterpartyId;
            CounterParty counterParty = CzekiRegistry.currentAccount.getDao().getCounterParty(counterpartyId);
            this.displayedTransaction.setCounterParty(counterParty);
            CzekiRegistry.currentAccount.getDao().add(displayedTransaction);
            CzekiRegistry.currentAccount.getDao().setMostRecentCheckNumber(displayedTransaction.getCheckNumber());
            if(MostRecentCheckNumberHandler.representsInt(displayedTransaction.getCheckNumber())) {
                checkNumberHandler.setLastCheckNumber(displayedTransaction.getCheckNumber());
                String newCheckNumber = checkNumberHandler.incrementCheckNumber();
                checkNumberButton.setText(newCheckNumber);
                CzekiRegistry.currentAccount.getDao().setMostRecentCheckNumber(newCheckNumber);

            }
        } else {
            this.displayBadInputMessage();
        }
    }

    private void resetErrorMessages() {
        this.dateErrorLabel.setText(CzekiRegistry.BLANK);
        this.amountErrorLabel.setText(CzekiRegistry.BLANK);
        this.checkNumberErrorLabel.setText(CzekiRegistry.BLANK);
        this.counterpartyNameErrorLabel.setText(CzekiRegistry.BLANK);
        this.commentErrorLabel.setText(CzekiRegistry.BLANK);
        this.memoErrorLabel.setText(CzekiRegistry.BLANK);
    }

    private void displayBadInputMessage() {
        //TODO display the message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(CzekiRegistry.primaryStage);
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        String errorMessage = "Some input fields are not OK";
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    /**
     * verify that data entered in the input fields is OK
     *
     * @return
     */
    private boolean dataEnteredIsOk() {
        boolean resultOk = true;

        if (!this.verifyTransactionDate()) {
            resultOk = false;
            this.dateErrorLabel.setText(CzekiRegistry.ERROR);
        }
        if (!this.verifyTransactionAmount()) {
            resultOk = false;
            this.amountErrorLabel.setText(CzekiRegistry.ERROR);
        }
        if (!this.verifyCheckNumber()) {
            resultOk = false;
            this.checkNumberErrorLabel.setText(CzekiRegistry.ERROR);
        }
        if (!this.verifyMemo()) {
            resultOk = false;
            this.memoErrorLabel.setText(CzekiRegistry.ERROR);
        }
        if (!this.verifyCounterpartyName()) {
            resultOk = false;
            this.counterpartyNameErrorLabel.setText(CzekiRegistry.ERROR);
        }
        return resultOk;
    }

    private boolean verifyTransactionDate() {
        return this.datePicker.getValue() != null;
    }

    private boolean verifyTransactionAmount() {
        boolean amountOk = true;
        try {
            double amount = Double.parseDouble(this.amountField.getText());
        } catch (Exception e) {
            LOGGER.error("Cannot parse amount ", e);
            amountOk = false;
        }
        return amountOk;
    }

    private boolean verifyCheckNumber() {
        return true;
    }

    private boolean verifyMemo() {
        return true;
    }

    private boolean verifyCounterpartyName() {
        if (this.counterpartyDisplay.getText() == null) {
            return false;
        } else {
            String cpName = this.counterpartyDisplay.getText().trim();
            return !"".equals(cpName);
        }
    }

    private void refreshTable() {
        transactionTable.getColumns().get(0).setVisible(false);
        transactionTable.getColumns().get(0).setVisible(true);
    }

    public boolean displayCzekImageWindow(String inputText, String imageFileName) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CzekiRegistry.class.getResource(CzekiRegistry.CZEK_IMAGE_DISPLAY_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Purchase");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CzekiImageDisplayController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setText(inputText);
            controller.setImage(imageFileName);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return true;
        } catch (IOException e) {
            LOGGER.error("Error occured while displaying image " + imageFileName, e);
            return false;
        }
    }

    private boolean displayCounterPartyOverviewWindow() {
        try {
            // remember the current fields in the form
            Transaction temporaryTransaction = new Transaction();
            formFields2Transaction(temporaryTransaction);

            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.COUNTERPARTY_OVERVIEW_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Counterparties");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CounterpartiesOverviewController controller = loader.getController();
            //controller.setDialogStage(dialogStage);
            controller.initCounterPartiesTable();

            controller.diplayCounterParty(this.displayedTransaction.getCounterParty());

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if (controller.getDisplayedCounterParty() == null) {
                LOGGER.info("CP selected is null");
            } else {
                LOGGER.info("CP selected=" + controller.getDisplayedCounterParty().toJsonString());
                this.displayedTransaction.setCounterParty(controller.getDisplayedCounterParty());
                this.refreshTable();
                diplayTransaction(this.displayedTransaction);
                // fill form with the data stored in the beginning
                fillFormFromTransaction(temporaryTransaction);
            }

            return controller.isOkClicked();
        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
            return false;
        }
    }

    private void displayTransactionsForCounterParty(CounterParty cp) {
        this.listOfTransactions = CzekiRegistry.currentAccount.getDao().getTransactionsData(cp);
        displaySelectedTransactions(this.listOfTransactions);
    }

    private boolean displaySelectTransactionsWindow() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.SELECT_TRANSACTIONS_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select Transactions");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SelectTransactionsController controller = loader.getController();

            controller.initCounterPartiesTable();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if (controller.isOkClicked()) {
                this.listOfTransactions = controller.getSelectedTransactions();
                displaySelectedTransactions(controller.getSelectedTransactions());
            }

            return controller.isOkClicked();
        } catch (Exception e) {
            LOGGER.error("Error occured while building query", e);
            return false;
        }
    }

    private void displayFrontAndBackPage() {
        DisplayCheckImagesBackAndFront displayCheckImageBackAndFront
                = new DisplayCheckImagesBackAndFront();
        displayCheckImageBackAndFront.display(
                displayedTransaction.getImageFront(),
                displayedTransaction.getImageBack());
    }

    /**
     * Disable transaction controls except of the 'New' transaction button
     */
    private void disableTransactionControlsExeptNew() {
        setDisabledFlagForControls(true);
    }

    /**
     * Enable all controls
     */
    private void enableTransactionControlsExeptNew() {
        setDisabledFlagForControls(false);
    }

    /**
     * set the disableProperty flag for controls
     *
     * @param flag
     */
    private void setDisabledFlagForControls(boolean flag) {
        datePicker.disableProperty().set(flag);
        amountField.disableProperty().set(flag);
        checkNumberField.disableProperty().set(flag);
        memoField.disableProperty().set(flag);
        commentField.disableProperty().set(flag);
        counterpartyDisplay.disableProperty().set(flag);
        autoSelectFrontPageButton.disableProperty().set(flag);
        selectFrontPageButton.disableProperty().set(flag);
        viewFrontPageButton.disableProperty().set(flag);
        deleteFrontPageButton.disableProperty().set(flag);
        autoSelectBackPageButton.disableProperty().set(flag);
        selectBackPageButton.disableProperty().set(flag);
        viewBackPageButton.disableProperty().set(flag);
        deleteBackPageButton.disableProperty().set(flag);
        checkImageFrontView.disableProperty().set(flag);
        checkImageBackView.disableProperty().set(flag);

        deleteButton.disableProperty().set(flag);
        clearButton.disableProperty().set(flag);
        saveButton.disableProperty().set(flag);

        selectCounterpartyButton.disableProperty().set(flag);

        clearedPropertyCheckBox.disableProperty().set(flag);
    }

    /**
     * reset the fields which display summary of transactions
     */
    private void resetSummary() {
        clearedTransactions.setText(CzekiRegistry.BLANK);
        clearedTransactionsNumber.setText(CzekiRegistry.BLANK);
        nonClearedTransactions.setText(CzekiRegistry.BLANK);
        nonClearedTransactionsNumber.setText(CzekiRegistry.BLANK);
        totalTransactions.setText(CzekiRegistry.BLANK);
        totalTransactionsNumber.setText(CzekiRegistry.BLANK);
    }

    public void calculateSums() {
        calculateSums(transactionTable.getItems());
    }

    public static void calculateSums(List<Transaction> listOfTransactions) {
        calculateSums(listOfTransactions, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Calculate sum of all transactions and
     *
     * @param listOfTransactions - list of transactions to analyze
     * @param sums - list of results [sum of cleared transactions,non cleared
     * transactions, all transactions]
     * @param nTransactions - list of results [number of cleared transactions,
     * non cleared transactions, all transactions]
     */
    public static void calculateSums(List<Transaction> listOfTransactions, List<Double> sums, List<Integer> nTransactions) {
        double clearedTransactionsAmount = 0.;
        int clearedTransactionsNumberOf = 0;

        double nonClearedTransactionsAmount = 0.;
        int nonClearedTransactionsNumberOf = 0;

        double totalTransactionsAmount = 0.;
        int totalTransactionsNumberOf = 0;

        Collections.sort(listOfTransactions, Transaction.TransactionByDateComparator);

        for (Transaction transaction : listOfTransactions) {
            totalTransactionsAmount = totalTransactionsAmount + transaction.getTransactionAmountProperty().get();
            transaction.setSumAllTransactions(totalTransactionsAmount);
            totalTransactionsNumberOf = totalTransactionsNumberOf + 1;

            if (transaction.isCleared()) {
                clearedTransactionsNumberOf = clearedTransactionsNumberOf + 1;
                clearedTransactionsAmount = clearedTransactionsAmount + transaction.getTransactionAmountProperty().get();
            } else {
                nonClearedTransactionsNumberOf = nonClearedTransactionsNumberOf + 1;
                nonClearedTransactionsAmount = nonClearedTransactionsAmount + transaction.getTransactionAmountProperty().get();
            }
            transaction.setSumClearedTransactions(clearedTransactionsAmount);
        }

        // pack the results
        sums.clear();
        sums.add(clearedTransactionsAmount);
        sums.add(nonClearedTransactionsAmount);
        sums.add(totalTransactionsAmount);

        nTransactions.clear();
        nTransactions.add(clearedTransactionsNumberOf);
        nTransactions.add(nonClearedTransactionsNumberOf);
        nTransactions.add(totalTransactionsNumberOf);
    }

    private void displaySummary() {

        List<Double> sums = new ArrayList<>();
        List<Integer> nTransactions = new ArrayList<>();

        calculateSums(transactionTable.getItems(), sums, nTransactions);

        double clearedTransactionsAmount = sums.get(0);
        int clearedTransactionsNumberOf = nTransactions.get(0);

        double nonClearedTransactionsAmount = sums.get(1);
        int nonClearedTransactionsNumberOf = nTransactions.get(1);

        double totalTransactionsAmount = sums.get(2);
        int totalTransactionsNumberOf = nTransactions.get(2);

        clearedTransactions.setText(TomwStringUtils.money2String(clearedTransactionsAmount));
        clearedTransactionsNumber.setText(clearedTransactionsNumberOf + "");
        nonClearedTransactions.setText(TomwStringUtils.money2String(nonClearedTransactionsAmount));
        nonClearedTransactionsNumber.setText(nonClearedTransactionsNumberOf + "");
        totalTransactions.setText(TomwStringUtils.money2String(totalTransactionsAmount));
        totalTransactionsNumber.setText(totalTransactionsNumberOf + "");
    }

    /**
     * reset the window, set transactions to new dao
     */
    public void reset() {
        initTransactionsTable();
        resetDisplay();
        redisplay();
    }

    private void displaySelectedTransactions(ObservableList<Transaction> selectedTransactions) {
        //transactionTable.getItems().clear();
        transactionTable.setItems(selectedTransactions);
//        for(Transaction t: selectedTransactions){
//            LOGGER.info("add t="+t);
//            transactionTable.getItems().add(t);
//        }
        redisplay();
    }

    private void toggleColorOfSaveButton() {
        saveButton.setDisable(!formNotSaved);
    }

}
