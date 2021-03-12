package ort.tomw.guiutils.entities.imagefile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.tomw.imagedao.ImageDaoException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageFileListImpl implements ImageFileList{
    private final static Logger LOGGER = Logger.getLogger(ImageFileListImpl.class.getName());


    private ImageFileDao dao;
    private List<ImageFile> imageFileList = new ArrayList<>();
    private int currentIndex = -1;

    private boolean hasBeenUpdated=false;

    public ImageFileListImpl(ImageFileDao dao, List<ImageFile>inputList){
        this.dao=dao;
        imageFileList.addAll(inputList);
        if(!isEmpty()) {
            currentIndex = 0;
        }
    }

    @Override
    public void add(ImageFile imageFile) {
        if(imageFileList.isEmpty()){
            imageFileList.add(imageFile);
            currentIndex=0;
        }else{
            imageFileList.add(currentIndex,imageFile);
        }
        hasBeenUpdated=true;
    }

    @Override
    public ImageFile getCurrent() {
        if(isEmpty()){
            return null;
        }else{
            return imageFileList.get(currentIndex);
        }
    }

    @Override
    public ImageFile deleteCurrent() {
        if(isEmpty()){
            return null;
        }else{
            hasBeenUpdated=true;
            ImageFile result = imageFileList.remove(currentIndex);
            if(isEmpty()){
                currentIndex=-1;
            }
            if(currentIndex>imageFileList.size()-1){
                currentIndex=imageFileList.size();
            }
            return result;
        }
    }

    @Override
    public ImageFile moveLeft() {
        if(isEmpty()){
            return null;
        }
        if(canMoveLeft()){
            currentIndex=currentIndex-1;
        }
        return imageFileList.get(currentIndex);
    }

    @Override
    public boolean canMoveLeft() {
        if(isEmpty()){
            return false;
        }else{
            return currentIndex>0;
        }
    }

    @Override
    public ImageFile moveRight() {
        if(isEmpty()){
            return null;
        }
        if(canMoveRight()){
            currentIndex=currentIndex+1;
        }
        return imageFileList.get(currentIndex);
    }

    @Override
    public boolean canMoveRight() {
        if(isEmpty()){
            return false;
        }else{
            return currentIndex<imageFileList.size()-1;
        }
    }

    @Override
    public ImageFile upload(File f) {
        try {
            String id = dao.upload(f);
            hasBeenUpdated=true;
            ImageFile imageFile = dao.getImageFile(id);
            imageFileList.add(imageFile);
            currentIndex=imageFileList.size()-1;
            return imageFile;
        } catch (ImageDaoException e) {
           LOGGER.error("Failed to upload file "+f,e);
           return null;
        }
    }

    @Override
    public File downloadCurrent(File localTargetDirectory) {
        if(isEmpty()){
            return null;
        }else{
            File sourceFile = imageFileList.get(currentIndex).getFile();
            File destination = new File(localTargetDirectory,sourceFile.getName());
            try {
                FileUtils.copyFile(sourceFile,destination);
                return destination;
            } catch (IOException e) {
                LOGGER.error("failed to download file "+sourceFile+" to "+destination,e);
                return null;
            }
        }
    }

    @Override
    public void commit() {
        for(ImageFile imageFile : imageFileList){
            dao.add(imageFile);
        }
        try {
            dao.commit();
            hasBeenUpdated=false;
        } catch (ImageDaoException e) {
            LOGGER.error("Failed to commit images to dao",e);
        }
    }

    @Override
    public boolean hasBeenUpdatedSinceLastCommit() {
        return hasBeenUpdated;
    }

    @Override
    public boolean isEmpty() {
        return imageFileList.isEmpty();
    }
}
