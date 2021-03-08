/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.fileutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.tomw.utils.TomwStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author tomw
 */
public class TomwFileUtilsTest {

    @Rule
    public TemporaryFolder temporaryFolder= new TemporaryFolder();

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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of deleteFileIfExists method, of class TomwFileUtils.
     */
    @Test
    public void testDeleteFileIfExists_String() throws IOException {
        System.out.println("deleteFileIfExists");
        
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
     * @throws IOException if something goes wrong
     */
    @Test
    public void testDeleteFileIfExists_File() throws IOException {
        System.out.println("deleteFileIfExists");
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
        System.out.println("deleteDirectoryWithFiles");
        File dir = new File("test_testDeleteDirectoryWithFiles");
        File file1 = new File(dir, "file1");
        File file2 = new File(dir, "file2");

        FileUtils.writeStringToFile(file1, "TowFileUtilsTest");
        FileUtils.writeStringToFile(file2, "TowFileUtilsTest");

        assertTrue(dir.exists());
        assertTrue(file1.exists());
        assertTrue(file2.exists());

        TomwFileUtils.deleteDirectoryWithFiles(dir);

        assertFalse(dir.exists());
    }

    /**
     * Test of createFile method, of class TomwFileUtils.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testCreateFile() throws Exception {
        System.out.println("testCreateFile");

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
     * @throws IOException
     */
    @Test
    public void testDeleteFile() throws IOException {
        System.out.println("testDeleteFile");
        
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
    public void test_copyFileFromResources() throws IOException {
        System.out.println("test_copyFileFromResources");

        File dst = new File("dst");
        TomwFileUtils.mkdirs(dst);
        String src= "LeftArrow_small.jpg";
        File destinationfile = new File(dst,src);
        TomwFileUtils.copyFileFromResources(new File(src),destinationfile);
        boolean copySucessful = destinationfile.exists();
        TomwFileUtils.deleteDirectoryWithFiles(dst);
        assertTrue(copySucessful);
    }

    @Test
    public void test_getListOfFilesInDirectory() throws IOException {
        System.out.println("test_getListOfFilesInDirectory");

        File dst = new File("dst");
        TomwFileUtils.mkdirs(dst);

        File dst2 = new File(dst,"dst2");
        TomwFileUtils.mkdirs(dst2);

        String src= "LeftArrow_small.jpg";
        File destinationfile = new File(dst,src);
        TomwFileUtils.copyFileFromResources(new File(src),destinationfile);

        String src2= "RightArrow_small.jpg";
        File destinationfile2 = new File(dst2,src2);
        TomwFileUtils.copyFileFromResources(new File(src2),destinationfile2);

        List<File> result = TomwFileUtils.getListOfFilesInDirectory(dst);
        //TODO make a better test here
        assertEquals(2,result.size());
    }

    @Test
    public void test_ReplaceBasedir() throws Exception {
        System.out.println("test_ReplaceBasedir");

        String sourceDirString="C:\\Users\\tomw\\Pictures";
        String fileNameString="C:\\Users\\tomw\\Pictures\\pictures\\picture album\\aaa\\2018\\2018-01-05\\a.jpg";
        String destinationDirString="H:\\Backup\\Images";

        File actual =TomwFileUtils.replaceBasedir(new File(sourceDirString),new File(fileNameString),new File(destinationDirString));

        File expected = new File("H:\\Backup\\Images\\pictures\\picture album\\aaa\\2018\\2018-01-05\\a.jpg");

        assertEquals(expected, actual);
    }


    @Test
    public void  test_isUnder(){
        File directory = new File("C:/a/b/c/");
        File file = new File("C:/a/b/c/d/e.txt");

        assertTrue(TomwFileUtils.isUnder(directory,file));

        File directory2 = new File("C:/a/b/c/");
        File file2 = new File("c/d/e.txt");

        assertFalse(TomwFileUtils.isUnder(directory2,file2));


    }

    @Test
    public void test_findImageFile() throws IOException {
        File testFolder = temporaryFolder.newFolder("test_findImageFile");
        File file1=new File(testFolder,"a.txt");
        File file2=new File(testFolder,"b.doc");
        File file3=new File(testFolder,"b.jpg");
        File file4=new File(testFolder,"c.doc");

        TomwFileUtils.createFile(file1);
        TomwFileUtils.createFile(file2);
        TomwFileUtils.createFile(file3);
        TomwFileUtils.createFile(file4);

        File result = TomwFileUtils.findImageFile(testFolder,"b");

        assertEquals(file3,result);
    }

    @Test
    public void test_changeFileNameTo(){
        File oldFile = new File("C:\\Users\\tomw\\AppData\\Local\\Temp\\junit996379714509053068\\test_findImageFile\\b.jpg");
        System.out.println(oldFile);

        File newFile = TomwFileUtils.changeFileNameTo(oldFile,"xyz");

        File expected = new File("C:\\Users\\tomw\\AppData\\Local\\Temp\\junit996379714509053068\\test_findImageFile\\xyz.jpg");

        System.out.println(newFile);

        assertEquals(expected,newFile);

    }

    @Test
    public void test_getShortUniqueString(){
        String s1 = TomwStringUtils.getShortUniqueString();
        String s2 = TomwStringUtils.getShortUniqueString();
        assertNotEquals(s1,s2);
    }

}
