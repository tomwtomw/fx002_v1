package org.tomw.guiutils.documentgalery.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.imagedao.ImageDaoException;
import ort.tomw.guiutils.entities.imagefile.ImageFile;
import ort.tomw.guiutils.entities.imagefile.ImageFileDao;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageFileListServiceImpl implements ImageFileListService {
    private final static Logger LOGGER = Logger.getLogger(ImageFileListServiceImpl.class.getName());



    private File cache;

    private ImageFileDao dao;

    private ObservableList<ImageFile> observableListOfDocuments = FXCollections.observableArrayList();

    public ImageFileListServiceImpl(ImageFileDao daoin, ObservableList<ImageFile> inputList) {
        dao = daoin;
        observableListOfDocuments.clear();
        for (ImageFile imageFile : inputList) {
            observableListOfDocuments.add(imageFile);
        }
    }

    boolean isInList(ImageFile imageFile) {
        for (ImageFile currentImageFile : getList()) {
            if (currentImageFile.getId().equals(imageFile.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ObservableList<ImageFile> getList() {
        return observableListOfDocuments;
    }

    @Override
    public void add(ImageFile imageFile) {
        if (!isInList(imageFile)) {
            observableListOfDocuments.add(imageFile);
        }
        dao.add(imageFile);
    }

    @Override
    public void delete(ImageFile imageFile) {
        dao.delete(imageFile);
        Iterator<ImageFile> iter = getList().iterator();
        while(iter.hasNext()){
            ImageFile c = (ImageFile)iter.next();
            if(c.getId().equals(imageFile.getId())){
                iter.remove();
            }
        }
    }

    @Override
    public void delete(String id) {
        delete(dao.getImageFile(id));
    }

    @Override
    public void upload(ImageFile imageFile, File input) {
        try {
            dao.upload(imageFile,input);
        } catch (ImageDaoException e) {
            LOGGER.error("failed to upload file "+input+" to imageFile "+imageFile);
        }
    }

    @Override
    public void downloadFile(ImageFile imageFile, File destination) {
        try {
            FileUtils.copyFileToDirectory(imageFile.getFile(),destination,true);
        } catch (IOException e) {
           LOGGER.error("Failed to download file "+imageFile+" to "+destination,e);
        }
    }

    @Override
    public ImageFile get(String id) {
        Iterator<ImageFile> iter = getList().iterator();
        while(iter.hasNext()){
            ImageFile c = (ImageFile)iter.next();
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
}
