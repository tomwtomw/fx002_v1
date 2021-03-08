package org.tomw.documentfile;

import java.io.File;

/**
 * Context of application which uses documentFile class
 */
public interface DocumentFileApplicationContext {

    public File getLastDocumentUploadDirectory();

    public void setLastDocumentUploadDirectory(File dir);

    public File getLastDocumentDownloadDirectory();

    public void setLastDocumentDownloadDirectory(File dir);
}
