package org.tomw.rachunki.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.fxmlutils.CrudButtonsModeSelector;
import org.tomw.fxmlutils.TableAndFormController;
import org.tomw.gui.textareaeditor.TextAreaEditor;
import org.tomw.guiutils.alerts.ConfirmDeleteAlert;
import org.tomw.rachunki.core.RachunkiException;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.core.RachunkiServiceImpl;
import org.tomw.rachunki.entities.Konto;
import org.tomw.utils.TomwStringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static org.tomw.utils.TomwStringUtils.BLANK;

public class AccountsTabController implements Initializable, TableAndFormController<Konto> {
    private final static Logger LOGGER = Logger.getLogger(AccountsTabController.class.getName());

    @FXML
    private Label mainTitleLabel = new Label("Accounts");

    @FXML
    private Button primaryAccountSummaryButton;

    @FXML
    private Button selectAccountButton;
    @FXML
    private Button selectAllAccountsButton;

    @FXML
    private CheckBox showOnlyLocalAccountsCheckBox;

    @FXML
    private TableView<Konto> entitiesTable;

    private Konto initialHighlightedAccount=null;

    @FXML
    private TableColumn<Konto, String> accountShortNameColumn;

    @FXML
    private TableColumn<Konto, String> accountFullNameColumn;

    @FXML
    private TableColumn<Konto, String> accountLocalColumn;

    @FXML
    private TableColumn<Konto, String> sumClearedColumn;

    @FXML
    private TableColumn<Konto, String> sumAllColumn;

    @FXML
    private TableColumn<Konto, String> accountCommentColumn;

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

    private Konto displayedEntity;

    private boolean edited = false;

    private RachunkiService service;

    // entries in form
    @FXML
    private Label shortNameLabel;
    @FXML
    private TextField shortNameTextField;
    @FXML
    private CheckBox localAccountCheckBox;
    @FXML
    private CheckBox primaryAccountCheckBox;
    @FXML
    private CheckBox showOnlyPrimaryAccountsCheckBox;

    @FXML
    private Label shortNameErrorLabel;

    @FXML
    private Label fullNameLabel;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private Label fullNameErrorLabel;

    @FXML
    private Button showDetailsButton;
    @FXML
    private Label detailsLabel;
    @FXML
    private Label detailsErrorLabel;

    @FXML
    private Button showDocumentsButton;
    @FXML
    private Label documentsLabel;
    @FXML
    private Label documentsErrorLabel;

    @FXML
    private Button commentsButton;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private Label commentsErorrLabel;

    @FXML
    private Button mergeAccountsButton;

    @FXML
    private Label noOfAccountsLabel;
    private String noOfAllAccountsText = "No of all accocunts=";

    @FXML
    private Label noOfLocalAccountsLabel;
    private String noOfLocalAccountsText = "No of local accocunts=";

    @FXML
    private Label noOfPrimaryAccountsLabel;
    private String noOfPrimaryAccountsText = "No of primary accocunts=";

    @FXML
    private Label noOfDisplayedAccountsLabel;
    private String noOfDisplayedAccountsText = "No of displayed accocunts=";

    @FXML
    private Button transactionsButton;
    @FXML
    private Label noOfTransactionsLabel;
    private String noOfTransactionsLabeltext = "No of transactions = ";

    @FXML
    private TextField accountShortNameFilterTextField;
    @FXML
    private TextField accountFullNameFilterTextField;
    @FXML
    private Button accountFilterSubmitButton;
    @FXML
    private Button accountFilterResetButton;

    public void setInitialHighlightedAccount(Konto initialHighlightedAccount) {
        this.initialHighlightedAccount = initialHighlightedAccount;
    }

    private Collection<Label> errorFields = new ArrayList<>();

    private boolean standaloneMode = false; // whether code runs as standalone window or tab
    private boolean limitedMode = false; // whether it displays all entities of particular type from db or only some
    private boolean okButtonClicked = false;

    private CrudButtonsModeSelector buttonsSelector;

    public RachunkiService getService() {
        return service;
    }

    public void setService(RachunkiService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize tab");

        commentsTextArea.setWrapText(true);

        accountShortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        accountFullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        sumClearedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        sumAllColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        accountCommentColumn.setStyle("-fx-alignment: CENTER-LEFT;");


        accountShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());
        accountFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        accountLocalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        TomwStringUtils.boolean2BlankY(cellData.getValue().isAccountLocal())
                ));

        //TODO enter sum columns here
        accountCommentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());

        entitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Konto> observable, Konto oldValue, Konto newValue) -> {
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

        defineErrorFields();

        resetErrorFields();
    }

    private void defineErrorFields() {
        errorFields.add(shortNameErrorLabel);
        errorFields.add(fullNameErrorLabel);
        errorFields.add(detailsErrorLabel);
        errorFields.add(documentsErrorLabel);
        errorFields.add(commentsErorrLabel);
    }

    @Override
    public void setTitle(String title) {
        mainTitleLabel.setText(title);
    }

    @Override
    public Konto getCurrentEntity() {
        return displayedEntity;
    }

    @Override
    public void setCurrentEntity(Konto e) {
        this.displayedEntity = e;
    }

    @Override
    public List<Konto> getDisplayedEntities() {
        List<Konto> listOfDisplayedEntities = new ArrayList<>();
        for (Konto entity : getService().getDisplayedAccounts()) {
            listOfDisplayedEntities.add(entity);
        }
        return listOfDisplayedEntities;
    }

    @FXML
    private void primaryAccountSummaryButtonAction(){
        LOGGER.info("primaryAccountSummaryButtonAction");

        displayPrimaryAccountSummaryWindow();
    }

    private void displayPrimaryAccountSummaryWindow() {
        PrimaryAccountSummaryWindowView primaryAccountSummaryWindow = new PrimaryAccountSummaryWindowView();

        primaryAccountSummaryWindow.initialize();

        PrimaryAccountSummaryWindowController primaryAccountSummaryWindowController = new PrimaryAccountSummaryWindowController();
        primaryAccountSummaryWindowController.setService(getService());
        primaryAccountSummaryWindowController.setView(primaryAccountSummaryWindow);
        primaryAccountSummaryWindowController.initialize();
        primaryAccountSummaryWindowController.initTable();

        primaryAccountSummaryWindow.showAndWait();


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
            }
        }
    }

    @FXML
    public void deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
        if ((new ConfirmDeleteAlert()).deleteConfirmed(getCurrentEntity().getFullName())) {
            perform_deleteButtonAction();
        }
    }

    @Override
    public void perform_deleteButtonAction() {
        LOGGER.info("perform_deleteButtonAction");
        try {
            if (this.getCurrentEntity() != null) {
                getService().delete(this.getCurrentEntity());
                if (getService().getDisplayedAccounts().isEmpty()) {
                    this.clearForm();
                }
                this.reDisplayCurrentEntity();
            }
        } catch (RachunkiException e) {
            LOGGER.error("Failed to delete entity " + getCurrentEntity(), e);
        }
    }

    @FXML
    public void addButtonAction() {
        perform_addButtonAction();
    }

    @Override
    public void perform_addButtonAction() {
        int numberOfEntriesBefore = getService().getDisplayedAccounts().size();
        displayAccountsOverviewWindow();
        int numberOfCategoriesAfter = getService().getDisplayedAccounts().size();
        if (numberOfCategoriesAfter != numberOfEntriesBefore) {
            perform_formHasBeenEdited();
        }
    }

    private void displayAccountsOverviewWindow() {
        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayAllAccounts();

        // we want window in standalone mode, displaying limited info
        AccountsTab tab = new AccountsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setTitle("Categories List ");

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Categories");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());
        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                getService().save(tab.getController().getCurrentEntity());
                displayEntity(tab.getController().getCurrentEntity());
            }
        }
    }

    @FXML
    public void removeButtonAction() {
        perform_removeButtonAction();
    }

    @Override
    public void perform_removeButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @FXML
    public void clearButtonAction() {
        perform_clearButtonAction();
    }

    @Override
    public void perform_clearButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
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
    public void transactionsButtonAction() {
        LOGGER.info("transactionsButtonAction");
        perform_transactionsButtonAction();
    }

    public void perform_transactionsButtonAction() {
        LOGGER.info("perform_transactionsButtonAction");
        displayTransactionsForCurrentAccountWindow();
    }

    private void displayTransactionsForCurrentAccountWindow() {

        RachunkiService service = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig()
        );
        service.displayTransactionsForAccount(getCurrentEntity());

        // we want window in standalone mode, displaying limited info
        TransactionsTab tab = new TransactionsTab(true, true);
        tab.setService(service);
        tab.setStandaloneMode(true);
        tab.setAccount(getCurrentEntity());

        tab.init();

        Stage dialogStage = new Stage();
        String instanceType = RachunkiMainRegistry.getRegistry().getSelfIdentificationService().instanceType();
        dialogStage.setTitle("Transactions for account " + getCurrentEntity().getFullName()+" instance "+instanceType);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(RachunkiMainRegistry.getRegistry().getPrimaryStage());
        Scene scene = new Scene(tab.getPage());
        dialogStage.setScene(scene);

        dialogStage.showAndWait();

        LOGGER.info("Ok clicked: " + tab.getController().okClicked());
        if (tab.getController().okClicked()) {
            if (tab.getController().getCurrentEntity() != null) {
                LOGGER.info("Selected entity=" + tab.getController().getCurrentEntity());
                //getCurrentEntity().setClassesTaken(tab.getController().getDisplayedEntities());
                reDisplayCurrentEntity();
            }
        }
    }

    @Override
    public void initTable() {
        LOGGER.info("We are in initTable");
        if (entitiesTable == null) {
            LOGGER.error("entitiesTable is null");
        }
        resetDisplay();
        displayAllEntities();
        displayAccountStatistics();
        disableForm();
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), null);
        if(initialHighlightedAccount!=null){
            entitiesTable.getSelectionModel().select(initialHighlightedAccount);
            focusSelectedRow();
        }
    }

    private void focusSelectedRow() {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                entitiesTable.requestFocus();
                int selectedRow = entitiesTable.getSelectionModel().getSelectedIndex();
                entitiesTable.getSelectionModel().select(selectedRow);
                entitiesTable.getFocusModel().focus(selectedRow);
                entitiesTable.scrollTo(selectedRow);
            }
        });
    }

    @Override
    public void displayAllEntities() {
        if (!isStandaloneMode()) {
            getService().displayAllAccounts();
        }
        entitiesTable.setItems(getService().getDisplayedAccounts());
    }

    @Override
    public void updateDisplayedEntity() {
        getCurrentEntity().setShortName(this.shortNameTextField.getText());
        getCurrentEntity().setFullName(this.fullNameTextField.getText());

        getCurrentEntity().setComment(this.commentsTextArea.getText());

        if(this.localAccountCheckBox.isSelected()){
            getCurrentEntity().setAccountLocal(true);
        }else{
            getCurrentEntity().setAccountLocal(false);
        }

        if(this.primaryAccountCheckBox.isSelected()){
            getCurrentEntity().setAccountPrimary(true);
        }else{
            getCurrentEntity().setAccountPrimary(false);
        }
    }

    @Override
    public void displayEntity(Konto e) {
        LOGGER.info("Display entity " + e);
        if (e != this.displayedEntity) this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), e);
        this.displayedEntity = e;
        enableForm();
        reDisplayCurrentEntity();
        setSaveButtonEnabled(false);
    }

    @Override
    public void reDisplayCurrentEntity() {
        if (this.displayedEntity != null) {
            this.shortNameTextField.setText(getCurrentEntity().getShortName());
            this.fullNameTextField.setText(getCurrentEntity().getFullName());
            this.commentsTextArea.setText(getCurrentEntity().getComment());

            displayDocumentsInfo();

            int numberOfTransactionsForCurrentAccount = this.service.getNumberOfTransactionsForAccount(getCurrentEntity());
            this.noOfDisplayedAccountsLabel.setText(this.noOfDisplayedAccountsText + numberOfTransactionsForCurrentAccount);

            if(this.displayedEntity.isAccountLocal()){
                this.localAccountCheckBox.setSelected(true);
            }else{
                this.localAccountCheckBox.setSelected(false);
            }

            this.primaryAccountCheckBox.setSelected(this.displayedEntity.isAccountPrimary());

            displayAccountStatistics();
        }
    }

    private void displayDocumentsInfo() {
        this.documentsLabel.setText("No of documents=" + getCurrentEntity().getDocuments().size());
    }

    public void displayAccountStatistics() {
        this.noOfAccountsLabel.setText(this.noOfAllAccountsText + getService().getAllAccounts().size());
        this.noOfLocalAccountsLabel.setText(this.noOfLocalAccountsText + getService().getDisplayedLocalAccounts().size());
        this.noOfPrimaryAccountsLabel.setText(
                this.noOfPrimaryAccountsText + getService().getDisplayedPrimaryAccounts().size()
        );

        this.noOfDisplayedAccountsLabel.setText(
                this.noOfDisplayedAccountsText + getService()
                        .getDisplayedAccounts()
                        .size()
        );
        this.noOfTransactionsLabel.setText(
                this.noOfTransactionsLabeltext + " " +
                        this.service.getNumberOfTransactionsForAccount(this.getCurrentEntity())
        );

    }

    @Override
    public void resetErrorFields() {
        for (Label label : this.errorFields) {
            label.setText(BLANK);
        }
    }

    @Override
    public void clearForm() {
        this.shortNameTextField.setText(BLANK);
        this.fullNameTextField.setText(BLANK);
        this.commentsTextArea.setText(BLANK);
        resetErrorFields();
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
        shortNameLabel.disableProperty().set(flag);
        shortNameTextField.disableProperty().set(flag);

        fullNameLabel.disableProperty().set(flag);
        fullNameTextField.disableProperty().set(flag);
        showDetailsButton.disableProperty().set(flag);
        showDocumentsButton.disableProperty().set(flag);

        localAccountCheckBox.disableProperty().set(flag);
        primaryAccountCheckBox.disableProperty().set(flag);

        commentsButton.disableProperty().set(flag);
        commentsTextArea.disableProperty().set(flag);

        showDetailsButton.disableProperty().set(flag);
        detailsLabel.disableProperty().set(flag);
        detailsErrorLabel.disableProperty().set(flag);

        showDocumentsButton.disableProperty().set(flag);
        documentsLabel.disableProperty().set(flag);
        documentsErrorLabel.disableProperty().set(flag);

        commentsButton.disableProperty().set(flag);
        commentsTextArea.disableProperty().set(flag);
        commentsErorrLabel.disableProperty().set(flag);

        transactionsButton.disableProperty().set(flag);
        noOfTransactionsLabel.disableProperty().set(flag);
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
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void createAndDisplayNewEntity() {
        this.displayedEntity = new Konto();
        resetDisplay();
        reDisplayCurrentEntity();
        saveButtonEnable(false);
    }

    @Override
    public void saveCurrentEntity() {
        LOGGER.info("save " + getCurrentEntity());
        this.getService().save(getCurrentEntity());
    }

    @Override
    public void formHasNotBeenEdited() {
        setEdited(false);
        saveButtonEnable(false);
    }

    @Override
    public void saveButtonEnable(boolean b) {
        this.saveButton.setDisable(!b);
    }

    @Override
    public Stage getCurrentStage() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( Stage != "void" )return null;# end
    }

    @FXML
    public void displayOnlyLocalAccountsAction() {
        LOGGER.info("displayOnlyLocalAccountsAction=" + showOnlyLocalAccountsCheckBox.isSelected());
        if (showOnlyLocalAccountsCheckBox.isSelected()) {
            //display only local accounts
            this.service.displayAccounts(service.getDisplayedLocalAccounts());
        } else {
            // display all accounts
            this.service.displayAccounts(service.getAllAccounts());
        }
        displayAccountStatistics();
    }

    @FXML
    public void displayOnlyPrimaryAccountsAction() {
        LOGGER.info("displayOnlyPrimaryAccountsAction=" + showOnlyPrimaryAccountsCheckBox.isSelected());
        if (showOnlyPrimaryAccountsCheckBox.isSelected()) {
            //display only primary accounts
            this.service.displayAccounts(service.getDisplayedPrimaryAccounts());
        } else {
            // display all accounts
            this.service.displayAccounts(service.getAllAccounts());
        }
        displayAccountStatistics();
    }

    @FXML
    public void commentsButtonAction(){
        TextAreaEditor editor = new TextAreaEditor(
                "Comments for account " + getCurrentEntity().toShortString(),
                this.commentsTextArea.getText());
        if (editor.okClicked()) {
            String editorContent = editor.getText();
            String formContent = commentsTextArea.getText();
            if (formContent == null && editorContent != null || !formContent.equals(editorContent)) {
                formHasBeenEdited();
                commentsTextArea.setText(editor.getText());
            }
        }
    }

    @FXML
    public void  showDocumentsButtonAction(){
        LOGGER.info("showDocumentsbuttonAction");

        if (getCurrentEntity()!=null && getCurrentEntity().getDocuments() != null) {

            int numberOfDocumentsBefore = getCurrentEntity().getDocuments().size();
            displayDocumentsOverviewWindow();
            int numberOfDocumentsAfter = getCurrentEntity().getDocuments().size();
            if (numberOfDocumentsBefore != numberOfDocumentsAfter) {
                formHasBeenEdited();
                displayDocumentsInfo();
            }
        }else{
            LOGGER.error("Either entity or its documents list is null "+getCurrentEntity());
        }
    }

    private void displayDocumentsOverviewWindow() {
        DocumentsWindowUtils documentWindowUtils = new DocumentsWindowUtils();
        DocumentsWindowResult result = documentWindowUtils.displayDocumentsForAccount(
                getCurrentEntity(),
                this.showDocumentsButton.getScene().getWindow()
        );

        if (result.isOkClicked()) {
            getCurrentEntity().setDocuments(result.getDisplayedEntities());
        }
    }


    @FXML
    private void accountFilterSubmitButtonAction(){
        LOGGER.info("accountFilterSubmitButtonAction");
        String shortNameFilter = accountShortNameFilterTextField.getText();
        String fullNameFilter = accountFullNameFilterTextField.getText();

        Konto selectedAccount = entitiesTable.getSelectionModel().getSelectedItem();

        getService().displayAccountsUsingFilters(shortNameFilter,fullNameFilter);

        entitiesTable.getSelectionModel().select(selectedAccount);

        displayAccountStatistics();
    }

    @FXML
    private void accountFilterResetButtonAction(){
        LOGGER.info("accountFilterResetButtonAction");
        accountShortNameFilterTextField.setText((BLANK));
        accountFullNameFilterTextField.setText(BLANK);

        Konto selectedAccount = entitiesTable.getSelectionModel().getSelectedItem();
        getService().displayAllAccounts();
        entitiesTable.getSelectionModel().select(selectedAccount);
        
        displayAccountStatistics();
    }

    @FXML
    private void mergeAccountsButtonAction(){
        LOGGER.info("mergeAccountsButtonAction");

        MergeAccountsWindowView mergeAccountsWindow = new MergeAccountsWindowView();

        mergeAccountsWindow.initialize();

        MergeAccountsWindowController mergeAccountsWindowController = new MergeAccountsWindowController();
        mergeAccountsWindowController.setService(getService());
        mergeAccountsWindowController.setView(mergeAccountsWindow);
        mergeAccountsWindowController.initialize();
        mergeAccountsWindowController.initTable();

        mergeAccountsWindow.showAndWait();


    }


}

