package org.tomw.rachunki.gui;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tomw.daoutils.DataIntegrityException;
import org.tomw.documentfile.DocumentFile;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.fxmlutils.ConfirmUploadedfileDeleteAlert;
import org.tomw.fxmlutils.CrudButtonsModeSelector;
import org.tomw.fxmlutils.MessageAlert;
import org.tomw.fxmlutils.TableAndFormController;
import org.tomw.gui.filechooser.DestinationDirectoryChooser;
import org.tomw.gui.textareaeditor.TextAreaEditor;
import org.tomw.gui.viewimage.ViewImageWindow;
import org.tomw.guiutils.alerts.ConfirmDeleteAlert;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.core.RachunkiContext;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.core.TransactionUtils;
import org.tomw.rachunki.entities.CheckDocument;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.rachunki.entities.TransakcjaUtils;
import org.tomw.utils.TomwStringUtils;
import org.tomw.timeutils.TimeLimitGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static org.tomw.utils.TomwStringUtils.*;

public class TransactionsTabController implements Initializable, TableAndFormController<Transakcja> {
    private final static Logger LOGGER = Logger.getLogger(TransactionsTabController.class.getName());

    private static final String SELECT_TEXT_FILE_TO_SAVE_TO = "Select file to write output to:";

    // account for which we are displaying transactions
    private Konto currentAccount;

    @FXML
    private Label mainTitleLabel = new Label("Transactions");

    @FXML
    private Button selectAccountButton;
    @FXML
    private Label transactionsForAccountLabel;

    @FXML
    private TableView<Transakcja> entitiesTable;
    @FXML
    private TableColumn<Transakcja, LocalDate> transactionDateColumn;

    @FXML
    private TableColumn<Transakcja, Number> transactionAmountColumn;

    @FXML
    private TableColumn<Transakcja, String> checkNumberColumn;

    @FXML
    private TableColumn<Transakcja, String> clearedLocalColumn;

    @FXML
    private TableColumn<Transakcja, String> clearedRemoteColumn;

    @FXML
    private TableColumn<Transakcja, String> counterpartyNameColumn;

    @FXML
    private TableColumn<Transakcja, String> commentColumn;

    @FXML
    private TableColumn<Transakcja, String> sumAllColumn;

    @FXML
    private TableColumn<Transakcja, String> sumClearedColumn;

    @FXML
    private Label numberOfTransactionsLabel;
    @FXML
    private Label numberOfClearedTransactionsLabel;
    @FXML
    private Label numberOfNonClearedTransactionsLabel;
    @FXML
    private Label sumOfTransactionsLabel;
    @FXML
    private Label sumOfClearedTransactionsLabel;
    @FXML
    private Label sumOfNonClearedTransactionsLabel;

    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button okButton;

    @FXML
    private Button exportToTextButton;
    @FXML
    private Button exportToJsonButton;
    @FXML
    private Button exportToSpreadsheetButton;
    @FXML
    private Button compareFiccButton;

    private Transakcja displayedEntity;

    private boolean edited = false;

    private RachunkiService service;

    @FXML
    private Label transactionDetailslabel;
    @FXML
    private Button selectTransactionsButton;
    @FXML
    private Button currentCounterPartyButton;
    @FXML
    private Button allTransactionsButton;

    @FXML
    private Label transactionDateLabel;
    @FXML
    private DatePicker transactionDatePicker;
    @FXML
    private Label transactionDateErrorLabel;

    @FXML
    private Label transactionAmountLabel;
    @FXML
    private TextField transactionAmountTextField;
    @FXML
    private CheckBox localClearedPropertyCheckBox;
    @FXML
    private CheckBox remoteClearedPropertyCheckBox;
    @FXML
    private Label transactionAmountErrorLabel;

    @FXML
    private Label checkNumberLabel;
    @FXML
    private TextField checkNumberTextField;
    @FXML
    private Button lastCheckNumberButton;
    private boolean checkNumberTextFieldHasBeenEdited = false;
    private String previousCheckNumberValue = BLANK;
    private boolean checkNumberhasChanged = false;

    @FXML
    private Button counterpartyButton;
    // add known counterparty, ie: show list of counterparties with whom
    // the current account did trade previously
    @FXML
    private Button addKnownCounterPartyButton;
    @FXML
    private Button addPrimaryCounterPartyButton;
    @FXML
    private Label counterpartyLabel;
    @FXML
    private Label counterpartyLocalLabel;
    @FXML
    private Label counterpartyErrorLabel;

    @FXML
    private Button flipCounterpartybutton;

    @FXML
    private Label memoLabel;
    @FXML
    private TextField memoTextField;
    @FXML
    private Label memoErrorLabel;

    @FXML
    private Button commentsButton;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Label commentErrorLabel;

    @FXML
    private Button documentsButton;
    @FXML
    private Label documentsLabel;
    @FXML
    private Label documentsErrorLabel;

    @FXML
    private Label checkImageLabel;
    @FXML
    private Button viewCheckImageFrontButton;
    @FXML
    private ImageView checkImageFrontView;
    @FXML
    private Button uploadCheckImageFrontButton;
    @FXML
    private Button downloadCheckImageFrontButton;
    @FXML
    private Button deleteCheckImageFrontButton;
    @FXML
    private Button autoCheckImageFrontButton;

    @FXML
    private Button viewCheckImageBackButton;
    @FXML
    private ImageView checkImageBackView;
    @FXML
    private Button uploadCheckImageBackButton;
    @FXML
    private Button downloadCheckImageBackButton;
    @FXML
    private Button deleteCheckImageBackButton;
    @FXML
    private Button autoCheckImageBackButton;

    @FXML
    private Button editCheckImageButton;

    @FXML
    private Label checkNumberErrorLabel;

    @FXML
    private Button moreDetailsButton;

    @FXML
    private TextField filterByCheckNumberTextField;
    @FXML
    private TextField filterByCounterPartyShortNameTextField;
    @FXML
    private Button filterSubmitButton;
    @FXML
    private Button filterResetButton;

    @FXML
    private Button filterCurrentCounterPartyButton;
    @FXML
    private Button filterSelectCounterPartyButton;
    @FXML
    private Label filterCounterpartyShortNameLabel;
    @FXML
    private Button filterResetCounterPartyButton;
    @FXML
    private Button filterCounterpartySubmitButton;

    @FXML
    private Button filterThisMonthButton;
    @FXML
    private Button filterThisYearButton;
    @FXML
    private Button filterLastMonthButton;
    @FXML
    private Button filterLastYearButton;

    @FXML
    private Label filterDateFromLabel;
    @FXML
    private DatePicker filterDataFromDatePicker;
    @FXML
    private Label filterDateToLabel;
    @FXML
    private DatePicker filterDataToDatePicker;
    @FXML
    private Button filterBydateSubmitButton;

    @FXML
    private Button showAlltransactionsButton;

    private Collection<Label> errorFields = new ArrayList<>();

    private boolean standaloneMode = false; // whether code runs as standalone window or tab
    private boolean limitedMode = false; // whether it displays all entities of particular type from db or only some
    private boolean okButtonClicked = false;

    private CrudButtonsModeSelector buttonsSelector;

    // this transaction should be highlighted when we start the window
    private Transakcja transactionToHighlight;

    public RachunkiService getService() {
        return service;
    }

    public void setService(RachunkiService service) {
        this.service = service;
    }

    public Konto getCurrentAccount() {
        return currentAccount;
    }


    void setCurrentAccount(Konto currentAccount) {
        this.currentAccount = currentAccount;
    }

    void setTransactionToHighlight(Transakcja transactionToHighlight) {
        this.transactionToHighlight = transactionToHighlight;
    }

    private Transakcja getTransactionToHighlight() {
        return transactionToHighlight;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Transactions tab");
        transactionDateColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        transactionAmountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        checkNumberColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        clearedLocalColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        clearedRemoteColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        counterpartyNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        commentColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        sumAllColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        sumClearedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        transactionDateColumn.setCellValueFactory(cellData -> cellData.getValue().transactionDateProperty());
        transactionAmountColumn.setCellValueFactory(cellData ->
                cellData.getValue().getTransactionDirectionAmount(getCurrentAccount()));
        checkNumberColumn.setCellValueFactory(cellData -> cellData.getValue().checkNumberProperty());

        clearedLocalColumn.setCellValueFactory(
                cellData -> updateClearedLocal(cellData)
        );

        clearedRemoteColumn.setCellValueFactory(
                cellData -> updateClearedRemote(cellData)
        );

        counterpartyNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().
                        getOther(currentAccount).
                        get().shortNameProperty());

        commentColumn.setCellValueFactory(cellData -> cellData.getValue().shortCommentProperty());

        sumClearedColumn.setCellValueFactory(cellData -> cellData.getValue().runningSumClearedTransactionsProperty());

        sumAllColumn.setCellValueFactory(cellData -> cellData.getValue().runningSumAllTransactionsProperty());

        entitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Transakcja> observable, Transakcja oldValue, Transakcja newValue) -> {
                    displayEntity(newValue);
                });
        buttonsSelector = new CrudButtonsModeSelector(
                newButton,
                saveButton,
                deleteButton,
                addButton,
                removeButton,
                cancelButton,
                clearButton,
                okButton);

        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), getCurrentEntity());
        setDisabledFlagForControls(true);

        disableUnimplementedControls();

        defineErrorFields();

        redisplay();
    }

    /**
     * Disable controls which are not implemented yet
     */
    private void disableUnimplementedControls() {
        this.exportToJsonButton.setDisable(true);
        this.exportToSpreadsheetButton.setDisable(true);
        //this.compareFiccButton.setDisable(true);
    }

    private ObservableValue<String> updateClearedLocal(TableColumn.CellDataFeatures<Transakcja, String> cellData) {
        cellData.getValue().clearedLocalProperty().setValue(TomwStringUtils.boolean2BlankY(
                cellData.getValue().isClearedLocal(getCurrentAccount())
        ));
        return cellData.getValue().clearedLocalProperty();
    }

    private ObservableValue<String> updateClearedRemote(TableColumn.CellDataFeatures<Transakcja, String> cellData) {
        cellData.getValue().clearedRemoteProperty().setValue(TomwStringUtils.boolean2BlankY(
                cellData.getValue().isClearedRemote(getCurrentAccount())
        ));
        return cellData.getValue().clearedRemoteProperty();
    }

    private void redisplay() {
        resetErrorFields();
        resetSummary();
        displaySummary();
        //TODO displayMostRecentCheckNumber();
    }

    /**
     * Display summary of statistics about transactions
     */
    private void displaySummary() {
        List<Double> sums = new ArrayList<>();
        List<Integer> nTransactions = new ArrayList<>();

        TransactionUtils.calculateSums(entitiesTable.getItems(), getCurrentAccount(), sums, nTransactions);

        double clearedTransactionsAmount = sums.get(0);
        int clearedTransactionsNumberOf = nTransactions.get(0);

        double nonClearedTransactionsAmount = sums.get(1);
        int nonClearedTransactionsNumberOf = nTransactions.get(1);

        double totalTransactionsAmount = sums.get(2);
        int totalTransactionsNumberOf = nTransactions.get(2);

        sumOfClearedTransactionsLabel.setText(TomwStringUtils.money2String(clearedTransactionsAmount));
        numberOfClearedTransactionsLabel.setText(clearedTransactionsNumberOf + "");

        sumOfNonClearedTransactionsLabel.setText(TomwStringUtils.money2String(nonClearedTransactionsAmount));
        numberOfNonClearedTransactionsLabel.setText(nonClearedTransactionsNumberOf + "");

        sumOfTransactionsLabel.setText(TomwStringUtils.money2String(totalTransactionsAmount));
        numberOfTransactionsLabel.setText(totalTransactionsNumberOf + "");
    }

    private void defineErrorFields() {
        errorFields.add(transactionDateErrorLabel);
        errorFields.add(transactionAmountErrorLabel);
        errorFields.add(memoErrorLabel);
        errorFields.add(documentsErrorLabel);
        errorFields.add(commentErrorLabel);
        errorFields.add(checkNumberErrorLabel);
        errorFields.add(counterpartyErrorLabel);
    }

    @Override
    public void setTitle(String title) {
        mainTitleLabel.setText(title);
    }

    @Override
    public Transakcja getCurrentEntity() {
        return displayedEntity;
    }

    @Override
    public void setCurrentEntity(Transakcja e) {
        this.displayedEntity = e;
    }

    @Override
    public List<Transakcja> getDisplayedEntities() {
        List<Transakcja> listOfDisplayedEntities = new ArrayList<>();
        listOfDisplayedEntities.addAll(getService().getDisplayedTransactions());
        return listOfDisplayedEntities;
    }

    @FXML
    public void newButtonAction() {
        LOGGER.info("newButtonAction");
        perform_newButtonAction();
    }

    @Override
    public void perform_newButtonAction() {
        LOGGER.info("perform_newButtonAction");
        setDisabledFlagForControls(false);
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), null);
        createAndDisplayNewEntity();
    }

    @FXML
    public void saveButtonAction() {
        LOGGER.info("saveButtonAction");
        perform_saveButtonAction();
    }

    @Override
    public void perform_saveButtonAction() {
        LOGGER.info("perform_saveButtonAction");
        if (this.getCurrentEntity() != null) {
            boolean validationResult = validateFormInput();
            if (validationResult) {
                updateDisplayedEntity();
                saveCurrentEntity();
                formHasNotBeenEdited();
                resetErrorFields();
                getService().addToDisplay(getCurrentEntity());
                displaySummary();
            }
        }
    }

    public boolean validateFormInput() {
        resetErrorFields();
        boolean result = true;
        result = result && validateDate();
        result = result && validateAmount();
        result = result && validateCounterParty();
        return result;
    }

    /**
     * validate if counterparty has been set. Mark error label if not
     *
     * @return true if the counterparty has been defined, false otherwise
     */
    private boolean validateCounterParty() {
        if (getCurrentEntity().getCounterParty(getCurrentAccount()) != null) {
            return true;
        } else {
            this.counterpartyErrorLabel.setText(ERROR);
            return false;
        }
    }

    /**
     * validate transaction amount
     *
     * @return true if amount seems valid
     */
    private boolean validateAmount() {
        try {
            Double.parseDouble(this.transactionAmountTextField.getText());
            return true;
        } catch (Exception e) {
            this.transactionAmountErrorLabel.setText(ERROR);
        }
        return false;
    }

    /**
     * validate date
     *
     * @return true if date is defined
     */
    private boolean validateDate() {
        if (this.transactionDatePicker.getValue() != null) {
            return true;
        } else {
            this.transactionDateErrorLabel.setText(ERROR);
            return false;
        }
    }

    @FXML
    public void deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
        if ((new ConfirmDeleteAlert()).deleteConfirmed(getCurrentEntity().toString())) {
            perform_deleteButtonAction();
        }
    }

    @Override
    public void perform_deleteButtonAction() {
        LOGGER.info("perform_deleteButtonAction");

        if (this.getCurrentEntity() != null) {
            getService().delete(this.getCurrentEntity());
            if (getService().getDisplayedTransactions().isEmpty()) {
                this.clearForm();
            }
        }
    }

    @FXML
    public void addButtonAction() {
        perform_addButtonAction();
    }

    @Override
    public void perform_addButtonAction() {
        LOGGER.warn("perform_addButtonAction - not implemented!");
//        int numberOfEntriesBefore = getService().getDisplayedAccounts().size();
//        displayTransactionsOverviewWindow();
//        int numberOfCategoriesAfter = getService().getDisplayedAccounts().size();
//        if (numberOfCategoriesAfter != numberOfEntriesBefore) {
//            perform_formHasBeenEdited();
//        }
    }

    @FXML
    public void removeButtonAction() {
        perform_removeButtonAction();
    }

    @Override
    public void perform_removeButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
    }

    @FXML
    public void clearButtonAction() {
        perform_clearButtonAction();
    }

    @Override
    public void perform_clearButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
    }

    @FXML
    public void cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
        perform_cancelButtonAction();
    }

    @Override
    public void perform_cancelButtonAction() {
        LOGGER.info("perform_cancelButtonAction");
        this.okButtonClicked = false;
        if (isStandaloneMode()) {
            closeWindow();
        }
    }

    @FXML
    public void okButtonAction() {
        LOGGER.info("okButtonAction");
        perform_okButtonAction();
    }

    @Override
    public void perform_okButtonAction() {
        LOGGER.info("perform_okButtonAction");
        this.okButtonClicked = true;
        if (isStandaloneMode()) {
            closeWindow();
        }
    }

    @FXML
    public void exportToTextButtonAction() {
        LOGGER.info("exportToTextButtonAction");
        performExportTotextButtonAction();
    }

    @FXML
    public void exportToJsonButtonAction() {
        LOGGER.info("exportToJsonButtonAction - Not implemented!");
    }

    @FXML
    public void exportToSpreadsheetButtonAction() {
        LOGGER.info("exportToSpreadsheetButtonAction - Not implemented!");
    }

    @FXML
    public void compareFiccButtonAction() {
        LOGGER.info("compareFiccButtonAction");
        performCompareFiccButtonAction();
    }

    private void performCompareFiccButtonAction() {
        FiccComparatorWindow ficcComparatorWindow = new FiccComparatorWindow();
        ficcComparatorWindow.display();
    }

    private void performExportTotextButtonAction() {
        File selectedFile = selectTextFileToSaveTo();
        LOGGER.info("File selected to export transactions=" + selectedFile);

        if (selectedFile != null) {
            try {
                exportTransactionsToFile(selectedFile);
                rememberSelectedDirectoryForTextExport(selectedFile);
            } catch (FileNotFoundException e) {
                LOGGER.error("failed to write data to file " + selectedFile, e);
            }
        }
    }

    private void rememberSelectedDirectoryForTextExport(File selectedFile) {
        getContext().setLastTransactionsToTextDirectory(selectedFile.getParentFile());
    }

    private void exportTransactionsToFile(File selectedFile) throws FileNotFoundException {
        LOGGER.info("Write transactions to file " + selectedFile);
        PrintWriter out = new PrintWriter(selectedFile);
        for (Transakcja t : entitiesTable.getItems()) {
            String line = TransakcjaUtils.transactionToTabSeparatedString(t, getCurrentAccount());
            System.out.println(line);
            out.write(line);
            out.write(TomwStringUtils.EOL);
        }
        out.flush();
        out.close();
    }

    private File selectTextFileToSaveTo() {
        File directoryToExport = getContext().getLastTransactionsToTextDirectory();
        LOGGER.info("directoryToExport=" + directoryToExport);

        FileChooser outputTextFileChooser = new FileChooser();
        outputTextFileChooser.setTitle(SELECT_TEXT_FILE_TO_SAVE_TO);
        outputTextFileChooser.setInitialDirectory(directoryToExport);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = outputTextFileChooser.showSaveDialog(RachunkiMainRegistry.getRegistry().getPrimaryStage());

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }


    @FXML
    private void documentsButtonAction() {
        LOGGER.info("documentsButtonAction");
        if (getCurrentEntity().getDocuments() != null) {

            int numberOfDocumentsBefore = getCurrentEntity().getDocuments().size();
            displayDocumentsOverviewWindow();
            int numberOfDocumentsAfter = getCurrentEntity().getDocuments().size();
            if (numberOfDocumentsBefore != numberOfDocumentsAfter) {
                formHasBeenEdited();
                redisplayDocumentsInfo();
            }
        }
    }

    private void displayDocumentsOverviewWindow() {
        DocumentsWindowUtils documentWindowUtils = new DocumentsWindowUtils();
        DocumentsWindowResult result = documentWindowUtils.displayDocumentsForTransaction(
                getCurrentEntity(),
                this.documentsButton.getScene().getWindow()
        );

        if (result.isOkClicked()) {
            getCurrentEntity().setDocuments(result.getDisplayedEntities());
        }
    }

    @FXML
    private void counterpartyButtonAction() {
        LOGGER.info("counterpartyButtonAction");

        //What is current counterparty account?
        Konto currentCounterParty = getCurrentEntity().getCounterParty(getCurrentAccount());

        TransactionsWindowUtils transactionsWindowUtils = new TransactionsWindowUtils();
        AccountsWindowResult result = transactionsWindowUtils.displayAccountsWindow(
                currentCounterParty,
                this.counterpartyButton.getScene().getWindow()
        );
        LOGGER.info("OK clicked = " + result.isOkClicked());
        if (result.isOkClicked()) {
            Konto selectedAccount = result.getSelectedAccount();
            LOGGER.info("selected account = " + selectedAccount);
            if (selectedAccount != null && !selectedAccount.equals(getCurrentAccount())) {
                getCurrentEntity().setNewCounterParty(
                        getCurrentAccount(),
                        selectedAccount
                );
                perform_formHasBeenEdited();
                this.counterpartyLabel.setText(selectedAccount.getFullName());
            }
        }
    }

    @FXML
    private void addPrimaryCounterPartyButtonAction() {
        LOGGER.info("addPrimaryCounterPartyButtonAction");

        //What is current counterparty account?
        Konto currentCounterParty = getCurrentEntity().getCounterParty(getCurrentAccount());

        TransactionsWindowUtils transactionsWindowUtils = new TransactionsWindowUtils();
        AccountsWindowResult result = transactionsWindowUtils.displayPrimaryAccountsWindow(
                getCurrentAccount(),
                currentCounterParty,
                this.counterpartyButton.getScene().getWindow()
        );
        LOGGER.info("OK clicked = " + result.isOkClicked());
        if (result.isOkClicked()) {
            Konto selectedAccount = result.getSelectedAccount();
            LOGGER.info("selected account = " + selectedAccount);
            if (selectedAccount != null && !selectedAccount.equals(getCurrentAccount())) {
                getCurrentEntity().setNewCounterParty(
                        getCurrentAccount(),
                        selectedAccount
                );
                perform_formHasBeenEdited();
                this.counterpartyLabel.setText(selectedAccount.getFullName());
            }
        }

    }

    @FXML
    private void addKnownCounterPartyButtonAction() {
        LOGGER.info("addKnownCounterPartyButtonAction");

        //What is current counterparty account?
        Konto currentCounterParty = getCurrentEntity().getCounterParty(getCurrentAccount());

        TransactionsWindowUtils transactionsWindowUtils = new TransactionsWindowUtils();
        AccountsWindowResult result = transactionsWindowUtils.displayAccountsPreviouslyKnownToAccountWindow(
                getCurrentAccount(),
                currentCounterParty,
                this.counterpartyButton.getScene().getWindow()
        );
        LOGGER.info("OK clicked = " + result.isOkClicked());
        if (result.isOkClicked()) {
            Konto selectedAccount = result.getSelectedAccount();
            LOGGER.info("selected account = " + selectedAccount);
            if (selectedAccount != null && !selectedAccount.equals(getCurrentAccount())) {
                getCurrentEntity().setNewCounterParty(
                        getCurrentAccount(),
                        selectedAccount
                );
                perform_formHasBeenEdited();
                this.counterpartyLabel.setText(selectedAccount.getFullName());
            }
        }
    }

    @FXML
    private void flipCounterpartyButtonAction() {
        LOGGER.info("flipCounterpartyButtonAction");
        TransactionsWindowUtils transactionsWindowUtils = new TransactionsWindowUtils();

        // find the other party of current transaction
        Konto counterPartyToCurrentlyDisplayedTransaction = getCurrentEntity().getCounterParty(getCurrentAccount());

        // display all transactions for the other party
        TransactionsWindowResult result = transactionsWindowUtils.displayTransactionsForAccountWindow(
                counterPartyToCurrentlyDisplayedTransaction,
                getCurrentEntity(), // current transaction, it needs to be highlighted in the new window
                this.flipCounterpartybutton.getScene().getWindow()
        );
        LOGGER.info("OK clicked = " + result.isOkClicked());
        LOGGER.info("selected entity = " + result.getSelectedEntity());
    }

    @Override
    public void initTable() {
        LOGGER.info("We are in initTable");
        if (entitiesTable == null) {
            LOGGER.error("entitiesTable is null");
        }
        resetDisplay();
        displayAllEntities();
        // not needed because displaySummary will do it calculateSums(getCurrentAccount());
        displaySummary();
        disableForm();
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), null);

        highlightSelectedTransaction();
    }

    private void highlightSelectedTransaction() {
        int indexOfEntityToHighlight = findIndexOfEntityInTable(getTransactionToHighlight());
        higlightIndex(indexOfEntityToHighlight);
    }

    /**
     * highlight entry at given index in entities table
     *
     * @param indexOfEntityToHighlight index of transaction to be highlighted in table
     */
    private void higlightIndex(int indexOfEntityToHighlight) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                entitiesTable.requestFocus();
                entitiesTable.getSelectionModel().select(indexOfEntityToHighlight);
                entitiesTable.getFocusModel().focus(indexOfEntityToHighlight);
                entitiesTable.scrollTo(indexOfEntityToHighlight);
            }
        });
    }

    /**
     * Find index where given entity is stored in entities table
     * TODO this can be moved to abstract class
     *
     * @param transactionToHighlight transaction to be found in table
     * @return index of the transaction in table
     */
    private int findIndexOfEntityInTable(Transakcja transactionToHighlight) {
        int index = 0;
        for (Transakcja transakcja : this.entitiesTable.getItems()) {
            if (transakcja.equals(transactionToHighlight)) {
                return index;
            }
            index = index + 1;
        }
        return -1;
    }


    public void calculateSums(Konto currentAccount) {
        calculateSums(entitiesTable.getItems(), currentAccount);
    }

    private void calculateSums(List<Transakcja> listOfTransactions, Konto account) {
        TransactionUtils.calculateSums(listOfTransactions, account, new ArrayList<>(), new ArrayList<>());
    }


    @Override
    public void displayAllEntities() {
        if (!isStandaloneMode()) {
            getService().displayAllAccounts();
        }
        entitiesTable.setItems(getService().getDisplayedTransactions());
        numberOfTransactionsLabel.setText(getService().getDisplayedTransactions().size() + "");
    }

    @Override
    public void updateDisplayedEntity() {
        getCurrentEntity().setTransactionDate(this.transactionDatePicker.getValue());
        //TODO cleared fields

        // update transaction amount. This is tricky. If transaction amount
        double transactionAmount = Double.parseDouble(this.transactionAmountTextField.getText());
        getCurrentEntity().updateAmount(
                transactionAmount,
                getCurrentAccount()
        );

        String checkNumberInForm = this.checkNumberTextField.getText();
        String checkNumberInEntity = getCurrentEntity().getCheckNumber();
        if (!TomwStringUtils.stringNullCompareOrBothEmpty(checkNumberInForm, checkNumberInEntity)) {
            checkNumberhasChanged = true;
        }
        getCurrentEntity().setCheckNumber(checkNumberInForm);

        // TODO add correct handling of countrparty. I do not know what it means
        getCurrentEntity().setComment(this.commentTextArea.getText());
        getCurrentEntity().setMemo(this.memoTextField.getText());
        //TODO fill est, documents, check images etc

        // handle the local cleeared field
        boolean localCleared = localClearedPropertyCheckBox.isSelected();
        getCurrentEntity().setClearedLocal(TomwStringUtils.boolean2BlankY(localCleared));
        getCurrentEntity().setCleared(getCurrentAccount(), localCleared);

        // handle the remote cleeared field
        boolean remoteCleared = remoteClearedPropertyCheckBox.isSelected();
        getCurrentEntity().setClearedRemote(TomwStringUtils.boolean2BlankY(remoteCleared));
        getCurrentEntity().setCleared(getCurrentEntity().getCounterParty(getCurrentAccount()), remoteCleared);
    }

    @Override
    public void displayEntity(Transakcja e) {
        LOGGER.info("Display entity " + e);
        if (e != this.displayedEntity) this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), e);
        this.displayedEntity = e;
        enableForm();
        reDisplayCurrentEntity();
        setSaveButtonEnabled(false);
    }

    @Override
    public void reDisplayCurrentEntity() {
        transactionsForAccountLabel.setText(this.currentAccount.getFullName());
        if (this.displayedEntity != null) {
            if (getCurrentEntity().isClearedLocal(getCurrentAccount())) {
                this.localClearedPropertyCheckBox.setSelected(true);
            } else {
                this.localClearedPropertyCheckBox.setSelected(false);
            }

            this.transactionDatePicker.setValue(getCurrentEntity().getTransactionDate());
            this.transactionAmountTextField.setText(
                    getCurrentEntity().getTransactionDirectionAmount(
                            getCurrentAccount()
                    ).get() + "");
            this.checkNumberTextField.setText(getCurrentEntity().getCheckNumber());
            this.commentTextArea.setText(getCurrentEntity().getComment());
            this.memoTextField.setText(getCurrentEntity().getMemo());

            // display info about documents
            redisplayDocumentsInfo();

            if (getCurrentEntity().isClearedRemote(getCurrentAccount())) {
                this.remoteClearedPropertyCheckBox.setSelected(true);
            } else {
                this.remoteClearedPropertyCheckBox.setSelected(false);
            }

            Konto counterparty = getCurrentEntity().getCounterParty(getCurrentAccount());
            if (counterparty != null) {
                this.counterpartyLabel.setText(
                        counterparty.getFullName()
                );
                if (counterparty.isAccountLocal()) {
                    this.counterpartyLocalLabel.setText(Konto.LOCAL);
                } else {
                    this.counterpartyLocalLabel.setText(Konto.REMOTE);
                }
            } else {
                this.counterpartyLabel.setText(BLANK);
                this.counterpartyLocalLabel.setText(BLANK);
            }
            //TODO fill rest
            if (getCurrentEntity().getCheckDocument() != null) {
                this.checkImageFrontView.setImage(
                        getService()
                                .getImage(
                                        getCurrentEntity()
                                                .getCheckDocument()
                                                .getCheckImageFront()
                                )
                );
            } else {
                this.checkImageFrontView.setImage(null);
            }
            if (getCurrentEntity().getCheckDocument() != null) {
                this.checkImageBackView.setImage(
                        getService()
                                .getImage(
                                        getCurrentEntity()
                                                .getCheckDocument()
                                                .getCheckImageBack()
                                )
                );
            } else {
                this.checkImageBackView.setImage(null);
            }
        }

        updateLastCheckNumberButton();

        displaySummary();
    }

    private void redisplayDocumentsInfo() {
        this.documentsLabel.setText("No of documents=" + getCurrentEntity().getDocuments().size());
    }

    @Override
    public void resetErrorFields() {
        for (Label label : this.errorFields) {
            label.setText(BLANK);
        }
    }

    /**
     * reset fields which display statistics about transactions
     */
    private void resetSummary() {
        numberOfTransactionsLabel.setText(BLANK);
        numberOfClearedTransactionsLabel.setText(BLANK);
        numberOfNonClearedTransactionsLabel.setText(BLANK);
        sumOfTransactionsLabel.setText(BLANK);
        sumOfClearedTransactionsLabel.setText(BLANK);
        sumOfNonClearedTransactionsLabel.setText(BLANK);
    }

    @Override
    public void clearForm() {
        this.transactionDatePicker.setValue(LocalDate.now());
        this.transactionAmountTextField.setText("0.00");
        this.checkNumberTextField.setText(BLANK);
        this.commentTextArea.setText(BLANK);
        this.memoTextField.setText(BLANK);
        this.documentsLabel.setText(BLANK);

        this.counterpartyLocalLabel.setText(BLANK);

        localClearedPropertyCheckBox.setSelected(false);
        remoteClearedPropertyCheckBox.setSelected(false);

        //TODO fill rest
    }

    @FXML
    public void checkNumberFieldHasBeenEdited() {
        this.checkNumberTextFieldHasBeenEdited = true;
        perform_formHasBeenEdited();
    }

    @FXML
    public void formHasBeenEdited() {
        perform_formHasBeenEdited();
    }

    @Override
    public void perform_formHasBeenEdited() {
        setEdited(true);
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), getCurrentEntity());
        saveButtonEnable(true);
        resetErrorFields();
    }

    @Override
    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    @Override
    public boolean formHasUnsavedChanges() {
        return edited;
    }

    @Override
    public void setDisabledFlagForControls(boolean flag) {

        this.transactionDateLabel.disableProperty().set(flag);

        transactionAmountLabel.disableProperty().set(flag);
        counterpartyLabel.disableProperty().set(flag);
        checkNumberLabel.disableProperty().set(flag);
        memoLabel.disableProperty().set(flag);

        this.transactionDatePicker.disableProperty().set(flag);
        this.transactionAmountTextField.disableProperty().set(flag);
        this.checkNumberTextField.disableProperty().set(flag);
        this.commentTextArea.disableProperty().set(flag);
        this.memoTextField.disableProperty().set(flag);
        this.documentsLabel.disableProperty().set(flag);

        remoteClearedPropertyCheckBox.disableProperty().set(flag);
        localClearedPropertyCheckBox.disableProperty().set(flag);

        flipCounterpartybutton.disableProperty().set(flag);

        commentsButton.disableProperty().set(flag);
        commentTextArea.disableProperty().set(flag);

        counterpartyButton.disableProperty().set(flag);
        this.addKnownCounterPartyButton.disableProperty().set(flag);
        this.addPrimaryCounterPartyButton.disableProperty().set(flag);
        lastCheckNumberButton.disableProperty().set(flag);

        moreDetailsButton.disableProperty().set(flag);

        documentsButton.disableProperty().set(flag);
        documentsLabel.disableProperty().set(flag);
        documentsErrorLabel.disableProperty().set(flag);

        commentsButton.disableProperty().set(flag);
        commentTextArea.disableProperty().set(flag);


        // check image related controlls
        checkImageLabel.disableProperty().set(flag);
        viewCheckImageFrontButton.disableProperty().set(flag);
        checkImageFrontView.disableProperty().set(flag);
        uploadCheckImageFrontButton.disableProperty().set(flag);
        downloadCheckImageFrontButton.disableProperty().set(flag);
        deleteCheckImageFrontButton.disableProperty().set(flag);
        autoCheckImageFrontButton.disableProperty().set(flag);
        viewCheckImageBackButton.disableProperty().set(flag);
        checkImageBackView.disableProperty().set(flag);
        uploadCheckImageBackButton.disableProperty().set(flag);
        downloadCheckImageBackButton.disableProperty().set(flag);
        deleteCheckImageBackButton.disableProperty().set(flag);
        autoCheckImageBackButton.disableProperty().set(flag);
        editCheckImageButton.disableProperty().set(flag);

    }

    @Override
    public void setSaveButtonEnabled(boolean enable) {
        if (enable) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    @Override
    public void setButtonsForStandaloneMode(boolean standaloneMode) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public void refresh() {
        getService().displayAllAccounts();
    }


    @Override
    public boolean isStandaloneMode() {
        return standaloneMode;
    }

    @Override
    public void setStandaloneMode(boolean standaloneMode) {
        this.standaloneMode = standaloneMode;
    }

    @Override
    public boolean isLimitedMode() {
        return limitedMode;
    }

    @Override
    public void setLimitedMode(boolean limitedMode) {
        this.limitedMode = limitedMode;
    }

    @Override
    public boolean okClicked() {
        return okButtonClicked;
    }

    @Override
    public void closeWindow() {
        getCurrentStage().close();
    }

    @Override
    public void createAndDisplayNewEntity() {
        this.displayedEntity = new Transakcja(LocalDate.now());
        this.displayedEntity.setSeller(getCurrentAccount());
        resetDisplay();
        reDisplayCurrentEntity();
        saveButtonEnable(false);
    }

    @Override
    public void saveCurrentEntity() {
        LOGGER.info("save " + getCurrentEntity());
        // first order of business is to figure out if the check number has been updated
        if (this.checkNumberTextFieldHasBeenEdited && this.checkNumberhasChanged) {
            this.checkNumberhasChanged = false;
            // find out who wrote the check
            Konto whoWroteThecheck = whoWritesCurrentcheck();
            if (whoWroteThecheck != null) {
                whoWroteThecheck.setMostRecentCheckNumber(getCurrentEntity().getCheckNumber());
            }
            updateLastCheckNumberButton();
        }
        this.getService().save(getCurrentEntity());
    }

    private Konto whoWritesCurrentcheck() {
        Konto whoWroteThecheck;
        whoWroteThecheck = getCurrentEntity().getBuyer();
//        if(getCurrentEntity().getTransactionDirectionAmount(getCurrentAccount()).get()<0.0){
//            whoWroteThecheck=getCurrentEntity().getBuyer();
//        }else{
//            whoWroteThecheck=getCurrentEntity().getSeller();
//        }
        return whoWroteThecheck;

    }

    @Override
    public void formHasNotBeenEdited() {
        setEdited(false);
        this.checkNumberTextFieldHasBeenEdited = false;
        saveButtonEnable(false);
    }

    @Override
    public void saveButtonEnable(boolean b) {
        this.saveButton.setDisable(!b);
    }

    @Override
    public Stage getCurrentStage() {
        return (Stage) okButton.getScene().getWindow();
    }

    @FXML
    public void commentsButtonAction() {
        TextAreaEditor editor = new TextAreaEditor(
                "Comments for " + getCurrentEntity().toString(),
                this.commentTextArea.getText());
        if (editor.okClicked()) {
            String editorContent = editor.getText();
            String formContent = commentTextArea.getText();
            if (formContent == null && editorContent != null || !formContent.equals(editorContent)) {
                formHasBeenEdited();
                commentTextArea.setText(editor.getText());
            }
        }
    }


    @FXML
    public void viewCheckImageFrontButtonAction() {
        performCheckImageFrontViewButtonAction();
    }

    @FXML
    public void viewCheckImageBackButtonAction() {
        performCheckImageBackViewButtonAction();
    }

    @FXML
    public void uploadCheckImageFrontButtonAction() {
        performCheckImageFrontUploadButtonAction();
    }

    @FXML
    private void deleteCheckImageFrontButtonAction() {
        LOGGER.info("deleteCheckImageFrontButtonAction");
        performDeleteCheckImageFrontButtonAction();
    }

    private void performDeleteCheckImageFrontButtonAction() {
        if (getCurrentEntity().getCheckDocument() == null) {
            LOGGER.warn("Cannot delete check image: check document is null: " + getCurrentEntity());
        } else {
            if (getCurrentEntity().getCheckDocument().getCheckImageFront() == null) {
                LOGGER.warn("Cannot delete check image: front check image is null: " + getCurrentEntity().getCheckDocument());
            } else {
                DocumentFile imageToBeDeleted = getCurrentEntity().getCheckDocument().getCheckImageFront();
                if ((new ConfirmDeleteAlert()).deleteConfirmed(imageToBeDeleted.toString())) {

                    LOGGER.info("Will delete front check image for " + getCurrentEntity());

                    getCurrentEntity().getCheckDocument().setCheckImageFront(null);
                    try {
                        service.delete(imageToBeDeleted);
                    } catch (DataIntegrityException e) {
                        LOGGER.error("Failed to delete " + imageToBeDeleted, e);
                    }
                    formHasBeenEdited();
                    reDisplayCurrentEntity();
                }
            }
        }
    }

    @FXML
    private void deleteCheckImageBackButtonAction() {
        LOGGER.info("deleteCheckImageBackButtonAction");
        performDeleteCheckImageBackButtonAction();
    }

    private void performDeleteCheckImageBackButtonAction() {
        if (getCurrentEntity().getCheckDocument() == null) {
            LOGGER.warn("Cannot delete check image: check document is null: " + getCurrentEntity());
        } else {
            if (getCurrentEntity().getCheckDocument().getCheckImageBack() == null) {
                LOGGER.warn("Cannot delete check image: back check image is null: " + getCurrentEntity().getCheckDocument());
            } else {
                DocumentFile imageToBeDeleted = getCurrentEntity().getCheckDocument().getCheckImageBack();
                if ((new ConfirmDeleteAlert()).deleteConfirmed(imageToBeDeleted.toString())) {

                    LOGGER.info("Will delete back check image for " + getCurrentEntity());

                    getCurrentEntity().getCheckDocument().setCheckImageBack(null);
                    try {
                        service.delete(imageToBeDeleted);
                    } catch (DataIntegrityException e) {
                        LOGGER.error("Failed to delete " + imageToBeDeleted, e);
                    }
                    formHasBeenEdited();
                    reDisplayCurrentEntity();
                }
            }
        }
    }

    private void performCheckImageFrontUploadButtonAction() {
        LOGGER.info("performCheckImageFrontUploadButtonAction");
        performCheckImageUploadButtonAction(CheckDocument.FRONT);
    }

    @FXML
    public void uploadCheckImageBackButtonAction() {
        performCheckImageBackUploadButtonAction();
    }

    private void performCheckImageBackUploadButtonAction() {
        LOGGER.info("performCheckImageBackUploadButtonAction");
        performCheckImageUploadButtonAction(CheckDocument.BACK);

    }

    private void performCheckImageUploadButtonAction(String checkSide) {
        LOGGER.info("performCheckImageUploadButtonAction, side=" + checkSide);

        if (StringUtils.isNotBlank(getCurrentEntity().getCheckNumber())) {
            CheckImageSelector checkImageSelector = new CheckImageSelector();
            CheckImageSelectorResult selectorResult = checkImageSelector.selectSingleFile(getCurrentStage());
            LOGGER.info("ok clicked = " + selectorResult.isOkClicked());
            LOGGER.info("selected file = " + selectorResult.getSelectedFile());
            File selectedFile = selectorResult.getSelectedFile();
            if (selectorResult.isOkClicked() && selectedFile != null && selectedFile.exists()) {
                // if there is no check document, create it
                if (getCurrentEntity().getCheckDocument() == null) {
                    CheckDocument checkDocument = new CheckDocument();
                    service.save(checkDocument);
                    checkDocument.setCheckNumber(getCurrentEntity().getCheckNumber());
                    getCurrentEntity().setCheckDocument(checkDocument);
                }
                // create new check image from the uploaded file
                DocumentFile checkImageNew = service.createDocumentFile(selectedFile, false);

                // get the old chek image, depending on which side is requested it may be front or back
                DocumentFile checkImageOld;
                if (CheckDocument.FRONT.equalsIgnoreCase(checkSide)) {
                    checkImageOld = getCurrentEntity().getCheckDocument().getCheckImageFront();
                } else {
                    if (CheckDocument.BACK.equalsIgnoreCase(checkSide)) {
                        checkImageOld = getCurrentEntity().getCheckDocument().getCheckImageBack();
                    } else {
                        throw new RuntimeException("Unknown check side " + checkSide + " for check " + getCurrentEntity().getCheckNumber());
                    }
                }
                // if there wa an old check image, delete it
                if (checkImageOld != null) {
                    try {
                        if (CheckDocument.FRONT.equalsIgnoreCase(checkSide)) {
                            getCurrentEntity().getCheckDocument().setCheckImageFront(null);
                        }
                        if (CheckDocument.BACK.equalsIgnoreCase(checkSide)) {
                            getCurrentEntity().getCheckDocument().setCheckImageBack(null);
                        }
                        service.delete(checkImageOld);
                    } catch (DataIntegrityException e) {
                        LOGGER.error("Failed to delete of check image document: " + checkImageOld);
                    }
                }
                if (CheckDocument.FRONT.equalsIgnoreCase(checkSide)) {
                    getCurrentEntity().getCheckDocument().setCheckImageFront(checkImageNew);
                }
                if (CheckDocument.BACK.equalsIgnoreCase(checkSide)) {
                    getCurrentEntity().getCheckDocument().setCheckImageBack(checkImageNew);
                }

                formHasBeenEdited();
                reDisplayCurrentEntity();

                // ask if delete the original file
                (new ConfirmUploadedfileDeleteAlert()).askForConfirmation(selectedFile);
            }
        }
    }

    private void performCheckImageFrontViewButtonAction() {
        LOGGER.info("performCheckImageFrontViewButtonAction");
        if (getCurrentEntity().getCheckDocument() != null) {
            if (getCurrentEntity().getCheckDocument().getCheckImageFront() != null) {
                if (this.service.canBeDisplayed(getCurrentEntity().getCheckDocument().getCheckImageFront())) {
                    ViewImageWindow viewImageWindow = new ViewImageWindow();
                    viewImageWindow.setPrimaryStage(getCurrentStage());
                    viewImageWindow.display(this.service.getImage(getCurrentEntity().getCheckDocument().getCheckImageFront()));
                }
            }
        }
    }

    private void performCheckImageBackViewButtonAction() {
        LOGGER.info("performCheckImageBackViewButtonAction");
        if (getCurrentEntity().getCheckDocument() != null) {
            if (getCurrentEntity().getCheckDocument().getCheckImageBack() != null) {
                if (this.service.canBeDisplayed(getCurrentEntity().getCheckDocument().getCheckImageBack())) {
                    ViewImageWindow viewImageWindow = new ViewImageWindow();
                    viewImageWindow.setPrimaryStage(getCurrentStage());
                    viewImageWindow.display(this.service.getImage(getCurrentEntity().getCheckDocument().getCheckImageBack()));
                }
            }
        }
    }

    @FXML
    public void autoCheckImageUploadFrontButtonAction() {
        LOGGER.info("autoCheckImageUploadFrontButtonAction");
        autoCheckImageUploadAction(CheckDocument.FRONT);
    }


    @FXML
    public void autoCheckImageUploadBackButtonAction() {
        LOGGER.info("autoCheckImageUploadBackButtonAction");
        autoCheckImageUploadAction(CheckDocument.BACK);
    }

    private File getCheckImageCandidateFile(String checkNumber, String side) {
        String candidateName = checkNumber + side;

        File lastUploadDirectory = lastDocumentUploadDirectory();
        if (lastUploadDirectory != null && lastUploadDirectory.exists()) {
            LOGGER.info("Search for file named " + candidateName + ".* in " + lastUploadDirectory);
            File uploadCandidate = TomwFileUtils.findImageFile(lastUploadDirectory, candidateName);
            return uploadCandidate;
        } else {
            LOGGER.error("Last upload directory is null or does not exist " + lastUploadDirectory);
            return null;
        }
    }

    private void autoCheckImageUploadAction(String side) {
        String checkNumber = getCurrentEntity().getCheckNumber();
        if (checkNumber != null && StringUtils.isNotBlank(checkNumber)) {
            // upload
            File uploadCandidateFile = getCheckImageCandidateFile(checkNumber, side);
            if (uploadCandidateFile != null && uploadCandidateFile.exists()) {
                String newBaseName = checkNumber + side + "-" + TomwStringUtils.getShortUniqueString();
                File renamedUploadCandidate = TomwFileUtils.changeFileNameTo(uploadCandidateFile, newBaseName);
                uploadCandidateFile.renameTo(renamedUploadCandidate);

                if (renamedUploadCandidate != null && renamedUploadCandidate.exists()) {
                    if (getCurrentEntity().getCheckDocument() == null) {
                        CheckDocument checkDocument = new CheckDocument();
                        service.save(checkDocument);
                        checkDocument.setCheckNumber(getCurrentEntity().getCheckNumber());
                        getCurrentEntity().setCheckDocument(checkDocument);
                    }
                    DocumentFile checkImageBack = service.createDocumentFile(renamedUploadCandidate, true);
                    if (getCurrentEntity().getCheckDocument().getCheckImage(side) != null) {
                        DocumentFile oldCheckImageFront = getCurrentEntity().getCheckDocument().getCheckImage(side);
                        try {
                            if (oldCheckImageFront != null) {
                                getCurrentEntity().getCheckDocument().setCheckImage(side, null);
                                service.delete(oldCheckImageFront);
                            }
                        } catch (DataIntegrityException e) {
                            LOGGER.error("Failed to delete of check image document: " + oldCheckImageFront);
                        }
                    }
                    getCurrentEntity().getCheckDocument().setCheckImage(side, checkImageBack);
                    formHasBeenEdited();
                    reDisplayCurrentEntity();
                }
            } else {
                File directorySearched = lastDocumentUploadDirectory();
                String message = "Failed to find candidate for image of check " +
                        checkNumber +
                        " in directory " +
                        directorySearched;
                LOGGER.warn(message);
                (new MessageAlert()).notify(message);
            }
        }
    }

    private File lastDocumentUploadDirectory() {
        return getContext().getLastDocumentUploadDirectory();
    }

    private RachunkiContext getContext() {
        RachunkiConfiguration config = RachunkiMainRegistry.getRegistry().getConfig();
        return config.getContext();
    }

    @FXML
    public void downloadCheckImageFrontButtonAction() {
        LOGGER.info("downloadCheckImageFrontButtonAction");
        perform_downloadcheckImageAction(CheckDocument.FRONT);
    }

    @FXML
    public void downloadCheckImageBackButtonAction() {
        LOGGER.info("downloadCheckImageBackButtonAction");
        perform_downloadcheckImageAction(CheckDocument.BACK);
    }

    private void perform_downloadcheckImageAction(String side) {
        CheckDocument checkDocument = getCurrentEntity().getCheckDocument();
        if (checkDocument != null && checkDocument.getCheckImageFront() != null) {
            DocumentFile checkImage = checkDocument.getCheckImage(side);
            // select directory where to download
            DestinationDirectoryChooser chooser = new DestinationDirectoryChooser("Select download directory",
                    getContext().getLastDocumentDownloadDirectory(),
                    this.getCurrentStage());
            File directory = chooser.selectDirectory();
            if (directory != null && directory.exists() && directory.isDirectory()) {
                try {
                    File destinationFile = this.service.downloadFile(checkImage, directory);
                    getContext().setLastDocumentDownloadDirectory(directory);
                    //TODO add window with display message
                } catch (IOException e) {
                    String message = "Failed to download file " + checkImage.getExternalFileName();
                    LOGGER.error(message, e);
                    //TODO add error window here
                }
            }
        }
    }

    @FXML
    private void filterCurrentCounterPartyButtonAction() {
        LOGGER.info("filterCurrentCounterPartyButtonAction");

        Transakcja highlightedTransaction = entitiesTable.getSelectionModel().getSelectedItem();

        Konto currentCounterparty = getCurrentEntity().getCounterParty(getCurrentAccount());
        service.displayTransactionsBetweenAccounts(currentCounterparty, getCurrentAccount());

        entitiesTable.getSelectionModel().select(highlightedTransaction);
        entitiesTable.scrollTo(highlightedTransaction);
        displayEntity(highlightedTransaction);
        displaySummary();
    }

    @FXML
    private void filterSelectCounterPartyButtonAction() {
        LOGGER.info("filterSelectCounterPartyButtonAction");

        TransactionsWindowUtils transactionsWindowUtils = new TransactionsWindowUtils();
        AccountsWindowResult result = transactionsWindowUtils.displayAccountsWindow(
                getCurrentAccount(),
                this.counterpartyButton.getScene().getWindow()
        );
        LOGGER.info("OK clicked = " + result.isOkClicked());
        if (result.isOkClicked()) {
            Konto selectedAccount = result.getSelectedAccount();
            LOGGER.info("selected account = " + selectedAccount);
            if (selectedAccount != null && !selectedAccount.equals(getCurrentAccount())) {
                this.filterCounterpartyShortNameLabel.setText(selectedAccount.getShortName());
                Transakcja highlightedTransaction = entitiesTable.getSelectionModel().getSelectedItem();
                getService().displayTransactionsBetweenAccounts(getCurrentAccount(), selectedAccount);
                entitiesTable.getSelectionModel().select(highlightedTransaction);
                entitiesTable.scrollTo(highlightedTransaction);
                displayEntity(highlightedTransaction);
                displaySummary();
            }
        }

    }

    @FXML
    private void filterCounterpartyShortNameLabelAction() {
        LOGGER.info("filterCounterpartyShortNameLabelAction");
    }

    @FXML
    private void filterResetCounterPartyButtonAction() {
        LOGGER.info("filterResetCounterPartyButtonAction");
        this.filterCounterpartyShortNameLabel.setText(BLANK);
        displayAlltransactionsForCurrentAccount();
    }

    @FXML
    private void filterCounterpartySubmitButtonAction() {
        LOGGER.info("filterCounterpartySubmitButtonAction");
    }

    @FXML
    private void filterThisMonthButtonAction() {
        LOGGER.info("filterThisMonthButtonAction");
        LocalDate startDate = TimeLimitGenerator.getStartOfCurrentMonth();
        LocalDate endDate = TimeLimitGenerator.getEndOfCurrentMonth();

        displatTransactionsFromDateToDate(startDate, endDate);
    }

    private void displatTransactionsFromDateToDate(LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null && startDate.isBefore(endDate)) {
            Transakcja selectedTransaction = entitiesTable.getSelectionModel().getSelectedItem();
            getService().displayTransactionsForAccount(getCurrentAccount(), startDate, endDate);
            entitiesTable.getSelectionModel().select(selectedTransaction);
            entitiesTable.scrollTo(selectedTransaction);
            displaySummary();

        }
    }

    @FXML
    private void filterThisYearButtonAction() {
        LOGGER.info("filterThisYearButtonAction");
        LocalDate startDate = TimeLimitGenerator.getStartOfCurrentYear();
        LocalDate endDate = TimeLimitGenerator.getEndOfCurrentYear();

        displatTransactionsFromDateToDate(startDate, endDate);
    }

    @FXML
    private void filterLastMonthButtonAction() {
        LOGGER.info("filterLastMonthButtonAction");
        LocalDate startDate = TimeLimitGenerator.getStartOfPreviousMonth();
        LocalDate endDate = TimeLimitGenerator.getEndOfPreviousMonth();

        displatTransactionsFromDateToDate(startDate, endDate);
    }

    @FXML
    private void filterLastYearButtonAction() {
        LOGGER.info("filterLastYearButtonAction");
        LocalDate startDate = TimeLimitGenerator.getStartOfPreviousYear();
        LocalDate endDate = TimeLimitGenerator.getEndOfPreviousYear();

        displatTransactionsFromDateToDate(startDate, endDate);
    }


    @FXML
    private void filterBydateSubmitButtonAction() {
        LOGGER.info("filterBydateSubmitButtonAction");
        LocalDate startDate = this.filterDataFromDatePicker.getValue();
        LocalDate endDate = this.filterDataToDatePicker.getValue();

        displatTransactionsFromDateToDate(startDate, endDate);
    }

    @FXML
    private void showAlltransactionsButtonAction() {
        LOGGER.info("showAlltransactionsButtonAction");
        displayAlltransactionsForCurrentAccount();
    }

    private void displayAlltransactionsForCurrentAccount() {
        Transakcja highlightedTransaction = entitiesTable.getSelectionModel().getSelectedItem();
        service.displayTransactionsForAccount(getCurrentAccount());
        entitiesTable.getSelectionModel().select(highlightedTransaction);
        entitiesTable.scrollTo(highlightedTransaction);
        displayEntity(highlightedTransaction);
        displaySummary();
    }

    @FXML
    public void lastCheckNumberButtonAction() {
        String lastcheckNumberInButton = this.lastCheckNumberButton.getText();
        if (lastcheckNumberInButton != null) {
            this.checkNumberTextField.setText(lastcheckNumberInButton);
        }
        checkNumberFieldHasBeenEdited();
    }

    private void updateLastCheckNumberButton() {
        Konto checkWriter = whoWritesCurrentcheck();
        String incrementedCheckNumber = incrementCheckNumber(checkWriter.getMostRecentCheckNumber());
        this.lastCheckNumberButton.setText(incrementedCheckNumber);
    }

    @FXML
    private void filterSubmitButtonAction() {
        LOGGER.info("filterSubmitButtonAction");

        if (getCurrentAccount() != null) {

            String checkNumberFilter = filterByCheckNumberTextField.getText();
            String counterPartyShorttnameFilter = filterByCounterPartyShortNameTextField.getText();

            getService().displayTransactionsForAccountHavingCheckNumberAndCpty(
                    getCurrentAccount(),
                    checkNumberFilter,
                    counterPartyShorttnameFilter
            );
        }
    }

    @FXML
    private void filterResetButtonAction() {
        LOGGER.info("filterResetButtonAction");
        filterByCheckNumberTextField.setText(BLANK);
        filterByCounterPartyShortNameTextField.setText(BLANK);
        displayAlltransactionsForCurrentAccount();
    }
}
