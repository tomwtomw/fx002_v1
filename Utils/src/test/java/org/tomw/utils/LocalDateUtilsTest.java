package org.tomw.utils;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author tomw
 */
public class LocalDateUtilsTest {
    private final static Logger LOGGER = Logger.getLogger(LocalDateUtilsTest.class.getName());

    public LocalDateUtilsTest() {
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

    @Test
    public void test_parseUsStyleDate(){

        assertEquals(LocalDate.of(2019,12,7),LocalDateUtils.parseUsStyleDate("12/7/2019"));
        assertEquals(LocalDate.of(2019,12,7),LocalDateUtils.parseUsStyleDate("12/07/2019"));
        assertEquals(LocalDate.of(2019,7,12),LocalDateUtils.parseUsStyleDate("7/12/2019"));
        assertEquals(LocalDate.of(2019,11,12),LocalDateUtils.parseUsStyleDate("11/12/2019"));

        // bad date
        boolean exThrown = false;
        try{
            LocalDateUtils.parseUsStyleDate("6/31/2019");
        }catch(Exception e){
            exThrown=false;
        }
    }


    /**
     * Test of fromString method, of class LocalDateUtils.
     */
    @Test
    public void testFromString() {
        LOGGER.info("fromString");
        String input = "1999-10-12";
        LocalDate expResult = LocalDate.of(1999, 10, 12);
        LocalDate result = LocalDateUtils.fromString(input);
        assertEquals(expResult, result);

        String input2 = "10/12/1999";
        LocalDate result2 = LocalDateUtils.fromString(input2);
        assertEquals(expResult, result2);

        String input3 = "10/1/1999";
        LocalDate expResult3 = LocalDate.of(1999, 10, 1);
        LocalDate result3 = LocalDateUtils.fromString(input3);
        assertEquals(expResult3, result3);

        String input4 = "1/2/1999";
        LocalDate expResult4 = LocalDate.of(1999, 1, 2);
        LocalDate result4 = LocalDateUtils.fromString(input4);
        assertEquals(expResult4, result4);

    }

    @Test
    public void testToYYYYMM() {
        LOGGER.info("testToYYYYMM");
        LocalDate date = LocalDate.of(1999, 10, 12);
        String expected = "1999-10";
        String actual = LocalDateUtils.toYYYYMM(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testToYYYYMMDD() {
        LOGGER.info("testToYYYYMMDD");
        LocalDate date = LocalDate.of(1999, 10, 12);
        String expected = "1999-10-12";
        String actual = LocalDateUtils.toYYYYMMDD(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetYearsAndMonthsBetween() {
        LOGGER.info("testGetYearsAndMonthsBetween");

        LocalDate startDate = LocalDate.of(1999, 10, 2);
        LocalDate endDate = LocalDate.of(2000, 6, 12);

        List<String> expectedYearsAndMonths = Arrays.asList(
                "1999-10",
                "1999-11",
                "1999-12",
                "2000-01",
                "2000-02",
                "2000-03",
                "2000-04",
                "2000-05",
                "2000-06"
        );

        List<String> actualYearsAndMonths = LocalDateUtils.getYearsAndMonthsBetween(startDate, endDate);

        assertEquals(expectedYearsAndMonths, actualYearsAndMonths);
    }

    @Test
    public void testGetYearsAndMonths() {
        LOGGER.info("testGetYearsAndMonths");

        List<LocalDate> listOfDates = new ArrayList<>();
        listOfDates.add(LocalDate.of(2000, 6, 12));
        listOfDates.add(LocalDate.of(1999, 10, 2));
        listOfDates.add(LocalDate.of(2000, 1, 14));

        List<String> expectedYearsAndMonths = Arrays.asList(
                "1999-10",
                "1999-11",
                "1999-12",
                "2000-01",
                "2000-02",
                "2000-03",
                "2000-04",
                "2000-05",
                "2000-06"
        );

        List<String> actualYearsAndMonths = LocalDateUtils.getYearsAndMonths(listOfDates);

        assertEquals(expectedYearsAndMonths, actualYearsAndMonths);
    }


    @Test
    public void test_FromYYYY_MM_DD_String() throws Exception {
        System.out.println("test_FromYYYY_MM_DD_String");

        String s = "2017-11-07";
        LocalDate date = LocalDateUtils.fromYYYY_MM_DD_String(s);
        assertEquals(2017,date.getYear());
        assertEquals(11,date.getMonthValue());
        assertEquals(7,date.getDayOfMonth());
    }
}
