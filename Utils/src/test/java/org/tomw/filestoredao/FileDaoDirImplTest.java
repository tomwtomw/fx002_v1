package org.tomw.filestoredao;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.envutils.TomwEnvUtils;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.imagedao.fileimpl.ImageDaoFileImplTest;

import java.io.File;

import static org.junit.Assert.*;

public class FileDaoDirImplTest {
    private final static Logger LOGGER = Logger.getLogger(ImageDaoFileImplTest.class.getName());

    private File homeDirectory = TomwEnvUtils.getApplicationDirectory();

    private File repository = new File(homeDirectory,"repository");
    private File source = new File(homeDirectory,"source");
    private File destination = new File(homeDirectory,"destination");

    private String fileName1 = "LeftArrow_small.jpg";
    private String fileName2 = "RightArrow_small.jpg";

    private File sourceFile1 = new File(source,fileName1);
    private File sourceFile2 = new File(source,fileName2);

    private FileDaoDirImpl dao;

    @Before
    public void setUp() throws Exception {

        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);
        TomwFileUtils.deleteDirectoryWithFiles(destination);

        TomwFileUtils.mkdirs(repository);
        TomwFileUtils.mkdirs(source);
        TomwFileUtils.mkdirs(destination);

        TomwFileUtils.copyFileFromResources(new File(fileName1),new File(source,fileName1));
        TomwFileUtils.copyFileFromResources(new File(fileName2),new File(source,fileName2));

        FileDaoDrirImplConfiguration config = new FileDaoDirImplConfigurationTest();
        config.setFileStoreDirectory(repository);

        dao = new FileDaoDirImpl(config);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);
        TomwFileUtils.deleteDirectoryWithFiles(destination);
    }

    @Test
    public void upload() throws Exception {
        String internalFileName = "1234.jpg";
        dao.upload(sourceFile1,internalFileName);
        File internalFile = dao.getPathToFile(internalFileName);
        assertTrue(internalFile!=null);
        assertTrue(internalFile.exists());
        assertEquals(sourceFile1.length(),internalFile.length());
    }

    @Test
    public void upload1() throws Exception {
        File internalFile = new File("1234.jpg");
        dao.upload(sourceFile1,internalFile);
        File internalFileFullPath = dao.getPathToFile(internalFile);
        assertTrue(internalFileFullPath!=null);
        assertTrue(internalFileFullPath.exists());
        assertEquals(sourceFile1.length(),internalFileFullPath.length());
    }

    @Test
    public void upload2() throws Exception {
        String internalFileName = "1234.jpg";
        boolean deleteSource = true;
        long externalFileSize = sourceFile1.length();

        dao.upload(sourceFile1,internalFileName,deleteSource);

        File internalFile = dao.getPathToFile(internalFileName);
        assertTrue(internalFile!=null);
        assertTrue(internalFile.exists());
        assertEquals(externalFileSize,internalFile.length());
        assertFalse(sourceFile1.exists());
    }

    @Test
    public void upload3() throws Exception {
        File internalFile = new File("1234.jpg");
        boolean deleteSource = true;
        long externalFileSize = sourceFile1.length();

        dao.upload(sourceFile1,internalFile,deleteSource);
        File internalFileFullPath = dao.getPathToFile(internalFile);
        assertTrue(internalFileFullPath!=null);
        assertTrue(internalFileFullPath.exists());
        assertEquals(externalFileSize,internalFileFullPath.length());
        assertFalse(sourceFile1.exists());
    }

    @Test
    public void download() throws Exception {
        assertTrue(sourceFile1.exists());

        File internalFile = new File("1234.jpg");
        dao.upload(sourceFile1,internalFile,true);

        assertFalse(sourceFile1.exists());

        dao.download("1234.jpg", sourceFile1);
        assertTrue(sourceFile1.exists());

    }

    @Test
    public void deleteFile() throws Exception {
        String internalFileName = "1234.jpg";
        dao.upload(sourceFile1,internalFileName);

        File internalFile = dao.getPathToFile(internalFileName);
        assertTrue(internalFile.exists());

        dao.deleteFile(internalFileName);
        assertFalse(internalFile.exists());
    }

    @Test
    public void deleteFile1() throws Exception {
        File  internalFile = new File("1234.jpg");
        dao.upload(sourceFile1,internalFile);

        File internalFileFullPath = dao.getPathToFile(internalFile);
        assertTrue(internalFileFullPath.exists());

        dao.deleteFile(internalFile);
        assertFalse(internalFileFullPath.exists());
    }

    @Test
    public void getPathToFile() throws Exception {
        String internalFileName = "1234.jpg";

        assertFalse((dao.getPathToFile(internalFileName).exists()));

        dao.upload(sourceFile1,internalFileName);

        File internalFile = dao.getPathToFile(internalFileName);
        assertTrue(internalFile.exists());
    }

    @Test
    public void getPathToFile1() throws Exception {
        String internalFileName = "1234.jpg";
        File internalFile = new File(internalFileName);

        File fileInRepository = dao.getPathToFile(internalFile);
        assertFalse(fileInRepository.exists());

        dao.upload(sourceFile1,internalFileName);

        File internalFileFullPath = dao.getPathToFile(internalFileName);
        assertTrue(internalFileFullPath.exists());
    }

    @Test
    public void test_getInternalSubdirectory(){
        String expected="00001";
        String actual = dao.getInternalSubdirectory(1234);
        assertEquals(expected, actual);

        expected="00000";
        actual = dao.getInternalSubdirectory(534);
        assertEquals(expected, actual);

        expected="01234";
        actual = dao.getInternalSubdirectory(1234534);
        assertEquals(expected, actual);
    }

    @Test
    public void test_internalNameWithInternalDirectory(){
        String actual = dao.internalNameWithInternalDirectory(1234,"jpg");
        String expected = "00001/1234.jpg";
        assertEquals(expected, actual);
    }
}