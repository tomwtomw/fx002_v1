/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.fileutils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for performing copying and moving files
 *
 * @author tomw
 */
public class FileTransporter {

    private final static Logger LOGGER = Logger.getLogger(FileTransporter.class.getName());

    public static void copyFile(File src, File dst) throws IOException, FileTransportException {

        if (dst.isDirectory()) {
            if (!dst.exists()) {
                dst.mkdirs();
            }
            FileUtils.copyFileToDirectory(src, dst);
        } else {
            FileUtils.copyFile(src, dst);
        }
        if (!validCopy(src, dst)) {
            LOGGER.error("Failed to copy " + src);
            throw new FileTransportException("failed to copy file " + src);
        }
    }

    public static List<File> copyFiles(List<File> sourceFiles, File dst) throws IOException, FileTransportException {
        List<File> failedFiles = new ArrayList<>();

        if (!dst.exists()) {
            dst.mkdirs();
        }

        for (File file : sourceFiles) {
            try {
                copyFile(file, dst);
            } catch (FileTransportException e) {
                failedFiles.add(file);
            }
        }
        return failedFiles;
    }

    public static void moveFile(File src, File dst) throws IOException, FileTransportException {
        copyFile(src, dst);
        if (validCopy(src, dst)) {
            src.delete();
        } else {
            throw new FileTransportException("Failed to move file " + src);
        }
    }

    public static List<File> moveFiles(List<File> sourceFiles, File dst) throws IOException, FileTransportException {
        List<File> failedFiles = new ArrayList<>();

        if (!dst.exists()) {
            dst.mkdirs();
        }
        for (File src : sourceFiles) {
            try {
                moveFile(src, dst);
            } catch (FileTransportException e) {
                failedFiles.add(src);
            }
        }

        return failedFiles;
    }

    /**
     * compare the source to destination and verify the the copy is valid
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean validCopy(File src, File dst) {
        File destination = destinationFile(src, dst);
        if (!destination.exists()) {
            return false;
        } else {
            if (!sameSize(src, destination)) {
                return false;
            }
            return sourceIsNotNewerThanDestination(src, destination);
        }
    }

    /**
     * source and destination should be the same size
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean sameSize(File src, File dst) {

        if (src == null && dst == null) {
            return true;
        } else {
            if (src == null || dst == null) {
                return false;
            } else {
                if (src.isDirectory() || src.isDirectory()) {
                    return false;
                }
                return src.length() == dst.length();
            }
        }
    }

    /**
     * verify that destination file has been created after source
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean sourceIsNotNewerThanDestination(File src, File dst) {
        return (src.lastModified() <= dst.lastModified());
    }

    /**
     * if dst is file, return dst if dst is directory then take file name and
     * combine it with dst return the result
     *
     * @param src
     * @param dst
     * @return
     */
    public static File destinationFile(File src, File dst) {
        if (dst.isFile()) {
            return dst;
        } else {
            String name = src.getName();
            return new File(dst, name);
        }
    }
}
