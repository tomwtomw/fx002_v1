package org.tomw.utils;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import static org.junit.Assert.*;

/**
 * @author tomw
 */
public class TomwFileUtilsTest {
    private final static Logger LOGGER = Logger.getLogger(TomwFileUtilsTest.class.getName());

    private static final String ENCODING = "ISO-8859-1";

    private File testDirectory;

    public TomwFileUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testDirectory = new File(TomwFileUtils.getApplicationDirectory(),"test_delete_this");
        testDirectory.mkdirs();
    }

    @After
    public void tearDown() {
        TomwFileUtils.deleteDirectoryWithFiles(testDirectory);
    }

    @Test
    public void test_firstModifiedAfterSecond() throws IOException, InterruptedException {
        LOGGER.info("test_firstModifiedAfterSecond");
        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");
        File dir3 = new File(testDirectory,"test3");

        dir1.mkdirs();
        dir2.mkdirs();
        dir3.mkdirs();

        File file1 = new File(dir1,"file1");
        File file2 = new File(dir1,"file2");
        File file3 = new File(dir1,"file3");

        FileUtils.writeStringToFile(file1,"abcd","ISO-8859-1" );
        Thread.sleep(100);
        FileUtils.writeStringToFile(file2,"abcd","ISO-8859-1");
        Thread.sleep(100);
        FileUtils.writeStringToFile(file3,"xyz","ISO-8859-1");
        Thread.sleep(100);

        assertTrue(TomwFileUtils.firstModifiedAfterSecond(file3,file1));
        assertFalse(TomwFileUtils.firstModifiedAfterSecond(file1,file2));
    }

    @Test
    public void test_textFilesHaveSameContent() throws IOException {
        LOGGER.info("test_textFilesHaveSameContent");

        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");
        File dir3 = new File(testDirectory,"test3");

        dir1.mkdirs();
        dir2.mkdirs();
        dir3.mkdirs();

        File file1 = new File(dir1,"file1");
        File file2 = new File(dir1,"file2");
        File file3 = new File(dir1,"file3");

        FileUtils.writeStringToFile(file1,"abcd",ENCODING);
        FileUtils.writeStringToFile(file2,"abcd",ENCODING);
        FileUtils.writeStringToFile(file3,"xyz",ENCODING);

        assertTrue(TomwFileUtils.textFilesHaveSameContent(file1,file2));
        assertFalse(TomwFileUtils.textFilesHaveSameContent(file1,file3));
    }

    /**
     * Test of deleteFileIfExists method, of class TomwFileUtils.
     */
    @Test
    public void testDeleteFileIfExists_String() throws IOException {
        LOGGER.info("deleteFileIfExists");

        String fileName = "test_file_delete_it_testDeleteFileIfExists_String";

        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

        assertFalse(file.exists());

        TomwFileUtils.createFile(file);

        assertTrue(file.exists());

        TomwFileUtils.deleteFileIfExists(fileName);

        assertFalse(file.exists());

        TomwFileUtils.deleteFileIfExists(file);

        assertFalse(file.exists());
    }

    /**
     * Test of deleteFileIfExists method, of class TomwFileUtils.
     *
     * @throws java.io.IOException if something wrong
     */
    @Test
    public void testDeleteFileIfExists_File() throws IOException {
        LOGGER.info("deleteFileIfExists");
        File file = new File("test_file_delete_it_testDeleteFile");

        if (file.exists()) {
            file.delete();
        }

        assertFalse(file.exists());

        TomwFileUtils.createFile(file);

        assertTrue(file.exists());

        TomwFileUtils.deleteFileIfExists(file);

        assertFalse(file.exists());

        TomwFileUtils.deleteFileIfExists(file);

        assertFalse(file.exists());
    }

    /**
     * Test of deleteDirectoryWithFiles method, of class TomwFileUtils.
     */
    @Test
    public void testDeleteDirectoryWithFiles() throws IOException {
        LOGGER.info("deleteDirectoryWithFiles");
        File dir = new File("test_testDeleteDirectoryWithFiles");
        File file1 = new File(dir, "file1");
        File file2 = new File(dir, "file2");

        FileUtils.writeStringToFile(file1, "TowFileUtilsTest",ENCODING);
        FileUtils.writeStringToFile(file2, "TowFileUtilsTest",ENCODING);

        assertTrue(dir.exists());
        assertTrue(file1.exists());
        assertTrue(file2.exists());

        TomwFileUtils.deleteDirectoryWithFiles(dir);

        assertFalse(dir.exists());
    }

    /**
     * Test of createFile method, of class TomwFileUtils.
     *
     * @throws java.lang.Exception if something wrong
     */
    @Test
    public void testCreateFile() throws Exception {
        LOGGER.info("testCreateFile");

        File file = new File("test_file_delete_it");

        if (file.exists()) {
            file.delete();
        }

        assertFalse(file.exists());

        TomwFileUtils.createFile(file);

        assertTrue(file.exists());

        file.delete();
    }

    /**
     * Test of deleteFile method, of class TomwFileUtils.
     *
     * @throws java.io.IOException if error
     */
    @Test
    public void testDeleteFile() throws IOException {
        LOGGER.info("testDeleteFile");

        File file = new File("test_file_delete_it_testDeleteFile");

        if (file.exists()) {
            file.delete();
        }

        assertFalse(file.exists());

        TomwFileUtils.createFile(file);

        assertTrue(file.exists());

        file.delete();

        assertFalse(file.exists());
    }

    @Test
    public void getAllFilesInDirectory() throws Exception {

        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");

        dir1.mkdirs();
        dir2.mkdirs();

        TomwFileUtils.createFile(new File(dir1,"file1.txt"));
        TomwFileUtils.createFile(new File(dir2,"file2.txt"));


        List<File> result = TomwFileUtils.getAllFilesInDirectory(testDirectory);
        assertEquals(2,result.size());

        List<File> result2 = TomwFileUtils.getAllEntriesInDirectory(testDirectory);
        assertEquals(4,result2.size());
    }

    @Test
    public void test_getAllSubdirectoryes() throws IOException {
        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");

        dir1.mkdirs();
        dir2.mkdirs();

        TomwFileUtils.createFile(new File(dir1,"file1.txt"));
        TomwFileUtils.createFile(new File(dir2,"file2.txt"));
        TomwFileUtils.createFile(new File(testDirectory,"file3.txt"));

        List<File>result = TomwFileUtils.getAllSubdirectories(testDirectory);
        assertEquals(2,result.size());
        assertTrue(result.contains(dir1));
        assertTrue(result.contains(dir2));
    }

    @Test
    public void test_getEmptySubdirs() throws IOException {
        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");
        File dir3 = new File(dir1,"dir3");

        dir1.mkdirs();
        dir2.mkdirs();
        dir3.mkdirs();

        TomwFileUtils.createFile(new File(dir1,"file1.txt"));

        assertFalse(TomwFileUtils.isEmptyDirectory(dir1));
        assertTrue(TomwFileUtils.isEmptyDirectory(dir2));
        assertTrue(TomwFileUtils.isEmptyDirectory(dir3));

        List<File> result = TomwFileUtils.getEmptyBranches(testDirectory);
        assertEquals(2,result.size());

        assertTrue(result.contains(dir2));
        assertTrue(result.contains(dir3));
    }

    @Test
    public void test_pruneEmptyBranches() throws IOException {
        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");
        File dir3 = new File(dir1,"dir3");

        dir1.mkdirs();
        dir2.mkdirs();
        dir3.mkdirs();

        File test0 = new File(testDirectory,"file0.txt");
        File test1 = new File(dir1,"file1.txt");
        TomwFileUtils.createFile(test0);
        TomwFileUtils.createFile(test1);

        assertFalse(TomwFileUtils.isEmptyDirectory(dir1));
        assertTrue(TomwFileUtils.isEmptyDirectory(dir2));
        assertTrue(TomwFileUtils.isEmptyDirectory(dir3));
        assertTrue(test0.exists());
        assertTrue(test1.exists());

        TomwFileUtils.pruneEmptyBranches(testDirectory);

        assertTrue(dir1.exists());
        assertFalse(dir2.exists());
        assertFalse(dir3.exists());
        assertTrue(test0.exists());
        assertTrue(test1.exists());
    }

    @Test
    public void test_isEmptyDirectory() throws IOException {
        File dir1 = new File(testDirectory,"test1");
        File dir2 = new File(testDirectory,"test2");

        dir1.mkdirs();
        dir2.mkdirs();

        TomwFileUtils.createFile(new File(dir1,"file1.txt"));

        assertFalse(TomwFileUtils.isEmptyDirectory(dir1));
        assertTrue(TomwFileUtils.isEmptyDirectory(dir2));
    }

    @Test
    public void test_readTextFileFromResources() throws IOException {
        String expected="qwerty\n";
        String actual = TomwFileUtils.readTextFileFromResources("test_file1.txt");
        assertEquals(expected, actual);
    }

    @Test
    public void test_readTextFileFromResources_File() throws IOException {
        String expected="qwerty\n";
        String actual = TomwFileUtils.readTextFileFromResources(new File("test_file1.txt"));
        assertEquals(expected, actual);
    }

    @Test
    public void test_readTextFileFromResourcesAsStream_File() throws IOException {
        String expected="qwerty";
        String actual = TomwFileUtils.readTextFileFromResourcesAsStream(new File("/test_file1.txt"));
        assertEquals(expected, actual);
    }

    @Test
    public void test_readTextFileFromResourcesAsStream() throws IOException {
        String expected="qwerty";
        String actual = TomwFileUtils.readTextFileFromResourcesAsStream("/test_file1.txt");
        assertEquals(expected, actual);
    }
}
