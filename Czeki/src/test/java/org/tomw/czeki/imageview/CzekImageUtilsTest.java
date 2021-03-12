package org.tomw.czeki.imageview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CzekImageUtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_IsImageFile() throws Exception {
        assertTrue(CzekImageUtils.isImageFile(new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpg")));
        assertTrue(CzekImageUtils.isImageFile(new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpeg")));
        assertTrue(CzekImageUtils.isImageFile(new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.gif")));
        assertTrue(CzekImageUtils.isImageFile(new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.png")));
        assertFalse(CzekImageUtils.isImageFile(new File("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpgx")));

    }

    @Test
    public void test_IsImageFileName() throws Exception {
        System.out.println("test_IsImageFileName");
            assertTrue(CzekImageUtils.isImageFileName("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpg"));
            assertTrue(CzekImageUtils.isImageFileName("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpeg"));
            assertTrue(CzekImageUtils.isImageFileName("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.gif"));
            assertTrue(CzekImageUtils.isImageFileName("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.png"));
            assertFalse(CzekImageUtils.isImageFileName("C:\\Users\\tomw\\Documents\\NetbeansProjectsData\\data\\New folder\\EA5DA3CC376212610009F.jpgx"));

        }



}