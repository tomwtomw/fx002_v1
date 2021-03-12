/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tomw.czeki.entities;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.tomw.czeki.dao.CzekiDaoJsonImpl;
import org.tomw.utils.LocalDateUtils;

/**
 *
 * @author tomw
 */
public class TransactionParserTest {
    private final static Logger LOGGER = Logger.getLogger(TransactionParserTest.class.getName());

    File file;
    String fileName = "test_file";
    
    File imageDirectory;
    String imageDirectoryString = "images";
    

//    private final String line01 = "5/31/2011	182.88		starting balance on checking	comment1	memo 1		";
//    private final String line02 = "5/31/2011	-721.02		john smith		comment 2	memo 2";
//    private final String line03 = "5/31/2011	6043.31		att";
//    private final String line04 = "5/31/2011	-1.39		loan interest	comment 3	memo3";
//    private final String line05 = "5/31/2011	-20		atm main street		memo 4";
//    private final String line06 = "6/1/2011	-20		atm main street		memo 5";
//    private final String line07 = "6/1/2011	-21	1234	chase	comment 7";
//    private final String line08 = "6/1/2011	-21	1235	chase";
//    private final String line09 = "6/1/2011	-90	1236	john smith";
//    private final String line10 = "6/1/2011	-20	1237	gas";
//    private final String line11 = "6/2/2011	-2070	1238	pseg";
//    private final String line12 = "6/2/2011	-14.5	9999	insurance";
//    private final String line13 = "6/2/2011	1066.33		cablevision";
  
    private final String line01 = "5/31/2011\t182.88\t\t\tstarting balance on checking	comment1	memo 1		";
    private final String line02 = "5/31/2011\t-721.02\t\t\t\tjohn smith\tcomment 2\tmemo 2";
    private final String line03 = "5/31/2011\t6043.31\t\t\tatt";
    private final String line04 = "5/31/2011\t-1.39\t\t\tloan interest\tcomment 3\tmemo3";
    private final String line05 = "5/31/2011\t-20\t\t\tatm main street\t\tmemo 4";
    private final String line06 = "6/1/2011\t-20\t\t\tatm main street\t\tmemo 5";
    private final String line07 = "6/1/2011\t-21\t1234\t\tchase\tcomment 7\t";
    private final String line08 = "6/1/2011\t-21\t1235\t\tchase";
    private final String line09 = "6/1/2011\t-90\t1236\ty\tjohn smith";
    private final String line10 = "6/1/2011\t-20\t1237\ty\tgas\tcomment 10\tmemo10";
    private final String line11 = "6/2/2011\t-2070\t1238\t\tpseg\t\tmemo11";
    private final String line12 = "6/2/2011\t-14.5\t9999\t\tinsurance";
    private final String line13 = "6/2/2011\t1066.33\t\t\tcablevision";
    
    
    private final JSONParser jsonParser = new JSONParser();

    public TransactionParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.file = new File(this.fileName);
        if (file.exists()) {
            file.delete();
        }
        this.imageDirectory = new File(imageDirectoryString);
        if(this.imageDirectory.exists()){
            this.imageDirectory.delete();
        }
        this.imageDirectory.mkdir();
        
    }

    @After
    public void tearDown() {
        if (file.exists()) {
            file.delete();
        }
        if(this.imageDirectory.exists()){
            this.imageDirectory.delete();
        }
    }

    /**
     * Test of buildTransaction method, of class TransactionParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testBuildTransaction_String_CzekiDao() throws Exception {
        LOGGER.info("buildTransaction testBuildTransaction_String_CzekiDao");

        CzekiDaoJsonImpl dao = new CzekiDaoJsonImpl(file, imageDirectory) ;

        Transaction t1 = TransactionParser.buildTransaction(line01, dao);

        assertEquals(t1.getCheckNumber(), "");
        assertEquals(t1.getComment(), "comment1");
        assertEquals(t1.getMemo(), "memo 1");
        assertEquals(new Double(182.88), t1.getTransactionAmount());
        assertEquals(t1.getTransactionDate(), LocalDateUtils.fromString("5/31/2011"));
        assertEquals(t1.getCounterParty().getShortName(), "starting balance on checking");
        assertFalse(t1.isCleared());

        Transaction t5 = TransactionParser.buildTransaction(line05, dao);
        assertEquals(t5.getCheckNumber(), "");
        assertEquals(t5.getComment(), "");
        assertEquals(t5.getMemo(), "memo 4");
        assertEquals(new Double(-20), t5.getTransactionAmount());
        assertEquals(t5.getTransactionDate(), LocalDateUtils.fromString("5/31/2011"));
        assertEquals(t5.getCounterParty().getShortName(), "atm main street");
        assertFalse(t5.isCleared());

        Transaction t6 = TransactionParser.buildTransaction(line06, dao);
        assertEquals(t6.getCheckNumber(), "");
        assertEquals(t6.getComment(), "");
        assertEquals(t6.getMemo(), "memo 5");
        assertEquals(new Double(-20), t6.getTransactionAmount());
        assertEquals(t6.getTransactionDate(), LocalDateUtils.fromString("6/1/2011"));
        assertEquals(t6.getCounterParty().getShortName(), "atm main street");
        assertFalse(t6.isCleared());

        //the counterparty for t5 and t6 is the same, so for now there should be 2 counterparties total
        assertEquals(2, dao.getCounterParties().size());

        Transaction t7 = TransactionParser.buildTransaction(line07, dao);
        assertEquals(t7.getCheckNumber(), "1234");
        assertEquals(t7.getComment(), "comment 7");
        assertEquals(t7.getMemo(), "");
        assertEquals(new Double(-21), t7.getTransactionAmount());
        assertEquals(t7.getTransactionDate(), LocalDateUtils.fromString("6/1/2011"));
        assertEquals(t7.getCounterParty().getShortName(), "chase");
        assertFalse(t7.isCleared());

        Transaction t8 = TransactionParser.buildTransaction(line08, dao);
        assertEquals(t8.getCheckNumber(), "1235");
        assertEquals(t8.getComment(), "");
        assertEquals(t8.getMemo(), "");
        assertEquals(new Double(-21), t8.getTransactionAmount());
        assertEquals(t8.getTransactionDate(), LocalDateUtils.fromString("6/1/2011"));
        assertEquals(t8.getCounterParty().getShortName(), "chase");
        assertFalse(t8.isCleared());
        
        Transaction t9 = TransactionParser.buildTransaction(line09, dao);
        assertEquals(t9.getCheckNumber(), "1236");
        assertEquals(t9.getComment(), "");
        assertEquals(t9.getMemo(), "");
        assertEquals(new Double(-90), t9.getTransactionAmount());
        assertEquals(t9.getTransactionDate(), LocalDateUtils.fromString("6/1/2011"));
        assertEquals(t9.getCounterParty().getShortName(), "john smith");
        assertTrue(t9.isCleared());
        
        Transaction t10 = TransactionParser.buildTransaction(line10, dao);
        assertEquals(t10.getCheckNumber(), "1237");
        assertEquals(t10.getComment(), "comment 10");
        assertEquals(t10.getMemo(), "memo10");
        assertEquals(new Double(-20), t10.getTransactionAmount());
        assertEquals(t10.getTransactionDate(), LocalDateUtils.fromString("6/1/2011"));
        assertEquals(t10.getCounterParty().getShortName(), "gas");
        assertTrue(t10.isCleared());        
        
        Transaction t11 = TransactionParser.buildTransaction(line11, dao);
        assertEquals(t11.getCheckNumber(), "1238");
        assertEquals(t11.getComment(), "");
        assertEquals(t11.getMemo(), "memo11");
        assertEquals(new Double(-2070), t11.getTransactionAmount());
        assertEquals(t11.getTransactionDate(), LocalDateUtils.fromString("6/2/2011"));
        assertEquals(t11.getCounterParty().getShortName(), "pseg");
        assertFalse(t11.isCleared());        
        
    }

    @Test
    public void test_extractDateFromLine(){
        LocalDate expected05 = LocalDate.of(2011, 5, 31);
        LocalDate expected06 = LocalDate.of(2011, 6,  1);
        
        LocalDate actual05 = TransactionParser.extractDateFromLine(line05);
        LocalDate actual06 = TransactionParser.extractDateFromLine(line06);
        
        assertTrue(expected05.equals(actual05));
        assertTrue(expected06.equals(actual06));
    }
    
    @Test
    public void test_linesAreInconsistent(){
        List<String> consistentList=new ArrayList<>();
        consistentList.add(line01);
        consistentList.add(line06);
        consistentList.add(line07);
        consistentList.add(line13);
                
        List<String> inconsistentList=new ArrayList<>();
        inconsistentList.add(line01);
        inconsistentList.add(line06);
        inconsistentList.add(line13);
        inconsistentList.add(line07);
        
        assertTrue(TransactionParser.linesAreInconsistent(inconsistentList));
        assertFalse(TransactionParser.linesAreInconsistent(consistentList));
    }
    
//    @Test
//    public void testBuildTransaction_With_CheckImage() throws Exception {
//        
//        CzekiDaoFileImpl dao = new CzekiDaoFileImpl(this.store);
//        
//        CounterParty counterParty1=new CounterParty();
//        CounterParty counterParty2=new CounterParty();
//        
//        dao.add(counterParty1);
//        dao.add(counterParty2);
//        
//        TwoSidedCheckImage checkImage1 = new TwoSidedCheckImage();
//
//        String expFront = "frontPage";
//        String expBack = "backPage";
//        TwoSidedCheckImage checkImage2 = new TwoSidedCheckImage(expFront, expBack);
//        
//        String expected_id1 = "99e3dd67-08c0-40a9-a1b0-30a43b23073f";
//        String expected_id2 = "99e3dd67-08c0-40a9-a1b0-22222222222f";
//        
//        Transaction transaction1 = new Transaction(expected_id1, LocalDate.now(), 5.0, "1234", counterParty1, "memo", "comment");
//        Transaction transaction2 = new Transaction(expected_id2, LocalDate.now(), 5.0, "2222", counterParty2, "memo", "comment");
//        
//        transaction2.setCheckImage(checkImage2);
//        
//        String transaction2AsString = transaction2.toJson().toString();
//        JSONObject reconstructedJson1 = (JSONObject)jsonParser.parse(transaction2AsString);
//        Transaction reconstructedTransaction2 = TransactionParser.buildTransaction(reconstructedJson1,dao);
//        
//        assertEquals(transaction2,reconstructedTransaction2);
//    }

}
