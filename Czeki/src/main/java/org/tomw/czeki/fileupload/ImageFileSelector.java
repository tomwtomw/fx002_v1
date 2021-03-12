/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.fileupload;

import org.tomw.czeki.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

/**
 *
 * @author tomw
 */
public class ImageFileSelector {

    private static final String SELECT_IMAGE_FILES = "Select Image Files";

    private static final FileChooser imageFileChooser = new FileChooser();
    private static final String SELECT_IMAGE_FILE = "Select image file";

    static List<File> selectImageFiles(File startDirectory) {
        imageFileChooser.setTitle(SELECT_IMAGE_FILES);
        imageFileChooser.setInitialDirectory(startDirectory);
        boolean filesSelected = false;
        boolean cancelPressed = false;

        List<File> selectedFiles = new ArrayList<>();

        while (!(filesSelected || cancelPressed)) {

            selectedFiles = imageFileChooser.showOpenMultipleDialog(CzekiRegistry.primaryStage);

            if (selectedFiles == null) {
                cancelPressed = true;
            } else {
                if (selectedFiles.size() > 0) {
                    filesSelected = true;
                } else {
                    cancelPressed = true;
                }
            }
        }
        return selectedFiles;
    }
}
