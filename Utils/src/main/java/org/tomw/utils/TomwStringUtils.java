package org.tomw.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * static utility class for performing common operations on strings
 * @author tomw
 */
public class TomwStringUtils {
    public static final String BLANK="";
    public static final String TAB="\t";
    public static final String SPACE=" ";
    public static final String EOL="\n";
    public static final String COMMENT_SIGN="#";
    public static final String Y="y";

    public static final String ERROR="Error";

    private static final long KILOBYTE = 1000L;
    private static final long MEGABYTE = KILOBYTE*1000L;
    private static final long GIGABYTE = MEGABYTE*1000L;
    private static final long TERABYTE = GIGABYTE*1000L;

    private static final String[] stringTrue={"y","yes","true","t","1"};
    private static final String[] stringFalse={"n","no","false","f","0",BLANK};

    public static final String ENCODING_ISO="ISO-8859-1";
    public static final String ENCODING_UTF8="utf8";
    public static final String ENCODING_LATIN1="latin1";


    /**
     * remove from string adjacent spaces, replace them with single space
     * @param input string
     * @return string without double empty spaces
     */
    public static String removeDoubleSpaces(String input){
        String doubleSpace=SPACE+SPACE;

        String newString=input.replace(doubleSpace,SPACE );
        while(!input.equals(newString)){
            input=newString;
            newString=input.replace(doubleSpace,SPACE );
        }
        return newString;
    }

    /**
     * remove comment  lines from string
     * @param input input string
     * @return input without comment  lines
     */
    public static String filterCommentLines(String input){
        StringBuilder result= new StringBuilder();
        for(String line : input.split(EOL)){
            if(!line.startsWith(COMMENT_SIGN)){
                result.append(line).append(EOL);
            }
        }
        return result.toString();
    }

    /**
     * remove empty lines from string
     * @param input string
     * @return input without empty lines
     */
    public static String filterEmptyLines(String input){
        StringBuilder result= new StringBuilder();
        for(String line : input.split(EOL)){
            line=line.trim();
            if(!BLANK.equals(line)){
                result.append(line).append(EOL);
            }
        }
        return result.toString();
    }

    /**
     * Convert bytes to string
     * @param nBytes bymber of bytes to be formatted as string
     */
    public static String bytes2String(long nBytes){
        if(nBytes<KILOBYTE){
            return nBytes+" bytes";
        }
        if(nBytes<MEGABYTE){
            return String.format("%.1f Kb",((double)nBytes)/KILOBYTE);
        }
        if(nBytes<GIGABYTE){
            return String.format("%.1f Mb",((double)nBytes)/MEGABYTE);
        }
        if(nBytes<TERABYTE){
            double x = ((double)nBytes)/GIGABYTE;
            return String.format("%.1f Gb",x);
        }
        System.out.println("Terabyte="+TERABYTE);
        System.out.println(nBytes/TERABYTE);
        return String.format("%.1f Tb",((double)nBytes)/TERABYTE);
    }

    public static boolean stringToBoolean(String s){
        if(s==null){
            return false;
        }
        for(String word : stringTrue){
            if(word.equalsIgnoreCase(s)){
                return true;
            }
        }
        for(String word : stringFalse){
            if(word.equalsIgnoreCase(s)){
                return false;
            }
        }
        throw new RuntimeException("String "+s+" does not represent known boolean value");
    }

    public static String boolean2BlankY(boolean b){
        if(b){
            return Y;
        }else{
            return BLANK;
        }
    }

    public static String normalizeBooleanToBlankY(String s) {
        return boolean2BlankY(stringToBoolean(s));
    }

    public static String money2String(double value) {
        return String.format("%.2f", (double) value);
    }
    /**
     * remove last coma trom string "a,bcd,e" --> "a,bcde"
     * @param s string representing money
     * @return string without last coma
     */
    public static String removeLastComa(String s){
        if (!s.contains(",")) {
            return s;
        }else{
            int lastIndexOfComa = s.lastIndexOf(",");
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(lastIndexOfComa);
            return sb.toString();
        }

    }

    /**
     * format double into money format ie two decimal places
     * @param sum money as double
     * @return money up to two decimal points
     */
    public static String formatMoney(double sum) {
        return String.format("%1$.2f",sum);
    }

    /**
     * replace backslash charactes with a slash
     * @param s string containing \ chars
     * @return string without \, replaced by /
     */
    public static String backslash2slash(String s){
        return s.replace("\\","/");
    }

    /**
     * convert string to integer. If string is empty or null return 0
     * @param s string to be converted
     * @return integer
     */
    public static int string2IntIncludingBlank(String s) {
        if(StringUtils.isBlank(s)){
            return 0;
        }else{
            return Integer.parseInt(s.trim());
        }
    }

    /**
     * if is is null, return blank, otherwise return s
     * @param s string
     * @return s or blank
     */
    public static String null2blank(String s) {
        if(s==null){
            return BLANK;
        }else{
            return s;
        }
    }

    /**
     * Compare two strings, take int account that they may be nulls
     * @param text first text to compare
     * @param text1 second to compare
     * @return true of both strings are the same or both are null
     */
    public static boolean stringNullEquals(String text, String text1) {
        if(text==null && text1==null){
            return true;
        }
        if(text==null){
            return false;
        }
        if(text1==null){
            return false;
        }
        return text1.equals(text);
    }

    /**
     * Compare the strings. Include the case where either can be null.
     * If one is blank string and other null return true
     * @param text1 first string
     * @param text2 second string
     * @return true if strings are equal or if both are empty
     */
    public static boolean stringNullCompareOrBothEmpty(String text1, String text2) {
        if(stringNullEquals(text1,text2)){
            return true;
        }else{
            if(StringUtils.isBlank(text1) && StringUtils.isBlank((text2))){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * cut string to given length. Add ... at the end if it is cut.
     * @param s input string
     * @param len desired length
     * @return short string
     */
    public static String cutString(String s, int len){
        if(s==null)return null;
        if(s.length()>len){
            return s.substring(0,len)+"...";
        }else{
            return s;
        }
    }
    /**
     * Cut the given string to length 25. Add ... if longer than 25.
     * @param s string to be cut
     * @return the trimmed string
     */
    public static String cutString(String s){
        return cutString(s,25);
    }

    /**
     * return a short unique string, a substring of the uuid string
     * @return short unique string
     */
    public static String getShortUniqueString(){
        return UUID.randomUUID().toString().split("-")[0];
    }


    /**
     * Parse string to int
     * @param s string
     * @return int value
     */
    public static int parseToInt(String s){
        int result = Integer.parseInt(s);
        return result;
    }

    /**
     * Does the string represent it?
     * @param s string
     * @return true if it is int
     */
    public static boolean representsInt(String s){
        boolean isInt=true;
        try{
            Integer.parseInt(s);
        }catch(NumberFormatException e){
            isInt=false;
        }
        return isInt;
    }

    /**
     * take a long string. Trim it by removing fron and end whitespaces. Return first line of result
     * @param commentIn input string
     * @return first not blank line of input string
     */
    public static String firstLine(String commentIn) {
        if(commentIn==null){
            return BLANK;
        }else{
            String trimmed = commentIn.trim();
            String firstline = trimmed.split(EOL)[0];
            return firstline;
        }
    }


    /**
     * Compare two strings, taking into account that they may be null
     * @param obj1 first string to be compared
     * @param obj2 second string
     * @return -1,0 or 1
     */
    public static int compareStrings(String obj1, String obj2) {
        if (obj1 == obj2) {
            return 0;
        }
        if (obj1 == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        return obj1.compareTo(obj2);
    }

    /**
     * if lastCheckNumber represents positive integer, incremant it by 1. Otherwise do not change it
     * @param lastCheckNumber string representing check number
     * @return the check number incremanted by 1, if it is a positive integer
     */
    public static String incrementCheckNumber(String lastCheckNumber) {
        if (representsInt(lastCheckNumber)) {
            int nextCheckNumber = parseToInt(lastCheckNumber) + 1;
            if (nextCheckNumber > 0) {
                lastCheckNumber = Integer.toString(nextCheckNumber);
            }
        }
        return lastCheckNumber;
    }


}
