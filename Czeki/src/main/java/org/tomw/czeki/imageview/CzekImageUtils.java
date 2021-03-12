/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author tomw
 */

public class CzekImageUtils {
    public static String[] imageExtensions = {"JPG", "JPEG", "GIF", "PNG"};

    public static String FRONT="F";
    public static String BACK="B";

    public static CheckImageNameDecoder decoder1 = new CheckImageNameDecoder01();
    public static CheckImageNameDecoder decoder2 = new CheckImageNameDecoder02();
    public static CheckImageNameDecoder decoder3 = new CheckImageNameDecoder03();

    private static List<CheckImageNameDecoder> decoders = new ArrayList<>();
    static{
        decoders.add(decoder1);
        decoders.add(decoder2);
        decoders.add(decoder3);
    }

    /**
     * find correct decoder for given file name
     *
     * @param fileName image file name
     * @return decoder to process this file name
     */
    public static CheckImageNameDecoder getDecoder(String fileName){
        if (isImageFileName(fileName)) {
            for (CheckImageNameDecoder c : decoders) {
                if (c.isCorrectFormat(fileName)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * get correct decoder for given file
     * @param file decoder for this image file
     * @return decoder
     */
    public static CheckImageNameDecoder getDecoder(File file){
        return getDecoder(file.toString());
    }

    public static boolean isImageFile(File file) {
        return isImageFileName(file.getAbsolutePath());
    }

    public static boolean isImageFileName(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        for (String e : imageExtensions) {
            if (e.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
