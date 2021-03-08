package org.tomw.fileutils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileUtils {

    /**
     * read file into list of strings
     * @param file to be read
     * @return lines in file as list of strings
     */
    public static List<String> readlines(File file) throws IOException {
        List<String> result = new ArrayList<>();
        return FileUtils.readLines(file, Charsets.UTF_8);
    }

    /**
     * read file to list of lines. Omit comment and empty ones
     * @param file to be read
     * @return list of lines, excluding comment ones
     * @throws IOException if something goes wrong with reading file
     */
    public static List<String> readNonEmptynonCommentLines(File file) throws IOException {
        List<String> result = new ArrayList<>();
        for(String line : TextFileUtils.readlines(file)){
            if(!TomwStringUtils.BLANK.equals(line.trim()) && TextFileUtils.isNotCommentLine(line)){
                result.add(line);
            }
        }
        return result;
    }

    /**
     * verify if line is not a comment line
     * @param line to be checked for comment sign
     * @return true if it is not a comment line
     */
    public static boolean isNotCommentLine(String line) {
        if(line==null){
            return true;
        }
        line=line.trim();
        if(line.startsWith("#") || line.startsWith("//")){
            return false;
        }else{
            return true;
        }
    }
}
