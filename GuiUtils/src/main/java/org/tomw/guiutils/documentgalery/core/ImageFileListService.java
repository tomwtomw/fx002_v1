package org.tomw.guiutils.documentgalery.core;

import javafx.collections.ObservableList;
import ort.tomw.guiutils.entities.imagefile.ImageFile;

import java.io.File;

public interface ImageFileListService {
    public ObservableList<ImageFile> getList();
    public void add(ImageFile imageFile);
    public void delete(ImageFile imageFile);
    public void delete(String id);
    public void upload(ImageFile imageFile, File input);
    public void downloadFile(ImageFile imageFile, File destination);
    public ImageFile get(String id);

}
