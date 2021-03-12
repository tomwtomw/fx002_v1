package org.tomw.rachunki.gui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tomw.rachunki.config.RachunkiConfiguration;
import org.tomw.rachunki.core.RachunkiContext;
import org.tomw.rachunki.core.RachunkiMainRegistry;

import java.io.File;

/**
 * Utility class to select for upload check image files
 */
public class CheckImageSelector {

    private RachunkiConfiguration config = RachunkiMainRegistry.getRegistry().getConfig();

    private RachunkiContext context = config.getContext();

    public CheckImageSelectorResult selectSingleFile(Stage stage){
        File startDirectory =  context.getLastDocumentUploadDirectory();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select check image file");
        fileChooser.setInitialDirectory(startDirectory);

        File selectedFile = fileChooser.showOpenDialog(stage);

        CheckImageSelectorResult result = new CheckImageSelectorResult();

        if(selectedFile!=null){
            File lastUploadDirectory = selectedFile.getParentFile();
            context.setLastDocumentUploadDirectory(lastUploadDirectory);
            result.setOkClicked(true);
            result.setSelectedFile(selectedFile);
        }else{
            result.setOkClicked(false);
            result.setSelectedFile(null);
        }

        return result;
    }
}
