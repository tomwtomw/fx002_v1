package org.tomw.filestoredao;

import org.tomw.documentfile.DocumentFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface describing mechanism for storing disk files in persistence layer
 */
public interface FileDao {
    /**
     * Upload external file, copy it to persistence. Do not delete external file
     * Internally assign it id internalFileName
     * @param externalFile external representation of file
     * @param internalFileName internal representation of file
     */
    void upload(File externalFile, String internalFileName) throws IOException;

    /**
     * Upload external file, copy it to persistence. Do not delete external file
     * @param externalFile external representation of file
     * @param internalFile internal representation of file
     */
    void upload(File externalFile, File internalFile) throws IOException;

    /**
     * Upload external file, copy it to persistence. Do not delete external file
     * Internally assign it id internalFileName
     * @param externalFile external representation of file
     * @param internalFileName internal representation of file
     * @param deleteSource true if you want the source to be deleted after upload
     */
    void upload(File externalFile, String internalFileName, boolean deleteSource) throws IOException;

    /**
     * Upload external file, copy it to persistence. Do not delete external file
     * Internally assign it id internalFileName
     * @param externalFile external representation of file
     * @param internalFile internal representation of file
     * @param deleteSource true if you want the source to be deleted after upload
     */
    void upload(File externalFile, File internalFile, boolean deleteSource) throws IOException;

    /**
     * Download file known by its internal id internalFileId into external location externalFile
     * @param internalFileId internal file id
     * @param externalFile external file
     */
    void download(String internalFileId, File externalFile) throws IOException;

    /**
     * delete internal file described by its id
     * @param internalFileId internal file id
     */
    void deleteFile(String internalFileId);

    /**
     * delete internal file described by its id
     * @param internalFile intenal file
     */
    void deleteFile(File internalFile);

    /**
     * return full path to internal file representation
     * @param internalFileId internal file id (name plus extension)
     * @return full path to internal file
     */
    File getPathToFile(String internalFileId);

    /**
     * return full path to internal file representation
     * @param internalFile internal file
     * @return full path to internal file
     */
    File getPathToFile(File internalFile);

    /**
     * get name of internal subdirectory for file with id id
     *
     * @param id id
     * @return string with subdirectory name
     */
    String getInternalSubdirectory(int id);

    /**
     * Take id, create file id.extension (123.doc for example) and append to it in front
     * internal subdirectory defined by method getInternalSubdirectory
     * @param id document id
     * @param extension document extension
     * @return internal file location, including subdirectory
     */
    String internalNameWithInternalDirectory(int id, String extension);


}
