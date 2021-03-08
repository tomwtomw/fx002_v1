package org.tomw.documentfile;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.daoutils.DataIntegrityException;
import org.tomw.fxmlutils.ConfirmUploadedfileDeleteAlert;
import org.tomw.fxmlutils.CrudButtonsModeSelector;
import org.tomw.fxmlutils.TableAndFormController;
import org.tomw.gui.StageProvider;
import org.tomw.gui.filechooser.DestinationDirectoryChooser;
import org.tomw.gui.textareaeditor.TextAreaEditor;
import org.tomw.gui.viewimage.ViewImageWindow;
import org.tomw.guiutils.alerts.ErrorAlert;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class DocumentFileTabAbstractController<S extends DocumentFileService> implements Initializable, TableAndFormController<DocumentFile> {
    private final static Logger LOGGER = Logger.getLogger(DocumentFileTabAbstractController.class.getName());

    private DocumentFile displayedEntity;

    private boolean edited = false;

    private S service;

    private boolean standaloneMode = false; // whether code runs as standalone window or tab
    private boolean limitedMode = false; // whether it displays all entities of particular type from db or only some
    private boolean okButtonClicked = false;

    private CrudButtonsModeSelector buttonsSelector;

    public CrudButtonsModeSelector getButtonsSelector() {
        return buttonsSelector;
    }

    public void setButtonsSelector(CrudButtonsModeSelector buttonsSelector) {
        this.buttonsSelector = buttonsSelector;
    }

    public S getService() {
        return service;
    }

    public void setService(S service) {
        this.service = service;
    }

    private DocumentFileApplicationContext context;

    public DocumentFileApplicationContext getContext() {
        return context;
    }

    public void setContext(DocumentFileApplicationContext context) {
        this.context = context;
    }

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public abstract TableView<DocumentFile> getEntitiesTable();

    public abstract Label getDocumentFileNameLabel();

    public abstract Label getUploadTimeValueLabel();

    public abstract void displayDocumentIcon();

    public abstract TextField getDocumentTitleTextField();

    public abstract Label getDocumentTitleErrorLabel();

    public abstract TextArea getDocumentDescriptionTextArea();

    public abstract TextArea getDocumentCommentTextArea();

    public abstract Button getSaveButton();

    public abstract Button getOkButton();

    public abstract LocalDateTime getUploadTime();

    public abstract void setUploadTime(LocalDateTime uploadTime);

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    @Override
    public abstract void setTitle(String title);

    @Override
    public void perform_newButtonAction() {
        LOGGER.info("newButtonAction");
        setDisabledFlagForControls(false);
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), null);
        createAndDisplayNewEntity();
    }

    @Override
    public List<DocumentFile> getDisplayedEntities() {
        List<DocumentFile> listOfDisplayedEntities = new ArrayList<>();
        for (DocumentFile entity : getService().getListOfDisplayedDocuments()) {
            listOfDisplayedEntities.add(entity);
        }
        return listOfDisplayedEntities;
    }

    @Override
    public void perform_saveButtonAction() {
        LOGGER.info("saveButtonAction");
        if (getCurrentEntity() != null) {
            boolean validationResult = validateFormInput();
            if (validationResult) {
                updateDisplayedEntity();
                saveCurrentEntity();
                formHasNotBeenEdited();
                resetErrorFields();
                refreshTable();
            }
        }
    }

    public abstract boolean confirmIfRemoveFromOwners(DocumentFile documentFile);
    public abstract void removeFromOwners(DocumentFile currentEntity);

    @Override
    public void perform_deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
        try {
            if (this.getCurrentEntity() != null) {
                boolean removeFromOwnersConfirmed = confirmIfRemoveFromOwners(this.getCurrentEntity());
                if(removeFromOwnersConfirmed) {
                    removeFromOwners(this.getCurrentEntity());
                    getService().delete(this.getCurrentEntity());
                    getService().removeFromDisplay(this.getCurrentEntity());
                    if (getService().getListOfDisplayedDocuments().isEmpty()) {
                        this.clearForm();
                        this.setCurrentEntity(null);
                    }

                }
            }
        } catch (DataIntegrityException e) {
            //TODO display error window here
            LOGGER.error("Failed to delete entity " + getCurrentEntity(), e);
        }
    }



    @Override
    public void perform_addButtonAction() {
        LOGGER.info("addButtonAction");
        int numberOfDocumentsBefore = getService().getListOfDisplayedDocuments().size();
        displayDocumentsOverviewWindow();
        int numberOfDocumentsAfter = getService().getListOfDisplayedDocuments().size();
        if (numberOfDocumentsBefore != numberOfDocumentsAfter) {
            perform_formHasBeenEdited();
        }
    }

    @Override
    public void perform_removeButtonAction() {
        LOGGER.info("removeButtonAction");
        getService().removeFromDisplay(getCurrentEntity());
    }

    @Override
    public void perform_clearButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public void perform_cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
        this.okButtonClicked = false;
        if (isStandaloneMode()) {
            closeWindow();
        }
    }

    @Override
    public void perform_okButtonAction() {
        LOGGER.info("okButtonAction");
        this.okButtonClicked = true;
        if (isStandaloneMode()) {
            closeWindow();
        }
    }

    public void performDocumentFileUploadAction(){
        LOGGER.info("documentFileUploadButtonAction");
        LOGGER.info("Upload document file for entity "+getCurrentEntity());
        if(getCurrentEntity()==null || getCurrentEntity().getId()==0 ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Document first");
            alert.setHeaderText("Before uploading file save the document first ");
            alert.setContentText("Document needs to be saved before file is uploaded.");

            alert.showAndWait();
        }else {

            File startDirectoryForChooser = (getContext()).getLastDocumentUploadDirectory();
            LOGGER.info("Open directory chooser starting with" + startDirectoryForChooser);
            DocumentFileChooser chooser = new DocumentFileChooser("Test",
                    startDirectoryForChooser,
                    this.getCurrentStage());
            File file = chooser.selectDirectory();
            LOGGER.info("File=" + file);
            if (file != null && file.exists()) {
                try {
                    File resultFile = this.service.uploadFile(getCurrentEntity(), file);
                    getDocumentFileNameLabel().setText(file.getName());
                    setUploadTime(getCurrentEntity().getUploadDateTime());
                    getUploadTimeValueLabel().setText(getUploadTime().toString());
                    this.displayDocumentIcon();
                    this.perform_formHasBeenEdited();
                    File lastUploadDirectory = file.getParentFile();
                    getContext().setLastDocumentUploadDirectory(lastUploadDirectory);

                    // ask if delete the original file
                    (new ConfirmUploadedfileDeleteAlert()).askForConfirmation(file);
                } catch (IOException e) {
                    String message = "Failed to upload file " + file;
                    LOGGER.error(message, e);
                    ErrorAlert alert = new ErrorAlert();
                    alert.error(message);
                }
            }
        }
    }


    public void performDocumentFileDownloadButtonAction(){
        LOGGER.info("documentFileDownloadButtonAction");
        if (getCurrentEntity().getInternalFileName() != null) {
            // select directory where to download
            DestinationDirectoryChooser chooser = new DestinationDirectoryChooser("Select destination directory",
                    getContext().getLastDocumentDownloadDirectory(),
                    this.getCurrentStage());
            File directory = chooser.selectDirectory();
            if (directory != null && directory.exists() && directory.isDirectory()) {
                try {
                    File destinationFile = this.service.downloadFile(getCurrentEntity(), directory);
                    getContext().setLastDocumentDownloadDirectory(directory);
                    //TODO add window with display message
                } catch (IOException e) {
                    String message = "Failed to download file " + getCurrentEntity().getExternalFileName();
                    LOGGER.error(message, e);
                    //TODO add error window here
                }
            }
        }
    }

    public void performDocumentFileDeleteButtonAction(){
        LOGGER.info("documentFileDeleteButtonAction");
        if (getCurrentEntity().getInternalFileName() != null) {
            //TODO add confirmation window here
            this.service.deleteFileFromDocument(getCurrentEntity());
            this.reDisplayCurrentEntity();
        }
    }

    public void performDescriptionButtonAction(){
        LOGGER.info("descriptionButtonAction");
        TextAreaEditor editor = new TextAreaEditor(
                "Description of document " + getDocumentTitleTextField().getText(),
                getDocumentDescriptionTextArea().getText());
        if (editor.okClicked()) {
            if (!getDocumentDescriptionTextArea().getText().equals(editor.getText())) {
                perform_formHasBeenEdited();
                getDocumentDescriptionTextArea().setText(editor.getText());
            }
        }
    }

    public void performCommentButtonAction(){
        LOGGER.info("commentButtonAction");
        TextAreaEditor editor = new TextAreaEditor(
                "Comments about " + getDocumentTitleTextField().getText(),
                getDocumentCommentTextArea().getText());
        if (editor.okClicked()) {
            if (!getDocumentCommentTextArea().getText().equals(editor.getText())) {
                perform_formHasBeenEdited();
                getDocumentCommentTextArea().setText(editor.getText());
            }
        }
    }

    public void performDocumentViewButtonAction(){
        LOGGER.info("documentViewButtonAction");
        if(this.service.canBeDisplayed(getCurrentEntity())) {
            ViewImageWindow viewImageWindow = new ViewImageWindow();
            viewImageWindow.setPrimaryStage(getStage());
            viewImageWindow.display(this.service.getImage(getCurrentEntity()));
        }
    }

    @Override
    public abstract void initTable();

    @Override
    public void displayAllEntities() {
        if (!isStandaloneMode()) {
            //getService().displayAllDocuments();
            getService().displayAllDocumentsNotPersonalPicture();
        }
        getEntitiesTable().setItems(getService().getListOfDisplayedDocuments());
    }

    @Override
    public abstract void updateDisplayedEntity();

    @Override
    public void displayEntity(DocumentFile e) {
        LOGGER.info("Display entity " + e);
        if (e != getCurrentEntity()) this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), e);
        setCurrentEntity(e);
        enableForm();
        reDisplayCurrentEntity();
        setSaveButtonEnabled(false);
    }

    @Override
    public abstract void reDisplayCurrentEntity();

    @Override
    public abstract void resetErrorFields();

    @Override
    public abstract void clearForm();

    @Override
    public void perform_formHasBeenEdited() {
        setEdited(true);
        this.buttonsSelector.activateButtons(isStandaloneMode(), isLimitedMode(), getCurrentEntity());
        saveButtonEnable(true);
        resetErrorFields();
    }

    @Override
    public void formHasNotBeenEdited() {
        setEdited(false);
        saveButtonEnable(false);
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
    public abstract void setDisabledFlagForControls(boolean flag);

    @Override
    public void setSaveButtonEnabled(boolean enable) {
        if (enable) {
            getSaveButton().setDisable(false);
        } else {
            getSaveButton().setDisable(true);
        }
    }

    @Override
    public void setButtonsForStandaloneMode(boolean standaloneMode) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public abstract boolean validateFormInput();

    @Override
    public void refresh() {
       getService().displayAllDocuments();
    }

    public void refreshTable() {
        getEntitiesTable().getColumns().get(0).setVisible(false);
        getEntitiesTable().getColumns().get(0).setVisible(true);
    }

    @Override
    public boolean isStandaloneMode() {
        return standaloneMode;
    }

    @Override
    public void setStandaloneMode(boolean standaloneMode) {
        this.standaloneMode = standaloneMode;
    }

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
    public Stage getCurrentStage() {
        return (Stage) getOkButton().getScene().getWindow();
    }

    @Override
    public abstract void createAndDisplayNewEntity();

    @Override
    public void saveCurrentEntity() {
        LOGGER.info("save " + getCurrentEntity());
        getService().save(getCurrentEntity());
    }

    @Override
    public void saveButtonEnable(boolean b) {
        getSaveButton().setDisable(!b);
    }

    public boolean validateDocumentTitle() {
        if (getDocumentTitleTextField().getText() == null ||
                TomwStringUtils.BLANK.equals(getDocumentTitleTextField().getText().trim())) {
            LOGGER.error("Document title is blank");
            getDocumentTitleErrorLabel().setText(TomwStringUtils.ERROR);
            return false;
        }
        return true;
    }

    public abstract void displayDocumentsOverviewWindow();
}
