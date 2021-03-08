package org.tomw.imagedao;

import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

/**
 * defines interface for image dao
 */
public interface ImageDao {

    boolean containsImage(String id);
    Image getImage(String id);
    // return id
    String upload(Image image) throws ImageDaoException;
    String upload(File imageFile) throws ImageDaoException;
    String upload(File imageFile, boolean deleteSource) throws ImageDaoException;

    String upload(File directory,File imageFile) throws ImageDaoException;
    String upload(File directory,File imageFile, boolean deleteSource) throws ImageDaoException;

    List<String> uploadAll(File directory, boolean deleteSource) throws ImageDaoException;

    boolean imageExists(String id);
    boolean deleteImage(String id);
    boolean deleteImages(List<String> listOfIds);

    // I am not sure if this is right idea, but we may try it
    File getFile(String id)  throws ImageDaoException;

    void commit()  throws ImageDaoException;
}
