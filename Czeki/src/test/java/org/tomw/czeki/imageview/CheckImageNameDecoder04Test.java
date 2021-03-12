package org.tomw.czeki.imageview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CheckImageNameDecoder04Test {

    CheckImageNameDecoder decoder = new CheckImageNameDecoder04();
    File directory = new File("directory");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_IsCorrectFormat() throws Exception {
        System.out.println("test_IsCorrectFormat");
        assertTrue(decoder.isCorrectFormat(new File(directory,"1245F.jpg")));
        assertFalse(decoder.isCorrectFormat(new File(directory,"1245-F.jpg")));
    }

    @Test
    public void test_IsValid() throws Exception {
        System.out.println("test_IsValid");
        assertTrue(decoder.isValid(new File(directory,"1245F.jpg")));
        assertFalse(decoder.isValid(new File(directory,"1245-F.jpg")));
        assertFalse(decoder.isValid(new File(directory,"1245F.txt")));
    }

    @Test
    public void test_HasCorrectLength() throws Exception {
        System.out.println("test_HasCorrectLength");
        assertTrue(decoder.hasCorrectLength(new File(directory,"1245F.jpg")));
        assertFalse(decoder.hasCorrectLength(new File(directory,"1245-F.jpg")));
    }

    @Test
    public void test_IsFront() throws Exception {
        System.out.println("test_IsFront");
        assertTrue(decoder.isFront(new File(directory,"1245F.jpg")));
        assertFalse(decoder.isFront(new File(directory,"1245B.jpg")));
    }

    @Test
    public void test_IsBack() throws Exception {
        System.out.println("test_IsBack");
        assertTrue(decoder.isBack(new File(directory,"1245B.jpg")));
        assertFalse(decoder.isBack(new File(directory,"1245F.jpg")));
    }


    @Test
    public void test_GetCheckNumber() throws Exception {
        System.out.println("test_GetCheckNumber");
        assertEquals("1245",decoder.getCheckNumber(new File(directory,"1245B.jpg")));
        assertEquals("1245",decoder.getCheckNumber(new File(directory,"1245F.jpg")));
    }

    @Test
    public void test_GetSide() throws Exception {
        System.out.println("test_GetSide");
        assertEquals(CzekImageUtils.BACK,decoder.getSide(new File(directory,"1245B.jpg")));
        assertEquals(CzekImageUtils.FRONT,decoder.getSide(new File(directory,"1245F.jpg")));
    }


}