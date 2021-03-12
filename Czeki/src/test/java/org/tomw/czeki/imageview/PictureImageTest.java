/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
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
public class PictureImageTest {

    File file;
    String fileName = "test_file";
    
    
    public PictureImageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        this.file = new File(this.fileName);
        if (file.exists()) {
            file.delete();
        }
        FileUtils.writeStringToFile(file, "test file, delete it");
    }

    @After
    public void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Test of getNameProperty method, of class PictureImage.
     */
    @Test
    public void testGetNameProperty() {
        System.out.println("getNameProperty");
        PictureImage pictureImage = new PictureImage(file);        
        assertEquals(pictureImage.getNameProperty().get(),file.getName());
    }

    

    /**
     * Test of getFullFileNameProperty method, of class PictureImage.
     */
    @Test
    public void testGetFullFileNameProperty() {
        System.out.println("getFullFileNameProperty");
        PictureImage pictureImage = new PictureImage(file);
        assertEquals(pictureImage.getFullFileNameProperty().get(),file.getAbsolutePath());
    }

    /**
     * Test of getFile method, of class PictureImage.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        PictureImage pictureImage = new PictureImage(file);
        assertEquals(file,pictureImage.getFile());
    }

    /**
     * Test of toJson method, of class PictureImage.
     * @throws java.io.IOException if something goes wrong
     */
    @Test
    public void testToJson() throws IOException {
        System.out.println("toJson");
        testToJsonAndToString();
    }

    /**
     * Test of toString method, of class PictureImage.
     * @throws java.io.IOException
     */
    @Test
    public void testToString() throws IOException {
        System.out.println("toString");
        testToJsonAndToString();
    }

    public void testToJsonAndToString() throws IOException {
        PictureImage instance = new PictureImage(this.file);

        JSONObject expResult = new JSONObject();
        expResult.put(PictureImage.FILE_KEY, file.getName());

        JSONObject result = instance.toJson();
        
        assertEquals(expResult, result);
    }


    
}
