/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.fileutils.*;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author tomw
 */
public class AccountCrudController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(AccountCrudController.class.getName());

    private Stage dialogStage;
    // Buttons
    @FXML
    private Button okButton;
    private boolean okButtonPressed = false;
    @FXML
    private Button cancelButton;
    private boolean cancelButtonPressed = false;
    @FXML
    private Button newButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Button fileNameButton;
    @FXML
    private Button imageDirectoryButton;

    // Labels
    @FXML
    private Label accountShortNameLabel;
    @FXML
    private Label accountShortNameLabelError;
    @FXML
    private Label accountFullNameLabel;
    @FXML
    private Label accountFullNameLabelError;
    @FXML
    private Label accountCommentLabel;
    @FXML
    private Label accountCommentError;
    @FXML
    private Label accountFileError;
    @FXML
    private Label accountImageDirectoryError;

    // text areas
    @FXML
    private TextField accountShortNameTextField;
    @FXML
    private TextField accountFullNameTextField;
    @FXML
    private TextField accountCommentTextField;
    @FXML
    private TextField accountFileTextField;
    @FXML
    private TextField accountImageDirectoryTextField;

    // table
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, String> accontShortNameColumn;
    @FXML
    private TableColumn<Account, String> accontFullNameColumn;
    @FXML
    private TableColumn<Account, String> accontSumClearedColumn;
    @FXML
    private TableColumn<Account, String> accontSumAllColumn;

    private Account selectedAccount;

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        accontShortNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        accontFullNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        accontSumClearedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        accontSumAllColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        accontShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().getShortNameProperty());
        accontFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        accontSumClearedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumClearedTransactions())));
        accontSumAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TomwStringUtils.money2String(cellData.getValue().getSumAllTransactions())));

        accountTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Account> observable, Account oldValue, Account newValue) -> {
                    showAccountDetails(newValue);
                });

        resetAllFields();
        disableInputFields();
        disableControls();
    }

    public void initAccountTable() {
        LOGGER.info("We are in initAccountTable");
        if (accountTable == null) {
            LOGGER.error("accountTable is null");
        }
        accountTable.setItems(CzekiRegistry.accountsDao.getAccountsData());
    }

    private ObservableList<Account> getListOfAccounts() {
        return getListOfAccounts(CzekiRegistry.accountsDao.getAccountsData());
    }

    private ObservableList<Account> getListOfAccounts(List<Account> listOfAccounts) {
        ObservableList<Account> accountsObservableList = FXCollections.observableArrayList();
        for (Account account : listOfAccounts) {
            accountsObservableList.add(account);
        }
        return accountsObservableList;
    }

    // button actions
    @FXML
    public void handleOKButton() {
        LOGGER.info("handleOKButton");
        okButtonPressed = true;
        dialogStage = (Stage) okButton.getScene().getWindow();
        dialogStage.close();
        //TODO implement rest ...
    }

    @FXML
    public void handleCancelButton() {
        LOGGER.info("handleCancelButton");
        cancelButtonPressed = true;
        dialogStage = (Stage) okButton.getScene().getWindow();
        dialogStage.close();
        //TODO implement rest ...
    }

    @FXML
    public void handleFileNameButton() {
        LOGGER.info("handleFileNameButton");
        selectAccountFile();
    }

    @FXML
    public void handleImageDirectoryButton() {
        LOGGER.info("handleImageDirectoryButton");
        selectImageDirectory();
    }

    @FXML
    public void handleNewButton() {
        LOGGER.info("handleNewButton");

        enableInputFields();
        enableControls();
        resetInputFields();
        selectedAccount = new Account();
        displayAccount(selectedAccount);
    }

    @FXML
    public void handleUpdateButton() {
        LOGGER.info("handleUpdateButton");
        updateSelectedAccountFromForm();
        addSelectedAccountToTable();
        refreshTable();
    }

    @FXML
    public void handleDeleteButton() {
        LOGGER.info("handleDeleteButton");

        CzekiRegistry.accountsDao.deleteAccount(selectedAccount);
        selectedAccount=null;
    }

    private void selectImageDirectory() {
        File imageDirectory = FileSelectors.selectDirectory();
        LOGGER.info("Directory selected=" + imageDirectory);
        selectedAccount.setImageDirectory(imageDirectory.toString());
        accountImageDirectoryTextField.setText(selectedAccount.getImageDirectoryString());
    }

    
    public void selectAccountFile() {
        File accountFile = FileSelectors.selectTextFileToImportFromAllowNonexistent();        
        LOGGER.info("File selected=" + accountFile);
        selectedAccount.setFileName(accountFile.toString());
        accountFileTextField.setText(selectedAccount.getFileName());
    }

    public void displaySelectedAccount() {
        displayAccount(getSelectedAccount());
    }

    public void displayAccount(Account account) {
        if (account == null) {
            LOGGER.info("No account selected to display");
        } else {
            accountFullNameTextField.setText(selectedAccount.getName());
            accountShortNameTextField.setText(selectedAccount.getShortName());
            accountCommentTextField.setText(selectedAccount.getComment());
            accountFileTextField.setText(selectedAccount.getFileName());
            accountImageDirectoryTextField.setText(selectedAccount.getImageDirectoryString());
        }
    }

    public void updateSelectedAccountFromForm() {
        selectedAccount.setName(accountFullNameTextField.getText());
        selectedAccount.setShortName(accountShortNameTextField.getText());
        selectedAccount.setComment(accountCommentTextField.getText());

        selectedAccount.setFileName(accountFileTextField.getText());
        selectedAccount.setImageDirectory(accountImageDirectoryTextField.getText());
    }

    public void resetAllFields() {
        resetInputFields();
        resetErrorFields();
    }

    public void resetInputFields() {
        accountShortNameTextField.setText(CzekiRegistry.BLANK);
        accountFullNameTextField.setText(CzekiRegistry.BLANK);
        accountFileTextField.setText(CzekiRegistry.BLANK);
        accountCommentTextField.setText(CzekiRegistry.BLANK);
        accountImageDirectoryTextField.setText(CzekiRegistry.BLANK);
    }

    public void resetErrorFields() {
        accountShortNameLabelError.setText(CzekiRegistry.BLANK);
        accountFullNameLabelError.setText(CzekiRegistry.BLANK);
        accountCommentError.setText(CzekiRegistry.BLANK);
        accountFileError.setText(CzekiRegistry.BLANK);
        accountImageDirectoryError.setText(CzekiRegistry.BLANK);
    }

    public void disableInputFields() {
        setFieldsDisabledFlag(true);
    }

    public void enableInputFields() {
        setFieldsDisabledFlag(false);
    }

    private void setFieldsDisabledFlag(boolean disabled) {
        accountShortNameTextField.setDisable(disabled);
        accountFullNameTextField.setDisable(disabled);
        accountCommentTextField.setDisable(disabled);
        accountFileTextField.setDisable(disabled);
        accountImageDirectoryTextField.setDisable(disabled);
    }

    public void disableControls() {
        controldSetDisabledFlag(true);
    }

    public void enableControls() {
        controldSetDisabledFlag(false);
    }

    public void controldSetDisabledFlag(boolean disabled) {
        fileNameButton.setDisable(disabled);
        imageDirectoryButton.setDisable(disabled);
        updateButton.setDisable(disabled);
        deleteButton.setDisable(disabled);

    }

    private void showAccountDetails(Account newValue) {
        LOGGER.info("Account details: " + newValue.toString());
        enableControls();
        enableInputFields();
        this.selectedAccount = newValue;
        this.displaySelectedAccount();
    }

    public boolean isOkClicked() {
        return okButtonPressed;
    }

    private void addSelectedAccountToTable() {
        if (this.dataEnteredIsOk()) {
            resetErrorFields();
            unpackInputForm();
            CzekiRegistry.accountsDao.add(selectedAccount);
        } else {
            this.displayBadInputMessage();
        }
    }

    private boolean dataEnteredIsOk() {
        // later maybe add some sanity checks here
        return true;
    }

    /**
     * unpack input fields, copy them to selected account object
     */
    private void unpackInputForm() {
        selectedAccount.setShortName(accountShortNameTextField.getText());
        selectedAccount.setName(accountFullNameTextField.getText());
        selectedAccount.setComment(accountCommentTextField.getText());
        selectedAccount.setFileName(accountFileTextField.getText());
        selectedAccount.setImageDirectory(accountImageDirectoryTextField.getText());
    }

    private void refreshTable() {
        accountTable.getColumns().get(0).setVisible(false);
        accountTable.getColumns().get(0).setVisible(true);
    }

    private void displayBadInputMessage() {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
