package org.tomw.guiutils.imagegallery;

import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public interface ImageGalleryService<T> {
    public List<T> getImages();
    public void view();
    public void delete(T e);
    public T upload(File f);
    public File download(T e);
    public void okButton(T e);
    public void cancelbutton();

}
