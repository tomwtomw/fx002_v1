/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.fileutils;

import org.junit.*;
import org.tomw.utils.String2Boolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author tomw
 */
public class String2BooleanTest {
    
    public String2BooleanTest() {
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
     * Test of parse method, of class String2Boolean.
     */
    @Test
    public void testParse() {
        System.out.println("testParse");
        String s = "Yes";
        assertTrue(String2Boolean.parse("Yes"));
        assertTrue(String2Boolean.parse("YES"));
        assertTrue(String2Boolean.parse("yes"));
        assertTrue(String2Boolean.parse("y"));
        assertTrue(String2Boolean.parse("t"));
        assertTrue(String2Boolean.parse("true"));
        
        assertFalse(String2Boolean.parse("F"));
        assertFalse(String2Boolean.parse("False"));
        assertFalse(String2Boolean.parse("No"));
        assertFalse(String2Boolean.parse("N"));
        assertFalse(String2Boolean.parse(""));
        assertFalse(String2Boolean.parse(" "));
        assertFalse(String2Boolean.parse(null));
        
        assertFalse(String2Boolean.parse("0"));
        assertFalse(String2Boolean.parse("0000"));
        assertFalse(String2Boolean.parse("-0000"));
        
        assertTrue(String2Boolean.parse("1"));
        assertTrue(String2Boolean.parse("001"));
        assertTrue(String2Boolean.parse("+1"));
        
        boolean exceptionThrown=false;
        try{
            assertTrue(String2Boolean.parse("ABChjwe"));
        }catch(RuntimeException e){
            exceptionThrown=true;
        }
        assertTrue(exceptionThrown);
    }
    
}
