/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
public class AccountTest {

    private final JSONParser parser = new JSONParser();

    public AccountTest() {
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
     * Test of toJson method, of class Account.
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testToJson() throws ParseException {
        System.out.println("toJson");
        Account account = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );

        String s = "{\"Short Name\":\"undefined short name\",\"Comment\":\"\",\"Image Directory\":\"undefined image directory\",\"name\":\"undefined name\",\"id\":\"b5ca52e4-c5b1-462c-b8a9-380550ea57a6\",\"File Name\":\"undefined file name\"}";
        JSONObject jsonExpected = (JSONObject) parser.parse(s);

        JSONObject result = account.toJson();

        assertEquals(jsonExpected, result);
    }

    /**
     * Test of toJsonString method, of class Account.
     */
    @Test
    public void testToJsonString() {
        System.out.println("toJsonString");
        Account account = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        String expected = "{\"Short Name\":\"undefined short name\",\"Comment\":\"\",\"Image Directory\":\"undefined image directory\",\"name\":\"undefined name\",\"id\":\"b5ca52e4-c5b1-462c-b8a9-380550ea57a6\",\"File Name\":\"undefined file name\"}";
        String actual = account.toJsonString();
        assertEquals(expected, actual);
    }

    /**
     * Test of fromJson method, of class Account.
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testFromJson() throws ParseException {
        System.out.println("fromJson");

        String s = "{\"Short Name\":\"undefined short name\",\"Comment\":\"\",\"Image Directory\":\"undefined image directory\",\"name\":\"undefined name\",\"id\":\"b5ca52e4-c5b1-462c-b8a9-380550ea57a6\",\"File Name\":\"undefined file name\"}";
        JSONObject json = (JSONObject) parser.parse(s);

        Account actual = Account.fromJson(json);
        Account expected = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        assertEquals(actual, expected);
    }

    /**
     * Test of fromJsonString method, of class Account.
     * @throws java.lang.Exception
     */
    @Test
    public void testFromJsonString() throws Exception {
        System.out.println("fromJsonString");
        String s = "{\"Short Name\":\"undefined short name\",\"Comment\":\"\",\"Image Directory\":\"undefined image directory\",\"name\":\"undefined name\",\"id\":\"b5ca52e4-c5b1-462c-b8a9-380550ea57a6\",\"File Name\":\"undefined file name\"}";
        Account actual = Account.fromJsonString(s);
        Account expected = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        assertEquals(actual, expected);
    }

    /**
     * Test of hashCode method, of class Account.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account2 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        assertEquals(account1.hashCode(), account2.hashCode());

        int expectedHashCode = -565348879;
        int actualHashCode = account1.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    /**
     * Test of equals method, of class Account.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account2 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account3 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a7",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account4 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a7",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory2"
        );

        assertEquals(account1, account2);
        assertFalse(account1.equals(account3));
    }

    /**
     * Test of equalNonIdFields method, of class Account.
     */
    @Test
    public void testEqualNonIdFields() {
        System.out.println("equalNonIdFields");

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account2 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account3 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a7",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        Account account4 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a7",
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory2"
        );

        assertTrue(account1.equalNonIdFields(account2));
        assertTrue(account1.equalNonIdFields(account3));
        assertFalse(account3.equalNonIdFields(account4));
    }

    /**
     * Test of getIdProperty method, of class Account.
     */
    @Test
    public void testGetIdProperty() {
        System.out.println("getIdProperty");

        String expectedId = "b5ca52e4-c5b1-462c-b8a9-380550ea57a6";

        Account account1 = new Account(expectedId,
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );

        String actualId = account1.getIdProperty().get();

        assertEquals(expectedId, actualId);
    }

    /**
     * Test of setIdProperty method, of class Account.
     */
    @Test
    public void testSetIdProperty() {
        System.out.println("setIdProperty");
        StringProperty idProperty = new SimpleStringProperty("b5ca52e4-c5b1-462c-b8a9-380550ea57a6");
        Account instance = new Account();
        instance.setIdProperty(idProperty);

        assertEquals(idProperty.get(), instance.getIdProperty().get());
    }

    /**
     * Test of getId method, of class Account.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        String expectedId = "b5ca52e4-c5b1-462c-b8a9-380550ea57a6";

        Account account1 = new Account(expectedId,
                "undefined short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );

        String actualId = account1.getId();

        assertEquals(expectedId, actualId);
    }

    /**
     * Test of setId method, of class Account.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String idExpected = "b5ca52e4-c5b1-462c-b8a9-380550ea57a6";
        Account instance = new Account();
        instance.setId(idExpected);

        assertEquals(idExpected, instance.getId());
    }

    /**
     * Test of getNameProperty method, of class Account.
     */
    @Test
    public void testGetNameProperty() {
        System.out.println("getNameProperty");
        String expectedName = "undefined name";

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                expectedName,
                "undefined file name",
                "undefined image directory"
        );

        String actualName = account1.getNameProperty().get();

        assertEquals(expectedName, actualName);
    }

    /**
     * Test of setNameProperty method, of class Account.
     */
    @Test
    public void testSetNameProperty() {
        System.out.println("setNameProperty");

        String nameExpected = "qwerty";
        Account instance = new Account();
        instance.setNameProperty(new SimpleStringProperty(nameExpected));

        assertEquals(nameExpected, instance.getName());
    }

    /**
     * Test of getName method, of class Account.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        String expectedName = "undefined name";

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                expectedName,
                "undefined file name",
                "undefined image directory"
        );

        String actualName = account1.getName();

        assertEquals(expectedName, actualName);
    }

    /**
     * Test of setName method, of class Account.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String nameExpected = "qwerty";
        Account instance = new Account();
        instance.setName(nameExpected);

        assertEquals(nameExpected, instance.getName());
    }

    /**
     * Test of getShortNameProperty method, of class Account.
     */
    @Test
    public void testGetShortNameProperty() {
        System.out.println("getShortNameProperty");
        String expectedShortName = "expectedShortName";

        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                expectedShortName,
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );

        String actualShortName = account1.getShortNameProperty().get();

        assertEquals(expectedShortName, actualShortName);
    }

    /**
     * Test of setShortNameProperty method, of class Account.
     */
    @Test
    public void testSetShortNameProperty() {
        System.out.println("setShortNameProperty");

        String expectedShortName = "expectedShortName";

        Account account1 = new Account();
        account1.setShortNameProperty(new SimpleStringProperty(expectedShortName));

        String actualShortName = account1.getShortName();

        assertEquals(expectedShortName, actualShortName);
        assertEquals(expectedShortName, account1.getShortNameProperty().get());
    }

    /**
     * Test of getShortName method, of class Account.
     */
    @Test
    public void testGetShortName() {
        System.out.println("getShortName");
        String expectedShortName = "expectedShortName";

        Account account1 = new Account();
        account1.setShortNameProperty(new SimpleStringProperty(expectedShortName));

        String actualShortName = account1.getShortName();
        assertEquals(expectedShortName, actualShortName);
    }

    /**
     * Test of setShortName method, of class Account.
     */
    @Test
    public void testSetShortName() {
        System.out.println("setShortName");

        String expectedShortName = "expectedShortName";

        Account account1 = new Account();
        account1.setShortName(expectedShortName);

        String actualShortName = account1.getShortName();

        assertEquals(expectedShortName, actualShortName);
        assertEquals(expectedShortName, account1.getShortNameProperty().get());
    }

    /**
     * Test of getCommentProperty method, of class Account.
     */
    @Test
    public void testGetCommentProperty() {
        System.out.println("getCommentProperty");
        Account account = new Account();
        String expectedComment = "expected comment";
        account.setComment(expectedComment);
        String actualComment = account.getCommentProperty().get();
        assertEquals(actualComment, expectedComment);
    }

    /**
     * Test of setCommentProperty method, of class Account.
     */
    @Test
    public void testSetCommentProperty() {
        System.out.println("setCommentProperty");
        Account account = new Account();
        String expectedComment = "expected comment";
        account.setComment(expectedComment);
        String actualComment = account.getCommentProperty().get();
        assertEquals(actualComment, expectedComment);
    }

    /**
     * Test of getComment method, of class Account.
     */
    @Test
    public void testGetComment() {
        System.out.println("getComment");
        Account account = new Account();
        String expectedComment = "expected comment";
        account.setComment(expectedComment);
        String actualComment = account.getComment();
        assertEquals(actualComment, expectedComment);
    }

    /**
     * Test of setComment method, of class Account.
     */
    @Test
    public void testSetComment() {
        System.out.println("setComment");
        Account account = new Account();
        String expectedComment = "expected comment";
        account.setComment(expectedComment);
        String actualComment = account.getComment();
        assertEquals(actualComment, expectedComment);
    }

    /**
     * Test of getFileNameProperty method, of class Account.
     */
    @Test
    public void testGetFileNameProperty() {
        System.out.println("getFileNameProperty");
        Account account = new Account();
        String expectedFileName = "expectedFileName";
        account.setFileName(expectedFileName);
        String actualFileName = account.getFileName();
        assertEquals(actualFileName, expectedFileName);
    }

    /**
     * Test of setFileNameProperty method, of class Account.
     */
    @Test
    public void testSetFileNameProperty() {
        System.out.println("setFileNameProperty");
        Account account = new Account();
        String expectedFileName = "expectedFileName";
        account.setFileName(expectedFileName);
        String actualFileName = account.getFileName();
        assertEquals(actualFileName, expectedFileName);
    }

    /**
     * Test of getFileName method, of class Account.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        Account account = new Account();
        String expectedFileName = "expectedFileName";
        account.setFileName(expectedFileName);
        String actualFileName = account.getFileName();
        assertEquals(actualFileName, expectedFileName);
    }

    /**
     * Test of setFileName method, of class Account.
     */
    @Test
    public void testSetFileName() {
        System.out.println("setFileName");
        Account account = new Account();
        String expectedFileName = "expectedFileName";
        account.setFileName(expectedFileName);
        String actualFileName = account.getFileName();
        assertEquals(actualFileName, expectedFileName);
    }

    /**
     * Test of getImageDirectoryProperty method, of class Account.
     */
    @Test
    public void testGetImageDirectoryProperty() {
        System.out.println("getImageDirectoryProperty");
        Account account = new Account();
        String expectedImageDirectoryString = "expectedImageDirectoryString";
        account.setImageDirectory(expectedImageDirectoryString);
        String actualImageDirectory = account.getImageDirectoryProperty().get();
        assertEquals(actualImageDirectory, expectedImageDirectoryString);
    }

    /**
     * Test of setImageDirectoryProperty method, of class Account.
     */
    @Test
    public void testSetImageDirectoryProperty() {
        System.out.println("setImageDirectoryProperty");
        Account account = new Account();
        String expectedImageDirectoryString = "expectedImageDirectoryString";
        account.setImageDirectoryProperty(new SimpleStringProperty(expectedImageDirectoryString));
        String actualImageDirectory = account.getImageDirectoryProperty().get();
        assertEquals(actualImageDirectory, expectedImageDirectoryString);
    }

    /**
     * Test of getImageDirectoryString method, of class Account.
     */
    @Test
    public void testGetImageDirectoryString() {
        System.out.println("getImageDirectoryString");
        Account account = new Account();
        String expectedImageDirectoryString = "expectedImageDirectoryString";
        account.setImageDirectoryProperty(new SimpleStringProperty(expectedImageDirectoryString));
        String actualImageDirectory = account.getImageDirectoryString();
        assertEquals(actualImageDirectory, expectedImageDirectoryString);
    }

    /**
     * Test of setImageDirectory method, of class Account.
     */
    @Test
    public void testSetImageDirectory() {
        System.out.println("setImageDirectory");
        Account account = new Account();
        String expectedImageDirectoryString = "expectedImageDirectoryString";
        account.setImageDirectory(expectedImageDirectoryString);
        String actualImageDirectory = account.getImageDirectoryString();
        assertEquals(actualImageDirectory, expectedImageDirectoryString);
    }
    
    @Test
    public void testToString(){
        System.out.println("testToString");
        
        Account account1 = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "expected short name",
                "undefined name",
                "undefined file name",
                "undefined image directory"
        );
        
        String expected = "{\"Short Name\":\"expected short name\",\"Comment\":\"\",\"Image Directory\":\"undefined image directory\",\"name\":\"undefined name\",\"id\":\"b5ca52e4-c5b1-462c-b8a9-380550ea57a6\",\"File Name\":\"undefined file name\"}" ;
        
        String actual = account1.toString();

        assertEquals(expected,actual);
    }

}
