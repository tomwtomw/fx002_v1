/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.fileutils;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author tomw
 */
public class FileTransporterTest {

    private static final File testDirectory = new File(System.getProperty("user.dir"), "unitTestDirectory");

    public FileTransporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        if (!testDirectory.exists()) {
            testDirectory.mkdirs();
        }
        clearTestDirectory();
    }

    @AfterClass
    public static void tearDownClass() {
        if (testDirectory.exists()) {
            testDirectory.delete();
        }
    }

    @Before
    public void setUp() {
        clearTestDirectory();
    }

    @After
    public void tearDown() {
        clearTestDirectory();
    }

    private static void clearTestDirectory() {
        if(testDirectory!=null) {
            for (File file : testDirectory.listFiles()) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    /**
     * Test of copyFile method, of class FileTransporter.
     *
     * @throws Exception
     */
    @Test
    public void testCopyFile() throws Exception {
        System.out.println("copyFile");

        File srcDir = new File(testDirectory, "src");
        File dstDir = new File(testDirectory, "dst");

        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }

        File src1 = new File(srcDir, "src1.txt");
        String s1 = "eeljgrh;aerjbrjb";
        FileUtils.writeStringToFile(src1, s1);

        FileTransporter.copyFile(src1, dstDir);

        assertTrue(FileTransporter.validCopy(src1, dstDir));

        File src2 = new File(srcDir, "src2.txt");
        File dst2 = new File(dstDir, "src2.txt");
        String s2 = "2222222222222222222222222222222";
        FileUtils.writeStringToFile(src2, s1);

        FileTransporter.copyFile(src2, dst2);

        assertTrue(FileTransporter.validCopy(src2, dst2));
    }

    /**
     * Test of copyFiles method, of class FileTransporter.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testCopyFiles() throws Exception {
        System.out.println("copyFiles");

        File srcDir = new File(testDirectory, "src");
        File dstDir = new File(testDirectory, "dst");

        File src1 = new File(srcDir, "src1.txt");
        String s1 = "eeljgrh;aerjbrjb";
        FileUtils.writeStringToFile(src1, s1);

        File src2 = new File(srcDir, "src2.txt");
        String s2 = "22";
        FileUtils.writeStringToFile(src2, s2);

        List<File> sourceFiles = new ArrayList<>();
        sourceFiles.add(src1);
        sourceFiles.add(src2);

        for (File src : sourceFiles) {
            assertFalse(FileTransporter.validCopy(src, dstDir));
        }

        List<File> failedFiles = FileTransporter.copyFiles(sourceFiles, dstDir);

        assertEquals(failedFiles.size(), 0);

        for (File src : sourceFiles) {
            assertTrue(FileTransporter.validCopy(src, dstDir));
        }
    }

    /**
     * Test of moveFile method, of class FileTransporter.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testMoveFile() throws Exception {
        System.out.println("moveFile");

        File srcDir = new File(testDirectory, "src");
        File dstDir = new File(testDirectory, "dst");
        dstDir.mkdirs();

        File src1 = new File(srcDir, "src1.txt");
        String s1 = "eeljgrh;aerjbrjb";
        FileUtils.writeStringToFile(src1, s1);

        long sourceSize = src1.length();
        long sourceModificationTime = src1.lastModified();

        File dst = FileTransporter.destinationFile(src1, dstDir);

        assertFalse(dst.exists());
        assertTrue(src1.exists());

        FileTransporter.moveFile(src1, dstDir);

        assertTrue(dst.exists());
        assertFalse(src1.exists());
        assertEquals(sourceSize, dst.length());
        assertFalse(sourceModificationTime > dst.lastModified());
    }

    /**
     * Test of moveFiles method, of class FileTransporter.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testMoveFiles() throws Exception {
        System.out.println("moveFiles");

        File srcDir = new File(testDirectory, "src");
        File dstDir = new File(testDirectory, "dst");

        File src1 = new File(srcDir, "src1.txt");
        String s1 = "eeljgrh;aerjbrjb";
        FileUtils.writeStringToFile(src1, s1);

        File src2 = new File(srcDir, "src2.txt");
        String s2 = "22";
        FileUtils.writeStringToFile(src2, s2);

        List<File> sourceFiles = new ArrayList<>();
        sourceFiles.add(src1);
        sourceFiles.add(src2);

        for (File src : sourceFiles) {
            assertFalse(FileTransporter.validCopy(src, dstDir));
        }

        List<File> failedFiles = FileTransporter.moveFiles(sourceFiles, dstDir);

        assertEquals(failedFiles.size(), 0);

        for (File src : sourceFiles) {
            assertFalse(src.exists());
        }
    }

    /**
     * Test of validCopy method, of class FileTransporter.
     * @throws IOException if something goes wrong
     */
    @Test
    public void testValidCopy() throws IOException {
        System.out.println("validCopy");

        File srcDir = new File(testDirectory, "src");
        File dstDir = new File(testDirectory, "dst");

        File src1 = new File(srcDir, "src1.txt");
        String s1 = "eeljgrh;aerjbrjb";
        FileUtils.writeStringToFile(src1, s1);

        File dst1 = new File(dstDir, "src1.txt");
        FileUtils.writeStringToFile(dst1, s1);

        assertTrue(FileTransporter.validCopy(src1, dst1));
        assertTrue(FileTransporter.validCopy(src1, dstDir));

        File src2 = new File(srcDir, "src2.txt");
        String s2 = "qwert";
        FileUtils.writeStringToFile(src1, s2);
        assertFalse(FileTransporter.validCopy(src2, dstDir));

        File dst2 = new File(dstDir, "src2.txt");
        FileUtils.writeStringToFile(dst2, s1);
        assertFalse(FileTransporter.validCopy(src2, dst2));

        File dst3 = new File(dstDir, "src3.txt");
        String s3 = "abcdefghijklmskjfgnjdbfg jdfb";
        FileUtils.writeStringToFile(dst3, s3);

        File src3 = new File(srcDir, "src3.txt");
        FileUtils.writeStringToFile(dst3, s3);
        assertFalse(FileTransporter.validCopy(src3, dst3));
    }

    /**
     * Test of sameSize method, of class FileTransporter.
     * @throws IOException if something goes wrong
     */
    @Test
    public void testSameSize() throws IOException {
        System.out.println("sameSize");

        File src = new File(testDirectory, "source.txt");
        File dst1 = new File(testDirectory, "dst1.txt");
        File dst2 = new File(testDirectory, "dst2.txt");

        FileUtils.writeStringToFile(src, "abcd");
        FileUtils.writeStringToFile(dst1, "abcd");

        FileUtils.writeStringToFile(dst2, "abcdxxxxxxxxxxxxx");

        assertTrue(FileTransporter.sameSize(src, dst1));
        assertFalse(FileTransporter.sameSize(src, dst2));

    }

    /**
     * Test of sourceIsOlderThanDestination method, of class FileTransporter.
     * @throws Exception if something goes wrong
     */
    @Test
    public void testSourceIsNotNewerThanDestination() throws Exception {
        System.out.println("sourceIsOlderThanDestination");

        File src = new File(testDirectory, "source.txt");
        FileUtils.writeStringToFile(src, "abcd");

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        File dst = new File(testDirectory, "destination.txt");
        FileUtils.writeStringToFile(dst, "abcd");

        assertTrue(FileTransporter.sourceIsNotNewerThanDestination(src, dst));
        assertFalse(FileTransporter.sourceIsNotNewerThanDestination(dst, src));

    }

    /**
     * Test of destinationFile method, of class FileTransporter.
     * @throws IOException if something goes wrong
     */
    @Test
    public void testDestinationFile() throws IOException {
        System.out.println("destinationFile");
        File src = new File(testDirectory, "source.txt");
        File dst = new File(testDirectory, "dst");

        FileUtils.writeStringToFile(src, "abcd");
        if (!dst.exists()) {
            dst.mkdirs();
        }

        File expResult = new File(dst, "source.txt");
        File result = FileTransporter.destinationFile(src, dst);
        assertEquals(expResult, result);

        src.delete();
        dst.delete();
    }

}
