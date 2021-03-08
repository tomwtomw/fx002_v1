package org.tomw.mediautils;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 * Utility functions to manipulate media files
 * TODO add tests for media file utils
 * @author tomw 
 */
public class MediaFileUtils {
    //pictures
    public static final String JPG="jpg";
    public static final String JPEG="jpeg";
    public static final String GIF="gif";
    public static final String TIF="tif";
    public static final String TIFF="tiff";
    public static final String PNG="png";
    // movies
    public static final String WMV="wmv";
    public static final String VOB="VOB";
    public static final String MOV="mov";
    public static final String MSWMM="MSWMM";
    public static final String MP4="mp4";
    public static final String AVI="AVI";

    public static boolean isJpg(File file){
        return isJpg(file.toString());
    }
    public static boolean isJpg(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(JPG) || extension.equalsIgnoreCase(JPEG);
    }
    public static boolean isGif(File file){
        return isGif(file.toString());
    }
    public static boolean isGif(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(GIF);
    }
    public static boolean isTif(File file){
        return isTif(file.toString());
    }
    public static boolean isTif(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(TIF) || extension.equalsIgnoreCase(TIFF);
    }
    public static boolean isPng(File file){
        return isPng(file.toString());
    }
    public static boolean isPng(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(PNG);
    }
    public static boolean isWmv(File file){
        return isWmv(file.toString());
    }
    public static boolean isWmv(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(WMV);
    }
    public static boolean isVob(File file){
        return isVob(file.toString());
    }
    public static boolean isVob(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return extension.equalsIgnoreCase(VOB);
    }
    public static boolean isMov(File file){
        return isMov(file.toString());
    }
    public static boolean isMov(String fileName){
        return FilenameUtils.getExtension(fileName).equalsIgnoreCase(MOV);
    }
    public static boolean isMswmm(File file){
        return isMswmm(file.toString());
    }
    public static boolean isMswmm(String fileName){
        return FilenameUtils.getExtension(fileName).equalsIgnoreCase(MSWMM);
    }
    public static boolean isMp4(File file){
        return isMp4(file.toString());
    }
    public static boolean isMp4(String fileName){
        return FilenameUtils.getExtension(fileName).equalsIgnoreCase(MP4);
    }
    public static boolean isAvi(File file){
        return isAvi(file.toString());
    }
    public static boolean isAvi(String fileName){
        return FilenameUtils.getExtension(fileName).equalsIgnoreCase(AVI);
    }

    //
    public static boolean isImage(File file){
        return isImage(file.toString());
    }
    public static boolean isImage(String fileName){
        return isJpg(fileName)||isGif(fileName)||isTif(fileName)||isPng(fileName);
    }
    //
    public static boolean isMovie(File file){
        return isMovie(file.toString());
    }
    public static boolean isMovie(String fileName){
        return isWmv(fileName)||isVob(fileName)||isMov(fileName)||isMswmm(fileName)||isMp4(fileName)||isAvi(fileName);
    }
    //
    public static boolean isMedia(File file){
        return isMedia(file.toString());
    }
    public static boolean isMedia(String fileName){
        return isImage(fileName)||isMovie(fileName);
    }

}