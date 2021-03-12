package ort.tomw.guiutils.entities.imagefile;

import org.tomw.imagedao.ImageDao;
import org.tomw.imagedao.ImageDaoException;

import java.io.File;
import java.util.List;

public interface ImageFileDao extends ImageDao {
    List<ImageFile> getAll();

    ImageFile getImageFile(String id);

    /**
     * return id of the added object, if everything ok
     *
     * @param imageFile image file location, in repository
     * @return id of the object if ok
     */
    String add(ImageFile imageFile); // return id of the added object

    /**
     * delete file which corresponds to this ImageFile
     * @param imageFile
     */
    void deleteFile(ImageFile imageFile);

    /**
     * Upload externalFile, add the file reference to ImageFile object. Delete source file
     * @param imageFile object to be updated
     * @param file to be uploaded
     */
    void upload(ImageFile imageFile, File file) throws ImageDaoException;

    /**
     *
     * Upload externalFile, add the file reference to ImageFile object.
     * @param imageFile object to be updated
     * @param file to be uploaded
     * @param deleteSource if true, delete source
     */
    void upload(ImageFile imageFile, File file, boolean deleteSource) throws ImageDaoException;

    ImageFile upload(File directory, File imageFile, String name) throws ImageDaoException;

    ImageFile upload(File directory, File imageFile, String name, String comment) throws ImageDaoException;

    ImageFile upload(File directory, File imageFile, String name, boolean deleteSource) throws ImageDaoException;

    ImageFile upload(File directory, File imageFile, String name, String comment, boolean deleteSource) throws ImageDaoException;

    boolean imageFileExists(String id);

    boolean contains(ImageFile imageFile);

    ImageFile delete(ImageFile imageFile);

    ImageFile deleteImageFile(String id);

}
