package org.tomw.rachunki.ficc;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.log4j.Logger;
import org.tomw.fileutils.FileSelector;
import org.tomw.rachunki.core.RachunkiContext;
import org.tomw.rachunki.core.RachunkiMainRegistry;
import org.tomw.rachunki.gui.TransactionsTabController;

import java.io.File;

/**
 * wrapper around file chooser to select files with Ficc transactions
 */
public class FiccFileSelector {
    private final static Logger LOGGER = Logger.getLogger(FiccFileSelector.class.getName());


    // the key to where we store last directory used to get ficc files
    public static String FICC_FILE_DIRECTORY_1_KEY = "ficcFileDirectory1Key";
    public static String FICC_FILE_DIRECTORY_2_KEY = "ficcFileDirectory2Key";

    /**
     * get the last directory used for FICC_FILE_DIRECTORY_1_KEY and FICC_FILE_DIRECTORY_2_KEY
     * @param key key
     * @return last directory used, default directory if unknown
     */
    public File getLastDirectoryUsed(String key){
        if(!FICC_FILE_DIRECTORY_1_KEY.equals(key) && !FICC_FILE_DIRECTORY_2_KEY.equals(key)){
            String message =  "Unknown key: "+key;
            LOGGER.error(message);
            throw new RuntimeException(message);
        }

        // get context
        String lastDirectoryUsed = RachunkiMainRegistry.getRegistry().getConfig().getContext().getProperties().getProperty(key);
        if(lastDirectoryUsed!=null && (new File(lastDirectoryUsed)).exists() && (new File(lastDirectoryUsed)).isDirectory()){
            return new File(lastDirectoryUsed);
        }else{
            // TODO define some default directory somewhere
            return new File(System.getProperty("user.dir"));
        }
    }

    public FiccFileSelectorResult selectFile(int fileNumber, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file with ficc transactions");

        FiccFileSelectorResult result = new FiccFileSelectorResult();

        File initialDirectory;
        String key;
        if(fileNumber==1){
            key=FICC_FILE_DIRECTORY_1_KEY;
        }else{
            key=FICC_FILE_DIRECTORY_2_KEY;
        }
        initialDirectory = getLastDirectoryUsed(key);

        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(stage);

        if(selectedFile!=null){
            File lastUploadDirectory = selectedFile.getParentFile();
            RachunkiMainRegistry.getRegistry().getConfig().getContext().getProperties().setProperty(key,lastUploadDirectory.toString());
            result.setOkClicked(true);
            result.setSelectedFile(selectedFile);
        }else{
            result.setOkClicked(false);
            result.setSelectedFile(null);
        }

        return result;
    }


}
