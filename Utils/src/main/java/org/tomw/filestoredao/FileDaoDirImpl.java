package org.tomw.filestoredao;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Disk implementation for file storage
 */
public class FileDaoDirImpl implements FileDao {
    private final static Logger LOGGER = Logger.getLogger(FileDaoDirImpl.class.getName());

    private FileDaoDrirImplConfiguration configuration;

    public FileDaoDirImpl(FileDaoDrirImplConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void upload(File externalFile, String internalFileName) throws IOException {
        upload(externalFile, internalFileName, false);
    }

    @Override
    public void upload(File externalFile, File internalFile) throws IOException {
        upload(externalFile, internalFile, false);
    }

    @Override
    public void upload(File externalFile, String internalFileName, boolean deleteSource) throws IOException {
        File internalFile = new File(configuration.getFileStoreDirectory(), internalFileName);
        upload(externalFile, internalFile, deleteSource);
    }

    @Override
    public void upload(File externalFile, File internalFile, boolean deleteSource) throws IOException {

        File destination = internalFile;
        if (TomwFileUtils.isUnder(configuration.getFileStoreDirectory(), internalFile)) {
            destination = internalFile;
        } else {
            destination = new File(configuration.getFileStoreDirectory(), internalFile.getName());
        }

        FileUtils.copyFile(externalFile, destination);

        // verify copy was ok
        if (!destination.exists() || destination.length() != externalFile.length()) {
            String message = "Failed to upload " + externalFile + " to " + destination;
            LOGGER.error(message);
            throw new IOException(message);
        }
        // if we are here then the copy operation was successful

        // if we are here, then we have successfully uploaded and stored in db the document
        if (deleteSource) {
            externalFile.delete();
        }
    }

    @Override
    public void download(String internalFileId, File externalFile) throws IOException {
        File destination = externalFile;

        File internalFile = new File(internalFileId);

        if (TomwFileUtils.isUnder(configuration.getFileStoreDirectory(), internalFile)) {
            internalFile = internalFile;
        } else {
            internalFile = new File(configuration.getFileStoreDirectory(), internalFile.toString());
        }

        FileUtils.copyFile(internalFile, destination);

        // verify copy was ok
        if (!destination.exists() || destination.length() != internalFile.length()) {
            String message = "Failed to download  " + internalFile + " to " + destination;
            LOGGER.error(message);
            throw new IOException(message);
        }
    }

    @Override
    public void deleteFile(String internalFileId) {
        if(internalFileId!=null) {
            deleteFile(new File(internalFileId));
        }
    }

    @Override
    public void deleteFile(File internalFile) {
        File fileLocation = getPathToFile(internalFile);
        if(fileLocation.exists()) {
            fileLocation.delete();
        }
    }

    @Override
    public File getPathToFile(String internalFileId) {
        if(internalFileId==null){
            return null;
        }else {
            File result = getPathToFile(new File(internalFileId));
            return result;
        }
    }

    @Override
    public File getPathToFile(File internalFile) {
        File result;
        if (TomwFileUtils.isUnder(configuration.getFileStoreDirectory(), internalFile)) {
            result = internalFile;
        } else {
            //result =  new File(configuration.getFileStoreDirectory(),internalFile.getName());
            result = new File(configuration.getFileStoreDirectory(), internalFile.toString());

        }
        if (!result.exists()) {
            File parentDirectory = result.getParentFile();
            TomwFileUtils.mkdirs(parentDirectory);
        }
        return result;

    }

    @Override
    public String getInternalSubdirectory(int id) {
        return String.format("%05d", id / 1000);
    }

    @Override
    public String internalNameWithInternalDirectory(int id, String extension) {
        String internalSubdir = getInternalSubdirectory(id);
        String internalFilename = id + "." + extension;
        return internalSubdir + "/" + internalFilename;
    }
}
