package ort.tomw.guiutils.entities.imagefile;

import java.io.File;

public interface ImageFileList {
     void add(ImageFile imageFile);
     ImageFile getCurrent();
     ImageFile deleteCurrent();
     ImageFile moveLeft();
     boolean canMoveLeft();
     ImageFile moveRight();
     boolean canMoveRight();
     ImageFile upload(File f);
     File downloadCurrent(File localTargetDirectory);
     void commit();
     boolean hasBeenUpdatedSinceLastCommit();
     boolean isEmpty();
}
