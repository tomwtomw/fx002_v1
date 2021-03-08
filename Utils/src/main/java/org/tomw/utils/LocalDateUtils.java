package org.tomw.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.apache.log4j.Logger;

import static org.tomw.utils.TomwStringUtils.BLANK;

/**
 * Static class for performing utility operations on LocalDate objects
 *
 * @author tomw
 */
public class LocalDateUtils {

    private final static Logger LOGGER = Logger.getLogger(LocalDateUtils.class.getName());

    private static final String pattern1 = "yyyy-MM-dd";
    private static final DateTimeFormatter formatter1
            = DateTimeFormatter.ofPattern(pattern1);

    private static final String pattern2 = "MM/dd/yyyy";
    private static final DateTimeFormatter formatter2
            = DateTimeFormatter.ofPattern(pattern2);

    private static final String pattern3 = "MM/d/yyyy";
    private static final DateTimeFormatter formatter3
            = DateTimeFormatter.ofPattern(pattern3);

    private static final String pattern4 = "M/dd/yyyy";
    private static final DateTimeFormatter formatter4
            = DateTimeFormatter.ofPattern(pattern4);

    private static final String pattern5 = "M/d/yyyy";
    private static final DateTimeFormatter formatter5
            = DateTimeFormatter.ofPattern(pattern5);

    private static final String pattern6 = "yyyy-MM";
    private static final DateTimeFormatter formatter6
            = DateTimeFormatter.ofPattern(pattern6);

    private static final String pattern7 = "yyyy-MM-dd-HH-mm-ss";
    private static final DateTimeFormatter formatter7
            = DateTimeFormatter.ofPattern(pattern7);

    private static final String pattern8 = "MM/dd/yy";
    private static final DateTimeFormatter formatter8
            = DateTimeFormatter.ofPattern(pattern8);


    static ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

    private static DateTimeParseException parseException;

    public static final DateTimeFormatter usDateFormatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter usDateFormatter2 = DateTimeFormatter.ofPattern("M/dd/yyyy");
    public static final DateTimeFormatter usDateFormatter3 = DateTimeFormatter.ofPattern("MM/d/yyyy");
    public static final DateTimeFormatter usDateFormatter4 = DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Parse strings in us style date MM/DD/yyyy into local date
     * @param s date string
     * @return local date object
     */
    public static LocalDate parseUsStyleDate(String s) {
        boolean exThrown = false;
        Exception e1 = null;
        Exception e2 = null;
        Exception e3 = null;
        Exception e4 = null;
        try {
            LocalDate date = LocalDate.parse(s, usDateFormatter1);
            return date;
        } catch (Exception e) {
            // do nothing
        }
        try {
            LocalDate date = LocalDate.parse(s, usDateFormatter2);
            return date;
        } catch (Exception e) {
            // do nothing
            e2 = e;
        }
        try {
            LocalDate date = LocalDate.parse(s, usDateFormatter3);
            return date;
        } catch (Exception e) {
            // do nothing
            e3 = e;
        }
        try {
            LocalDate date = LocalDate.parse(s, usDateFormatter4);
            return date;
        } catch (Exception e) {
            // do nothing
            e4 = e;
        }
        // if we are here then string has wrong format
        throw new RuntimeException("I cannot parse string " + s + " to date", e2);
    }


    public static LocalDate fromString(String input) {
        LocalDate date;

        input = input.trim();

        date = parsePattern(input, formatter1);
        if (date != null) {
            return date;
        }
        date = parsePattern(input, formatter2);
        if (date != null) {
            return date;
        }
        date = parsePattern(input, formatter3);
        if (date != null) {
            return date;
        }

        date = parsePattern(input, formatter4);
        if (date != null) {
            return date;
        }

        date = parsePattern(input, formatter5);
        if (date != null) {
            return date;
        }

        date = parsePattern(input, formatter8);
        if (date != null) {
            return date;
        }
        parseException.printStackTrace();
        throw parseException;
//        try {
//            date = LocalDate.parse(input, formatter1);
//        } catch (DateTimeParseException ex1) {
//            String message = "failed to parse the date in pattern " + pattern1;
//            //LOGGER.info(message, ex1);
//            //System.out.println(message);
//        }
//        if (date == null) {
//            try {
//                date = LocalDate.parse(input, formatter2);
//            } catch (DateTimeParseException ex2) {
//                String message = "failed to parse the date in pattern " + pattern2;
//                LOGGER.error(message, ex2);
//                throw ex2;
//            }
//        }
//        return date;
    }

    /**
     * Parse date from string in the format YYYY-MM-DD, ex 2017-11-07
     * @param input string
     * @return local date object
     */
    public static LocalDate fromYYYY_MM_DD_String(String input){
        LocalDate date = null;
        input = input.trim();
        return parsePattern(input, formatter1);
    }

    private static LocalDate parsePattern(String in, DateTimeFormatter formatter) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(in, formatter);
        } catch (DateTimeParseException ex) {
            parseException = ex;
//                String message = "failed to parse the date in pattern " + pattern2;
//                LOGGER.error(message, ex2);
//                throw ex2;
        }
        return date;
    }

    /**
     * convert date to string in the form YYYY-MM
     *
     * @param date date to be converted
     * @return date as YYYY-MM string
     */
    public static String toYYYYMM(LocalDate date) {
        return date.format(formatter6);
    }

    /**
     * Take list of dates, get the first and last one. Return list of years and
     * months YYYY-MM which correspond to those dates, including any gaps in the
     * months.
     *
     * @param dates dates to be converted
     * @return dates as YYYY-MM strings
     */
    public static List<String> getYearsAndMonths(List<LocalDate> dates) {
        if (dates == null) {
            return new ArrayList<>();
        }

        if (dates.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDate startDate = dates.get(0);
        LocalDate endDate = startDate;

        for (LocalDate date : dates) {
            if (startDate.isAfter(date)) {
                startDate = date;
            }
            if (endDate.isBefore(date)) {
                endDate = date;
            }
        }
        return getYearsAndMonthsBetween(startDate, endDate);
    }

    /**
     * Take input dates start and end. Return list of strings YYYY-MM
     * representing all months and years between those dates, including start
     * and end date
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of months and years between
     */
    public static List<String> getYearsAndMonthsBetween(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        String endDateAsYYYYMM = toYYYYMM(endDate);
        String currentDateAsYYYMM = toYYYYMM(currentDate);

        List<String> listOfYearsAndMonthsBetween = new ArrayList<>();

        listOfYearsAndMonthsBetween.add(currentDateAsYYYMM);

        while (!currentDateAsYYYMM.equals(endDateAsYYYYMM)) {
            currentDate = currentDate.plusMonths(1);
            currentDateAsYYYMM = toYYYYMM(currentDate);
            if (!listOfYearsAndMonthsBetween.contains(currentDateAsYYYMM)) {
                listOfYearsAndMonthsBetween.add(currentDateAsYYYMM);
            }
        }

        return listOfYearsAndMonthsBetween;
    }

    public static String toYYYYMMDD(LocalDate date) {
        return date.format(formatter1);
    }

    public static String toYYYYMMDDHHMMSS(LocalDateTime date) {

        if(date==null) {
            return BLANK;
        }else{
            return date.format(formatter7);
        }
    }

    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0) {
            return null;
        }
//        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone
//                .getDefault().toZoneId());
       return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getTimeZone("US/Eastern").toZoneId());

    }

    public static long getTimestampFromLocalDateTime(LocalDateTime localDateTime){
        return localDateTime.toEpochSecond(zoneOffset);
    }
}
