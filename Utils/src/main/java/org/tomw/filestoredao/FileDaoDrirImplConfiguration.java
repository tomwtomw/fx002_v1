package org.tomw.filestoredao;

import java.io.File;

/**
 * describes file store.
 * Allows set the location of storage directory
 */
public interface FileDaoDrirImplConfiguration {
     File getFileStoreDirectory();
     void setFileStoreDirectory(File fileStoreDirectory);
}
