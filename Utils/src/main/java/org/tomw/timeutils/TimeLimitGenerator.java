/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.timeutils;

import java.time.LocalDate;
import java.time.Month;

/**
 * Get limits of time intervals
 *
 * @author tomw
 */
public class TimeLimitGenerator {    

    public static LocalDate getStartOfCurrentMonth() {
        return getStartOfMonth(LocalDate.now());
    }

    public static LocalDate getEndOfCurrentMonth() {
        return getEndOfMonth(LocalDate.now());
    }

    public static LocalDate getStartOfCurrentYear() {
        return getStartOfYear(LocalDate.now());
    }

    public static LocalDate getEndOfCurrentYear() {
        return getEndOfYear(LocalDate.now());
    }

    public static LocalDate getStartOfPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate monthAgo = today.plusMonths(-1);
        return getStartOfMonth(monthAgo);
    }

    public static LocalDate getEndOfPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate monthAgo = today.plusMonths(-1);
        return getEndOfMonth(monthAgo);
    }

    public static LocalDate getStartOfPreviousYear() {
        LocalDate today = LocalDate.now();
        LocalDate yearAgo = today.plusYears(-1);
        return getStartOfYear(yearAgo);
    }

    public static LocalDate getEndOfPreviousYear() {
        LocalDate today = LocalDate.now();
        LocalDate yearAgo = today.plusYears(-1);
        return getEndOfYear(yearAgo);
    }

    public static LocalDate getStartOfMonth(LocalDate now) {
        return LocalDate.of(now.getYear(),now.getMonth().getValue(),1);
    }

    public static LocalDate getEndOfMonth(LocalDate now) {
        return LocalDate.of(now.getYear(),now.getMonth().getValue(),now.lengthOfMonth());
    }

    public static LocalDate getStartOfYear(LocalDate now) {
        return LocalDate.of(now.getYear(),Month.JANUARY,1);
    }

    public static LocalDate getEndOfYear(LocalDate now) {
        return LocalDate.of(now.getYear(),Month.DECEMBER,31);
    }

}
