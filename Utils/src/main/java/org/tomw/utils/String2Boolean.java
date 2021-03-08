package org.tomw.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Converting strings to boolean
 * @author tomw
 */
public class String2Boolean {
    private static final List<String> trueStrings = new ArrayList<>();
    private static final List<String> falseStrings = new ArrayList<>();

    static{
        trueStrings.add("T");
        trueStrings.add("True");
        trueStrings.add("Yes");
        trueStrings.add("Y");

        falseStrings.add("False");
        falseStrings.add("F");
        falseStrings.add("No");
        falseStrings.add("N");
        falseStrings.add("");
    }

    public static boolean parse(String s){
        if(s==null){
            return false;
        }
        s=s.trim();
        if(paseTrueString(s)){
            return true;
        }
        if(parseFalseString(s)){
            return false;
        }
        if(parseTrueInteger(s)){
            return true;
        }
        if(parseFalseInteger(s)){
            return false;
        }
        throw new RuntimeException(s+" cannot be parsed as boolean value");
    }

    private static boolean paseTrueString(String s) {
        for(String entry : trueStrings){
            if(entry.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    private static boolean parseFalseString(String s) {
        for(String entry : falseStrings){
            if(entry.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }

    private static  boolean parseTrueInteger(String s) {
        int intS = Integer.parseInt(s);
        return intS==1;
    }

    private static boolean parseFalseInteger(String s) {
        int intS = Integer.parseInt(s);
        return intS==0;
    }
}


