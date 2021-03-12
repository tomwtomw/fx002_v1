package org.tomw.rachunki.gui;

import java.io.File;

/**
 * Utility class to pass the result of CheckImageSelector
 */
public class CheckImageSelectorResult {
    private File selectedFile=null;
    private boolean okClicked=false;

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
}
