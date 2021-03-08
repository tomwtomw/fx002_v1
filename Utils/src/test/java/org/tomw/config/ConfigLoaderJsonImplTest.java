package org.tomw.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigLoaderJsonImplTest {

    SelfIdentificationService selfIdService = new SelfIdentificationServiceForConfigLoaderTest();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_Load() throws Exception {
        boolean exceptionThrown = false;
        try {
            ApplicationConfigLoader configLoader = new ConfigLoaderJsonImpl(selfIdService);
            configLoader.load();
        }catch(Exception e){
            exceptionThrown=true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void test_GetValue() throws Exception {
        System.out.println("test_GetValue");
        ApplicationConfigLoader configLoader = new ConfigLoaderJsonImpl(selfIdService);
        configLoader.load();
        String expected="value123";
        String actual = configLoader.getValue("key123");
        assertEquals(expected, actual);
    }
}