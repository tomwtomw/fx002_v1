package org.tomw.rachunki.ficc;

import java.io.File;

/**
 * returns result of selecting ficc file
 */
public class FiccFileSelectorResult {
    private File selectedFile;
    private boolean okClicked;

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
