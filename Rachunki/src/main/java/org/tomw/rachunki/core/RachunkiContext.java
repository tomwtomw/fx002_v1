package org.tomw.rachunki.core;

import org.tomw.config.ContextBasicFileImpl;

import java.io.File;

public class RachunkiContext extends ContextBasicFileImpl {

    private static final String LAST_TRANSACTIONS_TO_TEXT_FILE_DIRECTORY_KEY = "lastTransactionsTotextFileDirectory";

    /**
     * Normal constructor
     *
     * @param contextFileName file where context is to be stored
     */
    public RachunkiContext(String contextFileName) {
        super(contextFileName);
    }

    public void setLastTransactionsToTextDirectory(File dir){
        getProperties().setProperty(LAST_TRANSACTIONS_TO_TEXT_FILE_DIRECTORY_KEY,dir.toString());
    }

    public void setLastTransactionsToTextDirectory(String  dir){
        getProperties().setProperty(LAST_TRANSACTIONS_TO_TEXT_FILE_DIRECTORY_KEY,dir);
    }

    public File getLastTransactionsToTextDirectory(){
        String lastDir = getProperties().getProperty(LAST_TRANSACTIONS_TO_TEXT_FILE_DIRECTORY_KEY);
        if(lastDir==null){
            return new File(defaultDirname);
        }else{
            File lastDirectory = new File(lastDir);
            if(lastDirectory.exists()){
                return lastDirectory;
            }else{
                return new File(defaultDirname);
            }
        }
    }
}
