package org.tomw.fileutils;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.tomw.mediautils.MediaFileUtils;
import org.tomw.utils.TomwStringUtils;

/**
 * Static utility class for performing common file operations
 *
 * @author tomw
 */
public class TomwFileUtils {
    private final static Logger LOGGER = Logger.getLogger(TomwFileUtils.class.getName());
    public static final String ENCODING = "ISO-8859-1";

    /**
     * return true if file 1 is older than file 2 (by modification time)
     *
     * @param file1 first file
     * @param file2 second file
     * @return true if file1 modified after file2
     */
    public static boolean firstModifiedAfterSecond(File file1, File file2) {
        return file1.lastModified() > file2.lastModified();
    }

    /**
     * check if two files, assumed to be text files, have the same content
     *
     * @param f1 file 1
     * @param f2 file 2
     * @return true if content is identical
     */
    public static boolean textFilesHaveSameContent(File f1, File f2) {
        try {
            String s1 = FileUtils.readFileToString(f1, "UTF-8");
            String s2 = FileUtils.readFileToString(f2, "UTF-8");
            return Objects.equals(s1, s2);
        } catch (IOException e) {
            LOGGER.error("Cannot read one of the files " + f1 + " , " + f2);
            return false;
        }
    }

    /**
     * delete the specified file if it exists
     *
     * @param fileName file to be deleted
     */
    public static void deleteFileIfExists(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            deleteFileIfExists(file);
        }
    }

    /**
     * delete file if it exists
     * <p>
     *
     * @param file to be deleted
     */
    public static void deleteFileIfExists(File file) {
        if (file != null) {
            if (file.exists()) {
                boolean result = file.delete();
                if (!result) {
                    LOGGER.error("Failed to delete file " + file);
                }
            }
        }
    }

    /**
     * @param file create file, write a date into it
     * @throws IOException if fails
     */
    public static void createFile(File file) throws IOException {
        deleteFile(file);
        FileUtils.writeStringToFile(file, "test file " + LocalDate.now(), ENCODING);
    }

    /**
     * check if file existst and delete if yes
     *
     * @param file to be deleted
     */
    public static void deleteFile(File file) {
        if (file != null) {
            if (file.exists()) {
                boolean result = file.delete();
                if (!result) {
                    LOGGER.error("Failed to delete file " + file);
                }
            }
        }
    }

    /**
     * Delete directory. If not empty, delete its files first
     *
     * @param dir directory to be deleted
     */
    public static void deleteDirectoryWithFiles(File dir) {
        if (dir != null) {
            if (dir.exists()) {
                if (dir.isFile()) {
                    boolean result = dir.delete();
                    if (!result) {
                        LOGGER.error("Failed to delete file " + dir);
                    }
                } else {
                    if (dir.isDirectory()) {
                        deleteFilesInDirectory(dir);
                    }
                }
            }
        }
    }

    /**
     * delete all files and subdirectories in the directory
     *
     * @param dir where files are to be deleted
     */
    public static void deleteFilesInDirectory(File dir) {
        if (dir != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deleteDirectoryWithFiles(file);
                }
                if (file.isFile()) {
                    boolean result = file.delete();
                    if (!result) {
                        LOGGER.error("Failed to delete file " + file);
                    }
                }
            }
            boolean result = dir.delete();
            if (!result) {
                LOGGER.error("Failed to delete file " + dir);
            }
        }
    }

    /**
     * recursively get all entries in directory (including directories)
     *
     * @param sourceDirectory sourceDirectory
     * @return list of files and directories
     */
    public static List<File> getAllEntriesInDirectory(File sourceDirectory) {
        List<File> result = new ArrayList<>();
        if (sourceDirectory != null) {
            try {
                for (File file : sourceDirectory.listFiles()) {
                    if (file.isFile()) {
                        result.add(file);
                    }
                    if (file.isDirectory()) {
                        result.add(file);
                        result.addAll(getAllEntriesInDirectory(file));
                    }
                }
            } catch (java.lang.NullPointerException e) {
                LOGGER.warn("Cannot read " + sourceDirectory);
            }
        }
        return result;
    }

    /**
     * recursively get all files in directory (excluding directories)
     *
     * @param sourceDirectory sourceDirectory
     * @return list of files
     */
    public static List<File> getAllFilesInDirectory(File sourceDirectory) {
        List<File> result = new ArrayList<>();
        if (sourceDirectory != null) {
            try {
                for (File file : sourceDirectory.listFiles()) {
                    if (file.isFile()) {
                        result.add(file);
                    }
                    if (file.isDirectory()) {
                        result.addAll(getAllFilesInDirectory(file));
                    }
                }
            } catch (java.lang.NullPointerException e) {
                LOGGER.warn("Cannot read " + sourceDirectory);
            }
        }
        return result;
    }

    /**
     * return true if this is empty directory
     *
     * @param sourceDirectory directory
     * @return true if directory is empty
     */
    public static boolean isEmptyDirectory(File sourceDirectory) {
        try {
            if (sourceDirectory.exists()) {
                return sourceDirectory.listFiles().length == 0;
            } else {
                return true;
            }

        } catch (java.lang.NullPointerException e) {
            LOGGER.error("Cannot read " + sourceDirectory, e);
            throw new RuntimeException("Cannot read " + sourceDirectory, e);
        }
    }

    /**
     * get all subdirectories of dir
     *
     * @param dir directory
     * @return list of subdirectories
     */
    public static List<File> getAllSubdirectories(File dir) {
        List<File> result = new ArrayList<>();
        if (dir != null) {
            try {
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        result.add(file);
                    }
                }
            } catch (java.lang.NullPointerException e) {
                LOGGER.warn("Cannot read " + dir, e);
            }
        }
        return result;
    }

    /**
     * starting from dir recursively walk down the tree and return directories which are empty
     *
     * @param dir start directory
     * @return list of empty directories
     */
    public static List<File> getEmptyBranches(File dir) {
        List<File> result = new ArrayList<>();
        if (dir != null) {
            try {
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        if (isEmptyDirectory(file)) {
                            result.add(file);
                        } else {
                            result.addAll(getEmptyBranches(file));
                        }
                    }
                }
            } catch (java.lang.NullPointerException e) {
                LOGGER.warn("Cannot read " + dir);
            }
        }
        return result;
    }

    /**
     * prune empty directories from the tree
     *
     * @param dir directory to clean
     */
    public static void pruneEmptyBranches(File dir) {
        if (dir != null) {
            try {
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        if (!isEmptyDirectory(file)) {
                            pruneEmptyBranches(file);
                        }
                        if (isEmptyDirectory(file)) {
                            boolean result = file.delete();
                            if (!result) {
                                LOGGER.error("Failed to delete file " + file);
                            }
                        }
                    }
                }
                if (isEmptyDirectory(dir)) {
                    boolean result = dir.delete();
                    if (!result) {
                        LOGGER.error("Failed to delete file " + dir);
                    }
                }
            } catch (java.lang.NullPointerException e) {
                LOGGER.warn("Cannot read " + dir);
            }
        }
    }

    public static File getApplicationDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static String fileToString(File file) {
        if (file == null) {
            return TomwStringUtils.BLANK;
        } else {
            return file.toString();
        }
    }

    /**
     * Write string to file
     *
     * @param f file
     * @param s string
     */
    public static void string2File(File f, String s) {
        //TODO implement
        throw new RuntimeException("Not implemented");
    }

    public static String readTextFileFromResources(String fileName) {
        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = TomwFileUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    /**
     * read text file from resources using input strem. To be used when
     * reading a file in jar
     *
     * @param inputFileName file in resources directory
     * @return text content of this file
     */
    public static String readTextFileFromResourcesAsStream(String inputFileName) throws IOException {

        InputStream in = TomwFileUtils.class.getResourceAsStream(inputFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder out = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
        } catch (IOException e) {
            String message = "Failed to read input file "+inputFileName;
            LOGGER.error(message,e);
            throw e;
        }

        return out.toString();
    }

    /**
     * read text file from resources using input strem. To be used when
     * reading a file in jar
     *
     * @param inputFile file in resources directory
     * @return text content of this file
     */
    public static String readTextFileFromResourcesAsStream(File inputFile) throws IOException {
        String inputFileAsString = TomwStringUtils.backslash2slash(inputFile.toString());
        return readTextFileFromResourcesAsStream(inputFileAsString);
    }

    /**
     * read text file from resources
     *
     * @param inputFile file in resources directory
     * @return text content of this file
     */
    public static String readTextFileFromResources(File inputFile) {
        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = TomwFileUtils.class.getClassLoader();
        File file = new File(classLoader.getResource(inputFile.toString()).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * Copy a binary file from resources directory to destination directory
     *
     * @param src source file, in resources
     * @param dst destination file
     * @throws IOException if something goes wrong
     */
    public static void copyFileFromResources(File src, File dst) throws IOException {
        String s = src.getPath();
        InputStream in = TomwFileUtils.class.getClassLoader().getResourceAsStream(s);
        OutputStream out = new FileOutputStream(dst);
        IOUtils.copy(in, out);
        in.close();
        out.close();
    }

    /**
     * create directory, verify it it exists
     * TODO add unit test for this
     *
     * @param dir directory to be created
     */
    public static void mkdirs(File dir) {
        synchronized (TomwFileUtils.class) {
            if (dir != null) {
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    if (!result) {
                        LOGGER.error("Failed to create directory " + dir);
                    }
                }
            }
        }
    }

    /**
     * TODO add unit test for this
     * return all files in sundirectories of dir
     *
     * @param dir top lever directory
     * @return list of files
     */
    public static List<File> getListOfFilesInDirectory(File dir) {
        FileSelector selector = new FileSelector() {
            public boolean select(File f) {
                return true;
            }
        };
        return getListOfFilesInDirectory(dir, selector);
    }

    /**
     * TODO add unit test for this
     * return all files in sundirectories of dir which satisfy the selction criteria f
     *
     * @param dir      directory
     * @param selector selector function, decides if file should be included in rresult or not
     * @return list of files which satisfy selction criteria
     */
    public static List<File> getListOfFilesInDirectory(File dir, FileSelector selector) {
        List<File> result = new ArrayList<>();
        if(dir!=null) {
            for (File f : dir.listFiles()) {
                f = f.getAbsoluteFile();
                if (f.isFile()) {
                    if (selector.select(f)) {
                        result.add(f);
                    }
                }
                if (f.isDirectory()) {
                    result.addAll(getListOfFilesInDirectory(f, selector));
                }
            }
        }
        return result;
    }

    public static File replaceBasedir(File sourceBasedir, File file, File destinationBasedir){
        String fileString = TomwStringUtils.backslash2slash(file.toString());
        String sourceBasedirString = TomwStringUtils.backslash2slash(sourceBasedir.toString());

        String baseSourceFileName = fileString.replace(sourceBasedirString,"");

        File destination = new File(destinationBasedir,baseSourceFileName);
        return destination;
    }

    /**
     * return true if sedond file is under directory tree of first one
     * for example dir=C:/a/b/c/ file=C:/a/b/c/d/e.txt ==> return true
     * @param directory top level directory
     * @param file if file is under it
     * @return true if second is under first tree
     */
    public static boolean isUnder(File directory, File file){
        if(directory==null || file==null)return false;
        return file.toString().contains(directory.toString());
    }

    /**
     * go to given directory. Find first image file with given name
     * @param directory where to look for file
     * @param fileName require file name
     * @return file object of the file found, or null if not found
     */
    public static File findImageFile(File directory , String fileName){
        for(File file : directory.listFiles()){
            String name = file.getName();
            if (name.contains(fileName)) {
                if(MediaFileUtils.isImage(file)){
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * change file name only, rest stays intact for example
     * oldFileWithPath=C:\Users\tomw\AppData\Local\Temp\junit996379714509053068\test_findImageFile\b.jpg
     * newName=xyz
     * result will be C:\Users\tomw\AppData\Local\Temp\junit996379714509053068\test_findImageFile\xyz.jpg
     * @param oldFileWithPath old file
     * @param newBasename new name
     * @return new file, with name changed
     */
    public static File changeFileNameTo(File oldFileWithPath, String newBasename){
        File oldDirectory = oldFileWithPath.getParentFile();
        String oldName = oldFileWithPath.getName();
        String oldbasename = FilenameUtils.getBaseName(oldName);
        String extension = FilenameUtils.getExtension(oldName);

        File newFile = new File(oldDirectory,newBasename+"."+extension);

        return newFile;
    }

}


