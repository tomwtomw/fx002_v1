package org.tomw.guiutils.documentgalery.gui;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.tomw.fxmlutils.TableAndFormController;
import org.tomw.guiutils.documentgalery.core.DocumentGaleryRegistry;
import org.tomw.guiutils.documentgalery.core.ImageFileListService;
import org.tomw.gui.viewimage.ViewImageWindow;
import ort.tomw.guiutils.entities.imagefile.ImageFile;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.tomw.utils.TomwStringUtils.BLANK;
import static org.tomw.utils.TomwStringUtils.ERROR;

public class DocumentGalleryController implements Initializable, TableAndFormController<ImageFile> {
    private final static Logger LOGGER = Logger.getLogger(DocumentGalleryController.class.getName());


    @FXML
    private TableView<ImageFile> entitiesTable;
    @FXML
    private TableColumn<ImageFile, String> documentNameColumn;

    @FXML
    private ImageView imageView;

    @FXML
    private Label documentNameLabel;
    @FXML
    private TextField documentNameTextField;
    @FXML
    private Label documentErrorLabel;

    @FXML
    private Label documentFileLabel;
    @FXML
    private Button uploadButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button deleteDocumentFileButton;
    @FXML
    private Label documentFileErrorLabel;
    private File displayedDocumentFile;

    @FXML
    private Label documentDescriptionLabel;
    @FXML
    private TextArea documentDescriptionTextArea;
    @FXML
    private Label documentDescriptionErrorLabel;

    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private boolean okClicked = false;

    @FXML
    private AnchorPane anchorPane;

    private ImageFile displayedEntity;
    private File currentlySelectedFile;

    private boolean edited = false;

    private boolean limitedMode = false; // whether it displays all entities of particular type from db or only some

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    private ImageFileListService service;

    public void setService(ImageFileListService service) {
        this.service = service;
    }

    public File getCurrentlySelectedFile() {
        return currentlySelectedFile;
    }

    public void setCurrentEntity(ImageFile currentEntity) {
        this.displayedEntity = currentEntity;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize controller");
        documentNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        documentNameColumn.setCellValueFactory(cellData -> cellData.getValue().namePropertyProperty());

        entitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends ImageFile> observable, ImageFile oldValue, ImageFile newValue) -> {
                    displayEntity(newValue);
                });
    }

    @Override
    public void setTitle(String title) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public ImageFile getCurrentEntity() {
        return this.displayedEntity;
    }

    @Override
    public List<ImageFile> getDisplayedEntities() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( List<ImageFile> != "void" )return null;# end
    }

    @FXML
    public void uploadButtonAction() {
        LOGGER.info("uploadButtonAction");
        FileChooser imageFileChooser = new FileChooser();
        List<File> selectedFiles = imageFileChooser.showOpenMultipleDialog(DocumentGaleryRegistry.primaryStage);
        if (selectedFiles != null && selectedFiles.size() > 0) {
            File selectedFile = selectedFiles.get(0);
            LOGGER.info("selected file=" + selectedFile);
            service.upload(this.displayedEntity, selectedFile);
            this.displayedDocumentFile = this.displayedEntity.getFile();
            displayFile(this.displayedDocumentFile);
        }

    }

    private void displayFile(File file) {
        if (file != null && file.exists()) {
            this.imageView.setImage(
                    new Image(file.toURI().toString()));
        }else{
            this.imageView.setImage(null);
        }
    }

    @FXML
    public void downloadButtonAction() {
        LOGGER.info("downloadButtonAction");

        DirectoryChooser imageFileChooser = new DirectoryChooser();
        File selectedDirectory = imageFileChooser.showDialog(DocumentGaleryRegistry.primaryStage);
        if (selectedDirectory != null) {
            LOGGER.info("selected directory=" + selectedDirectory);
            service.downloadFile(this.getCurrentEntity(),selectedDirectory);
        }
    }

    @FXML
    public void viewButtonAction(){
        ViewImageWindow viewImageWindow = new ViewImageWindow();
        viewImageWindow.setPrimaryStage(DocumentGaleryRegistry.primaryStage);
        viewImageWindow.display(getCurrentEntity().getFile());
    }

    @FXML
    public void deleteFileButtonAction() {
        //TODO implement
        LOGGER.info("deleteFileButtonAction");
    }

    @FXML
    public void newButtonAction() {
        perform_newButtonAction();
    }

    @Override
    public void perform_newButtonAction() {
        LOGGER.info("newButtonAction");

        enableForm();

        resetDisplay();
        displayedEntity = new ImageFile();
        reDisplayCurrentEntity();
    }

    @FXML
    public void saveButtonAction() {
        LOGGER.info("saveButtonAction");
        perform_saveButtonAction();
    }

    @Override
    public void perform_saveButtonAction() {
        LOGGER.info("perform_saveButtonAction");

        if (validateFormInput()) {
            updateDisplayedEntity();
            service.add(getCurrentEntity());
            refreshTable();
        } else {
            LOGGER.error("Error occured while validating form");
        }
    }

    @FXML
    public void deleteButtonAction() {
        perform_deleteButtonAction();
    }
    @Override
    public void perform_deleteButtonAction() {
        LOGGER.info("deleteButtonAction");
        service.delete(getCurrentEntity());
    }


    @FXML
    public void addButtonAction() {
        perform_addButtonAction();
    }
    @Override
    public void perform_addButtonAction() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
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
        LOGGER.info("clearButtonAction");
        perform_clearButtonAction();
    }

    @Override
    public void perform_clearButtonAction() {
        LOGGER.info("perform_clearButtonAction");
        resetDisplay();
    }

    @FXML
    public void cancelButtonAction() {
        perform_cancelButtonAction();
    }
    @Override
    public void perform_cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
        okClicked = false;
        closeWindow();
    }

    @FXML
    public void okButtonAction() {
        LOGGER.info("okButtonAction");
        okClicked = true;
        closeWindow();
    }
    @Override
    public void perform_okButtonAction() {
        LOGGER.info("okButtonAction");
        okClicked = true;
        closeWindow();
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
    }

    @Override
    public void displayAllEntities() {
        entitiesTable.setItems(service.getList());
    }

    @Override
    public void updateDisplayedEntity() {
        getCurrentEntity().setName(documentNameTextField.getText());
        getCurrentEntity().setComment(documentDescriptionTextArea.getText());
        getCurrentEntity().setFile(getCurrentlySelectedFile());
    }

    /**
     * display entity
     *
     * @param e entity to be displayed
     */
    @Override
    public void displayEntity(ImageFile e) {
        LOGGER.info("Display entity " + e);
        this.displayedEntity = e;
        enableForm();
        reDisplayCurrentEntity();
        setSaveButtonEnabled(false);
    }

    @Override
    public void reDisplayCurrentEntity() {
        ImageFile imageFile = getCurrentEntity();
        if (imageFile != null) {
            documentNameTextField.setText(imageFile.getName());

            if (imageFile.getFile() != null && imageFile.getFile().exists()) {
                this.displayedDocumentFile = imageFile.getFile();
                this.imageView.setImage(
                        new Image(this.displayedDocumentFile.toURI().toString()));
            }

            documentDescriptionTextArea.setText(imageFile.getComment());
        }
    }

    @Override
    public void resetErrorFields() {
        documentDescriptionErrorLabel.setText(BLANK);
        documentFileErrorLabel.setText(BLANK);
        documentErrorLabel.setText(BLANK);
    }

    /**
     * clear form
     */
    @Override
    public void clearForm() {
        documentNameTextField.setText(BLANK);
        documentDescriptionTextArea.setText(BLANK);
        displayFile(null);
    }

    @FXML
    public void formHasBeenEdited() {
        perform_formHasBeenEdited();
    }

    @Override
    public void perform_formHasBeenEdited() {
        setSaveButtonEnabled(true);
        enableForm();
    }

    @Override
    public void setEdited(boolean edited) {
        this.edited = edited;
        if (this.edited) {
            formHasBeenEdited();
        } else {
            setSaveButtonEnabled(false);
        }
    }

    @Override
    public boolean formHasUnsavedChanges() {
        return edited;
    }

    @Override
    public void setDisabledFlagForControls(boolean flag) {
        documentDescriptionTextArea.disableProperty().set(flag);
        documentNameTextField.disableProperty().set(flag);
        uploadButton.disableProperty().set(flag);
        downloadButton.disableProperty().set(flag);
        deleteDocumentFileButton.disableProperty().set(flag);

        saveButton.disableProperty().set(flag);
        deleteButton.disableProperty().set(flag);
        clearButton.disableProperty().set(flag);
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
        updateDocumentsTable();
    }

    @Override
    public void setStandaloneMode(boolean mode) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public boolean isStandaloneMode() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( boolean != "void" )return false;# end
    }

    @Override
    public boolean isLimitedMode() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( boolean != "void" )return false;# end
    }

    @Override
    public void setLimitedMode(boolean limitedMode) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public boolean okClicked() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( boolean != "void" )return false;# end
    }

    private void updateDocumentsTable() {
        //TODO implement
    }

    @Override
    public boolean validateFormInput() {
        boolean result = true;
        result = result && validateName();
        result = result && validateFile();
        return result;
    }

    private boolean validateName() {
        boolean resultOk = StringUtils.isNotBlank(documentNameTextField.getText());
        if (resultOk) {
            return true;
        } else {
            LOGGER.error("Failed validation file name");
            this.documentErrorLabel.setText(ERROR);
            return false;
        }
    }

    private boolean validateFile() {
        File f = getCurrentEntity().getFile();
        boolean resultOk = f != null && f.exists();
        if (resultOk) {
            return true;
        } else {
            LOGGER.error("Failed validation file");
            this.documentFileErrorLabel.setText(ERROR);
            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void createAndDisplayNewEntity() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public void saveCurrentEntity() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public void formHasNotBeenEdited() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public void saveButtonEnable(boolean b) {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( void != "void" )return 0;# end
    }

    @Override
    public Stage getCurrentStage() {
        //TODO implement
        throw new RuntimeException("Not implemented");
        //if ( Stage != "void" )return null;# end
    }

    public ObservableList<ImageFile> getObservableListOfDocuments() {
        return service.getList();
    }

    private void refreshTable() {
        entitiesTable.getColumns().get(0).setVisible(false);
        entitiesTable.getColumns().get(0).setVisible(true);
    }
}
