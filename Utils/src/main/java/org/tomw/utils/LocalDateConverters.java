package org.tomw.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author tomw
 */
public class LocalDateConverters {

    public static YYYMMDD yyyyMMdd = new YYYMMDD();

    public static class YYYMMDD extends StringConverter<LocalDate>{

        private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

        public YYYMMDD() {
        }


        @Override
        public String toString(LocalDate localDate) {
            if (localDate == null) {
                return "";
            }
            return dateTimeFormatter.format(localDate);
        }

        @Override
        public LocalDate fromString(String dateString) {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateString, dateTimeFormatter);
        }

    }
}

