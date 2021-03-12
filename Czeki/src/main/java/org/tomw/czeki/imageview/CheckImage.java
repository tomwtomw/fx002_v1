/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.util.Comparator;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONObject;
import org.tomw.czeki.CzekiRegistry;

/**
 * @author tomw
 */
public class CheckImage {
    public static String BLANK = "";

    public static final String FILE_KEY = "File";
    public static final String ID = "Id";

    private CheckImageNameDecoder decoder;

    private StringProperty idProperty;

    private File imageDirectory=null;
    private StringProperty fileNameProperty;

    private StringProperty checkNumberProperty;
    private StringProperty checkFrontBackProperty;

    public StringProperty getIdProperty() {
        return idProperty;
    }

    public String getId() {
        return getIdProperty().get();
    }

    public StringProperty getFileNameProperty() {
        return fileNameProperty;
    }

    public StringProperty getCheckNumberProperty() {
        return checkNumberProperty;
    }

    public StringProperty getCheckFrontBackProperty() {
        return checkFrontBackProperty;
    }

    /**
     * static constructor from json object
     *
     *
     * @param imageDirectory
     * @param json
     * @return
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    public static CheckImage fromJson(File imageDirectory, JSONObject json) throws CheckImageException {
        String fileName = (String) json.get(FILE_KEY);
        return new CheckImage(imageDirectory,fileName);
    }

    public CheckImage(File file) throws CheckImageException {
        String fileName = file.getName();
        File directory = file.getParentFile();
        findDecoder(fileName);
        fillProperties(directory,fileName);
        verifyInputCorrectness();
    }

    public CheckImage(File imageDirectory,File file) throws CheckImageException {
        String fileName = file.getName();
        findDecoder(fileName);
        fillProperties(imageDirectory,fileName);
        verifyInputCorrectness();
    }

    public CheckImage(File imageDirectory,String fileName) throws CheckImageException {
        findDecoder(fileName);
        fillProperties(imageDirectory,fileName);
        verifyInputCorrectness();
    }

    /**
     * find decoder for current file. Reject non check image files
     *
     * @param fileName file for which decoder is to be identified
     * @throws CheckImageException if something goes wrong
     */
    private void findDecoder(String fileName) throws CheckImageException {
        if (!CzekImageUtils.isImageFileName(fileName)) {
            String message = fileName + " is not image file!";
            throw new CheckImageException(message);
        }
        decoder = CzekImageUtils.getDecoder(fileName);
        if (decoder == null) {
            String message = fileName + " does not represent a known format";
            throw new CheckImageException(message);
        }
    }

    /**
     * verify if the check image makes sense
     *
     * @throws CheckImageException
     */
    private void verifyInputCorrectness() throws CheckImageException {
        // file must exist
        if (!getFile().exists()) {
            throw new CheckImageException("Missing file " + getFile().getAbsolutePath());
        }

        //file must have front and back
        if (!isFront() && !isBack() ) {
            throw new CheckImageException("Filename " + getFile() + " is neither front nor back");
        }
        // it must be an image file
        if (!CzekImageUtils.isImageFile(getFile())) {
            throw new CheckImageException("Filename " + getFile() + " is not an image file");
        }
    }

    /**
     * is this image front of check?
     *
     * @return true if front
     */
    public boolean isFront() {
        return decoder.isFront(fileNameProperty.get());
    }

    /**
     * is this back of check image?
     *
     * @return true if back
     */
    public boolean isBack() {
        return decoder.isBack(fileNameProperty.get());
    }

    /**
     * get check number for this file
     * @return check number
     */
    public String getCheckNumber() {
        return this.checkNumberProperty.get();
    }

    private void fillProperties(File imageDirectory,String fileName) {
        this.imageDirectory = imageDirectory;

        idProperty = new SimpleStringProperty(fileName);
        fileNameProperty = new SimpleStringProperty(fileName);
        String checkNumber = decoder.getCheckNumber(fileName);
        checkNumberProperty = new SimpleStringProperty(checkNumber);
        checkNumberProperty.set(checkNumber);

        checkFrontBackProperty = new SimpleStringProperty(BLANK);

        if (decoder.isFront(fileName)) {
            checkFrontBackProperty.set(CzekImageUtils.FRONT);
        } else {
            if (decoder.isBack(fileName)) {
                checkFrontBackProperty.set(CzekImageUtils.BACK);
            }
        }

    }

    /**
     * get fully qualified name (path+name) of the image file
     *
     * @return
     */
    public File getFile() {
        if(imageDirectory!=null) {
            return new File(
                    imageDirectory,
                    fileNameProperty.get()
            );
        }else{
            return  new File(fileNameProperty.get());
        }
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(FILE_KEY, fileNameProperty.get());
        return json;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.fileNameProperty.get());
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
        final CheckImage other = (CheckImage) obj;
        return Objects.equals(this.fileNameProperty.get(), other.fileNameProperty.get());
    }

    public String getSide() {
        return checkFrontBackProperty.get();
    }

    public static Comparator<CheckImage> compareByImageNumber
            = new Comparator<CheckImage>() {

        @Override
        public int compare(CheckImage c1, CheckImage c2) {
            String number1 = c1.getCheckNumber();
            String number2 = c2.getCheckNumber();
            if (number1 != null) {
                return number1.compareTo(number2);
            } else {
                if (number2 == null) {
                    return 0;
                } else {
                    return -number2.compareTo(number1);
                }
            }
        }
    };

}
