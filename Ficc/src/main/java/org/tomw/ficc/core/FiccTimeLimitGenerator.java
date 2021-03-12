package org.tomw.ficc.core;

import org.tomw.timeutils.TimeLimitGenerator;

import java.time.LocalDate;
import java.time.Month;

public class FiccTimeLimitGenerator extends TimeLimitGenerator {

    public static final int PERIOD_STARTS = 15;

    /**
     * get start of current period
     * @return
     */
    public static LocalDate getStartOfCurrentPeriod(){
        return getStartOfPeriod(LocalDate.now());
    }

    /**
     * get statrt of previous period
     * @return
     */
    public static LocalDate getStartOfPreviousPeriod(){
        return getStartOfPeriod(getStartOfCurrentMonth());
    }

    /**
     * get start accounting period for day date
     * @param date
     * @return
     */
    public static LocalDate getStartOfPeriod(LocalDate date){
        Month month = date.getMonth();
        int day = date.getDayOfMonth();
        if(day<PERIOD_STARTS){
            month=month.minus(1);
        }
        LocalDate result = LocalDate.of(date.getYear(),month.getValue(),PERIOD_STARTS);
        return result;
    }
}
