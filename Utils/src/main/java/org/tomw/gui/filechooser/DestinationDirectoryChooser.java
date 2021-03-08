package org.tomw.gui.filechooser;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class DestinationDirectoryChooser {
    private static final DirectoryChooser chooser = new DirectoryChooser();

    private String windowTitle="undefined title";
    private File startDirectory;
    private Stage stage;

    public DestinationDirectoryChooser(String title, File startDirectory, Stage stage){
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

            selectedDirectory = chooser.showDialog(this.stage);

            if (selectedDirectory != null) {
                fileSelected = true;
            } else {
                cancelPressed = true;
            }
        }
        return selectedDirectory;
    }
}
