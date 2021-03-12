package org.tomw.rachunki.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.tomw.documentfile.DocumentFile;
import org.tomw.documentfile.DocumentFileChooser;
import org.tomw.documentfile.DocumentFileTabAbstractController;
import org.tomw.fxmlutils.ConfirmUploadedfileDeleteAlert;
import org.tomw.fxmlutils.CrudButtonsModeSelector;
import org.tomw.fxmlutils.TableAndFormController;
import org.tomw.guiutils.alerts.ConfirmDeleteAlert;
import org.tomw.guiutils.alerts.ConfirmationAlert;
import org.tomw.guiutils.alerts.ErrorAlert;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.core.RachunkiService;
import org.tomw.rachunki.core.RachunkiServiceImpl;
import org.tomw.rachunki.entities.Konto;
import org.tomw.rachunki.entities.Transakcja;
import org.tomw.utils.LocalDateUtils;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ResourceBundle;

import static org.tomw.utils.TomwStringUtils.BLANK;
import static org.tomw.utils.TomwStringUtils.EOL;

public class DocumentsTabController  extends DocumentFileTabAbstractController<RachunkiService> implements Initializable, TableAndFormController<DocumentFile> {

    private final static Logger LOGGER = Logger.getLogger(DocumentsTabController.class.getName());

    @FXML
    private Label mainTitleLabel = new Label("Documents");

    @FXML
    private TableView<DocumentFile> entitiesTable;

    @FXML
    private TableColumn<DocumentFile, String> documentTitleColumn;
    @FXML
    private TableColumn<DocumentFile, String> externalFileNameColumn;
    @FXML
    private TableColumn<DocumentFile, String> documentDescriptionColumn;
    @FXML
    private TableColumn<DocumentFile, LocalDateTime> uploadDateTimeColumn;

    @FXML
    private Button newButton;
    @FXML
    private Button newFromFileButton;
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

    private DocumentFile displayedEntity;

    // entries in form
    @FXML
    private Label documentTitleLabel;
    @FXML
    private TextField documentTitleTextField;
    @FXML
    private Label documentTitleErrorLabel;

    @FXML
    private Label documentInternalIdLabel;
    @FXML
    private Label documentInternalId;

    @FXML
    private Button uploadButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button deleteFileButton;
    @FXML
    private Label documentFileNameLabel;
    @FXML
    private Label documentFileNameErrorLabel;

    @FXML
    private Button documentDescriptionButton;
    @FXML
    private TextArea documentDescriptionTextArea;
    @FXML
    private Label documentDescriptionErrorLabel;

    @FXML
    private Button documentCommentButton;
    @FXML
    private TextArea documentCommentTextArea;
    @FXML
    private Label documentCommentErrorLabel;

    @FXML
    private Label uploadTimeLabel;
    @FXML
    private Label uploadTimeValueLabel;
    @FXML
    private Label uploadTimeErrorLabel;
    private LocalDateTime uploadTime = null;

    @FXML
    private Button documentViewButton;
    @FXML
    private ImageView documentViewImage;
    @FXML
    private Label documentViewImageErrorLabel;

    public Button getNewFromFileButton() {
        return newFromFileButton;
    }

    @Override
    public TableView<DocumentFile> getEntitiesTable() {
        return entitiesTable;
    }

    @Override
    public Label getDocumentFileNameLabel() {
        return documentFileNameLabel;
    }

    @Override
    public Label getUploadTimeValueLabel() {
        return uploadTimeValueLabel;
    }

    @Override
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    @Override
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize tab");
        documentTitleColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        documentTitleColumn.setCellValueFactory(cellData -> cellData.getValue().documentTitleProperty());

        externalFileNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        externalFileNameColumn.setCellValueFactory(cellData -> cellData.getValue().externalFileNameProperty());

        documentDescriptionColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        documentDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        TomwStringUtils.cutString(cellData.getValue().documentDescriptionProperty().getValue())
                ));

        uploadDateTimeColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        uploadDateTimeColumn.setCellValueFactory(cellData -> cellData.getValue().uploadDateTimeProperty());

        entitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends DocumentFile> observable, DocumentFile oldValue, DocumentFile newValue) -> {
                    displayEntity(newValue);
                });

        setButtonsSelector(new CrudButtonsModeSelector(
                newButton,
                saveButton,
                deleteButton,
                addButton,
                removeButton,
                cancelButton,
                clearButton,
                okButton)
        );
        getNewFromFileButton().setDisable(true);

        getButtonsSelector().activateButtons(isStandaloneMode(), isLimitedMode(), getCurrentEntity());
        setDisabledFlagForControls(true);
    }

    @Override
    public void setTitle(String title) {
        mainTitleLabel.setText(title);
    }

    @Override
    public boolean confirmIfRemoveFromOwners(DocumentFile documentFile) {
        Collection<Konto> accountsWhichContainDocument = getService().getAccountsWhichContain(documentFile);
        Collection<Transakcja> transactionsWhichContainDocument = getService().getTransactionsWhichContain(documentFile);

        boolean removeFromTransactionsOk=false;
        boolean removeFromAccounsOk=false;

        if(!accountsWhichContainDocument.isEmpty()) {
            String message = "Document is referenced by account(s) " + EOL;
            for (Konto konto : accountsWhichContainDocument){
                message = message + konto.getShortName() + EOL;
            }
            message=message+"Should I remove document from account?";

            if ((new ConfirmationAlert()).confirm(message)) {
                removeFromAccounsOk=true;
            }
        }else{
            removeFromAccounsOk=true;
        }
        if(!transactionsWhichContainDocument.isEmpty()) {
            String message = "Document is referenced by transaction(s) " + EOL;
            for (Transakcja transakcja : transactionsWhichContainDocument){
                message = message + transakcja + EOL;
            }
            message=message+"Should I remove document from them?";

            if ((new ConfirmationAlert()).confirm(message)) {
                removeFromTransactionsOk=true;
            }
        }else{
            removeFromTransactionsOk=true;
        }

        boolean result = removeFromAccounsOk && removeFromTransactionsOk;
        return result;
    }

    @Override
    public void removeFromOwners(DocumentFile documentFile) {
        Collection<Konto> accountsWhichContainDocument = getService().getAccountsWhichContain(documentFile);
        Collection<Transakcja> transactionsWhichContainDocument = getService().getTransactionsWhichContain(documentFile);

        boolean removeFromTransactionsOk=false;
        boolean removeFromAccounsOk=false;

        if(!accountsWhichContainDocument.isEmpty()) {
            for(Konto konto : accountsWhichContainDocument) {
                LOGGER.info("Remove " + documentFile + " from " +konto);
                if(konto.getDocuments()!=null){
                    konto.getDocuments().remove(documentFile);
                }
            }
        }
        if(!transactionsWhichContainDocument.isEmpty()) {

            for (Transakcja transakcja : transactionsWhichContainDocument){
                LOGGER.info("Remove " + documentFile + " from " +transakcja);
                if(transakcja.getDocuments()!=null){
                    transakcja.getDocuments().remove(documentFile);
                }
            }

        }
    }

    @Override
    public DocumentFile getCurrentEntity() {
        return displayedEntity;
    }

    @Override
    public void setCurrentEntity(DocumentFile documentFile) {
        this.displayedEntity = documentFile;
    }

    @FXML
    public void newButtonAction() {
        LOGGER.info("newButtonAction");
        perform_newButtonAction();
    }

    @FXML
    public void newFromFileButtonAction() {
        LOGGER.info("newFromFileButtonAction");
        perform_newFromFileButtonAction();
    }

    public void perform_newFromFileButtonAction() {

        setDisabledFlagForControls(false);
        getButtonsSelector().activateButtons(isStandaloneMode(), isLimitedMode(), null);

        createAndDisplayNewEntity();

        //give the document temporary title, to enable saving it
        String temporaryDocumentTitle = "Temporary document title "+LocalDateTime.now();
        this.documentTitleTextField.setText(temporaryDocumentTitle);

        // save the newly create entity
        perform_saveButtonAction();

        updateDisplayedEntitybasedOnFromFile();
    }

    public void updateDisplayedEntitybasedOnFromFile(){

        File startDirectoryForChooser = (getContext()).getLastDocumentUploadDirectory();
        LOGGER.info("Open directory chooser starting with" + startDirectoryForChooser);
        DocumentFileChooser chooser = new DocumentFileChooser("Select document file",
                startDirectoryForChooser,
                this.getCurrentStage());
        File file = chooser.selectDirectory();
        LOGGER.info("File=" + file);
        if (file != null && file.exists()) {
            try {
                File resultFile = getService().uploadFile(getCurrentEntity(), file);
                getDocumentTitleTextField().setText(
                        FilenameUtils.removeExtension(
                                file.getName()
                        )
                );
                getDocumentFileNameLabel().setText(file.getName());
                setUploadTime(getCurrentEntity().getUploadDateTime());
                getUploadTimeValueLabel().setText(getUploadTime().toString());

                getCurrentEntity().setDocumentTitle(file.getName());

                this.displayDocumentIcon();
                this.perform_formHasBeenEdited();
                File lastUploadDirectory = file.getParentFile();
                getContext().setLastDocumentUploadDirectory(lastUploadDirectory);


//                this.displayedEntity = new DocumentFile();
//                this.displayedEntity.setDocumentTitle(BLANK);
//                this.displayedEntity.setInternalFileName(BLANK);
//                this.displayedEntity.setExternalFileName(BLANK);
//                this.displayedEntity.setDocumentDescription(BLANK);
//                this.displayedEntity.setUploadDateTime(null);
//                this.displayedEntity.setComment(BLANK);
//                this.displayedEntity.setDocumentType(DocumentFile.DOCUMENT);

                getNewFromFileButton().setDisable(true);

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


    @FXML
    public void saveButtonAction() {
        LOGGER.info("saveButtonAction");
        perform_saveButtonAction();
    }

    @FXML
    public void deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
        if ((new ConfirmDeleteAlert()).deleteConfirmed(getCurrentEntity().getDocumentTitle())) {
            perform_deleteButtonAction();
        }
    }

    @FXML
    public void addButtonAction() {
        LOGGER.info("addButtonAction");
        perform_addButtonAction();
    }

    @FXML
    public void removeButtonAction() {
        LOGGER.info("removeButtonAction");
        perform_removeButtonAction();
    }

    @FXML
    public void clearButtonAction() {
        perform_clearButtonAction();
    }

    @FXML
    public void cancelButtonAction() {
        perform_cancelButtonAction();
    }


    @FXML
    public void okButtonAction() {
        LOGGER.info("okButtonAction");
        perform_okButtonAction();
    }

    @FXML
    public void documentFileUploadButtonAction() {
        performDocumentFileUploadAction();
    }

    @FXML
    public void documentFileDownloadButtonAction() {
        performDocumentFileDownloadButtonAction();
    }

    @FXML
    public void documentFileDeleteButtonAction() {
        performDocumentFileDeleteButtonAction();
    }

    @FXML
    public void descriptionButtonAction() {
        performDescriptionButtonAction();
    }

    @FXML
    public void commentButtonAction() {
        performCommentButtonAction();
    }

    @FXML
    public void documentViewButtonAction() {
        performDocumentViewButtonAction();
    }

    @Override
    public void initTable() {
        LOGGER.info("We are in initTable");
        if (entitiesTable == null) {
            LOGGER.error("entitiesTable is null");
        }
        resetDisplay();
        displayAllEntities();
        disableForm();
        getButtonsSelector().activateButtons(isStandaloneMode(), isLimitedMode(), null);
    }

    @Override
    public void updateDisplayedEntity() {
        getCurrentEntity().setDocumentTitle(documentTitleTextField.getText());
        getCurrentEntity().setExternalFileName(documentFileNameLabel.getText());
        getCurrentEntity().setDocumentDescription(documentDescriptionTextArea.getText());
        getCurrentEntity().setComment(documentCommentTextArea.getText());
        getCurrentEntity().setUploadDateTime(uploadTime);
    }

    @Override
    public void displayEntity(DocumentFile e) {
        LOGGER.info("Display entity " + e);
        if (e != getCurrentEntity()) getButtonsSelector().activateButtons(isStandaloneMode(), isLimitedMode(), e);
        setCurrentEntity(e);
        enableForm();
        reDisplayCurrentEntity();
        setSaveButtonEnabled(false);
    }

    @Override
    public void reDisplayCurrentEntity() {
        if (getCurrentEntity() != null) {
            documentTitleTextField.setText(getCurrentEntity().getDocumentTitle());
            documentInternalId.setText(getCurrentEntity().getId() + "");
            documentFileNameLabel.setText(getCurrentEntity().getExternalFileName());
            documentDescriptionTextArea.setText(getCurrentEntity().getDocumentDescription());
            documentCommentTextArea.setText(getCurrentEntity().getComment());
            uploadTime = getCurrentEntity().getUploadDateTime();
            uploadTimeValueLabel.setText(LocalDateUtils.toYYYYMMDDHHMMSS(uploadTime));

            displayDocumentIcon();
        }
    }

    /**
     * If the document has a file and the file is image - display its icon
     */
    @Override
    public void displayDocumentIcon(){
        Image image = getService().getImage(getCurrentEntity());
        documentViewImage.setImage(image);
    }

    @Override
    public TextField getDocumentTitleTextField() {
        return documentTitleTextField;
    }

    @Override
    public Label getDocumentTitleErrorLabel() {
        return documentTitleErrorLabel;
    }

    @Override
    public TextArea getDocumentDescriptionTextArea() {
        return documentDescriptionTextArea;
    }

    @Override
    public TextArea getDocumentCommentTextArea() {
        return documentCommentTextArea;
    }

    @Override
    public Button getSaveButton() {
        return saveButton;
    }

    @Override
    public Button getOkButton() {
        return okButton;
    }

    @Override
    public void resetErrorFields() {
        documentTitleErrorLabel.setText(BLANK);
        documentFileNameErrorLabel.setText(BLANK);
        documentDescriptionErrorLabel.setText(BLANK);
        documentCommentErrorLabel.setText(BLANK);
        uploadTimeErrorLabel.setText(BLANK);
        documentViewImageErrorLabel.setText(BLANK);
    }

    @Override
    public void clearForm() {
        documentTitleTextField.setText(BLANK);
        documentInternalId.setText(BLANK);
        documentTitleErrorLabel.setText(BLANK);
        documentFileNameLabel.setText(BLANK);
        documentFileNameErrorLabel.setText(BLANK);

        documentDescriptionTextArea.setText(BLANK);
        documentDescriptionErrorLabel.setText(BLANK);

        documentCommentTextArea.setText(BLANK);
        documentCommentErrorLabel.setText(BLANK);

        uploadTimeValueLabel.setText(BLANK);
        uploadTimeErrorLabel.setText(BLANK);

        documentViewImage.setImage(null);
        documentViewImageErrorLabel.setText(BLANK);
    }

    @FXML
    public void formHasBeenEdited() {
        perform_formHasBeenEdited();
    }


    @Override
    public void setDisabledFlagForControls(boolean flag) {

        documentTitleLabel.disableProperty().set(flag);
        documentTitleTextField.disableProperty().set(flag);
        documentTitleErrorLabel.disableProperty().set(flag);

        uploadButton.disableProperty().set(flag);
        downloadButton.disableProperty().set(flag);
        documentFileNameLabel.disableProperty().set(flag);
        documentFileNameErrorLabel.disableProperty().set(flag);

        documentDescriptionButton.disableProperty().set(flag);
        documentDescriptionTextArea.disableProperty().set(flag);
        documentDescriptionErrorLabel.disableProperty().set(flag);

        documentCommentButton.disableProperty().set(flag);
        documentCommentTextArea.disableProperty().set(flag);
        documentCommentErrorLabel.disableProperty().set(flag);

        uploadTimeLabel.disableProperty().set(flag);
        uploadTimeValueLabel.disableProperty().set(flag);
        uploadTimeErrorLabel.disableProperty().set(flag);

        documentViewButton.disableProperty().set(flag);
        documentViewImage.disableProperty().set(flag);
        documentViewImageErrorLabel.disableProperty().set(flag);
    }


    @Override
    public void setButtonsForStandaloneMode(boolean standaloneMode) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public boolean validateFormInput() {
        if (getCurrentEntity() == null) {
            LOGGER.error("Current entity is null");
            return false;
        }
        resetErrorFields();
        boolean result = true;
        result = result && validateDocumentTitle();
        // for now we onlu require title
        return result;
    }

    @Override
    public void createAndDisplayNewEntity() {
        this.displayedEntity = new DocumentFile();
        this.displayedEntity.setDocumentTitle(BLANK);
        this.displayedEntity.setInternalFileName(BLANK);
        this.displayedEntity.setExternalFileName(BLANK);
        this.displayedEntity.setDocumentDescription(BLANK);
        this.displayedEntity.setUploadDateTime(null);
        this.displayedEntity.setComment(BLANK);
        this.displayedEntity.setDocumentType(DocumentFile.DOCUMENT);

        resetDisplay();
        reDisplayCurrentEntity();
        saveButtonEnable(false);

        getNewFromFileButton().setDisable(false);
    }


    @Override
    public void displayDocumentsOverviewWindow() {
        RachunkiService serviceDocuments = new RachunkiServiceImpl(
                RachunkiMainRegistry.getRegistry().getDao(),
                RachunkiMainRegistry.getRegistry().getConfig());
        serviceDocuments.displayAllDocumentsNotPersonalPicture();

        DocumentsTab tab = new DocumentsTab();
        tab.setService(serviceDocuments);
        tab.setStandaloneMode(true);
        tab.setTitle("Edit document list");

        tab.init();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Documents");
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
}
