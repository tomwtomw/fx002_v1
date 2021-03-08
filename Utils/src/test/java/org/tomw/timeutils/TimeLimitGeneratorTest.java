/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.timeutils;

import java.time.LocalDate;
import java.time.Month;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomw
 */
public class TimeLimitGeneratorTest {
    private final static Logger LOGGER = Logger.getLogger(TimeLimitGeneratorTest.class.getName());
    
    public TimeLimitGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStartOfMonth method, of class TimeLimitGenerator.
     */
    @Test
    public void testGetStartOfMonth() {
        LOGGER.info("testGetStartOfMonth");
        LocalDate now = LocalDate.of(2017, Month.MARCH, 19);
        LocalDate expResult = LocalDate.of(2017, Month.MARCH, 1);
        LocalDate result = TimeLimitGenerator.getStartOfMonth(now);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndOfMonth method, of class TimeLimitGenerator.
     */
    @Test
    public void testGetEndOfMonth() {
        LOGGER.info("getEndOfMonth");
        LocalDate now = LocalDate.of(2017, Month.MARCH, 19);
        LocalDate expResult = LocalDate.of(2017, Month.MARCH, 31);
        LocalDate result = TimeLimitGenerator.getEndOfMonth(now);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartOfYear method, of class TimeLimitGenerator.
     */
    @Test
    public void testGetStartOfYear() {
        LOGGER.info("getStartOfYear");
        LocalDate now = LocalDate.of(2017, Month.MARCH, 19);
        LocalDate expResult = LocalDate.of(2017, Month.JANUARY, 1);
        LocalDate result = TimeLimitGenerator.getStartOfYear(now);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndOfYear method, of class TimeLimitGenerator.
     */
    @Test
    public void testGetEndOfYear() {
        LOGGER.info("getEndOfYear");
        LocalDate now = LocalDate.of(2017, Month.MARCH, 19);
        LocalDate expResult = LocalDate.of(2017, Month.DECEMBER, 31);
        LocalDate result = TimeLimitGenerator.getEndOfYear(now);
        assertEquals(expResult, result);
    }
    
}
