package org.tomw.filestoredao;

import org.tomw.filestoredao.FileDaoDrirImplConfiguration;

import java.io.File;

/**
 * Implementation of configurator for File Dao class, to be used for tests only.
 */
public class FileDaoDirImplConfigurationTest implements FileDaoDrirImplConfiguration {

    private File fileStoreDirectory;

    @Override
    public File getFileStoreDirectory() {
        return fileStoreDirectory;
    }

    @Override
    public void setFileStoreDirectory(File fileStoreDirectory) {
        this.fileStoreDirectory = fileStoreDirectory;
    }

    public FileDaoDirImplConfigurationTest(){

    }

    public FileDaoDirImplConfigurationTest(File dir){
        this.fileStoreDirectory =dir;
    }
}
