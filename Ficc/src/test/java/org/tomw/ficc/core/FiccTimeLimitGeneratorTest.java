package org.tomw.ficc.core;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class FiccTimeLimitGeneratorTest {
    @Test
    public void test_GetStartOfPeriod() throws Exception {
        LocalDate day = LocalDate.of(2017, 7, 18);
        LocalDate expected = LocalDate.of(2017, 7, 15);
        LocalDate actual = FiccTimeLimitGenerator.getStartOfPeriod(day);
        assertEquals(expected, actual);

        day = LocalDate.of(2017, 7, 12);
        expected = LocalDate.of(2017, 6, 15);
        actual = FiccTimeLimitGenerator.getStartOfPeriod(day);
        assertEquals(expected, actual);

        day = LocalDate.of(2017, 7, 15);
        expected = LocalDate.of(2017, 7, 15);
        actual = FiccTimeLimitGenerator.getStartOfPeriod(day);
        assertEquals(expected, actual);
    }
}