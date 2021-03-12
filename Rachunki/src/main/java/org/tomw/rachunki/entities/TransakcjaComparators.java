package org.tomw.rachunki.entities;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Comparators to compare Transakcja objects
 */
public class TransakcjaComparators {

    public static Comparator dateComparator = new Comparator<Transakcja>(){

        @Override
        public int compare(Transakcja t1, Transakcja t2) {
            if(t1==null && t2==null){
                return 0;
            }
            if(t1==null){
                return 1;
            }
            if(t2==null){
                return -1;
            }
            LocalDate date1= t1.getTransactionDate();
            LocalDate date2= t2.getTransactionDate();
            if(date1==null && date2==null){
                return 0;
            }
            if(date1==null){
                return 1;
            }
            if(date2==null){
                return -1;
            }
            return date1.compareTo(date2);
        }
    } ;
}
