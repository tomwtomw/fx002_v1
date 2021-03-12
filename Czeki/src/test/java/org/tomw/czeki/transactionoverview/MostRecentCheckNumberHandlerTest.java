package org.tomw.czeki.transactionoverview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MostRecentCheckNumberHandlerTest {

    private MostRecentCheckNumberHandler handler = new MostRecentCheckNumberHandler();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_IncrementCheckNumber() throws Exception {
        System.out.println("test_IncrementCheckNumber");

        handler.setLastCheckNumber(null);
        assertEquals(null, handler.getLastCheckNumber());
        assertEquals(null, handler.incrementCheckNumber());
        assertEquals(null, handler.getLastCheckNumber());

        handler.setLastCheckNumber("");
        assertEquals("", handler.getLastCheckNumber());
        assertEquals("", handler.incrementCheckNumber());
        assertEquals("", handler.getLastCheckNumber());

        handler.setLastCheckNumber("ABCD");
        assertEquals("ABCD", handler.getLastCheckNumber());
        assertEquals("ABCD", handler.incrementCheckNumber());
        assertEquals("ABCD", handler.getLastCheckNumber());

        handler.setLastCheckNumber("1234");
        assertEquals("1234", handler.getLastCheckNumber());
        assertEquals("1235", handler.incrementCheckNumber());
        assertEquals("1235", handler.getLastCheckNumber());

        handler.setLastCheckNumber("-234");
        assertEquals("-234", handler.getLastCheckNumber());
        assertEquals("-234", handler.incrementCheckNumber());
        assertEquals("-234", handler.getLastCheckNumber());

        handler.setLastCheckNumber("1.34");
        assertEquals("1.34", handler.getLastCheckNumber());
        assertEquals("1.34", handler.incrementCheckNumber());
        assertEquals("1.34", handler.getLastCheckNumber());
    }

    @Test
    public void test_ParseToInt() throws Exception {
        System.out.println("test_ParseToInt");
        assertEquals(123, MostRecentCheckNumberHandler.parseToInt("123"));
        assertEquals(-123, MostRecentCheckNumberHandler.parseToInt("-123"));

        boolean exceptionThrown = false;
        try {
            MostRecentCheckNumberHandler.parseToInt("-1.23");
        } catch (NumberFormatException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            MostRecentCheckNumberHandler.parseToInt("123A");
        } catch (NumberFormatException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void test_RepresentsInt() throws Exception {
        System.out.println("test_RepresentsInt");

        assertTrue(MostRecentCheckNumberHandler.representsInt("1234"));
        assertTrue(MostRecentCheckNumberHandler.representsInt("-1234"));
        assertTrue(MostRecentCheckNumberHandler.representsInt("0"));

        assertFalse(MostRecentCheckNumberHandler.representsInt("1234.0"));
        assertFalse(MostRecentCheckNumberHandler.representsInt("-1234.0"));
        assertFalse(MostRecentCheckNumberHandler.representsInt("0."));

        assertFalse(MostRecentCheckNumberHandler.representsInt("123A"));
    }

}