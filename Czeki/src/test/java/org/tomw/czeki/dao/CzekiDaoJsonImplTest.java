/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.dao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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
import org.tomw.czeki.entities.CounterParty;
import org.tomw.czeki.entities.Transaction;
import org.tomw.czeki.imageview.CheckImage;
import org.tomw.czeki.imageview.CheckImageException;
import org.tomw.czeki.imageview.CzekImageUtils;
import org.tomw.fileutils.TomwFileUtils;

/**
 *
 * @author tomw
 */
public class CzekiDaoJsonImplTest {

    private final static Logger LOGGER = Logger.getLogger(CzekiDaoJsonImplTest.class.getName());

    Account account;

    String dataFileName = "dataFile";
    String imageDirectoryName = "images";

    File dataFile = new File(dataFileName);
    File imageDirectory = new File(imageDirectoryName);

    String dataFileName2 = "dataFile2";
    File dataFile2 = new File(dataFileName2);

    Transaction t1;
    Transaction t2;
    Transaction t3;
    Transaction t4;
    Transaction t5;

    CounterParty cp1;
    CounterParty cp2;
    CounterParty cp3;
    CounterParty cp4;

    // image files
    private File file1f;
    private File file1b;
    private File file2f;
    private File file2b;
    private File file3f;
    private File file3b;
    private File file4f;
    private File file4b;
    private File file5;
    private File file6;
    private File file7;
    private File file8;

    private CheckImage image1f;
    private CheckImage image1b;
    private CheckImage image2f;
    private CheckImage image2b;
    private CheckImage image3f;
    private CheckImage image3b;
    private CheckImage image4f;
    private CheckImage image4b;

    public CzekiDaoJsonImplTest() {
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

        LOGGER.info("imageDirectory=" + imageDirectory.getCanonicalPath());

        if (this.imageDirectory.exists()) {
            TomwFileUtils.deleteDirectoryWithFiles(this.imageDirectory);
        }
        LOGGER.info("imageDirectory.exists()=" + imageDirectory.exists());
        this.imageDirectory.mkdir();

        this.account = new Account("b5ca52e4-c5b1-462c-b8a9-380550ea57a6",
                "undefined short name",
                "undefined name",
                this.dataFile.toString(),
                this.imageDirectory.toString()
        );

        CzekiRegistry.currentAccount = account;

        initCounterParties();

        this.t1 = new Transaction("test_id_1", LocalDate.now(), 5.0, "1111", cp1, "memo1", "comment1");
        this.t2 = new Transaction("test_id_2", LocalDate.now(), 5.0, "2222", cp1, "memo2", "comment2");
        this.t3 = new Transaction("test_id_3", LocalDate.now(), 5.0, "3333", cp3, "memo3", "comment3");
        this.t4 = new Transaction("test_id_4", LocalDate.now(), 5.0, "4444", cp4, "memo4", "comment4");
        this.t5 = new Transaction("test_id_5", LocalDate.now(), 5.0, "5555", cp4, "memo5", "comment5");

        initImages();
    }

    private void initImages() throws IOException, CheckImageException {
        file1f = new File("C7D17FBA1111200F.jpg");
        file1b = new File("C7D17FBA1111200B.jpg");

        file2f = new File("C7D17FBA2222200F.jpg");
        file2b = new File("C7D17FBA2222200B.jpg");

        file3f = new File("EA5DA3CC333314710009F.jpg");
        file3b = new File("EA5DA3CC333314710009B.jpg");

        file4f = new File("EA5DA3CC444414710009F.jpg");
        file4b = new File("EA5DA3CC444414710009B.jpg");

        file5 = new File("EA5DA3CC375614710009X.jpg");
        file6 = new File("DSCF9217.jpg");
        file7 = new File("EA5DA3CC375614710009F.txt");
        file8 = new File("EA5DA3CC375614710009F.jpg");
          
        TomwFileUtils.createFile(new File(this.imageDirectory, file1f.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file2f.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file3f.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file4f.toString()));

        TomwFileUtils.createFile(new File(this.imageDirectory, file1b.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file2b.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file3b.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file4b.toString()));

        TomwFileUtils.createFile(new File(this.imageDirectory, file5.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file6.toString()));
        TomwFileUtils.createFile(new File(this.imageDirectory, file7.toString()));

        image1f = new CheckImage(imageDirectory,file1f);
        image1b = new CheckImage(imageDirectory,file1b);
        image2f = new CheckImage(imageDirectory,file2f);
        image2b = new CheckImage(imageDirectory,file2b);
        image3f = new CheckImage(imageDirectory,file3f);
        image3b = new CheckImage(imageDirectory,file3b);
        image4f = new CheckImage(imageDirectory,file4f);
        image4b = new CheckImage(imageDirectory,file4b);
    }

    public void initCounterParties() {
        String id1 = "id_1";
        String shortName1 = "short name 1";
        String name1 = "name 1";
        List<String> otherNames1 = new ArrayList<>();
        otherNames1.add("a1");
        otherNames1.add("b1");
        otherNames1.add("c1");
        this.cp1 = new CounterParty(id1, shortName1, name1, otherNames1);

        String id2 = "id_2";
        String shortName2 = "short name 2";
        String name2 = "name 2";
        List<String> otherNames2 = new ArrayList<>();
        otherNames2.add("a2");
        otherNames2.add("b2");
        otherNames2.add("c2");
        this.cp2 = new CounterParty(id2, shortName2, name2, otherNames2);

        String id3 = "id_3";
        String shortName3 = "short name 3";
        String name3 = "name 3";
        List<String> otherNames3 = new ArrayList<>();
        otherNames3.add("a3");
        otherNames3.add("b3");
        otherNames3.add("c3");
        this.cp3 = new CounterParty(id3, shortName3, name3, otherNames3);

        String id4 = "id_4";
        String shortName4 = "short name 4";
        String name4 = "name 4";
        List<String> otherNames4 = new ArrayList<>();
        otherNames4.add("a4");
        otherNames4.add("b4");
        otherNames4.add("c4");
        this.cp4 = new CounterParty(id4, shortName4, name4, otherNames4);
    }

    @After
    public void tearDown() {
        if (this.dataFile.exists()) {
            this.dataFile.delete();
        }
        if (this.dataFile2.exists()) {
            this.dataFile2.delete();
        }
        if (this.imageDirectory.exists()) {
            TomwFileUtils.deleteDirectoryWithFiles(this.imageDirectory);
        }
    }

    /**
     * Test of init method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInit() throws Exception {
        testLoad();
    }

    /**
     * Test of commit method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCommit() throws Exception {
        LOGGER.info("commit");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);

        dao.commit();

        CzekiDao dao2 = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertTrue(dao2.containsTransaction(t1.getId()));
        assertTrue(dao2.containsTransaction(t2.getId()));
        assertTrue(dao2.containsTransaction(t3.getId()));
        assertTrue(dao2.containsTransaction(t4.getId()));
        assertFalse(dao2.containsTransaction(t5.getId()));
    }

    /**
     * Test of add method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testAdd_Transaction() {
        LOGGER.info("testAdd_Transaction");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertFalse(dao.containsTransaction(t2.getId()));

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));

        assertTrue(dao.getTransactionsData().contains(t1));
        assertFalse(dao.getTransactionsData().contains(t2));
        assertFalse(dao.getTransactionsData().contains(t3));
    }

    /**
     * Test of add method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testAdd_CounterParty() throws CheckImageException {
        LOGGER.info("testAdd_CounterParty");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
    }

    /**
     * Test of add method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testAdd_CheckImage() throws CheckImageException {
        LOGGER.info("testAdd_CheckImage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image2f.getId()));
        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertFalse(dao.containsCheckImage(image4f.getId()));
    }

    /**
     * Test of getTransaction method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransaction() {
        LOGGER.info("testGetTransaction");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        assertTrue(dao.getTransaction(t1.getId()).equals(t1));
        assertTrue(dao.getTransaction(t2.getId()) == null);
    }

    /**
     * Test of getCounterParty method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetCounterParty() {
        LOGGER.info("getCounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        dao.add(cp3);

        assertTrue(dao.getCounterParty(cp1.getId()).equals(cp1));
        assertTrue(dao.getTransaction(cp2.getId()) == null);
        assertTrue(dao.getCounterParty(cp3.getId()).equals(cp3));
    }

    /**
     * Test of getCheckImage method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetCheckImage() throws CheckImageException {
        LOGGER.info("testGetCheckImage");

        CheckImage image1 = new CheckImage(this.imageDirectory,file1f);
        CheckImage image2 = new CheckImage(this.imageDirectory,file2f);
        CheckImage image3 = new CheckImage(this.imageDirectory,file3f);
        CheckImage image4 = new CheckImage(this.imageDirectory,file4f);

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1);
        dao.add(image2);
        dao.add(image3);

        assertEquals(dao.getCheckImage(image1.getId()), image1);
        assertEquals(dao.getCheckImage(image2.getId()), image2);
        assertEquals(dao.getCheckImage(image3.getId()), image3);
        assertEquals(dao.getCheckImage(image4.getId()), null);
    }

    /**
     * Test of containsTransaction method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testContainsTransaction() {
        LOGGER.info("testContainsTransaction");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertFalse(dao.containsTransaction(t2.getId()));
    }

    /**
     * Test of containsCounterParty method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testContainsCounterParty() {
        LOGGER.info("containsCounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of containsCheckImage method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testContainsCheckImage() throws CheckImageException {
        LOGGER.info("testContainsCheckImage");
        CheckImage image1 = new CheckImage(this.imageDirectory,file1f);
        CheckImage image2 = new CheckImage(this.imageDirectory,file2f);
        CheckImage image3 = new CheckImage(this.imageDirectory,file3f);
        CheckImage image4 = new CheckImage(this.imageDirectory,file4f);

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1);
        dao.add(image2);
        dao.add(image3);

        assertTrue(dao.containsCheckImage(image1.getId()));
        assertTrue(dao.containsCheckImage(image2.getId()));
        assertTrue(dao.containsCheckImage(image3.getId()));
        assertFalse(dao.containsCheckImage(image4.getId()));
    }

    /**
     * Test of getCounterPartyByShortName method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetCounterPartyByShortName() {
        LOGGER.info("getCounterPartyByShortName");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);

        assertEquals(dao.getCounterPartyByShortName(cp1.getShortName()), cp1);
        assertEquals(dao.getCounterPartyByShortName(cp2.getShortName()), null);
        assertEquals(dao.getCounterPartyByShortName(cp3.getShortName()), cp3);
        assertEquals(dao.getCounterPartyByShortName(cp4.getShortName()), cp4);
    }

    /**
     * Test of getTransactions method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactions() {
        LOGGER.info("testGetTransactions");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);

        Map<String, Transaction> result = dao.getTransactions();

        assertEquals(result.get(t1.getId()), t1);
        assertEquals(result.get(t2.getId()), t2);
        assertEquals(result.get(t3.getId()), t3);
        assertEquals(result.get(t4.getId()), t4);
        assertEquals(result.get(t5.getId()), null);
    }

    /**
     * Test of getCounterParties method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetCounterParties() {
        LOGGER.info("getCounterParties");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        Map<String, CounterParty> result = dao.getCounterParties();

        assertEquals(result.get(cp1.getId()), cp1);
        assertEquals(result.get(cp2.getId()), null);
        assertEquals(result.get(cp3.getId()), cp3);
    }

    /**
     * Test of getAllImages method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetAllImages() throws CheckImageException {
        LOGGER.info("getAllImages");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        List<CheckImage> result = dao.getAllImages();

        System.out.println("size=" + result.size());

        assertTrue(result.size() == 6);

        assertTrue(result.contains(image1f));
        assertTrue(result.contains(image1b));
        assertTrue(result.contains(image2f));
        assertTrue(result.contains(image2b));
        assertTrue(result.contains(image3f));
        assertTrue(result.contains(image3b));

        assertFalse(result.contains(image4f));
        assertFalse(result.contains(image4b));
    }

    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData() {
        LOGGER.info("testGetTransactionsData");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);

        ObservableList<Transaction> result = dao.getTransactionsData();

        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
        assertTrue(result.contains(t3));
        assertTrue(result.contains(t4));
        assertFalse(result.contains(t5));

        dao.delete(t1);

        assertFalse(result.contains(t1));
        assertTrue(result.contains(t2));
        assertTrue(result.contains(t3));
        assertTrue(result.contains(t4));
        assertFalse(result.contains(t5));
    }

    @Test
    public void getTransactionsData_2LocalDates() throws Exception {
        LOGGER.info("getTransactionsData");

        Transaction t1a = new Transaction("test_id_1", LocalDate.of(2017, Month.MARCH, 19), 5.0, "1111", cp1, "memo1", "comment1");
        Transaction t2a = new Transaction("test_id_2", LocalDate.of(2017, Month.FEBRUARY, 19), 5.0, "2222", cp1, "memo2", "comment2");
        Transaction t3a = new Transaction("test_id_3", LocalDate.of(2017, Month.JANUARY, 19), 5.0, "3333", cp3, "memo3", "comment3");
        Transaction t4a = new Transaction("test_id_4", LocalDate.of(2016, Month.DECEMBER, 19), 5.0, "4444", cp4, "memo4", "comment4");
        Transaction t5a = new Transaction("test_id_5", LocalDate.of(2016, Month.NOVEMBER, 19), 5.0, "5555", cp4, "memo5", "comment5");
        
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1a);
        dao.add(t2a);
        dao.add(t3a);
        dao.add(t4a);
        dao.add(t5a);
        
        LocalDate startDate = LocalDate.of(2016,Month.DECEMBER,1);
        LocalDate endDate = LocalDate.of(2017,Month.FEBRUARY,28);
        
        List<Transaction> expected = new ArrayList<>();
        expected.add(t2a);
        expected.add(t3a);
        expected.add(t4a);
        
        ObservableList<Transaction> result = dao.getTransactionsData(startDate, endDate);
        
        assertEquals(expected,result);
    }
    
    /**
     * Test of getCounterPartyData method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testGetCounterPartyData() throws DataIntegrityException {
        LOGGER.info("testGetCounterPartyData");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        dao.add(cp3);

        ObservableList<CounterParty> result = dao.getCounterPartyData();

        assertTrue(result.contains(cp1));
        assertTrue(result.contains(cp3));

        dao.delete(cp3);

        assertTrue(result.contains(cp1));
        assertFalse(result.contains(cp3));
    }

    /**
     * Test of getCheckImageData method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testGetCheckImageData() throws DataIntegrityException {
        LOGGER.info("testGetCheckImageData");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image1b);

        ObservableList<CheckImage> result = dao.getCheckImageData();

        assertTrue(result.contains(image1f));
        assertTrue(result.contains(image1b));

        dao.delete(image1b);

        assertTrue(result.contains(image1f));
        assertFalse(result.contains(image1b));
    }

    /**
     * Test of replaceCounterParty method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testReplaceCounterParty() {
        LOGGER.info("testReplaceCounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));

        assertEquals(dao.getTransaction(t1.getId()).getCounterParty(), cp1);
        assertEquals(dao.getTransaction(t2.getId()).getCounterParty(), cp1);

        dao.replaceCounterParty(cp1, cp2);

        assertFalse(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));

        assertEquals(dao.getTransaction(t1.getId()).getCounterParty(), cp2);
        assertEquals(dao.getTransaction(t2.getId()).getCounterParty(), cp2);

    }

    /**
     * Test of mergeCounterparties method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testMergeCounterparties() {
        LOGGER.info("mergeCounterparties");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertEquals(dao.getTransaction(t1.getId()).getCounterParty(), cp1);
        assertEquals(dao.getTransaction(t2.getId()).getCounterParty(), cp1);
        assertEquals(dao.getTransaction(t3.getId()).getCounterParty(), cp3);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        dao.mergeCounterparties(cp1, cp3, cp2);

        assertEquals(dao.getTransaction(t1.getId()).getCounterParty(), cp2);
        assertEquals(dao.getTransaction(t2.getId()).getCounterParty(), cp2);
        assertEquals(dao.getTransaction(t3.getId()).getCounterParty(), cp2);

        assertFalse(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertFalse(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of deleteTransaction method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testDeleteTransaction_String() {
        LOGGER.info("testDeleteTransaction_String");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));

        dao.deleteTransaction(t1.getId());

        assertFalse(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));
    }

    /**
     * Test of deleteCounterParty method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteCounterParty_String() throws Exception {
        LOGGER.info("deleteCounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);
        dao.add(cp2);
        dao.add(cp3);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        dao.deleteCounterParty(cp2.getId());

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        boolean exceptionThrown = false;
        try {
            dao.deleteCounterParty(cp3.getId());
        } catch (DataIntegrityException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of deleteImage method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testDeleteImage_String() throws CheckImageException, DataIntegrityException {
        LOGGER.info("testDeleteImage_String");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        t2.setImageFront(image2f);
        t2.setImageBack(image2b);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertTrue(dao.containsCheckImage(image3b.getId()));

        assertTrue(image3b.getFile().exists());

        dao.deleteImage(image3b.getId());

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertFalse(dao.containsCheckImage(image3b.getId()));

        assertFalse(image3b.getFile().exists());

        boolean exceptionThrown = false;
        try {
            dao.delete(image1f);
        } catch (DataIntegrityException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test of deleteTransaction method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testDeleteTransaction_Transaction() {
        LOGGER.info("deleteTransaction");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));

        dao.deleteTransaction(t1);

        assertFalse(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));
    }

    /**
     * Test of deleteCounterParty method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteCounterParty_CounterParty() throws Exception {
        LOGGER.info("deleteCounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);
        dao.add(cp2);
        dao.add(cp3);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        dao.deleteCounterParty(cp2);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        boolean exceptionThrown = false;
        try {
            dao.deleteCounterParty(cp3);
        } catch (DataIntegrityException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of deleteImage method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testDeleteImage_CheckImage() throws DataIntegrityException {
        LOGGER.info("testDeleteImage_CheckImage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        t2.setImageFront(image2f);
        t2.setImageBack(image2b);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertTrue(dao.containsCheckImage(image3b.getId()));

        assertTrue(image3b.getFile().exists());

        dao.deleteImage(image3b);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertFalse(dao.containsCheckImage(image3b.getId()));

        assertFalse(image3b.getFile().exists());

        boolean exceptionThrown = false;
        try {
            dao.delete(image1f);
        } catch (DataIntegrityException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test of delete method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testDelete_Transaction() {
        LOGGER.info("delete");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));

        dao.delete(t1);

        assertFalse(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));
    }

    /**
     * Test of delete method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testDelete_CheckImage() throws DataIntegrityException {
        LOGGER.info("testDelete_CheckImage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        t2.setImageFront(image2f);
        t2.setImageBack(image2b);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertTrue(dao.containsCheckImage(image3b.getId()));

        assertTrue(image3b.getFile().exists());

        dao.delete(image3b);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));

        assertTrue(dao.containsCheckImage(image3f.getId()));
        assertFalse(dao.containsCheckImage(image3b.getId()));

        assertFalse(image3b.getFile().exists());

        boolean exceptionThrown = false;
        try {
            dao.delete(image1f);
        } catch (DataIntegrityException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * @throws java.lang.Exception Test of delete method, of class
     * CzekiDaoJsonImpl.
     */
    @Test
    public void testDelete_CounterParty() throws Exception {
        LOGGER.info("testDelete_CounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);
        dao.add(cp2);
        dao.add(cp3);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        dao.delete(cp2);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        boolean exceptionThrown = false;
        try {
            dao.deleteCounterParty(cp3);
        } catch (DataIntegrityException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of deleteAll method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.dao.DataIntegrityException
     */
    @Test
    public void testDeleteAll() throws DataIntegrityException {
        LOGGER.info("testDeleteAll");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);
        dao.add(cp2);
        dao.add(cp3);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        dao.add(image1f);
        dao.add(image1b);
        dao.add(image2f);
        dao.add(image2b);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        assertTrue(dao.getCounterPartyData().contains(cp1));
        assertTrue(dao.getCounterPartyData().contains(cp2));
        assertTrue(dao.getCounterPartyData().contains(cp3));

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));

        assertTrue(dao.getTransactionsData().contains(t1));
        assertTrue(dao.getTransactionsData().contains(t2));
        assertTrue(dao.getTransactionsData().contains(t3));

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));
        assertTrue(dao.containsCheckImage(image2f.getId()));
        assertTrue(dao.containsCheckImage(image2b.getId()));

        assertTrue(dao.getCheckImageData().contains(image1f));
        assertTrue(dao.getCheckImageData().contains(image1b));
        assertTrue(dao.getCheckImageData().contains(image2f));
        assertTrue(dao.getCheckImageData().contains(image2b));

        dao.deleteAll();

        assertFalse(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertFalse(dao.containsCounterParty(cp3.getId()));

        assertFalse(dao.getCounterPartyData().contains(cp1));
        assertFalse(dao.getCounterPartyData().contains(cp2));
        assertFalse(dao.getCounterPartyData().contains(cp3));

        assertFalse(dao.containsTransaction(t1.getId()));
        assertFalse(dao.containsTransaction(t2.getId()));
        assertFalse(dao.containsTransaction(t3.getId()));

        assertFalse(dao.getTransactionsData().contains(t1));
        assertFalse(dao.getTransactionsData().contains(t2));
        assertFalse(dao.getTransactionsData().contains(t3));

        assertFalse(dao.containsCheckImage(image1f.getId()));
        assertFalse(dao.containsCheckImage(image1b.getId()));
        assertFalse(dao.containsCheckImage(image2f.getId()));
        assertFalse(dao.containsCheckImage(image2b.getId()));

        assertFalse(dao.getCheckImageData().contains(image1f));
        assertFalse(dao.getCheckImageData().contains(image1b));
        assertFalse(dao.getCheckImageData().contains(image2f));
        assertFalse(dao.getCheckImageData().contains(image2b));

        assertTrue(dao.getTransactions().entrySet().isEmpty());
        assertTrue(dao.getCounterParties().entrySet().isEmpty());
        assertTrue(dao.getCheckImages().entrySet().isEmpty());

        assertTrue(dao.getTransactionsData().isEmpty());
        assertTrue(dao.getCounterPartyData().isEmpty());
        assertTrue(dao.getCheckImageData().isEmpty());
    }

    /**
     * Test of deleteTransactions method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testDeleteTransactions() {
        LOGGER.info("deleteTransactions");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));

        dao.deleteTransactions();

        assertFalse(dao.containsTransaction(t1.getId()));
        assertFalse(dao.containsTransaction(t2.getId()));
        assertFalse(dao.containsTransaction(t3.getId()));

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertTrue(dao.containsCounterParty(cp3.getId()));
    }

    /**
     * Test of reload method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testReload() {
        LOGGER.info("reload");
        //TODO pass for the time being. Method not implemented yet
    }

    /**
     * Test of reloadImages method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testReloadImages() throws CheckImageException {
        LOGGER.info("testReloadImages");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertFalse(dao.containsCheckImage("C7D17FBA1111200B.jpg"));

        dao.reloadImages();

        assertTrue(dao.containsCheckImage("C7D17FBA1111200B.jpg"));
    }

    /**
     * Test of getTransactionsForCounterparty method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsForCounterparty_String() {
        LOGGER.info("testGetTransactionsForCounterparty_String");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        Map<String, Transaction> result = dao.getTransactionsForCounterparty(cp1.getId());

        assertEquals(result.get(t1.getId()), t1);
        assertEquals(result.get(t2.getId()), t2);
        assertEquals(result.keySet().size(), 2);
    }

    /**
     * Test of getTransactionsForCounterparty method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsForCounterparty_CounterParty() {
        LOGGER.info("testGetTransactionsForCounterparty_CounterParty");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);

        Map<String, Transaction> result = dao.getTransactionsForCounterparty(cp1);

        assertEquals(result.get(t1.getId()), t1);
        assertEquals(result.get(t2.getId()), t2);
        assertEquals(result.keySet().size(), 2);
    }

    /**
     * Test of getByCheckNumber method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetByCheckNumber() throws CheckImageException {
        LOGGER.info("testGetByCheckNumber");

        image1f = new CheckImage(this.imageDirectory,file1f);
        image1b = new CheckImage(this.imageDirectory,file1b);
        image2f = new CheckImage(this.imageDirectory,file2f);
        image2b = new CheckImage(this.imageDirectory,file2b);
        image3f = new CheckImage(this.imageDirectory,file3f);
        image3b = new CheckImage(this.imageDirectory,file3b);
        image4f = new CheckImage(this.imageDirectory,file4f);
        image4b = new CheckImage(this.imageDirectory,file4b);

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        List<CheckImage> result = dao.getByCheckNumber(image1f.getCheckNumber());

        assertTrue(result.size() == 2);
        assertTrue(result.contains(image1f));
        assertTrue(result.contains(image1b));

        assertFalse(result.contains(image2f));
        assertFalse(result.contains(image2b));
    }

    /**
     * Test of getImagesForTransaction method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetImagesForTransaction() throws CheckImageException {
        LOGGER.info("testGetImagesForTransaction");

        image1f = new CheckImage(this.imageDirectory,file1f);
        image1b = new CheckImage(this.imageDirectory,file1b);
        image2f = new CheckImage(this.imageDirectory,file2f);
        image2b = new CheckImage(this.imageDirectory,file2b);
        image3f = new CheckImage(this.imageDirectory,file3f);
        image3b = new CheckImage(this.imageDirectory,file3b);
        image4f = new CheckImage(this.imageDirectory,file4f);
        image4b = new CheckImage(this.imageDirectory,file4b);

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        t2.setImageFront(image2f);
        t2.setImageBack(image2b);

        List<CheckImage> result = dao.getImagesForTransaction(t1);

        assertTrue(result.size() == 2);
        assertTrue(result.contains(image1f));
        assertTrue(result.contains(image1b));

        assertFalse(result.contains(image2f));
        assertFalse(result.contains(image2b));
    }

    /**
     * Test of getByCheckNumberAndSide method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetByCheckNumberAndSide() throws CheckImageException {
        LOGGER.info("testGetByCheckNumberAndSide");

        image1f = new CheckImage(this.imageDirectory,file1f);
        image1b = new CheckImage(this.imageDirectory,file1b);
        image2f = new CheckImage(this.imageDirectory,file2f);
        image2b = new CheckImage(this.imageDirectory,file2b);
        image3f = new CheckImage(this.imageDirectory,file3f);
        image3b = new CheckImage(this.imageDirectory,file3b);
        image4f = new CheckImage(this.imageDirectory,file4f);
        image4b = new CheckImage(this.imageDirectory,file4b);

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        List<CheckImage> result = dao.getByCheckNumberAndSide(image1f.getCheckNumber(), CzekImageUtils.FRONT);

        assertTrue(result.size() == 1);
        assertTrue(result.contains(image1f));
        assertFalse(result.contains(image1b));

        assertFalse(result.contains(image2f));
        assertFalse(result.contains(image2b));
    }

    /**
     * Test of getBySubstring method, of class CzekiDaoJsonImpl.
     *
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testGetBySubstring() throws CheckImageException {
        LOGGER.info("testGetBySubstring");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        String substring1 = "C7D1";

        List<CheckImage> result = dao.getBySubstring(substring1);

        assertTrue(result.size() == 4);
        assertTrue(result.contains(image1f));
        assertTrue(result.contains(image1b));
        assertTrue(result.contains(image2f));
        assertTrue(result.contains(image2b));

        assertFalse(result.contains(image3f));
        assertFalse(result.contains(image3b));
    }

    /**
     * Test of inStorage method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testInStorage() {
        LOGGER.info("testInStorage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertTrue(dao.inStorage(image1f));

        image1f.getFile().delete();

        assertFalse(dao.inStorage(image1f));
    }

    /**
     * Test of exportToFile method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testExportToFile() throws Exception {
        LOGGER.info("testExportToFile");
        //TODO we pass this test for the time being, add real write to file test later
    }

    /**
     * Test of backupToCsvFile method, of class CzekiDaoJsonImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testBackupToCsvFile() throws IOException {
        LOGGER.info("testBackupToCsvFile");
        //TODO we pass this test for the time being, add real write to file test later
    }

    /**
     * Test of load method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoad() throws Exception {
        LOGGER.info("load");
        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);

        dao.commit();

        CzekiDaoJsonImpl dao2 = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao2.load();

        assertTrue(dao.containsTransaction(t1.getId()));
        assertTrue(dao.containsTransaction(t2.getId()));
        assertTrue(dao.containsTransaction(t3.getId()));
        assertTrue(dao.containsTransaction(t4.getId()));
        assertFalse(dao.containsTransaction(t5.getId()));
    }

    /**
     * Test of loadCheckImages method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadCheckImages() throws Exception {
        LOGGER.info("testLoadCheckImages");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        TomwFileUtils.createFile(file8);

        File fileInStorage = new File(this.imageDirectory, file8.getName());

        System.out.println("file8=" + file8.getCanonicalPath());
        System.out.println("fileInStorage=" + fileInStorage.getCanonicalPath());
        System.out.println("fileInStorage.exists()=" + fileInStorage.exists());

        assertFalse(fileInStorage.exists());

        CheckImage image8 = dao.addNewImage(file8);

        assertTrue(dao.containsCheckImage(image8.getId()));

        assertTrue(fileInStorage.exists());

        assertTrue(dao.inStorage(image8));

        JSONObject json = dao.toJson();

        CzekiDaoJsonImpl dao2 = new CzekiDaoJsonImpl(this.dataFile2, this.imageDirectory);

        assertFalse(dao2.containsCheckImage(image8.getId()));

        dao2.loadCheckImages(json);

        assertTrue(dao2.containsCheckImage(image8.getId()));
    }

    /**
     * Test of loadCounterParties method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadCounterParties() throws Exception {
        LOGGER.info("loadCounterParties");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));

        CzekiDaoJsonImpl dao2 = new CzekiDaoJsonImpl(this.dataFile2, this.imageDirectory);

        assertFalse(dao2.containsCounterParty(cp1.getId()));
        assertFalse(dao2.containsCounterParty(cp2.getId()));

        dao2.loadCounterParties(dao.toJson());

        assertTrue(dao2.containsCounterParty(cp1.getId()));
        assertFalse(dao2.containsCounterParty(cp2.getId()));
    }

    /**
     * Test of loadTransactions method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testLoadTransactions() throws CheckImageException {
        LOGGER.info("testLoadTransactions");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);

        assertTrue(dao.containsTransaction(t1.getId()));
        assertFalse(dao.containsTransaction(t2.getId()));

        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));

        assertTrue(dao.getTransactionsData().contains(t1));
        assertFalse(dao.getTransactionsData().contains(t2));
        assertFalse(dao.getTransactionsData().contains(t3));

        CzekiDaoJsonImpl dao2 = new CzekiDaoJsonImpl(this.dataFile2, this.imageDirectory);

        assertFalse(dao2.containsTransaction(t1.getId()));
        assertFalse(dao2.containsTransaction(t2.getId()));
        assertFalse(dao2.containsTransaction(t3.getId()));

        assertFalse(dao2.containsCounterParty(cp1.getId()));
        assertFalse(dao2.containsCounterParty(cp1.getId()));

        assertFalse(dao2.getTransactionsData().contains(t1));
        assertFalse(dao2.getTransactionsData().contains(t2));
        assertFalse(dao2.getTransactionsData().contains(t3));

        JSONObject json = dao.toJson();

        dao2.loadCounterParties(json);
        dao2.loadTransactions(json);

        assertTrue(dao2.containsTransaction(t1.getId()));
        assertFalse(dao2.containsTransaction(t2.getId()));
        assertFalse(dao2.containsTransaction(t3.getId()));

        assertTrue(dao2.containsCounterParty(cp1.getId()));
        assertFalse(dao2.containsCounterParty(cp2.getId()));

        assertTrue(dao2.transactionsDataContains(t1.getId()));
        assertFalse(dao2.transactionsDataContains(t2.getId()));
        assertFalse(dao2.transactionsDataContains(t3.getId()));

    }

    /**
     * Test of addToListIfNotInList method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testAddToListIfNotInList() {
        LOGGER.info("testAddToListIfNotInList");

        ObservableList<Transaction> list = FXCollections.observableArrayList();

        CzekiDaoJsonImpl.addToListIfNotInList(list, t1);
        assertTrue(list.size() == 1);
        CzekiDaoJsonImpl.addToListIfNotInList(list, t1);
        assertTrue(list.size() == 1);
        CzekiDaoJsonImpl.addToListIfNotInList(list, t2);
        assertTrue(list.size() == 2);
    }

    /**
     * Test of transactionInList method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testTransactionInList() {
        LOGGER.info("transactionInList");
        ObservableList<Transaction> data = FXCollections.observableArrayList();

        CzekiDaoJsonImpl.addToListIfNotInList(data, t1);
        assertTrue(data.contains(t1));
        assertFalse(data.contains(t2));
        CzekiDaoJsonImpl.addToListIfNotInList(data, t2);
        assertTrue(data.contains(t1));
        assertTrue(data.contains(t2));
    }

    /**
     * Test of writeJsonToFile method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteJsonToFile() throws Exception {
        LOGGER.info("writeJsonToFile");

        JSONObject json = new JSONObject();
        JSONArray transactions = new JSONArray();
        JSONArray counterparties = new JSONArray();
        JSONArray images = new JSONArray();

        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);

        counterparties.add(cp1);
        counterparties.add(cp2);
        counterparties.add(cp3);
        counterparties.add(cp4);

        json.put(CzekiDaoJsonImpl.TRANSACTIONS, transactions);
        json.put(CzekiDaoJsonImpl.COUNTERPARTIES, counterparties);

        CzekiDaoJsonImpl.writeJsonToFile(dataFile, json);

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        JSONObject json2 = dao.loadDataJson(dataFile);

        assertTrue(json.toString().equals(json2.toString()));
    }

    /**
     * Test of loadDataJson method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadDataJson() throws Exception {
        LOGGER.info("loadDataJson");
        testWriteJsonToFile();
    }

    /**
     * Test of getTransactionForCheckImage method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionForCheckImage() {
        LOGGER.info("testGetTransactionForCheckImage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);

        dao.add(image1f);
        dao.add(image2f);
        dao.add(image3f);

        dao.add(image1b);
        dao.add(image2b);
        dao.add(image3b);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        t2.setImageFront(image2f);
        t2.setImageBack(image2b);

        Transaction result = dao.getTransactionForCheckImage(image1f);

        assertEquals(result, t1);

        Transaction noSuchTransaction = dao.getTransactionForCheckImage(image3b);
        assertEquals(noSuchTransaction, null);
    }

    /**
     * Test of addCounterPartyToListIfNotInList method, of class
     * CzekiDaoJsonImpl.
     */
    @Test
    public void testAddCounterPartyToListIfNotInList() {
        LOGGER.info("testAddCounterPartyToListIfNotInList");
        ObservableList<CounterParty> counterpartiesData = FXCollections.observableArrayList();

        CzekiDaoJsonImpl.addCounterPartyToListIfNotInList(counterpartiesData, cp1);
        assertTrue(counterpartiesData.size() == 1);
        CzekiDaoJsonImpl.addCounterPartyToListIfNotInList(counterpartiesData, cp1);
        assertTrue(counterpartiesData.size() == 1);
        CzekiDaoJsonImpl.addCounterPartyToListIfNotInList(counterpartiesData, cp2);
        assertTrue(counterpartiesData.size() == 2);
    }

    /**
     * Test of counterpartyInList method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testCounterpartyInList() {
        LOGGER.info("testCounterpartyInList");
        ObservableList<CounterParty> counterpartiesData = FXCollections.observableArrayList();

        CzekiDaoJsonImpl.addCounterPartyToListIfNotInList(counterpartiesData, cp1);
        assertTrue(CzekiDaoJsonImpl.counterpartyInList(counterpartiesData, cp1));
        assertFalse(CzekiDaoJsonImpl.counterpartyInList(counterpartiesData, cp2));

        CzekiDaoJsonImpl.addCounterPartyToListIfNotInList(counterpartiesData, cp2);
        assertTrue(CzekiDaoJsonImpl.counterpartyInList(counterpartiesData, cp1));
        assertTrue(CzekiDaoJsonImpl.counterpartyInList(counterpartiesData, cp2));
    }

    /**
     * Test of addNewImage method, of class CzekiDaoJsonImpl.
     *
     * @throws java.io.IOException
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testAddNewImage_File() throws IOException, CheckImageException {
        LOGGER.info("addNewImage");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        TomwFileUtils.createFile(file8);

        File fileInStorage = new File(this.imageDirectory, file8.getName());

        assertFalse(fileInStorage.exists());

        CheckImage image8 = dao.addNewImage(file8);

        assertTrue(dao.containsCheckImage(image8.getId()));

        assertTrue(fileInStorage.exists());

        assertTrue(dao.inStorage(image8));
    }

    /**
     * Test of addNewImage method, of class CzekiDaoJsonImpl.
     *
     * @throws java.io.IOException
     * @throws org.tomw.czeki.imageview.CheckImageException
     */
    @Test
    public void testAddNewImage_File_boolean() throws IOException, CheckImageException {
        LOGGER.info("testAddNewImage_File_boolean");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        TomwFileUtils.createFile(file8);

        assertTrue(file8.exists());

        File fileInStorage = new File(this.imageDirectory, file8.getName());

        assertFalse(fileInStorage.exists());

        CheckImage image8 = dao.addNewImage(file8, true);

        assertFalse(file8.exists());

        assertTrue(dao.containsCheckImage(image8.getId()));

        assertTrue(fileInStorage.exists());

        assertTrue(dao.inStorage(image8));
    }

    /**
     * Test of addCheckImageToListIfNotInList method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testAddCheckImageToListIfNotInList() {
        LOGGER.info("testAddCheckImageToListIfNotInList");

        ObservableList<CheckImage> observableList = FXCollections.observableArrayList();

        CzekiDaoJsonImpl.addCheckImageToListIfNotInList(observableList, image1f);
        assertTrue(observableList.size() == 1);
        CzekiDaoJsonImpl.addCheckImageToListIfNotInList(observableList, image1f);
        assertTrue(observableList.size() == 1);
        CzekiDaoJsonImpl.addCheckImageToListIfNotInList(observableList, image2f);
        assertTrue(observableList.size() == 2);
    }

    /**
     * Test of checkImageInListInList method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testCheckImageInListInList() {
        LOGGER.info("testCheckImageInListInList");

        ObservableList<CheckImage> list = FXCollections.observableArrayList();
        list.add(image1f);
        list.add(image2f);

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertTrue(CzekiDaoJsonImpl.checkImageInListInList(list, image1f));
        assertTrue(CzekiDaoJsonImpl.checkImageInListInList(list, image2f));
        assertFalse(CzekiDaoJsonImpl.checkImageInListInList(list, image3f));
    }

    /**
     * Test of getCheckImages method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetCheckImages() {
        LOGGER.info("getCheckImages");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);

        dao.add(image1f);
        dao.add(image2f);

        dao.add(image1b);
        dao.add(image2b);

        assertTrue(dao.getCheckImages().get(image1f.getId()).equals(image1f));
        assertTrue(dao.getCheckImages().get(image3b.getId()) == null);
    }

    /**
     * Test of deleteCounterParties method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteCounterParties() throws Exception {
        LOGGER.info("testDeleteCounterParties");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(cp1);
        dao.add(cp2);
        assertTrue(dao.containsCounterParty(cp1.getId()));
        assertTrue(dao.containsCounterParty(cp2.getId()));
        assertFalse(dao.containsCounterParty(cp3.getId()));

        dao.deleteCounterParties();

        assertFalse(dao.containsCounterParty(cp1.getId()));
        assertFalse(dao.containsCounterParty(cp2.getId()));
        assertFalse(dao.containsCounterParty(cp3.getId()));

        dao.add(t1);
        dao.add(t2);

        boolean exceptionThrown = false;
        try {
            dao.deleteCounterParties();
        } catch (DataIntegrityException dataIntegrityException) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test of deleteCheckImages method, of class CzekiDaoJsonImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteCheckImages() throws Exception {
        LOGGER.info("deleteCheckImages");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(image1f);
        dao.add(image1b);

        assertTrue(dao.containsCheckImage(image1f.getId()));
        assertTrue(dao.containsCheckImage(image1b.getId()));
        assertFalse(dao.containsCheckImage(image2f.getId()));

        dao.deleteCheckImages();

        assertFalse(dao.containsCheckImage(image1f.getId()));
        assertFalse(dao.containsCheckImage(image1b.getId()));
        assertFalse(dao.containsCheckImage(image2f.getId()));

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        dao.add(t1);
        dao.add(t2);

        boolean exceptionThrown = false;
        try {
            dao.deleteCheckImages();
        } catch (DataIntegrityException dataIntegrityException) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test of toJson method, of class CzekiDaoJsonImpl.
     *
     * @throws org.json.simple.parser.ParseException
     */
    @Test
    public void testToJson() throws ParseException {
        LOGGER.info("toJson");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        LocalDate date = LocalDate.parse("2016-06-16");
        t1.setTransactionDate(date);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);
        t1.setCounterParty(cp1);

        dao.add(t1);

        JSONObject result = dao.toJson();

        System.out.println("result=" + result);

        String expectedString = "{\"Transactions\":[{\"Comment\":\"comment1\",\"Transaction amount\":5.0,\"Counterparty id\":\"id_1\",\"Cleared\":\"\",\"CheckImageFront\":{\"File\":\"C7D17FBA1111200F.jpg\"},\"CheckImageBack\":{\"File\":\"C7D17FBA1111200B.jpg\"},\"id\":\"test_id_1\",\"Check Number\":\"1111\",\"Date\":\"2016-06-16\",\"Memo\":\"memo1\"}],\"Counterparties\":[{\"Short Name\":\"short name 1\",\"Other Names\":[\"a1\",\"b1\",\"c1\"],\"Comment\":\"\",\"name\":\"name 1\",\"id\":\"id_1\"}],\"Images\":[{\"File\":\"C7D17FBA1111200B.jpg\"},{\"File\":\"C7D17FBA1111200F.jpg\"}],\"MostRecentCheckNumber\":null}\n"
                + "";
        JSONParser jsonParser = new JSONParser();

        JSONObject expectedJson = (JSONObject) jsonParser.parse(expectedString);

        assertEquals(expectedJson, result);
    }

    /**
     * Test of transactionsDataContains method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testTransactionsDataContains() {
        LOGGER.info("testTransactionsDataContains");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);

        assertTrue(dao.transactionsDataContains(t1.getId()));
        assertTrue(dao.transactionsDataContains(t2.getId()));
        assertFalse(dao.transactionsDataContains(t3.getId()));
    }

    /**
     * Test of toCsv method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testToCsv() {
        LOGGER.info("toCsv");
        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        t1.setImageFront(image1f);
        t1.setImageBack(image1b);

        dao.add(t1);
        dao.add(t2);

        String result = dao.toCsv();
        System.out.println("result=" + result);
    }

    @Test
    public void testGetClearedTransactions() {
        LOGGER.info("getClearedTransactions");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        this.t1.setCleared(true);
        this.t2.setCleared(false);
        this.t3.setCleared(true);
        this.t4.setCleared(false);
        this.t5.setCleared(false);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);
        dao.add(t5);

        Collection<Transaction> clearedTransactions = dao.getClearedTransactions();

        assertTrue(clearedTransactions.contains(this.t1));
        assertTrue(clearedTransactions.contains(this.t3));

        assertFalse(clearedTransactions.contains(this.t2));
        assertFalse(clearedTransactions.contains(this.t4));
        assertFalse(clearedTransactions.contains(this.t5));
    }

    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData_0args() {
        LOGGER.info("getTransactionsData");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);
        dao.add(t5);

        ObservableList<Transaction> expResult = FXCollections.observableArrayList();
        expResult.add(t1);
        expResult.add(t2);
        expResult.add(t3);
        expResult.add(t4);
        expResult.add(t5);

        ObservableList<Transaction> result = dao.getTransactionsData();

        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData_List() {
        LOGGER.info("getTransactionsData");

        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);
        dao.add(t5);

        ObservableList<Transaction> expResult = FXCollections.observableArrayList();
        expResult.add(t3);

        List<CounterParty> listOfCounterParties = new ArrayList<>();
        listOfCounterParties.add(cp3);

        ObservableList<Transaction> result = dao.getTransactionsData(listOfCounterParties);

        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData_Set() {
        System.out.println("getTransactionsData");
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1);
        dao.add(t2);
        dao.add(t3);
        dao.add(t4);
        dao.add(t5);

        ObservableList<Transaction> expResult = FXCollections.observableArrayList();
        expResult.add(t3);

        Set<CounterParty> setOfCounterParties = new HashSet<>();
        setOfCounterParties.add(cp3);

        ObservableList<Transaction> result = dao.getTransactionsData(setOfCounterParties);

        assertEquals(expResult, result);
    }
    
    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData_3args_1() {
        LOGGER.info("getTransactionsData");

        Transaction t1a = new Transaction("test_id_1", LocalDate.of(2017, Month.MARCH, 19), 5.0, "1111", cp1, "memo1", "comment1");
        Transaction t2a = new Transaction("test_id_2", LocalDate.of(2017, Month.FEBRUARY, 19), 5.0, "2222", cp1, "memo2", "comment2");
        Transaction t3a = new Transaction("test_id_3", LocalDate.of(2017, Month.JANUARY, 19), 5.0, "3333", cp3, "memo3", "comment3");
        Transaction t4a = new Transaction("test_id_4", LocalDate.of(2016, Month.DECEMBER, 19), 5.0, "4444", cp4, "memo4", "comment4");
        Transaction t5a = new Transaction("test_id_5", LocalDate.of(2016, Month.NOVEMBER, 19), 5.0, "5555", cp4, "memo5", "comment5");
        
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1a);
        dao.add(t2a);
        dao.add(t3a);
        dao.add(t4a);
        dao.add(t5a);
        
        // query returns result
        LocalDate startDate1 = LocalDate.of(2017, Month.FEBRUARY, 1);
        LocalDate endDate1 = LocalDate.of(2017, Month.MARCH, 1);
        List<CounterParty> listOfCounterParties1 = new ArrayList<>();
        listOfCounterParties1.add(cp1);
        ObservableList<Transaction> expResult1 = FXCollections.observableArrayList();
        expResult1.add(t2a);
        ObservableList<Transaction> result1 = dao.getTransactionsData(listOfCounterParties1, startDate1, endDate1);
        assertEquals(expResult1, result1);
        
        // query returns empty list
        LocalDate startDate2 = LocalDate.of(2017, Month.FEBRUARY, 1);
        LocalDate endDate2 = LocalDate.of(2017, Month.MARCH, 1);
        List<CounterParty> listOfCounterParties2 = new ArrayList<>();
        listOfCounterParties2.add(cp3);
        ObservableList<Transaction> expResult2 = FXCollections.observableArrayList();
        ObservableList<Transaction> result2 = dao.getTransactionsData(listOfCounterParties2, startDate2, endDate2);
        assertEquals(expResult2, result2);

        // start date is null
        LocalDate startDate3 = null;
        LocalDate endDate3 = LocalDate.of(2016, Month.DECEMBER, 1);
        List<CounterParty> listOfCounterParties3 = new ArrayList<>();
        listOfCounterParties3.add(cp4);
        ObservableList<Transaction> expResult3 = FXCollections.observableArrayList();
        expResult3.add(t5a);
        ObservableList<Transaction> result3 = dao.getTransactionsData(listOfCounterParties3, startDate3, endDate3);
        assertEquals(expResult3, result3);
        
        // end date is null
        LocalDate startDate4 = LocalDate.of(2016, Month.DECEMBER, 1);
        LocalDate endDate4 = null;
        List<CounterParty> listOfCounterParties4 = new ArrayList<>();
        listOfCounterParties4.add(cp4);
        ObservableList<Transaction> expResult4 = FXCollections.observableArrayList();
        expResult4.add(t4a);
        ObservableList<Transaction> result4 = dao.getTransactionsData(listOfCounterParties4, startDate4, endDate4);
        assertEquals(expResult4, result4);
        
        //both start and end dates are null
        LocalDate startDate5 = null;
        LocalDate endDate5 = null;
        List<CounterParty> listOfCounterParties5 = new ArrayList<>();
        listOfCounterParties5.add(cp4);
        ObservableList<Transaction> expResult5 = FXCollections.observableArrayList();
        expResult5.add(t4a);
        expResult5.add(t5a);
        ObservableList<Transaction> result5 = dao.getTransactionsData(listOfCounterParties5, startDate5, endDate5);
        assertEquals(expResult5, result5);
    }

    /**
     * Test of getTransactionsData method, of class CzekiDaoJsonImpl.
     */
    @Test
    public void testGetTransactionsData_3args_2() {
        LOGGER.info("getTransactionsData");
        
        Transaction t1a = new Transaction("test_id_1", LocalDate.of(2017, Month.MARCH, 19), 5.0, "1111", cp1, "memo1", "comment1");
        Transaction t2a = new Transaction("test_id_2", LocalDate.of(2017, Month.FEBRUARY, 19), 5.0, "2222", cp1, "memo2", "comment2");
        Transaction t3a = new Transaction("test_id_3", LocalDate.of(2017, Month.JANUARY, 19), 5.0, "3333", cp3, "memo3", "comment3");
        Transaction t4a = new Transaction("test_id_4", LocalDate.of(2016, Month.DECEMBER, 19), 5.0, "4444", cp4, "memo4", "comment4");
        Transaction t5a = new Transaction("test_id_5", LocalDate.of(2016, Month.NOVEMBER, 19), 5.0, "5555", cp4, "memo5", "comment5");
        
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        dao.add(t1a);
        dao.add(t2a);
        dao.add(t3a);
        dao.add(t4a);
        dao.add(t5a);
        
        // query returns result
        LocalDate startDate1 = LocalDate.of(2017, Month.FEBRUARY, 1);
        LocalDate endDate1 = LocalDate.of(2017, Month.MARCH, 1);
        Set<CounterParty> setOfCounterParties1 = new HashSet<>();
        setOfCounterParties1.add(cp1);
        ObservableList<Transaction> expResult1 = FXCollections.observableArrayList();
        expResult1.add(t2a);
        ObservableList<Transaction> result1 = dao.getTransactionsData(setOfCounterParties1, startDate1, endDate1);
        assertEquals(expResult1, result1);
        
        // query returns empty list
        LocalDate startDate2 = LocalDate.of(2017, Month.FEBRUARY, 1);
        LocalDate endDate2 = LocalDate.of(2017, Month.MARCH, 1);
        Set<CounterParty> setOfCounterParties2 = new HashSet<>();
        setOfCounterParties2.add(cp3);
        ObservableList<Transaction> expResult2 = FXCollections.observableArrayList();
        ObservableList<Transaction> result2 = dao.getTransactionsData(setOfCounterParties2, startDate2, endDate2);
        assertEquals(expResult2, result2);

        // start date is null
        LocalDate startDate3 = null;
        LocalDate endDate3 = LocalDate.of(2016, Month.DECEMBER, 1);
        Set<CounterParty> setOfCounterParties3 = new HashSet<>();
        setOfCounterParties3.add(cp4);
        ObservableList<Transaction> expResult3 = FXCollections.observableArrayList();
        expResult3.add(t5a);
        ObservableList<Transaction> result3 = dao.getTransactionsData(setOfCounterParties3, startDate3, endDate3);
        assertEquals(expResult3, result3);
        
        // end date is null
        LocalDate startDate4 = LocalDate.of(2016, Month.DECEMBER, 1);
        LocalDate endDate4 = null;
        Set<CounterParty> setOfCounterParties4 = new HashSet<>();
        setOfCounterParties4.add(cp4);
        ObservableList<Transaction> expResult4 = FXCollections.observableArrayList();
        expResult4.add(t4a);
        ObservableList<Transaction> result4 = dao.getTransactionsData(setOfCounterParties4, startDate4, endDate4);
        assertEquals(expResult4, result4);
        
        //both start and end dates are null
        LocalDate startDate5 = null;
        LocalDate endDate5 = null;
        Set<CounterParty> setOfCounterParties5 = new HashSet<>();
        setOfCounterParties5.add(cp4);
        ObservableList<Transaction> expResult5 = FXCollections.observableArrayList();
        expResult5.add(t4a);
        expResult5.add(t5a);
        ObservableList<Transaction> result5 = dao.getTransactionsData(setOfCounterParties5, startDate5, endDate5);
        assertEquals(expResult5, result5);
    }

    @Test
    public void test_setMostRecentCheckNumber() throws IOException {
        testSetAndGetMostRecentCheckNumber();
    }

    @Test
    public void test_getMostRecentCheckNumber() throws IOException {
        testSetAndGetMostRecentCheckNumber();
    }

    private void testSetAndGetMostRecentCheckNumber() throws IOException {
        CzekiDao dao = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertEquals(null, dao.getMostRecentCheckNumber());

        dao.setMostRecentCheckNumber("4567");

        assertEquals("4567", dao.getMostRecentCheckNumber());

        dao.commit();

        CzekiDao dao2 = new CzekiDaoJsonImpl(this.dataFile, this.imageDirectory);

        assertEquals("4567", dao2.getMostRecentCheckNumber());

    }

    @Test
    public void test_RenameSimpleFormatFiles() throws Exception {
        System.out.println("test_RenameSimpleFormatFiles");

        File testFile1 = new File(imageDirectory,"1234F.jpg");
        File testFile2 = new File(imageDirectory,"1234B.jpg");

        File resultFile1 = new File(imageDirectory,"2018-01-27-1234-F.jpg");
        File resultFile2 = new File(imageDirectory,"2018-01-27-1234-B.jpg");

        LocalDate date = LocalDate.of(2018,01,27);

        PrintWriter out = new PrintWriter(testFile1);
        out.write("test file1");
        out.close();

        PrintWriter out2 = new PrintWriter(testFile2);
        out2.write("test file2");
        out2.close();

        assertTrue(testFile1.exists());
        assertTrue(testFile2.exists());

        assertFalse(resultFile1.exists());
        assertFalse(resultFile2.exists());

        CzekiDaoJsonImpl.renameSimpleFormatFiles(imageDirectory,date);

        assertFalse(testFile1.exists());
        assertFalse(testFile2.exists());

        assertTrue(resultFile1.exists());
        assertTrue(resultFile2.exists());
    }


}
