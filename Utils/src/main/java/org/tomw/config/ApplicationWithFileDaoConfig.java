package org.tomw.config;

import org.tomw.config.ApplicationConfigLoader;
import org.tomw.filestoredao.FileDaoDrirImplConfiguration;

import java.io.File;

public interface ApplicationWithFileDaoConfig<T> extends ApplicationConfigLoader, FileDaoDrirImplConfiguration {
    File getDocumentFileDirectory();
    void setDocumentFileDirectory(File dir);

    File getApplicationDirectory();
    void setApplicationDirectory(File dir);

    T getContext();
}
