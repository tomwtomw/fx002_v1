package org.tomw.imagedao.fileimpl;

import javafx.scene.image.Image;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.envutils.TomwEnvUtils;
import org.tomw.fileutils.TomwFileUtils;
import org.tomw.imagedao.ImageDao;
import org.tomw.imagedao.ImageDaoException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ImageDaoFileImplTest {
    private final static Logger LOGGER = Logger.getLogger(ImageDaoFileImplTest.class.getName());

    private File homeDirectory = TomwEnvUtils.getApplicationDirectory();

    private File repository = new File(homeDirectory,"repository");
    private File source = new File(homeDirectory,"source");

    private String fileName1 = "LeftArrow_small.jpg";
    private String fileName2 = "RightArrow_small.jpg";

    private ImageDao dao;

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);

        TomwFileUtils.mkdirs(repository);
        TomwFileUtils.mkdirs(source);

        TomwFileUtils.copyFileFromResources(new File(fileName1),new File(source,fileName1));
        TomwFileUtils.copyFileFromResources(new File(fileName2),new File(source,fileName2));

        dao = new ImageDaoFileImpl(repository);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);
    }

    @Test
    public void test_constructor() throws Exception {
        System.out.println("test_constructor");
        //if we are here, then constructor passed, in setUp
    }

    @Test
    public void test_commit() throws ImageDaoException {
        System.out.println("test_commit");

        File sourceFile1 = new File(source,fileName1);
        File sourceFile2 = new File(source,fileName2);

        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        File result = new File(repository,fileName1);
        assertFalse(result.exists());
        String id1 = dao.upload(new File(source,fileName1),false);
        assertTrue(result.exists());
        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        File result2 = new File(repository,fileName2);
        assertFalse(result2.exists());
        String id2 = dao.upload(new File(source,fileName2),true);
        assertTrue(result2.exists());
        assertTrue(sourceFile1.exists());
        assertFalse(sourceFile2.exists());

        dao.commit();

        ImageDao dao2 = new ImageDaoFileImpl(repository);
        assertTrue(dao2.imageExists(id1));
        assertTrue(dao2.imageExists(id2));
        assertEquals(dao.getFile(id1),dao2.getFile(id1));
        assertEquals(dao.getFile(id2),dao2.getFile(id2));
    }

    @Test
    public void test_toFullFileName() throws ImageDaoException {
        ImageDaoFileImpl dao2 = new ImageDaoFileImpl(repository);
        String s ="abcd/efg/hj.jpg";
        File f = new File(s);
        File actual = dao2.toFullFileName(f);
        File expected = (new File("repository\\abcd\\efg\\hj.jpg")).getAbsoluteFile();
        assertEquals(expected,actual);

        String s2 ="/abcd/efg/hj.jpg";
        File f2 = new File(s);
        File actual2 = dao2.toFullFileName(f2);
        File expected2 = (new File("repository\\abcd\\efg\\hj.jpg")).getAbsoluteFile();
        assertEquals(expected2,actual2);
    }

    @Test
    public void test_toShortFileName() throws ImageDaoException {
        ImageDaoFileImpl dao2 = new ImageDaoFileImpl(repository);
        String s ="repository\\abcd\\efg\\hj.jpg";
        File f = (new File(s)).getAbsoluteFile();
        File actual = dao2.toShortFileName(f);
        File expected = new File("\\abcd\\efg\\hj.jpg");
        assertEquals(expected,actual);
    }

    @Test
    public void test_GetImage() throws Exception {
        System.out.println("test_GetImage - not implemented");
    }

    @Test
    public void test_Upload() throws Exception {
        System.out.println("test_Upload");
        File result = new File(repository,fileName1);
        assertFalse(result.exists());
        dao.upload(new File(source,fileName1));
        assertTrue(result.exists());
    }

    @Test
    public void test_Upload_boolean() throws Exception {
        System.out.println("test_Upload_boolean");
        File sourceFile1 = new File(source,fileName1);
        File sourceFile2 = new File(source,fileName2);

        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        File result = new File(repository,fileName1);
        assertFalse(result.exists());
        dao.upload(new File(source,fileName1),false);
        assertTrue(result.exists());
        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        File result2 = new File(repository,fileName2);
        assertFalse(result2.exists());
        dao.upload(new File(source,fileName2),true);
        assertTrue(result2.exists());
        assertTrue(sourceFile1.exists());
        assertFalse(sourceFile2.exists());
    }

    @Test
    public void test_Upload_file_file() throws Exception {
        System.out.println("test_Upload_file_file");
        test_upload_copy_and_move();
    }

    @Test
    public void test_Upload_file_file_boolean() throws Exception {
        System.out.println("test_Upload_file_file_boolean");
        test_upload_copy_and_move();
    }

     private void test_upload_copy_and_move() throws IOException {
        File source1 = (new File(source,"source1")).getAbsoluteFile();
        File source2 = (new File(source,"source2")).getAbsoluteFile();

        TomwFileUtils.mkdirs(source1);
        TomwFileUtils.mkdirs(source2);

        File sourceFile1 = (new File(source1,fileName1)).getAbsoluteFile();
        File sourceFile2 = (new File(source2,fileName2)).getAbsoluteFile();

        TomwFileUtils.copyFileFromResources(new File(fileName1),sourceFile1);
        TomwFileUtils.copyFileFromResources(new File(fileName2),sourceFile2);
        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        String id1 = dao.upload(source.getAbsoluteFile(),sourceFile1);
        String id2 = dao.upload(source.getAbsoluteFile(),sourceFile2,true);
        assertTrue(dao.imageExists(id1));
        assertTrue(dao.imageExists(id2));

        assertTrue(sourceFile1.exists());
        assertFalse(sourceFile2.exists());

    }

    @Test
    public void test_Upload_Image() throws Exception {
        System.out.println("test_Upload4 - not implemented");
        //TODO implement
    }

    @Test
    public void test_UploadAll() throws Exception {
        System.out.println("test_UploadAll");

        File source1 = new File(source,"source1");
        File source2 = new File(source,"source2");

        TomwFileUtils.mkdirs(source1);
        TomwFileUtils.mkdirs(source2);

        File sourceFile1 = new File(source1,fileName1);
        File sourceFile2 = new File(source2,fileName2);

        TomwFileUtils.copyFileFromResources(new File(fileName1),sourceFile1);
        TomwFileUtils.copyFileFromResources(new File(fileName2),sourceFile2);
        assertTrue(sourceFile1.exists());
        assertTrue(sourceFile2.exists());

        List<String> listOfIds = dao.uploadAll(source,true);
        for(String id : listOfIds) {
            assertTrue(dao.imageExists(id));
        }

        assertFalse(sourceFile1.exists());
        assertFalse(sourceFile2.exists());
    }

    @Test
    public void test_ImageExists() throws Exception {
        System.out.println("test_ImageExists");

        String id = dao.upload(new File(source,fileName1),false);
        assertTrue(dao.imageExists(id));
    }

    @Test
    public void test_DeleteImage() throws Exception {
        System.out.println("test_DeleteImage");

        String id = dao.upload(new File(source,fileName1),false);
        assertTrue(dao.imageExists(id));
        dao.deleteImage(id);
        assertFalse(dao.imageExists(id));
    }

    @Test
    public void test_DeleteImages() throws Exception {
        System.out.println("test_DeleteImages");
        String id1 = dao.upload(new File(source,fileName1),false);
        String id2 = dao.upload(new File(source,fileName2),false);
        assertTrue(dao.imageExists(id1));
        assertTrue(dao.imageExists(id2));
        List<String> listOfIds = new ArrayList<>();
        listOfIds.add(id1);
        listOfIds.add(id2);
        dao.deleteImages(listOfIds);
        assertFalse(dao.imageExists(id1));
        assertFalse(dao.imageExists(id2));
    }

    @Test
    public void test_getFile() throws ImageDaoException {
        System.out.println("test_getFile");
        String id = dao.upload(new File(source,fileName1),false);
        File actual = dao.getFile(id);
        File expected = (new File(repository,"LeftArrow_small.jpg")).getAbsoluteFile();
        assertEquals(expected, actual);
    }

    @Test
    public void test_ContainsImage() throws Exception {
        System.out.println("test_ContainsImage");
        String id = dao.upload(new File(source,fileName1),false);
        assertTrue(dao.containsImage(id));
        assertFalse(dao.containsImage(id+"x"));
        dao.deleteImage(id);
        assertFalse(dao.containsImage(id));
    }

}