/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import org.tomw.czeki.selectdirectorywindow.SelectDirectoryWindowController;
import org.tomw.czeki.counterparties.CounterpartiesOverviewController;
import org.tomw.czeki.fileupload.FileUploadWindowController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.cpsummary.CounterPartySummaryWindow;
import org.tomw.czeki.entities.CsvDataFormatException;
import org.tomw.czeki.imageview.CheckImageException;
import org.tomw.czeki.imageview.ImageStoreBrowser;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class MainPageNewController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MainPageNewController.class.getName());

    @FXML
    Button transactionsButton;

    @FXML
    Button counterpartiesButton;

    @FXML
    Button counterpartiesSummaryButton;

    @FXML
    private Label currentModeLabel;

    @FXML
    private Label currentFileNameLabel;

    @FXML
    private Label accountNameLabel;
    private final String NO_ACCOUNT_SELECTED = "No account selected";

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        unselectButtons();
        loadContext();
        displayAccountName();
    }

    /**
     * make all buttons "unselected"
     */
    private void unselectButtons() {
        this.currentModeLabel.setText(CzekiRegistry.BLANK);
    }

    /**
     * persist context info
     */
    public void persistContext() {
        CzekiRegistry.context.persist();
    }

    /**
     * load context info
     */
    public void loadContext() {
        CzekiRegistry.context.load();
    }

    @FXML
    private void menuFileAccountAction() {
        LOGGER.info("menuFileAccountAction");
        Account previousAccount = CzekiRegistry.currentAccount;
        Account selectedAccount = selectAccount();
        if (selectedAccount != previousAccount) {
            if (selectedAccount != null) {
                if (CzekiRegistry.currentAccount != null) {
                    if (CzekiRegistry.currentAccount.getDao() != null) {
                        try {
                            CzekiRegistry.currentAccount.getDao().commit();
                        } catch (IOException e) {
                            LOGGER.error("Failsed to save data ", e);
                        }
                    }
                }
                CzekiRegistry.currentAccount = selectedAccount;
                displayAccountName();
                CzekiRegistry.transactionsOverviewController.reset();
            }
        }
    }

    @FXML
    private void menuFileNewAction() {
        LOGGER.info("menuFileNewAction");
    }

    @FXML
    private void menuFileOpenAction() {
        LOGGER.info("menuFileOpenAction");
        //TODO implement or delete...
        throw new RuntimeException("Not implementd ...");
    }

    @FXML
    private void menuFileCloseAction() {
        LOGGER.info("menuFileCloseAction");
        CzekiRegistry.transactionsOverviewController.clearTransactionsInfo();
        closeDao();
    }

    @FXML
    private void menuFileImportFromTextFileAction() {
        LOGGER.info("menuFileImportFromTextFileAction");
        this.importFromTextFile();
    }

    @FXML
    private void menuFileExportToTextFileAction() {
        LOGGER.info("menuFileExportToTextFileAction");
        File textFile = FileSelectors.selectTextFileToSaveTo();
        this.saveAsTextAction(textFile);
    }

    @FXML
    private void menuFileImageDirectoryAction() {
        LOGGER.info("menuFileImageDirectoryAction");

    }

    @FXML
    private void menuFileSaveAction() {
        LOGGER.info("menuFileSaveAction");
        this.saveAction();
    }

    @FXML
    private void menuFileSaveAsAction() {
        LOGGER.info("menuFileSaveAsAction");
        throw new RuntimeException("Not implemented...");
        //TODO implement....

    }

    @FXML
    private void menuFileExitAction() {
        LOGGER.info("menuFileExitAction");
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save data before exit?");
        alert.setHeaderText("Save data before exit?");
        alert.setContentText("Should I save data before exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.saveAction();
        }

        CzekiRegistry.context.persist();

        Platform.exit();
    }

    @FXML
    private void menuEditDisplayConfigAction() {
        LOGGER.info("Display configuration object");
        (new TextWindowDisplay("Configuration Summary", CzekiRegistry.toStringSummary())).display();
    }

    @FXML
    private void menuImagesSetImagesDirectory() {
        LOGGER.info("menuImagesSetImagesDirectory");
        //TODO implement
    }

    @FXML
    private void menuImagesImportImages() {
        LOGGER.info("menuImagesImportImages");
        importImages();
        try {
            CzekiRegistry.currentAccount.getDao().reloadImages();
        } catch (CheckImageException ex) {
            LOGGER.error("Failed to reload images");
        }
    }

    @FXML
    private void menuImagesReloadImages() {
        LOGGER.info("menuImagesReloadImages");
        try {
            CzekiRegistry.currentAccount.getDao().reloadImages();
            (new TextWindowDisplay("Message", "Images Reloaded")).display();
        } catch (CheckImageException ex) {
            LOGGER.error("Failed to reload images");
        }
    }

    @FXML
    private void menuImagesDisplayImage() {
        LOGGER.info("menuImagesDisplayImage");
        ImageStoreBrowser imageStoreBrowser = new ImageStoreBrowser();
        imageStoreBrowser.display();
    }

    @FXML
    private void menuImagesDeleteImages() {
        LOGGER.info("menuImagesDeleteImages");
    }

    @FXML
    private void selectTransactionsAction() {
        LOGGER.info("selectTransactionsAction");
    }

    @FXML
    private void selectCounterPartiesAction() {
        LOGGER.info("selectCounterPartiesAction");
        displayCounterPartyOverviewWindow();
    }

    @FXML
    private void counterPartiesSummaryButtonAction() {
        LOGGER.info("counterPartiesSummaryButtonAction");
        displayCounterPartiesSummaryWindow();
    }

    @FXML
    private void backupCurrentDataAction() {
        LOGGER.info("backupCurrentDataAction");
        try {
            backupCurrentData();
        } catch (IOException ex) {
            LOGGER.error("Failed to back up the DAO", ex);
        }
    }

    @FXML
    private void toCsvButtonAction() {
        LOGGER.info("toCsvButtonAction()");
        try {
            backupCurrentDataToCsv();
        } catch (IOException ex) {
            LOGGER.error("Failed to back up data to csv file");
        }
    }

    //TODO SELECT_IMAGE_DIRECTORY_FXML has badly defined controller class
    private File displaySelectImageDirectoryWindow(File initialDirectory) {
        FXMLLoader loader = new FXMLLoader();
        LOGGER.info(CzekiRegistry.SELECT_IMAGE_DIRECTORY_FXML);
        loader.setLocation(Czeki.class.getResource(CzekiRegistry.SELECT_IMAGE_DIRECTORY_FXML));
        BorderPane page = null;
        try {
            page = (BorderPane) loader.load();
        } catch (IOException ex) {
            LOGGER.error("Cannot load page " + CzekiRegistry.SELECT_IMAGE_DIRECTORY_FXML);
        }

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Select Image Directory");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(CzekiRegistry.primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        SelectDirectoryWindowController controller = loader.getController();

        controller.setTitle("Select Image Directory");
        controller.setInitialDirectory(initialDirectory);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        if (controller.okButtonClicked()) {
            return controller.getSelectedDirectory();
        } else {
            return initialDirectory;
        }
    }

    private boolean displayCounterPartyOverviewWindow() {
        try {
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

            controller.diplayCounterParty(null);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            LOGGER.info("OK button clicked=" + controller.isOkClicked());
            if (controller.getDisplayedCounterParty() == null) {
                LOGGER.info("CP selected is null");
            } else {
                LOGGER.info("CP selected=" + controller.getDisplayedCounterParty().toJsonString());
            }

            return controller.isOkClicked();
        } catch (IOException e) {
            LOGGER.error("Error occured while editing purchase", e);
            return false;
        }
    }

    private void importFromTextFile() {
        File textFile = FileSelectors.selectTextFileToImportFrom();
        LOGGER.info("File selected=" + textFile);
        try {
            this.importDataFromTextFile(textFile);
        } catch (IOException | CsvDataFormatException ex) {
            //TODO add here alert windows
            LOGGER.error("Failed to import data from text file", ex);
        }
    }

    private void importDataFromTextFile(File textFile) throws IOException, CsvDataFormatException {
        LOGGER.info("Read data from CSV file " + textFile);
        throw new RuntimeException("Not implemented");
        //TODO implement
    }

    /**
     * save dao content
     */
    private void saveAction() {
        saveTransactions();
        saveAccounts();
    }

    private void saveAccounts() {
        LOGGER.info("Save accounts to  " + CzekiRegistry.accountsDao.toString());
        CzekiRegistry.accountsDao.commit();
    }

    private void saveTransactions() {
        LOGGER.info("Save transactions to  " + CzekiRegistry.currentAccount.getDao().toString());
        try {
            CzekiRegistry.currentAccount.getDao().commit();
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Failed to save data!");
            alert.setContentText("Failed to save data to " + CzekiRegistry.currentAccount.getDao().toString());
            Label label = new Label("The exception stacktrace was:");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();

            LOGGER.info("Failed to save data to " + CzekiRegistry.currentAccount.getDao().toString(), ex);
        }
    }

    private void saveAsAction(File jsonFile) {
        throw new RuntimeException("Not implemented ...");
        //TODO implement...

    }

    private void saveAsTextAction(File textFile) {
        throw new RuntimeException("Not implemented");
        //TODO implement

    }

    private void backupCurrentData() throws IOException {
        File file = CzekiRegistry.currentAccount.getDao().backupToFile();
        (new TextWindowDisplay("Data based up to file", file.getAbsolutePath())).display();

    }

    private void backupCurrentDataToCsv() throws IOException {
        CzekiRegistry.currentAccount.getDao().backupToCsvFile();
    }

    private void importImages() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Czeki.class.getResource(CzekiRegistry.IMPORT_IMAGES_FXML));

            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Upload Images");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(CzekiRegistry.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FileUploadWindowController controller = loader.getController();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException ex) {
            LOGGER.error("Failed to load page " + CzekiRegistry.IMPORT_IMAGES_FXML, ex);
        }

    }

    private void closeDao() {
        try {
            //TODO add confirm before comitting data
            CzekiRegistry.currentAccount.getDao().commit();
        } catch (IOException ex) {
            LOGGER.fatal("Failed to save data", ex);
            throw new RuntimeException("Failed to save data");
        }
        currentFileNameLabel.setText(CzekiRegistry.BLANK);
    }

    private void displayBackupConfirmationAlert(File file) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Backup Confirmation");
        alert.setHeaderText("The data has been backed up to file");
        alert.setContentText(file.toString());

        alert.showAndWait();
    }

    private Account selectAccount() {
        AccountCrud accountCrud = new AccountCrud();
        return accountCrud.selectAccount();
    }

    public void displayAccountName() {
        if (CzekiRegistry.currentAccount == null) {
            accountNameLabel.setText(NO_ACCOUNT_SELECTED);
        } else {
            accountNameLabel.setText(CzekiRegistry.currentAccount.getName());
        }
    }

    /**
     * Display window with counterparties summary
     */
    private boolean displayCounterPartiesSummaryWindow() {
        CounterPartySummaryWindow counterPartySummary = new CounterPartySummaryWindow();
        counterPartySummary.setTransactions(CzekiRegistry.transactionsOverviewController.getListOfTransactions());
        return counterPartySummary.display();
    }
}
