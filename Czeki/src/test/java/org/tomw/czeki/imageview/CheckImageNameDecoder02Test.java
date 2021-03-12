package org.tomw.czeki.imageview;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CheckImageNameDecoder02Test {
    private String correctFileNameFront="/test/C7D19FBA1234200F.jpg";
    private String correctFileNameBack= "/test/C7D19FBA1234200B.jpg";
    private String badFileNameNeitherFrontNorBack="/test/C7D19FBA1234200X.jpg";
    private String badFileNameWrongFormat="/test/C7D19FBA1----234200B.jpg";
    private String badFileNameWrongLength="/test/EA5A3CC123412610000000000009X.jpg";

    private File correctFileFront= new File(correctFileNameFront);
    private File correctFileBack= new File(correctFileNameBack);
    private File badFileNeitherFrontNorBack= new File(badFileNameNeitherFrontNorBack);
    private File badFileWrongFormat= new File(badFileNameWrongFormat);
    private File badFileWrongLength= new File(badFileNameWrongLength);

    private CheckImageNameDecoder decoder = new CheckImageNameDecoder02();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_IsCorrectFormat() throws Exception {
        System.out.println("test_IsCorrectFormat");

        assertTrue(decoder.isCorrectFormat(correctFileNameFront));
        assertTrue(decoder.isCorrectFormat(correctFileNameBack));
        assertFalse(decoder.isCorrectFormat(badFileNameWrongFormat));
    }

    @Test
    public void test_IsCorrectFormat_File() throws Exception {
        System.out.println("test_IsCorrectFormat_File");
        assertTrue(decoder.isCorrectFormat(correctFileFront));
        assertTrue(decoder.isCorrectFormat(correctFileBack));

        assertFalse(decoder.isCorrectFormat(badFileWrongFormat));
    }

    @Test
    public void test_IsValid() throws Exception {
        System.out.println("test_IsValid");
        assertTrue(decoder.isValid(correctFileNameFront));
        assertTrue(decoder.isValid(correctFileNameBack));
        assertFalse(decoder.isValid(badFileNameNeitherFrontNorBack));
        assertFalse(decoder.isValid(badFileNameWrongFormat));
        assertFalse(decoder.isValid(badFileNameWrongLength));
    }

    @Test
    public void test_IsValid_File() throws Exception {
        System.out.println("test_IsValid_File");
        assertTrue(decoder.isValid(correctFileFront));
        assertTrue(decoder.isValid(correctFileBack));
        assertFalse(decoder.isValid(badFileNeitherFrontNorBack));
        assertFalse(decoder.isValid(badFileWrongFormat));
        assertFalse(decoder.isValid(badFileWrongLength));
    }

    @Test
    public void test_HasCorrectLength() throws Exception {
        System.out.println("test_HasCorrectLength");
        assertTrue(decoder.hasCorrectLength(correctFileNameFront));
        assertTrue(decoder.hasCorrectLength(correctFileNameBack));
        assertFalse(decoder.hasCorrectLength(badFileNameWrongLength));
    }

    @Test
    public void test_HasCorrectLength_File() throws Exception {
        System.out.println("test_HasCorrectLength_File");
        assertTrue(decoder.hasCorrectLength(correctFileFront));
        assertTrue(decoder.hasCorrectLength(correctFileBack));
        assertFalse(decoder.hasCorrectLength(badFileWrongLength));
    }

    @Test
    public void test_IsFront() throws Exception {
        System.out.println("test_IsFront");
        assertTrue(decoder.isFront(correctFileNameFront));
        assertFalse(decoder.isFront(correctFileNameBack));
        assertFalse(decoder.isFront(badFileNameNeitherFrontNorBack));
    }

    @Test
    public void test_IsBack() throws Exception {
        System.out.println("test_IsBack");
        assertFalse(decoder.isBack(correctFileNameFront));
        assertTrue(decoder.isBack(correctFileNameBack));
        assertFalse(decoder.isFront(badFileNameNeitherFrontNorBack));
    }

    @Test
    public void test_IsFront_File() throws Exception {
        System.out.println("test_IsFront_File");
        assertTrue(decoder.isFront(correctFileFront));
        assertFalse(decoder.isFront(correctFileBack));
        assertFalse(decoder.isFront(badFileNeitherFrontNorBack));
    }

    @Test
    public void test_IsBack_File() throws Exception {
        System.out.println("test_IsBack_File");
        assertFalse(decoder.isBack(correctFileFront));
        assertTrue(decoder.isBack(correctFileBack));
        assertFalse(decoder.isFront(badFileNeitherFrontNorBack));
    }

    @Test
    public void test_GetCheckNumber() throws Exception {
        System.out.println("test_GetCheckNumber");
        assertEquals("1234",decoder.getCheckNumber(correctFileNameFront));
        assertEquals("1234",decoder.getCheckNumber(correctFileNameBack));
    }

    @Test
    public void test_GetCheckNumber_File() throws Exception {
        System.out.println("test_GetCheckNumberFile");
        assertEquals("1234",decoder.getCheckNumber(correctFileFront));
        assertEquals("1234",decoder.getCheckNumber(correctFileBack));
    }

    @Test
    public void test_GetSide() throws Exception {
        System.out.println("test_GetSide");
        assertEquals(CzekImageUtils.FRONT,decoder.getSide(correctFileNameFront));
        assertEquals(CzekImageUtils.BACK,decoder.getSide(correctFileNameBack));
    }

    @Test
    public void test_GetSide_File() throws Exception {
        System.out.println("test_GetSide1");
        assertEquals(CzekImageUtils.FRONT,decoder.getSide(correctFileFront));
        assertEquals(CzekImageUtils.BACK,decoder.getSide(correctFileBack));
    }

}