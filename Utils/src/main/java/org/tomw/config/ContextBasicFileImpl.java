package org.tomw.config;

import org.tomw.documentfile.DocumentFileApplicationContext;

import java.io.File;

/**
 * Simple extension of ContextFileImpl. To be shared between students and AddresBook applications
 */
public class ContextBasicFileImpl extends ContextFileImpl implements DocumentFileApplicationContext {
    public static final String LAST_DOCUMENT_UPLOAD_DIR_KEY = "lastDocumentUploadDirectory";
    public static final String LAST_DOCUMENT_DOWNLOAD_DIR_KEY = "lastDocumentDownloadDirectory";

    public static final String defaultDirname = "C:\\Users\\tomw\\Documents"; //TODO make it instance dependent

    /**
     * Normal constructor
     *
     * @param contextFileName file where context is to be stored
     */
    public ContextBasicFileImpl(String contextFileName) {
        super(contextFileName);
    }

    @Override
    public File getLastDocumentUploadDirectory() {
        String dirname = this.getProperties().getProperty(LAST_DOCUMENT_UPLOAD_DIR_KEY);
        if (dirname == null || !(new File(dirname)).exists()) {
            return new File(defaultDirname);
        } else {
            return new File(dirname);
        }
    }

    @Override
    public void setLastDocumentUploadDirectory(File dir) {
        this.getProperties().setProperty(LAST_DOCUMENT_UPLOAD_DIR_KEY, dir.toString());
    }

    @Override
    public File getLastDocumentDownloadDirectory() {
        String dirname = this.getProperties().getProperty(LAST_DOCUMENT_DOWNLOAD_DIR_KEY);
        if (dirname == null || !(new File(dirname)).exists()) {
            return new File(defaultDirname);
        } else {
            return new File(dirname);
        }
    }

    @Override
    public void setLastDocumentDownloadDirectory(File dir) {
        this.getProperties().setProperty(LAST_DOCUMENT_DOWNLOAD_DIR_KEY, dir.toString());
    }
}
