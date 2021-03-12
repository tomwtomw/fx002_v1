/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
public class TwoSidedCheckImageTest {

    JSONParser jsonParser = new JSONParser();

    public TwoSidedCheckImageTest() {
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
     * Test of getFrontPageLocation method, of class CheckImage.
     */
    @Test
    public void testGetFrontPageLocation() {
        System.out.println("testGetFrontPageLocation");
        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();

        assertEquals(TwoSidedCheckImage.BLANK, instance1.getFrontPageLocation());
        assertEquals(TwoSidedCheckImage.BLANK, instance1.getBackPageLocation());

        String expFront = "frontPage";
        String expBack  = "backPage";
        TwoSidedCheckImage instance2 = new TwoSidedCheckImage(expFront, expBack);
        assertEquals(expFront, instance2.getFrontPageLocation());
        assertEquals(expBack , instance2.getBackPageLocation());
    }

    /**
     * Test of setFrontPageLocation method, of class CheckImage.
     */
    @Test
    public void testSetFrontPageLocation() {
        System.out.println("setFrontPageLocation");

        String expFront = "frontPage";
        String expBack = "backPage";
        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();
        instance1.setFrontPageLocation(expFront);
        instance1.setBackPageLocation(expBack);

        assertEquals(expFront, instance1.getFrontPageLocation());
        assertEquals(expBack, instance1.getBackPageLocation());
    }

    /**
     * Test of getBackPageLocation method, of class CheckImage.
     */
    @Test
    public void testGetBackPageLocation() {
        testSetFrontPageLocation();
    }

    /**
     * Test of setBackPageLocation method, of class CheckImage.
     */
    @Test
    public void testSetBackPageLocation() {
        testSetFrontPageLocation();
    }

    /**
     * Test of toJson method, of class CheckImage.
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testToJson() throws ParseException {
        System.out.println("toJson");

        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();

        String expFront = "frontPage";
        String expBack = "backPage";
        TwoSidedCheckImage instance2 = new TwoSidedCheckImage(expFront, expBack);

        String s1 = "{\"Front\":\"\",\"Back\":\"\"}";
        JSONObject expResult1 = (JSONObject) jsonParser.parse(s1);
        JSONObject result1 = instance1.toJson();
        assertEquals(expResult1, result1);

        String s2 = "{\"Front\":\"frontPage\",\"Back\":\"backPage\"}";
        JSONObject expResult2 = (JSONObject) jsonParser.parse(s2);
        JSONObject result2 = instance2.toJson();
        assertEquals(expResult2, result2);
    }

    /**
     * Test of fromJson method, of class CheckImage.
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testFromJson() throws ParseException {
        System.out.println("fromJson");

        String s1 = "{\"Front\":\"\",\"Back\":\"\"}";
        JSONObject json1 = (JSONObject) jsonParser.parse(s1);
        TwoSidedCheckImage expected1 = new TwoSidedCheckImage();
        TwoSidedCheckImage actual1 = TwoSidedCheckImage.fromJson(json1);
        assertEquals(expected1, actual1);

        String s2 = "{\"Front\":\"frontPage\",\"Back\":\"backPage\"}";
        JSONObject json2 = (JSONObject) jsonParser.parse(s2);
        TwoSidedCheckImage expected2 = new TwoSidedCheckImage("frontPage", "backPage");
        TwoSidedCheckImage actual2 = TwoSidedCheckImage.fromJson(json2);
        assertEquals(expected2, actual2);
    }

    /**
     * Test of toString method, of class CheckImage.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        String s1 = "{\"Front\":\"\",\"Back\":\"\"}";
        String s2 = "{\"Front\":\"frontPage\",\"Back\":\"backPage\"}";

        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();

        String expFront = "frontPage";
        String expBack = "backPage";
        TwoSidedCheckImage instance2 = new TwoSidedCheckImage(expFront, expBack);

        assertEquals(s1, instance1.toString());
        assertEquals(s2, instance2.toString());
    }

    /**
     * Test of hashCode method, of class CheckImage.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();

        String expFront = "frontPage";
        String expBack = "backPage";
        TwoSidedCheckImage instance2 = new TwoSidedCheckImage(expFront, expBack);

        int h1 = 35287;
        int h2 = -1702364587;

        assertEquals(h1, instance1.hashCode());
        assertEquals(h1, instance1.hashCode());
    }

    /**
     * Test of equals method, of class CheckImage.
     */
    @Test
    public void testEquals() {
        
        TwoSidedCheckImage instance1 = new TwoSidedCheckImage();

        String expFront = "frontPage";
        String expBack = "backPage";
        TwoSidedCheckImage instance2 = new TwoSidedCheckImage(expFront, expBack);
        TwoSidedCheckImage instance3 = new TwoSidedCheckImage(expFront, expBack);
        
        assertTrue(instance2.equals(instance3));
        assertFalse(instance1.equals(instance3));
    }

}
