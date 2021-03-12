package org.tomw.ficc.gui;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 * select input csv files
 */
public class FiccCsvFileChooser {
    public static final String SELECT_CSV_FILE = "Seclect Csv File";

    private static final FileChooser inputTextFileChooser = new FileChooser();

    public static List<File> selectCsvFiles() {
        List<File> selectedFiles = null;

        inputTextFileChooser.setTitle(SELECT_CSV_FILE);
        inputTextFileChooser.setInitialDirectory(FiccRegistry.initialDirectory);
        boolean fileSelected = false;
        boolean cancelPressed = false;

        boolean filesHaveBeenSelected = false;

        while (!(filesHaveBeenSelected || cancelPressed)) {

            selectedFiles = inputTextFileChooser.showOpenMultipleDialog(FiccRegistry.primaryStage);

            if (selectedFiles != null) {
                filesHaveBeenSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedFiles;
    }
}
