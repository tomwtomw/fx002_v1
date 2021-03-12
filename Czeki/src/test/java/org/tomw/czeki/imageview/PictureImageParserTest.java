/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.imageview;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
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
public class PictureImageParserTest {
    
    private Account account;
    
    private final String dataFileName = "dataFile";
    private final String imageDirectoryName = "images";

    private final File dataFile = new File(dataFileName);
    private final File imageDirectory = new File(imageDirectoryName);    

    File file;
    String fileName = "test_file";

    JSONParser parser = new JSONParser();

    public PictureImageParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        
        initAccount();
        
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
        tearDownAccount();
    }

    public void initAccount() throws IOException{
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
    }
    
    public void tearDownAccount(){
        if (this.dataFile.exists()) {
            this.dataFile.delete();
        }
        
        if (this.imageDirectory.exists()) {
            TomwFileUtils.deleteDirectoryWithFiles(this.imageDirectory);
        }
    }
    
    /**
     * Test of fromString method, of class PictureImageParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testFromString() throws Exception {
        System.out.println("fromString");
        testFromStringAndFromJson();
        
    }

    /**
     * Test of fromJson method, of class PictureImageParser.
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testFromJson() throws ParseException {
        System.out.println("fromJson");
        testFromStringAndFromJson();
    }
    
    public void testFromStringAndFromJson() throws ParseException{
        PictureImage pictureImage1 = new PictureImage(file);
        String expected = pictureImage1.toString();
        PictureImage pictureImage2 = PictureImageParser.fromString(expected);
        String actual = pictureImage2.toString();
        
        System.out.println("1="+expected);
        System.out.println("2="+actual);
        
        assertEquals(pictureImage1,pictureImage2);
    }

}
