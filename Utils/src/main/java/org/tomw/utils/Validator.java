package org.tomw.utils;

import static org.tomw.utils.TomwStringUtils.BLANK;

public class Validator {
    public static boolean isInteger(String s){
        try {
            int v = Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public static boolean isPositiveInteger(String s){
        try {
            int v = Integer.parseInt(s);
            return v>0;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean isNonWhiteSpace(String s){
        if(s==null){
            return false;
        }else {
            String trimmed = s.trim();
            return !BLANK.equals(trimmed);
        }
    }

    public static boolean isNonNegativeIntegerOrBlank(String text) {
        if(text==null){
            return true;
        }
        text=text.trim();
        if(BLANK.equals(text)){
            return true;
        }
        try{
            int value = Integer.parseInt(text);
            if(value<0){
                return false;
            }else{
                return true;
            }
        }catch(Exception e){
            return false;
        }
    }
}
