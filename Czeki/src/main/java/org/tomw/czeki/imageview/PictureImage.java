/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.utils.LocalDateUtils;

/**
 *
 * @author tomw
 */
public class PictureImage {

    private final static Logger LOGGER = Logger.getLogger(PictureImage.class.getName());

    private File file;
    private StringProperty nameProperty;
    private StringProperty fullFileNameProperty;
    private ObjectProperty<LocalDateTime> modificationTimeProperty;

    public static final String FILE_KEY = "File";

    //public static String[] imageExtensions = {"JPG", "JPEG", "GIF", "PNG"};

    public PictureImage(File file) {
        if (!file.exists()) {
            throw new RuntimeException("File " + file + " does not exists");
        }
        this.file = new File(file.getParent(), file.getName());
        fillProperties();
    }

    public PictureImage(String fileName) {
        this.file = new File(CzekiRegistry.currentAccount.getImageDirectory(), fileName);
        if (!file.exists()) {
            LOGGER.error("fileName=" + fileName);
            LOGGER.error("file=" + file);
            LOGGER.error("File does not exist");           
            this.file = new File(fileName);
            if (!file.exists()) {
                throw new RuntimeException("File " + file + " does not exists");
            }
        }
        fillProperties();
    }

//    public static boolean isImageFile(File file) {
//        return isImageFileName(file.getAbsolutePath());
//    }
//
//    public static boolean isImageFileName(String fileName) {
//        String extension = FilenameUtils.getExtension(fileName);
//        for (String e : imageExtensions) {
//            if (e.equalsIgnoreCase(extension)) {
//                return true;
//            }
//        }
//        return false;
    //}

    private void fillProperties() {
        this.nameProperty = new SimpleStringProperty(file.getName());
        this.fullFileNameProperty = new SimpleStringProperty(file.getAbsolutePath());
        long timestamp = file.lastModified();
        this.modificationTimeProperty = new SimpleObjectProperty<>(LocalDateUtils.getDateTimeFromTimestamp(timestamp / 1000));
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public StringProperty getFullFileNameProperty() {
        return fullFileNameProperty;
    }

    public File getFile() {
        return file;
    }

    public ObjectProperty<LocalDateTime> getModificationTimeProperty() {
        return modificationTimeProperty;
    }

    public LocalDateTime getModificationTime() {
        return modificationTimeProperty.get();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(FILE_KEY, file.toString());
        return json;
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.file);
        hash = 83 * hash + Objects.hashCode(this.modificationTimeProperty.get());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PictureImage other = (PictureImage) obj;
       
        if (!Objects.equals(this.file, other.file)) {
            return this.file.getAbsolutePath().equals(other.file.getAbsolutePath());
        }
        return Objects.equals(this.modificationTimeProperty.get(), other.modificationTimeProperty.get());
    }

}
