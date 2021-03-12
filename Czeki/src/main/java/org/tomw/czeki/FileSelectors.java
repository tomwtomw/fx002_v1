/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author tomw
 */
public class FileSelectors {

    private static final String OPEN_RESOURCE_FILE = "Open Resource File";
    private static final String SELECT_FILE_TO_SAVE_TO = "Select file to save data";
    private static final String SELECT_TEXT_FILE_TO_SAVE_TO="Select text file to save data to";
    private static final String OPEN_JSON_FILE="Select Data file to open:";

    private static final FileChooser inputTextFileChooser = new FileChooser();
    private static final FileChooser outputJsonFileChooser = new FileChooser();
    private static final FileChooser outputTextFileChooser = new FileChooser();
    private static final FileChooser inputJsonFileChooser= new FileChooser();
    
    private static final FileChooser imageFileChooser= new FileChooser();
    private static final String SELECT_IMAGE_FILE="Select image file";
    
    private static final DirectoryChooser  directoryChooser = new DirectoryChooser();
    private static final String SELECT_IMAGE_DIRECTORY="Select Images Directory";

    public static File selectDirectory(){
        directoryChooser.setTitle(SELECT_IMAGE_DIRECTORY);
        directoryChooser.setInitialDirectory(CzekiRegistry.dataDirectory);
        
        boolean directorySelected = false;
        boolean cancelPressed = false;
        
        File selectedDirectory = null;

        while (!(directorySelected || cancelPressed)) {

            selectedDirectory = directoryChooser.showDialog(CzekiRegistry.primaryStage);

            if (selectedDirectory != null) {
                directorySelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedDirectory;
    }
    
    
    public static File selectTextFileToImportFrom() {
        inputTextFileChooser.setTitle(OPEN_RESOURCE_FILE);
        inputTextFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = inputTextFileChooser.showOpenDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }
    
    public static File selectTextFileToImportFromAllowNonexistent() {
        inputTextFileChooser.setTitle(OPEN_RESOURCE_FILE);
        inputTextFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = inputTextFileChooser.showSaveDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }

    public static File selectJsonFileToSaveTo() {
        outputJsonFileChooser.setTitle(SELECT_FILE_TO_SAVE_TO);
        outputJsonFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = outputJsonFileChooser.showSaveDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }

    public static File selectTextFileToSaveTo() {
        outputTextFileChooser.setTitle(SELECT_TEXT_FILE_TO_SAVE_TO);
        outputTextFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = outputTextFileChooser.showSaveDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }

    public static File selectJsonFileToOpen() {
        inputJsonFileChooser.setTitle(OPEN_JSON_FILE);
        inputJsonFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = inputJsonFileChooser.showOpenDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }
    
    public static File selectImageFileToOpen() {
        imageFileChooser.setTitle(SELECT_IMAGE_FILE);
        imageFileChooser.setInitialDirectory(CzekiRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedFile = null;

        while (!(fileSelected || cancelPressed)) {

            selectedFile = imageFileChooser.showOpenDialog(CzekiRegistry.primaryStage);

            if (selectedFile != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFile;
    }
}
