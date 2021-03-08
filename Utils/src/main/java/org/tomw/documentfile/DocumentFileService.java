package org.tomw.documentfile;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.tomw.daoutils.DataIntegrityException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface DocumentFileService {

    /**
     * get docuemnt fole with given id
     * @param id of docuemnt
     * @return document file, null if ddoes not exist
     */
    DocumentFile getDocumentFile(int id);

    void delete(DocumentFile documentFile) throws DataIntegrityException;

    /**
     * get image of the file in document file - if the file is image file. null otherwise
     * @param documentFile which should return its image representation
     * @return Image object or null
     */
    Image getImage(DocumentFile documentFile);

    /**
     * does the internal file exist?
     * @param documentFile dcument file
     * @return true if exists, false if does not exist or null
     */
    boolean internalFileExists(DocumentFile documentFile);


    /**
     * Size of internal file
     * @param documentFile document file
     * @return size of internal file. -1 if does not exist or null.
     */
    long internalFileSize(DocumentFile documentFile );

    /**
     * Tells if the document file contains an image file and this image file is not null
     * @param documentFile document object
     * @return true if it can be displayed
     */
    boolean canBeDisplayed(DocumentFile documentFile);

    /**
     * Display all documents
     */
    void displayAllDocuments();

    /**
     * Display all documents which are not personal picture
     */
    void displayAllDocumentsNotPersonalPicture();

    void displayOnly(Collection<DocumentFile> documents);
    void displayDocumentFiles(Collection<DocumentFile> documents);

    /**
     * get list of displayed documents
     * @return list of documents to be displayed
     */
    ObservableList<DocumentFile> getListOfDisplayedDocuments();

    /**
     * Save document file
     * @param documentFile to be saved
     */
    void save(DocumentFile documentFile);

    void removeFromDisplay(DocumentFile currentEntity);

    void clearDisplayedDocuments();

    /**
     * Upload file from disk and attach it to document file object.
     * If the document file has already a file - overwrite it.
     * @param documentFile documentFile object
     * @param file external file to be attached to the document
     * @return File path to internal file location
     */
    File uploadFile(DocumentFile documentFile, File file) throws IOException;

    File uploadFile(DocumentFile documentFile, File file, boolean deleteSource) throws IOException;

    File uploadFile(DocumentFile documentFile, File file, boolean deleteSource, boolean overwriteDestination) throws IOException;

    /**
     * Take file from documentFile and download it to directory provided, give it its original external name
     * @param documentFile from which file is to be downloaded
     * @param destinationdirectory where to download it
     * @return full name of file after download.
     */
    File downloadFile(DocumentFile documentFile, File destinationdirectory) throws IOException;

    /**
     * Delete file with internal name from file dao.
     * @param intenalFileName id of file to be deleted
     */
    void deleteFile(String intenalFileName);

    /**
     * Delete file from the document object. Also delete it from file store
     * @param documentFile document object
     */
    void deleteFileFromDocument(DocumentFile documentFile);


    /**
     * Create Document file object from file on disk. Delete original file
     * @param file file to be uploaded into new DocumentFile
     * @return document file object
     */
    DocumentFile createDocumentFile(File file);

    /**
     *
     * @param file file to be uploaded into new DocumentFile
     * @param deleteOriginalFile delete origianl after upload, if true
     * @return document file object
     */
    DocumentFile createDocumentFile(File file, boolean deleteOriginalFile);

    /**
     * Clone the document file, Also create the copy of the internal file
     * @param document to be cloned
     * @return cloned document
     */
    DocumentFile clone(DocumentFile document) throws IOException;
}
