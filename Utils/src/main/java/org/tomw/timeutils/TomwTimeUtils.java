package org.tomw.timeutils;

import java.time.LocalDate;

/**
 * Utilities for performing time operations
 * Created by tomw on 7/8/2017.
 */
public class TomwTimeUtils {
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int SECONDS_IN_HOUR = 60*60;
    public static final int SECONDS_IN_DAY = 60*60*24;

    public static String seconds2String(long seconds){
        String result="";
        long nDays = seconds / SECONDS_IN_DAY;
        if(nDays>0){
            result=result+nDays+"d";
        }
        long nHours = (seconds-nDays*SECONDS_IN_DAY)/SECONDS_IN_HOUR;
        if(nHours>0){
            result=result+nHours+"h";
        }else{
            result=result+"0h";
        }
        long nMinutes = (seconds-nDays*SECONDS_IN_DAY-nHours*SECONDS_IN_HOUR)/SECONDS_IN_MINUTE;
        if(nMinutes>0){
            result=result+nMinutes+"m";
        }else{
            result=result+"0m";
        }
        long nSeconds = (seconds-nDays*SECONDS_IN_DAY-nHours*SECONDS_IN_HOUR-nMinutes*SECONDS_IN_MINUTE);
        if(nSeconds>0){
            result=result+nSeconds+"s";
        }else{
            result=result+"0s";
        }
        return result;
    }


}
