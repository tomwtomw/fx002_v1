package org.tomw.documentfile;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DocumentFileChooser {
    private static final FileChooser chooser = new FileChooser();

    private String windowTitle="undefined title";
    private File startDirectory;
    private Stage stage;

    public DocumentFileChooser(String title, File startDirectory, Stage stage){
        this.windowTitle=title;
        this.startDirectory=startDirectory;
        this.stage=stage;
    }

    public  File selectDirectory(){
        chooser.setTitle(windowTitle);
        chooser.setInitialDirectory(startDirectory);

        boolean fileSelected = false;
        boolean cancelPressed = false;

        File selectedDirectory = null;

        while (!(fileSelected || cancelPressed)) {

            selectedDirectory = chooser.showOpenDialog(this.stage);

            if (selectedDirectory != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedDirectory;
    }
}
