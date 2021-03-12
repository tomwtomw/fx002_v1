/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.fileupload;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.floor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.fileutils.FileTransporter;

/**
 * FXML Controller class
 *
 * @author tomw
 */
public class FileUploadWindowController implements Initializable {

    private final static Logger LOGGER = Logger.getLogger(FileUploadWindowController.class.getName());

    @FXML
    private Button cancelButton;

    @FXML
    private Button selectFilesButton;

    @FXML
    private Button clearFileSelectionButton;

    @FXML
    private Button uploadFilesButton;

    @FXML
    private Label imageDirectoryLabel;

    @FXML
    private Label uploadedFileNameLabel;
    @FXML
    private Label uploadStatusLabel;

    @FXML
    private ListView<File> listViewImages;

    final ObservableList<File> listViewData = FXCollections.observableArrayList();

    final List<File> filesToBeUploaded = new ArrayList<>();

    @FXML
    ProgressBar progressBar = new ProgressBar();

    boolean uploadFinished = false;
    long bytesTransferred = 0;
    long bytesToBeTransferred = 0;

    private final String DONE = "Done!";

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageDirectoryLabel.setText(CzekiRegistry.context.getMostRecentImageUploadDirectory().toString());

        listViewImages.setItems(listViewData);

        listViewImages.setCellFactory((list) -> {
            return new ListCell<File>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        resetTransferInfo();

        enableButtons();

        progressBar.setProgress(0);

    }

    @FXML
    private void selectFilesButtonAction() {
        LOGGER.info("selectFilesButtonAction");

        resetTransferInfo();

        List<File> listOfSelectedImages = ImageFileSelector.selectImageFiles(CzekiRegistry.currentAccount.getImageDirectory());

        if (listOfSelectedImages != null) {
            updateContext(listOfSelectedImages);

            updateDisplay(listOfSelectedImages);

            for (File file : listOfSelectedImages) {
                addToListView(file);
            }
        }
    }

    @FXML
    private void clearFileSelectionAction() {
        resetTransferInfo();
        listViewData.clear();
    }

    @FXML
    private void uploadFilesButtonAction() {
        LOGGER.info("uploadFilesButtonAction");
        if (listViewData.size() > 0) {
            uploadFiles();
        }
    }

    @FXML
    private void cancelButtonAction() {
        LOGGER.info("cancelButtonAction");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void updateContext(List<File> listOfSelectedImages) {
        if (listOfSelectedImages.size() > 0) {
            File imagesUploadDirectory = listOfSelectedImages.get(0).getParentFile();
            CzekiRegistry.context.setMostRecentImageUploadDirectory(imagesUploadDirectory);
        }
    }

    private void updateDisplay(List<File> listOfSelectedImages) {
        if (listOfSelectedImages.size() > 0) {
            File imagesUploadDirectory = listOfSelectedImages.get(listOfSelectedImages.size()-1).getParentFile();
            imageDirectoryLabel.setText(imagesUploadDirectory.toString());
        }
    }

    public void addToListView(File file) {
        if (!listViewData.contains(file)) {
            listViewData.add(file);
        }
    }

    private void uploadFiles() {

        disableButtons();
        
        filesToBeUploaded.clear();
        for (File file : listViewData) {
            filesToBeUploaded.add(file);
        }

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int numberOfFilesToBeUploaded = listViewData.size();
                int numberOfFilesUploadedSoFar = 0;

                long numberOfBytesToBeUploaded = getTotalSize(filesToBeUploaded);
                long numberOfBytesUploadedSoFar = 0;

                for (File file : filesToBeUploaded) {
                    if (isCancelled()) {
                        break;
                    }
                    try {
                        setFileNameLabelText(file.getName());
                        
                        FileTransporter.copyFile(file, CzekiRegistry.currentAccount.getImageDirectory());
                        numberOfFilesUploadedSoFar = numberOfFilesUploadedSoFar + 1;
                        numberOfBytesUploadedSoFar = numberOfBytesUploadedSoFar + file.length();

                        updateUploadStatus(
                                numberOfFilesUploadedSoFar,
                                numberOfFilesToBeUploaded,
                                numberOfBytesUploadedSoFar,
                                numberOfBytesToBeUploaded);

                        final File fileToBeRemoved = file;
                        Platform.runLater(() -> {
                            listViewData.remove(fileToBeRemoved);
                        });
                    } catch (IOException ex) {
                        LOGGER.error("Failed to transfer file " + file);
                    }
                    updateProgress(numberOfFilesUploadedSoFar, numberOfFilesToBeUploaded);
                    updateMessage(String.valueOf(numberOfFilesUploadedSoFar));
                }
                resetTransferInfo();
                setStatusLabelDone();
                enableButtonsFromTask();
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);

        thread.start();
    }

    private void resetTransferInfo() {
        resetStatusLabel();
        resetFileNameLabel();
    }

    private void resetStatusLabel() {
        setStatusLabelText(CzekiRegistry.BLANK);
        setStatusLabelColor(Color.BLACK);
    }

    private void setStatusLabelDone() {
        setStatusLabelText(DONE);
        setStatusLabelColor(Color.GREEN);
    }

    private void updateUploadStatus(int filesCopied, int allFiles, long bytesCopied, long allBytes) {

        int percent = (int) floor(100.0 * (double) bytesCopied / (double) allBytes);

        String s = "Files: " + filesCopied + "/" + allFiles + " bytes: "
                + bytesCopied + "/" + allBytes + " (" + percent + "%)";
        setStatusLabelText(s);
    }

    private void setStatusLabelText(String text) {
        final String labelText = text;
        Platform.runLater(() -> {
            uploadStatusLabel.setText(labelText);
        });
    }

    private void setStatusLabelColor(Color color) {
        Platform.runLater(() -> {
            uploadStatusLabel.setTextFill(color);
        });
    }

    private void resetFileNameLabel() {
        setFileNameLabelText(CzekiRegistry.BLANK);
    }

    private void setFileNameLabelText(String text) {
        Platform.runLater(() -> {
            uploadedFileNameLabel.setText(text);
        });
    }

    private long getTotalSize(List<File> filesToBeUploaded) {
        long sum = 0;
        for (File file : filesToBeUploaded) {
            sum = sum + file.length();
        }
        return sum;
    }
    
    private void disableButtonsFromTask(){
        Platform.runLater(() -> {
            disableButtons();
        });
    }
    
    private void enableButtonsFromTask(){
        Platform.runLater(() -> {
            enableButtons();
        });
    }

    private void disableButtons() {
        setButtonsDisabledTo(true);
    }

    private void enableButtons() {
        setButtonsDisabledTo(false);
    }

    private void setButtonsDisabledTo(boolean b) {
        cancelButton.setDisable(b);
        selectFilesButton.setDisable(b);
        clearFileSelectionButton.setDisable(b);
        uploadFilesButton.setDisable(b);
    }

}
