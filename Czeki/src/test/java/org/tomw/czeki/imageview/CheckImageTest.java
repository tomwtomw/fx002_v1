/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.tomw.czeki.Account;
import org.tomw.czeki.CzekiRegistry;
import org.tomw.fileutils.TomwFileUtils;

/**
 *
 * @author tomw
 */
public class CheckImageTest {
    private final static Logger LOGGER = Logger.getLogger(CheckImageTest.class.getName());
    
    private Account account;
    
    private final String dataFileName = "dataFile";
    private final String imageDirectoryName = "images";

    private final File dataFile = new File(dataFileName);
    private final File imageDirectory = new File(imageDirectoryName);   
    
    private String file1Id;

    private File file1;
    private File file2;
    private File file3;
    private File file4;
    private File file5;
    private File file6;
    private File file7;
    private File file8;
    private File file9;

    private CheckImage czek1;
    private CheckImage czek2;
    private CheckImage czek3;
    private CheckImage czek4;
    private CheckImage czek5;
    private CheckImage czek6;
    private CheckImage czek7;
    private CheckImage czek8;
    private CheckImage czek9;
    
    private final JSONParser jsonParser = new JSONParser();

    public CheckImageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException, CheckImageException {
        if (this.dataFile.exists()) {
            this.dataFile.delete();
        }

        System.out.println("imageDirectory=" + imageDirectory.getCanonicalPath());

        if (this.imageDirectory.exists()) {
            TomwFileUtils.deleteDirectoryWithFiles(this.imageDirectory);
        }
        System.out.println("imageDirectory.exists()=" + imageDirectory.exists());
        this.imageDirectory.mkdir();
        
        
        this.account = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                this.dataFile.toString(),
                this.imageDirectory.toString()
        );

        CzekiRegistry.currentAccount = account;
        
        initImages();

    }
    
    public void initImages() throws IOException, CheckImageException{
        file1Id = "C7D17FBA3528200F.jpg";
        file1 = new File(file1Id);
        file2 = new File("C7D17FBA3529200B.jpg");
        file3 = new File("EA5DA3CC375614710009F.jpg");
        file4 = new File("EA5DA3CC375614710009B.jpg");
        file5 = new File("EA5DA3CC375614710009X.jpg");
        file6 = new File("DSCF9217.jpg");
        file7 = new File("EA5DA3CC375614710009F.txt");
        file8 = new File("EA5DA3CC375614710009F.jpg");
        file9 = new File("2017-08-03-4027-F.gif");

        TomwFileUtils.createFile(new File(this.imageDirectory, file1.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file2.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file3.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file4.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file5.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file6.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file7.toString()));

        TomwFileUtils.createFile(new File(this.imageDirectory, file9.toString()));

        czek1 = new CheckImage(imageDirectory,file1);
        czek2 = new CheckImage(imageDirectory,file2);
        czek3 = new CheckImage(imageDirectory,file3);
        czek4 = new CheckImage(imageDirectory,file4);
        //czek5=new CheckImage(imageDirectory,file5);
        //czek6=new CheckImage(imageDirectory,file6);
        //czek7=new CheckImage(imageDirectory,file7);
        czek9 = new CheckImage(imageDirectory,file9);
    }

    @After
    public void tearDown() {
        deleteFile(file1);
        deleteFile(file2);
        deleteFile(file3);
        deleteFile(file4);
        deleteFile(file5);
        deleteFile(file6);
        deleteFile(file7);
        deleteFile(file8);
        deleteFile(file9);
        
        if (this.dataFile.exists()) {
            this.dataFile.delete();
        }
        
        if (this.imageDirectory.exists()) {
            TomwFileUtils.deleteDirectoryWithFiles(this.imageDirectory);
        }
        
    }
    


    /**
     * Test of isFront method, of class CheckImage.
     */
    @Test
    public void testIsFront() {
        LOGGER.info("testIsFront");
        testFrontAndBack();
    }

    /**
     * Test of isBack method, of class CheckImage.
     */
    @Test
    public void testIsBack() {
        LOGGER.info("testIsBack");
        testFrontAndBack();
    }

    public void testFrontAndBack() {
        assertTrue(czek1.isFront());
        assertFalse(czek1.isBack());

        assertFalse(czek2.isFront());
        assertTrue(czek2.isBack());

        assertTrue(czek3.isFront());
        assertFalse(czek3.isBack());

        assertFalse(czek4.isFront());
        assertTrue(czek4.isBack());

        assertTrue(czek9.isFront());
        assertFalse(czek9.isBack());
    }

    /**
     * Test of getCheckNumber method, of class CheckImage.
     */
    @Test
    public void testGetCheckNumber() {
        LOGGER.info("getCheckNumber");
        assertEquals("3528", czek1.getCheckNumber());
        assertEquals("3529", czek2.getCheckNumber());
        assertEquals("3756", czek3.getCheckNumber());
        assertEquals("3756", czek4.getCheckNumber());
        assertEquals("4027", czek9.getCheckNumber());
    }

    @Test
    public void test_createBadFile() {
        boolean thrown5 = false;
        try {
            czek5 = new CheckImage(this.imageDirectory,file5);
        } catch (CheckImageException ex) {
            thrown5 = true;
        }
        assertTrue(thrown5);

        boolean thrown6 = false;
        try {
            czek6 = new CheckImage(this.imageDirectory,file6);
        } catch (CheckImageException ex) {
            thrown6 = true;
        }
        assertTrue(thrown6);

        boolean thrown7 = false;
        try {
            czek7 = new CheckImage(this.imageDirectory,file7);
        } catch (CheckImageException ex) {
            thrown7 = true;
        }
        assertTrue(thrown7);

        boolean thrown9 = false;
        try {
            czek9 = new CheckImage(this.imageDirectory,file9);
        } catch (CheckImageException ex) {
            thrown9 = true;
        }
        assertFalse(thrown9);


    }

    @Test
    public void checkFromJson() throws CheckImageException {
        JSONObject json = czek1.toJson();
        CheckImage reconstructed = CheckImage.fromJson(this.imageDirectory, json);
        assertEquals(reconstructed, czek1);

        json = czek9.toJson();
        reconstructed = CheckImage.fromJson(this.imageDirectory, json);
        assertEquals(reconstructed, czek9);
    }

    private void createFile(File file) throws IOException {
        deleteFile(file);
        FileUtils.writeStringToFile(file, "CheckImageTest");
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Test of getIdProperty method, of class CheckImage.
     */
    @Test
    public void testGetIdProperty() {
        System.out.println("testGetIdProperty");
        CheckImage instance = czek1;
        String expResult = file1Id;
        StringProperty result = instance.getIdProperty();
        assertEquals(expResult, result.get());
        
    }

    /**
     * Test of getId method, of class CheckImage.
     */
    @Test
    public void testGetId() {
        System.out.println("testGetId");
        CheckImage instance = czek1;
        String expResult = file1Id;
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileNameProperty method, of class CheckImage.
     */
    @Test
    public void testGetFileNameProperty() {
        System.out.println("getFileNameProperty");
        CheckImage instance = czek1;
        String expResult = file1Id;
        StringProperty result = instance.getFileNameProperty();
        assertEquals(expResult, result.get());
    }

    /**
     * Test of getCheckNumberProperty method, of class CheckImage.
     */
    @Test
    public void testGetCheckNumberProperty() {
        System.out.println("getCheckNumberProperty");
        assertEquals("3528", czek1.getCheckNumberProperty().get());
        assertEquals("3529", czek2.getCheckNumberProperty().get());
        assertEquals("3756", czek3.getCheckNumberProperty().get());
        assertEquals("3756", czek4.getCheckNumberProperty().get());
        assertEquals("4027", czek9.getCheckNumberProperty().get());

    }

    /**
     * Test of getCheckFrontBackProperty method, of class CheckImage.
     */
    @Test
    public void testGetCheckFrontBackProperty() {
        System.out.println("getCheckFrontBackProperty");
        
        assertEquals("F",czek1.getCheckFrontBackProperty().get());
        assertEquals("B",czek2.getCheckFrontBackProperty().get());

    }

    /**
     * Test of fromJson method, of class CheckImage.
     * @throws java.lang.Exception
     */
    @Test
    public void testFromJson() throws Exception {
        System.out.println("fromJson");
        
        String expResultString = "{\"File\":\"C7D17FBA3528200F.jpg\"}";
        JSONObject json = (JSONObject)jsonParser.parse(expResultString);
        
        CheckImage expResult = czek1;
        CheckImage result = CheckImage.fromJson(this.imageDirectory, json);
        assertEquals(expResult, result);

        expResultString = "{\"File\":\"2017-08-03-4027-F.gif\"}";
        json = (JSONObject)jsonParser.parse(expResultString);

        expResult = czek9;
        result = CheckImage.fromJson(this.imageDirectory, json);
        assertEquals(expResult, result);
    }

    /**
     * Test of getFile method, of class CheckImage.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        CheckImage instance = czek1;
        File expResult = new File(this.imageDirectory, file1.toString());
        File result = instance.getFile();
        assertEquals(expResult, result);


    }

    /**
     * Test of toJson method, of class CheckImage.
     */
    @Test
    public void testToJson() throws ParseException {
        System.out.println("toJson");
        CheckImage instance = czek1;
        
        String expResultString = "{\"File\":\"C7D17FBA3528200F.jpg\"}";
        JSONObject expResult = (JSONObject)jsonParser.parse(expResultString);
        JSONObject result = instance.toJson();
        assertEquals(expResult, result);

        instance = czek9;

        expResultString = "{\"File\":\"2017-08-03-4027-F.gif\"}";
        expResult = (JSONObject)jsonParser.parse(expResultString);
        result = instance.toJson();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class CheckImage.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        CheckImage instance = czek1;
        int expResult = 1427032509;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class CheckImage.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        assertEquals(czek1, czek1);
        assertFalse(czek1.equals(czek2));
    }

}
