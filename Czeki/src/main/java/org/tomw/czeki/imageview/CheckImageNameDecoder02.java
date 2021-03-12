package org.tomw.czeki.imageview;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class CheckImageNameDecoder02  implements CheckImageNameDecoder{
    private String sampleNameFront="C7D19FBA1234200F";
    private String sampleNameBack="C7D19FBA1234200B";

    private CheckImageNameDecoder decoder = new CheckImageNameDecoder01();

    private static String FRONT="F";
    private static String BACK="B";


    @Override
    public boolean isCorrectFormat(String fileName) {
        String basename= FilenameUtils.getBaseName(fileName);
        return basename.matches("^\\w{8}\\d{4}\\w{3}[FB]");
    }

    @Override
    public boolean isCorrectFormat(File f) {
        return isCorrectFormat(f.toString());
    }

    @Override
    public boolean isValid(String fileName) {
        return CzekImageUtils.isImageFileName(fileName) && isCorrectFormat(fileName);
    }

    @Override
    public boolean isValid(File f) {
        return CzekImageUtils.isImageFile(f) && isCorrectFormat(f);
    }

    @Override
    public boolean hasCorrectLength(String fileName) {
        String basename= FilenameUtils.getBaseName(fileName);
        return sampleNameFront.length()==basename.length();
    }

    @Override
    public boolean hasCorrectLength(File f) {
        return hasCorrectLength(f.toString());
    }

    @Override
    public boolean isFront(String fileName) {
        String basename= FilenameUtils.getBaseName(fileName);
        return basename.endsWith(CzekImageUtils.FRONT);
    }

    @Override
    public boolean isBack(String fileName) {
        String basename= FilenameUtils.getBaseName(fileName);
        return basename.endsWith(CzekImageUtils.BACK);
    }

    @Override
    public boolean isFront(File f) {
        return isFront(f.toString());
    }

    @Override
    public boolean isBack(File f) {
        return isBack(f.toString());
    }

    @Override
    public String getCheckNumber(String fileName) {
        String basename= FilenameUtils.getBaseName(fileName);
        return basename.substring(8, 12);
    }

    @Override
    public String getCheckNumber(File f) {
        return getCheckNumber(f.toString());
    }

    @Override
    public String getSide(String fileName) {
        if(isBack(fileName)){
            return CzekImageUtils.BACK;
        }
        if(isFront(fileName)){
            return CzekImageUtils.FRONT;
        }
        throw new RuntimeException ("File "+fileName+" has neither front nor back side");
    }

    @Override
    public String getSide(File f) {
        return getSide(f.toString());
    }
}
