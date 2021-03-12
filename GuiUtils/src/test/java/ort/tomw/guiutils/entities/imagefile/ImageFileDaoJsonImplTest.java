package ort.tomw.guiutils.entities.imagefile;

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tomw.fileutils.TomwFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ImageFileDaoJsonImplTest {


    private final static Logger LOGGER = Logger.getLogger(ImageFileDaoJsonImplTest.class.getName());

    private File repository = new File("repository");
    private File imagesDirectory = new File(repository,"images");
    private File dataFile = new File(repository,"dao.json");
    private File source = new File("source");

    private String fileName1 = "LeftArrow_small.jpg";
    private String fileName2 = "RightArrow_small.jpg";

    private File fileInSource1;
    private File fileInSource2;

    private File fileInRepository1;
    private File fileInRepository2;

    private ImageFileDao dao;

    @Before
    public void setUp() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);

        TomwFileUtils.mkdirs(repository);
        TomwFileUtils.mkdirs(imagesDirectory);
        TomwFileUtils.mkdirs(source);

        fileInSource1 = new File(source,fileName1);
        fileInSource2 = new File(source,fileName2);



        TomwFileUtils.copyFileFromResources(new File(fileName1),fileInSource1);
        TomwFileUtils.copyFileFromResources(new File(fileName2),fileInSource2);

        dao = new ImageFileDaoJsonImpl(dataFile,imagesDirectory);
    }

    @After
    public void tearDown() throws Exception {
        TomwFileUtils.deleteDirectoryWithFiles(repository);
        TomwFileUtils.deleteDirectoryWithFiles(source);
    }

    @Test
    public void test_GetImageFile() throws Exception {
        System.out.println("test_GetImageFile");
        test_add_get();
    }

    @Test
    public void test_Add() throws Exception {
        System.out.println("test_Add");
        test_add_get();
    }

    private void test_add_get() throws IOException {
        TomwFileUtils.copyFileFromResources(new File(fileName1), new File(imagesDirectory,fileName1));
        TomwFileUtils.copyFileFromResources(new File(fileName1), new File(imagesDirectory,fileName2));

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.containsImage(if1.getId()));
        assertFalse(dao.containsImage(if2.getId()));

        dao.add(if1);

        assertTrue(dao.containsImage(if1.getId()));
        assertFalse(dao.containsImage(if2.getId()));

        assertEquals(if1,dao.getImageFile(if1.getId()));
        assertEquals(null,dao.getImageFile(if2.getId()));

        dao.add(if2);

        assertTrue(dao.containsImage(if1.getId()));
        assertTrue(dao.containsImage(if2.getId()));

        assertEquals(if1,dao.getImageFile(if1.getId()));
        assertEquals(if2,dao.getImageFile(if2.getId()));
    }


    @Test
    public void test_Upload() throws Exception {
        System.out.println("test_Upload");

        String id = dao.upload(fileInSource1);
        assertTrue(dao.containsImage(id));
    }

    @Test
    public void test_Upload1() throws Exception {
        System.out.println("test_Upload1");

        File sourceDirectory= new File(source,"abcd");
        TomwFileUtils.mkdirs(sourceDirectory);
        File sourceDirectory2= new File(sourceDirectory,"efgh");
        TomwFileUtils.mkdirs(sourceDirectory2);
        File sourceDirectory3= new File(sourceDirectory2,"ijkl");
        TomwFileUtils.mkdirs(sourceDirectory3);
        File sourceDirectory4= new File(sourceDirectory3,"mno");
        TomwFileUtils.mkdirs(sourceDirectory4);

        File testSourceFile1 = new File(sourceDirectory4,"testFile1.jpg");
        File testSourceFile2 = new File(sourceDirectory4,"testFile2.jpg");

        FileUtils.copyFile(fileInSource1, testSourceFile1);
        FileUtils.copyFile(fileInSource1, testSourceFile2);

        String id = dao.upload(sourceDirectory,testSourceFile1);
        ImageFile if1 = dao.getImageFile(id);

        assertTrue(if1.getFile().exists());
        assertTrue(dao.contains(if1));
        assertTrue(testSourceFile1.exists());

        String id2 = dao.upload(sourceDirectory,testSourceFile2,true);
        ImageFile if2 = dao.getImageFile(id2);

        assertTrue(if2.getFile().exists());
        assertTrue(dao.contains(if2));
        assertFalse(testSourceFile2.exists());

    }

    /**
     * test upload(File directory, File imageFile, String name)
     *
     * @throws Exception if something is wrong
     */
    @Test
    public void test_Upload2() throws Exception {
        System.out.println("test_Upload2 upload(File directory, File imageFile, String name)");

        File sourceDirectory= new File(source,"abcd");
        TomwFileUtils.mkdirs(sourceDirectory);
        File sourceDirectory2= new File(sourceDirectory,"efgh");
        TomwFileUtils.mkdirs(sourceDirectory2);
        File sourceDirectory3= new File(sourceDirectory2,"ijkl");
        TomwFileUtils.mkdirs(sourceDirectory3);
        File sourceDirectory4= new File(sourceDirectory3,"mno");
        TomwFileUtils.mkdirs(sourceDirectory4);

        File testSourceFile1 = new File(sourceDirectory4,"testFile1.jpg");
        File testSourceFile2 = new File(sourceDirectory4,"testFile2.jpg");

        FileUtils.copyFile(fileInSource1, testSourceFile1);
        FileUtils.copyFile(fileInSource1, testSourceFile2);

        String desiredName = "name of test file";
        ImageFile if1 = dao.upload(sourceDirectory,testSourceFile1,desiredName);

        assertTrue(if1.getFile().exists());
        assertTrue(dao.contains(if1));
        assertTrue(testSourceFile1.exists());
        assertEquals(desiredName,if1.getName());

        ImageFile if2 = dao.upload(sourceDirectory,testSourceFile2,desiredName,true);

        assertTrue(if2.getFile().exists());
        assertTrue(dao.contains(if2));
        assertEquals(desiredName,if2.getName());
        assertFalse(testSourceFile2.exists());
    }

    @Test
    public void test_Upload3() throws Exception {
        System.out.println("test_Upload3");
        File sourceDirectory= new File(source,"abcd");
        TomwFileUtils.mkdirs(sourceDirectory);
        File sourceDirectory2= new File(sourceDirectory,"efgh");
        TomwFileUtils.mkdirs(sourceDirectory2);
        File sourceDirectory3= new File(sourceDirectory2,"ijkl");
        TomwFileUtils.mkdirs(sourceDirectory3);
        File sourceDirectory4= new File(sourceDirectory3,"mno");
        TomwFileUtils.mkdirs(sourceDirectory4);

        File testSourceFile1 = new File(sourceDirectory4,"testFile1.jpg");
        File testSourceFile2 = new File(sourceDirectory4,"testFile2.jpg");

        FileUtils.copyFile(fileInSource1, testSourceFile1);
        FileUtils.copyFile(fileInSource1, testSourceFile2);

        String desiredName = "name of test file";
        String desiredComment = "test comment";
        ImageFile if1 = dao.upload(sourceDirectory,testSourceFile1,desiredName,desiredComment);

        assertTrue(if1.getFile().exists());
        assertTrue(dao.contains(if1));
        assertTrue(testSourceFile1.exists());
        assertEquals(desiredName,if1.getName());
        assertEquals(desiredComment,if1.getComment());

        ImageFile if2 = dao.upload(sourceDirectory,testSourceFile2,desiredName,desiredComment,true);

        assertTrue(if2.getFile().exists());
        assertTrue(dao.contains(if2));
        assertEquals(desiredName,if2.getName());
        assertEquals(desiredComment,if2.getComment());
        assertFalse(testSourceFile2.exists());
    }

    @Test
    public void test_Upload4() throws Exception {
        System.out.println("test_Upload4");

        String baseName = fileInSource1.getName();

        ImageFile imageFile = new ImageFile();

        dao.upload(imageFile,fileInSource1);

        assertTrue(imageFile.getFile().exists());

        assertEquals(baseName,imageFile.getFile().getName());

        assertFalse(fileInSource1.exists());
    }

    @Test
    public void test_ImageFileExists() throws Exception {
        System.out.println("test_ImageFileExists");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName1), file2);

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if1.getId()));

        dao.add(if1);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));

        dao.add(if2);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

    }

    @Test
    public void test_Delete() throws Exception {
        System.out.println("test_Delete");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName1), file2);

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if1.getId()));

        dao.add(if1);
        dao.add(if2);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        ImageFile deletedImageFile = dao.delete(if1);

        assertEquals(if1,deletedImageFile);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        deletedImageFile = dao.delete(if2);

        assertEquals(if2,deletedImageFile);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));

        deletedImageFile = dao.delete(if2);

        assertNull(deletedImageFile);
    }

    @Test
    public void test_DeleteImageFile() throws Exception {
        System.out.println("test_DeleteImageFile");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        assertFalse(file1.exists());
        assertFalse(file1.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName1), file2);

        assertTrue(file1.exists());
        assertTrue(file1.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if1.getId()));

        dao.add(if1);
        dao.add(if2);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        ImageFile deletedImageFile = dao.deleteImageFile(if1.getId());

        assertEquals(if1,deletedImageFile);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        deletedImageFile = dao.deleteImageFile(if2.getId());

        assertEquals(if2,deletedImageFile);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));

        deletedImageFile = dao.deleteImageFile(if2.getId());

        assertNull(deletedImageFile);

        assertFalse(file1.exists());
        assertFalse(file1.exists());
    }


    @Test
    public void test_ContainsImage() throws Exception {
        System.out.println("test_ContainsImage");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        assertFalse(file1.exists());
        assertFalse(file2.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file2);

        assertTrue(file1.exists());
        assertTrue(file2.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if1.getId()));

        dao.add(if1);

        assertTrue(dao.containsImage(if1.getId()));
        assertFalse(dao.containsImage(if2.getId()));
    }

    @Test
    public void test_GetImage() throws Exception {
        //TODO find a way to implement it
        System.out.println("test_GetImage - not implemented yet");
    }

    @Test
    public void test_UploadAll() throws Exception {
        System.out.println("test_UploadAll");

        String fileName3="file3.txt";

        File source2 = new File(source,"source2");
        TomwFileUtils.mkdirs(source2);

        File file1 = new File(source2, fileName1);
        File file2 = new File(source2, fileName2);
        File file3 = new File(source2, fileName3);

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(file3.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file2);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file3);

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());

        List<String> listOfIds = dao.uploadAll(source2,true);

        assertEquals(2,listOfIds.size());

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertTrue(file3.exists());

        ImageFile if1 = dao.getImageFile(listOfIds.get(0));
        ImageFile if2 = dao.getImageFile(listOfIds.get(1));

        assertEquals(file1.getName(),if1.getFile().getName());
        assertEquals(file2.getName(),if2.getFile().getName());
    }

    @Test
    public void test_ImageExists() throws Exception {
        System.out.println("test_ImageExists");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        assertFalse(file1.exists());
        assertFalse(file1.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName1), file2);

        assertTrue(file1.exists());
        assertTrue(file1.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageExists(if1.getId()));
        assertFalse(dao.imageExists(if1.getId()));

        dao.add(if1);
        dao.add(if2);

        assertTrue(dao.imageExists(if1.getId()));
        assertTrue(dao.imageExists(if2.getId()));
    }

    @Test
    public void test_DeleteImage() throws Exception {
        System.out.println("test_DeleteImage");

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);

        assertFalse(file1.exists());
        assertFalse(file1.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName1), file2);

        assertTrue(file1.exists());
        assertTrue(file1.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if1.getId()));

        dao.add(if1);
        dao.add(if2);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        boolean deleteResult1 = dao.deleteImage(if1.getId());

        assertTrue(deleteResult1);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));

        boolean deleteResult2 = dao.deleteImage(if2.getId());

        assertTrue(deleteResult2);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));

        boolean deleteResult3 = dao.deleteImage(if2.getId());

        assertFalse(deleteResult3);

        assertFalse(file1.exists());
        assertFalse(file1.exists());
    }

    @Test
    public void test_DeleteImages() throws Exception {
        System.out.println("test_DeleteImages");

        String fileName3="file3.jpg";

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);
        File file3 = new File(imagesDirectory, fileName3);

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(file3.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file2);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file3);

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");
        ImageFile if3 = new ImageFile("file 3", file3, "comment 3");

        List<String> listOfImageFiles  = new ArrayList<>();
        listOfImageFiles.add(if1.getId());
        listOfImageFiles.add(if2.getId());

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));
        assertFalse(dao.imageFileExists(if3.getId()));

        dao.add(if1);
        dao.add(if2);
        dao.add(if3);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));
        assertTrue(dao.imageFileExists(if3.getId()));

        boolean deleteResult1 = dao.deleteImages(listOfImageFiles);

        assertTrue(deleteResult1);

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));
        assertTrue(dao.imageFileExists(if3.getId()));

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertTrue(file3.exists());
    }

    @Test
    public void test_GetFile() throws Exception {
        System.out.println("test_GetFile");
        String fileName3="file3.jpg";

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);
        File file3 = new File(imagesDirectory, fileName3);

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(file3.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file2);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file3);

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");
        ImageFile if3 = new ImageFile("file 3", file3, "comment 3");

        dao.add(if1);
        dao.add(if2);

        assertEquals(file1,dao.getFile(if1.getId()));
        assertEquals(file2,dao.getFile(if2.getId()));
        assertEquals(null,dao.getFile(if3.getId()));
    }

    @Test
    public void test_Commit() throws Exception {
        System.out.println("test_Commit");

        String fileName3="file3.jpg";

        File file1 = new File(imagesDirectory, fileName1);
        File file2 = new File(imagesDirectory, fileName2);
        File file3 = new File(imagesDirectory, fileName3);

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(file3.exists());

        TomwFileUtils.copyFileFromResources(new File(fileName1), file1);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file2);
        TomwFileUtils.copyFileFromResources(new File(fileName2), file3);

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());

        ImageFile if1 = new ImageFile("file 1", file1, "comment 1");
        ImageFile if2 = new ImageFile("file 2", file2, "comment 2");
        ImageFile if3 = new ImageFile("file 3", file3, "comment 3");

        List<String> listOfImageFiles  = new ArrayList<>();
        listOfImageFiles.add(if1.getId());
        listOfImageFiles.add(if2.getId());

        assertFalse(dao.imageFileExists(if1.getId()));
        assertFalse(dao.imageFileExists(if2.getId()));
        assertFalse(dao.imageFileExists(if3.getId()));

        dao.add(if1);
        dao.add(if2);
        dao.add(if3);

        assertTrue(dao.imageFileExists(if1.getId()));
        assertTrue(dao.imageFileExists(if2.getId()));
        assertTrue(dao.imageFileExists(if3.getId()));

        dao.commit();

        ImageFileDao dao2 = new ImageFileDaoJsonImpl(dataFile,imagesDirectory);

        assertTrue(dao2.containsImage(if1.getId()));
        assertTrue(dao2.containsImage(if2.getId()));
        assertTrue(dao2.containsImage(if3.getId()));

        assertEquals(if1,dao2.getImageFile(if1.getId()));
        assertEquals(if2,dao2.getImageFile(if2.getId()));
        assertEquals(if3,dao2.getImageFile(if3.getId()));

    }

}