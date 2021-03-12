/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.util.ArrayList;
import java.util.List;
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
public class CounterPartyTest {
    private final static Logger LOGGER = Logger.getLogger(CounterPartyTest.class.getName());

    public CounterPartyTest() {
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
     * Test of fromCsv method, of class CounterParty.
     */
    @Test
    public void testFromCsv() {
        LOGGER.info("testFromCsv");
        String inputCsv = "short name\tname\ta\tb\tc";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");

        CounterParty actual = CounterParty.fromCsv(inputCsv);

        CounterParty expected = new CounterParty(shortName, name, otherNames);

        boolean result = actual.equalNonIdFields(expected);
        assertTrue(result);
    }

    /**
     * Test of toCsv method, of class CounterParty.
     */
    @Test
    public void testToCsv() {
        LOGGER.info("testToCsv");

        String id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);

        String actual = cp.toCsv();
        String expected = "short name\tname\ta\tb\tc";
        assertEquals(expected, actual);
    }

    /**
     * Test of toJson method, of class CounterParty.
     */
    @Test
    public void testToJsonString() {
        LOGGER.info("testToJsonString");

        String id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty cp = new CounterParty(id, shortName, name, otherNames);
        String actual = cp.toJsonString();

        String expected = "{\"Short Name\":\"short name\",\"Other Names\":[\"a\",\"b\",\"c\"],\"Comment\":\"\",\"name\":\"name\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\"}";

        assertEquals(expected, actual);
    }

    /**
     * Test of fromJsonString method, of class CounterParty.
     * @throws java.lang.Exception
     */
    @Test
    public void testFromJsonString() throws Exception {
        LOGGER.info("testFromJsonString");
        String inputString = "{\"Short Name\":\"short name\",\"Other Names\":[\"a\",\"b\",\"c\"],\"name\":\"name\",\"id\":\"99e3dd67-08c0-40a9-a1b0-30a43b23073f\"}";
        CounterParty actual = CounterParty.fromJsonString(inputString);

        String id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");
        CounterParty expected = new CounterParty(id, shortName, name, otherNames);

        assertEquals(expected, actual);
    }

    /**
     * Test of hashCode method, of class CounterParty.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("testHashCode");

        String id = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName = "short name";
        String name = "name";
        List<String> otherNames = new ArrayList<>();
        otherNames.add("a");
        otherNames.add("b");
        otherNames.add("c");

        CounterParty counterParty = new CounterParty(id, shortName, name, otherNames);

        int actual = counterParty.hashCode();
        int expected = -1958668789;
        
        assertEquals(actual, expected);
    }

    /**
     * Test of equals method, of class CounterParty.
     */
    @Test
    public void testEquals() {
        LOGGER.info("testEquals");
        String id1 = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName1 = "short name";
        String name1 = "name";
        List<String> otherNames1 = new ArrayList<>();
        otherNames1.add("a");
        otherNames1.add("b");
        otherNames1.add("c");

        CounterParty counterParty1 = new CounterParty(id1, shortName1, name1, otherNames1);

        String id2 = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
        String shortName2 = "short name";
        String name2 = "name";
        List<String> otherNames2 = new ArrayList<>();
        otherNames2.add("a");
        otherNames2.add("b");
        otherNames2.add("c");

        CounterParty counterParty2 = new CounterParty(id2, shortName2, name2, otherNames2);

        String id3 = "99e3dd67-08c0-40a9-a1b0-30a43b23073fx";
        String shortName3 = "short name";
        String name3 = "name";
        List<String> otherNames3 = new ArrayList<>();
        otherNames3.add("a");
        otherNames3.add("b");
        otherNames3.add("c");

        CounterParty counterParty3 = new CounterParty(id3, shortName3, name3, otherNames3);

        assertEquals(counterParty1, counterParty2);

        assertFalse(counterParty1.equals(counterParty3));
    }

}
